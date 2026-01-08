package br.com.simplecatalog.di;

import android.content.Context;

import br.com.simplecatalog.data.local.AppDatabase;
import br.com.simplecatalog.data.local.dao.ItemDao;
import br.com.simplecatalog.domain.usecase.ClearItemsUseCase;
import br.com.simplecatalog.domain.usecase.GetItemsUseCase;
import br.com.simplecatalog.domain.usecase.InsertItemsUseCase;

/**
 * AppContainer = Service Locator manual (Singleton).
 * Centraliza composição de dependências (Room + UseCases).
 */
public final class AppContainer {

    private static volatile AppContainer INSTANCE;

    private final AppDatabase db;
    private final ItemDao itemDao;

    private final InsertItemsUseCase insertItemsUseCase;
    private final GetItemsUseCase getItemsUseCase;
    private final ClearItemsUseCase clearItemsUseCase;

    private AppContainer(Context appContext) {
        db = AppDatabase.getInstance(appContext);
        itemDao = db.itemDao();

        // DI manual (injeção via construtor)
        insertItemsUseCase = new InsertItemsUseCase(itemDao);
        getItemsUseCase = new GetItemsUseCase(itemDao);
        clearItemsUseCase = new ClearItemsUseCase(itemDao);
    }

    public static AppContainer getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppContainer.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppContainer(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    public InsertItemsUseCase insertItemsUseCase() { return insertItemsUseCase; }
    public GetItemsUseCase getItemsUseCase() { return getItemsUseCase; }
    public ClearItemsUseCase clearItemsUseCase() { return clearItemsUseCase; }
}
