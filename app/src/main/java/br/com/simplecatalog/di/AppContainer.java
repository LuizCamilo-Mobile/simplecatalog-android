package br.com.simplecatalog.di;

import android.content.Context;

import androidx.room.Room;

import br.com.simplecatalog.data.local.AppDatabase;
import br.com.simplecatalog.data.local.dao.ItemDao;
import br.com.simplecatalog.data.mapper.ItemMapper;
import br.com.simplecatalog.data.remote.ApiService;
import br.com.simplecatalog.data.remote.RetrofitClient;
import br.com.simplecatalog.domain.usecase.GetItemsUseCase;
import br.com.simplecatalog.repository.ItemRepository;
import br.com.simplecatalog.repository.ItemRepositoryImpl;

public class AppContainer {

    private static volatile AppContainer instance;

    private final AppDatabase db;
    private final ItemDao itemDao;

    private final ApiService apiService;
    private final ItemMapper mapper;

    private final ItemRepository itemRepository;
    private final GetItemsUseCase getItemsUseCase;

    private AppContainer(Context appContext) {
        // Room
        db = Room.databaseBuilder(appContext, AppDatabase.class, "simple_catalog.db")
                .fallbackToDestructiveMigration()
                .build();
        itemDao = db.itemDao();

        // Retrofit
        apiService = RetrofitClient.createApiService();

        // Mapper
        mapper = new ItemMapper();

        // Repository + UseCase
        itemRepository = new ItemRepositoryImpl(itemDao, apiService, mapper);
        getItemsUseCase = new GetItemsUseCase(itemRepository);
    }

    public static AppContainer getInstance(Context context) {
        if (instance == null) {
            synchronized (AppContainer.class) {
                if (instance == null) {
                    instance = new AppContainer(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public GetItemsUseCase getGetItemsUseCase() {
        return getItemsUseCase;
    }
}
