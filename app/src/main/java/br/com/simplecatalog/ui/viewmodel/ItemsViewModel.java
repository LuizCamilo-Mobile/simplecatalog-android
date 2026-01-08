package br.com.simplecatalog.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.simplecatalog.domain.model.Item;
import br.com.simplecatalog.domain.usecase.GetItemsUseCase;

public class ItemsViewModel extends ViewModel {

    private final GetItemsUseCase useCase;

    private final MutableLiveData<List<Item>> items = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>(null);

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ItemsViewModel(GetItemsUseCase useCase) {
        this.useCase = useCase;
    }

    public LiveData<List<Item>> getItems() { return items; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getError() { return error; }

    public void loadItems() {
        loading.setValue(true);
        error.setValue(null);

        executor.execute(() -> {
            try {
                List<Item> result = useCase.executeCacheFirst();
                items.postValue(result);
            } catch (Exception e) {
                // getItems() foi desenhado para não explodir, mas protegemos mesmo assim
                error.postValue("Erro ao carregar itens.");
            } finally {
                loading.postValue(false);
            }
        });
    }

    public void refreshItems() {
        loading.setValue(true);
        error.setValue(null);

        executor.execute(() -> {
            try {
                List<Item> refreshed = useCase.executeRefresh();
                items.postValue(refreshed);
            } catch (Exception e) {
                // Refresh falhou: avisar erro e manter lista (cache) visível
                error.postValue(e.getMessage() != null ? e.getMessage() : "Falha no refresh.");

                // Garante que a UI continue mostrando cache (caso esteja vazio na tela)
                List<Item> cached = useCase.executeCacheFirst();
                items.postValue(cached);
            } finally {
                loading.postValue(false);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}
