package br.com.simplecatalog.ui.viewmodel;

import androidx.annotation.MainThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.simplecatalog.domain.model.Item;

public class ItemsViewModel extends ViewModel {

    private final MutableLiveData<List<Item>> items = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>(null);

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public LiveData<List<Item>> getItems() {
        return items;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    @MainThread
    public void loadItems() {
        // Evita disparar cargas simultâneas (opcional, mas bom pra entrevista)
        Boolean isLoading = loading.getValue();
        if (isLoading != null && isLoading) return;

        loading.setValue(true);
        error.setValue(null);

        executor.execute(() -> {
            try {
                // Simula tarefa pesada (rede/DB/parse)
                Thread.sleep(1000);

                List<Item> hardcoded = Arrays.asList(
                        new Item(1L, "Café", "500g • torrado"),
                        new Item(2L, "Leite", "Integral • 1L"),
                        new Item(3L, "Arroz", "Tipo 1 • 5kg"),
                        new Item(4L, "Feijão", "Carioca • 1kg"),
                        new Item(5L, "Açúcar", "Refinado • 1kg"),
                        new Item(6L, "Macarrão", "Espaguete • 500g")
                );


                items.postValue(hardcoded);
            } catch (InterruptedException e) {
                error.postValue("Carga interrompida");
            } catch (Exception e) {
                error.postValue("Erro inesperado: " + e.getClass().getSimpleName());
            } finally {
                loading.postValue(false);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdownNow();
    }
}
