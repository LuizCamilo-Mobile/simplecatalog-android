package br.com.simplecatalog.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.simplecatalog.databinding.ActivityMainBinding;
import br.com.simplecatalog.domain.model.Item;
import br.com.simplecatalog.ui.adapter.ItemsAdapter;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private ItemsAdapter adapter;

    // Executor para rodar trabalho pesado fora da Main Thread
    private ExecutorService executor;

    // LiveData só na Activity (sem ViewModel)
    private final MutableLiveData<UiState> uiState = new MutableLiveData<>();

    // Estados possíveis da tela
    private enum UiState {
        LOADING,
        CONTENT,
        EMPTY,
        ERROR
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        executor = Executors.newSingleThreadExecutor();

        setupRecycler();

        // Começa vazio
        adapter.submitList(new ArrayList<>());
        uiState.setValue(UiState.EMPTY);

        observeUiState();

        binding.btnLoadItems.setOnClickListener(v -> loadItems());
    }

    private void setupRecycler() {
        adapter = new ItemsAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void observeUiState() {
        uiState.observe(this, state -> {
            if (state == null) return;

            // Sempre controla visibilidade por estado (padrão entrevista)
            binding.progressBar.setVisibility(state == UiState.LOADING ? View.VISIBLE : View.GONE);
            binding.recyclerView.setVisibility(state == UiState.CONTENT ? View.VISIBLE : View.GONE);
            binding.tvEmpty.setVisibility(state == UiState.EMPTY ? View.VISIBLE : View.GONE);
            binding.tvError.setVisibility(state == UiState.ERROR ? View.VISIBLE : View.GONE);
        });
    }

    private void loadItems() {
        // 1) UI: loading imediatamente (na Main Thread)
        uiState.setValue(UiState.LOADING);

        // 2) Trabalho pesado em background
        executor.execute(() -> {
            try {
                // Simula tarefa custosa (ex.: parse grande, processamento, etc)
                Thread.sleep(1200);

                // 3) Cria lista hardcoded de 7 itens
                List<Item> result = new ArrayList<>();
                for (int i = 1; i <= 7; i++) {
                    long itemId = i; // simulando um ID único
                    result.add(new Item(itemId, "Item " + i, "Subtitle do item " + i));
                }


                // 4) Publica resultado na UI thread
                runOnUiThread(() -> {
                    adapter.submitList(result);
                    uiState.setValue(result.isEmpty() ? UiState.EMPTY : UiState.CONTENT);
                });

            } catch (Exception e) {
                // 5) Se der erro, mostra estado ERROR na UI thread
                runOnUiThread(() -> uiState.setValue(UiState.ERROR));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Evita leaks e threads “penduradas”
        if (executor != null) {
            executor.shutdownNow();
            executor = null;
        }

        binding = null;
    }
}
