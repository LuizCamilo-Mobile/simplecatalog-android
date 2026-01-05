package br.com.simplecatalog.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.simplecatalog.domain.model.Item;

public final class ItemsViewModel extends ViewModel {

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<List<Item>> items = new MutableLiveData<>(new ArrayList<>());

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<List<Item>> getItems() { return items; }

    public void loadItems() {
        loading.setValue(true);

        executor.execute(() -> {
            // Simula tarefa "pesada" (ex: parse, IO, etc)
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {}

            List<Item> result = hardcodedItems();

            // Posta resultado na thread correta (LiveData thread-safe via postValue)
            items.postValue(result);
            loading.postValue(false);
        });
    }

    @NonNull
    private List<Item> hardcodedItems() {
        List<Item> list = new ArrayList<>();
        list.add(new Item(1, "Café", "500g • Torra média"));
        list.add(new Item(2, "Leite", "Integral • 1L"));
        list.add(new Item(3, "Arroz", "Tipo 1 • 5kg"));
        list.add(new Item(4, "Feijão", "Carioca • 1kg"));
        list.add(new Item(5, "Açúcar", "Cristal • 1kg"));
        return list;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdownNow();
    }
}
