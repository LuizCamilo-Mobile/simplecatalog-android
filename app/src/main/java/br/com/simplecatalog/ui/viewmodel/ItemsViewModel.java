package br.com.simplecatalog.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.simplecatalog.domain.model.Item;
import br.com.simplecatalog.domain.usecase.GetItemsUseCase;

/**
 * ViewModel (MVVM):
 * - Mantém o estado da tela
 * - Chama o UseCase (camada de domínio)
 * - Publica resultado via LiveData para a UI observar
 *
 * Importante:
 * - Não referencia Views, Activity ou Context (mantém desacoplamento)
 * - Operações pesadas (rede/banco) são feitas em background para evitar ANR
 */
public class ItemsViewModel extends ViewModel {

    private final GetItemsUseCase getItemsUseCase;

    // Estados observáveis pela UI
    private final MutableLiveData<List<Item>> items = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>(null);

    // Executor simples para rodar tarefas fora da UI thread
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ItemsViewModel(GetItemsUseCase getItemsUseCase) {
        this.getItemsUseCase = getItemsUseCase;
    }

    // Exposição “read-only” para a UI (boa prática)
    public LiveData<List<Item>> getItems() { return items; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getError() { return error; }

    /**
     * Carrega itens usando o UseCase.
     * Deve ser chamado pela Activity (ex: onCreate) para iniciar o fluxo.
     */
    public void loadItems() {
        loading.setValue(true);
        error.setValue(null);

        executor.execute(() -> {
            try {
                List<Item> result = getItemsUseCase.execute();

                // postValue porque estamos em thread de background
                items.postValue(result);
            } catch (Exception e) {
                e.printStackTrace();
                error.postValue("Falha ao carregar itens.");
            } finally {
                loading.postValue(false);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Libera o executor para não manter threads vivas após a tela morrer
        executor.shutdown();
    }
}

