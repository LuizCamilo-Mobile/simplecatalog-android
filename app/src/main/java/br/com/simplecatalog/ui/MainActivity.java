package br.com.simplecatalog.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import br.com.simplecatalog.data.local.entity.ItemEntity;
import br.com.simplecatalog.databinding.ActivityMainBinding;
import br.com.simplecatalog.di.AppContainer;
import br.com.simplecatalog.domain.usecase.ClearItemsUseCase;
import br.com.simplecatalog.domain.usecase.GetItemsUseCase;
import br.com.simplecatalog.domain.usecase.InsertItemsUseCase;
import br.com.simplecatalog.ui.adapter.ItemsAdapter;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ItemsAdapter adapter;

    // UseCases (DI manual via AppContainer)
    private InsertItemsUseCase insertItemsUseCase;
    private GetItemsUseCase getItemsUseCase;
    private ClearItemsUseCase clearItemsUseCase;

    // Dono do lifecycle → Activity cria e encerra
    private ExecutorService dbExecutor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Executor para operações SQLite/Room fora da UI
        dbExecutor = Executors.newSingleThreadExecutor();

        // DI manual
        AppContainer container = AppContainer.getInstance(this);
        insertItemsUseCase = container.insertItemsUseCase();
        getItemsUseCase = container.getItemsUseCase();
        clearItemsUseCase = container.clearItemsUseCase();

        // RecyclerView
        adapter = new ItemsAdapter();
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setAdapter(adapter);

        // Estado inicial
        adapter.submit(new ArrayList<>());
        showEmpty();

        // Botões
        binding.btnInsert.setOnClickListener(v -> onInsertClicked());
        binding.btnLoad.setOnClickListener(v -> onLoadClicked());
        binding.btnClear.setOnClickListener(v -> onClearClicked());
    }

    private void onInsertClicked() {
        showLoading();

        dbExecutor.execute(() -> {
            try {
                insertItemsUseCase.execute(buildHardcoded7());

                List<ItemEntity> items = getItemsUseCase.execute();

                runOnUiThread(() -> {
                    adapter.submit(items);
                    if (items == null || items.isEmpty()) showEmpty();
                    else showList();
                });
            } catch (Exception e) {
                runOnUiThread(() -> showError(e));
            }
        });
    }

    private void onLoadClicked() {
        showLoading();

        dbExecutor.execute(() -> {
            try {
                List<ItemEntity> items = getItemsUseCase.execute();

                runOnUiThread(() -> {
                    adapter.submit(items);
                    if (items == null || items.isEmpty()) showEmpty();
                    else showList();
                });
            } catch (Exception e) {
                runOnUiThread(() -> showError(e));
            }
        });
    }

    private void onClearClicked() {
        showLoading();

        dbExecutor.execute(() -> {
            try {
                clearItemsUseCase.execute();

                runOnUiThread(() -> {
                    adapter.submit(new ArrayList<>());
                    showEmpty();
                });
            } catch (Exception e) {
                runOnUiThread(() -> showError(e));
            }
        });
    }

    private List<ItemEntity> buildHardcoded7() {
        List<ItemEntity> items = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            items.add(new ItemEntity("Item " + i, "Subtitle do item " + i));
        }
        return items;
    }

    // ---------- Estados de UI ----------
    private void showLoading() {
        binding.progress.setVisibility(View.VISIBLE);
        binding.txtEmpty.setVisibility(View.GONE);
        binding.txtError.setVisibility(View.GONE);
        binding.recycler.setVisibility(View.GONE);
    }

    private void showEmpty() {
        binding.progress.setVisibility(View.GONE);
        binding.txtEmpty.setVisibility(View.VISIBLE);
        binding.txtError.setVisibility(View.GONE);
        binding.recycler.setVisibility(View.GONE);
    }

    private void showList() {
        binding.progress.setVisibility(View.GONE);
        binding.txtEmpty.setVisibility(View.GONE);
        binding.txtError.setVisibility(View.GONE);
        binding.recycler.setVisibility(View.VISIBLE);
    }

    private void showError(Exception e) {
        binding.progress.setVisibility(View.GONE);
        binding.txtEmpty.setVisibility(View.GONE);
        binding.recycler.setVisibility(View.GONE);

        binding.txtError.setVisibility(View.VISIBLE);
        String msg = (e.getMessage() != null) ? e.getMessage() : e.getClass().getSimpleName();
        binding.txtError.setText("Erro: " + msg);
    }

    // ---------- Lifecycle ----------
    @Override
    protected void onDestroy() {
        super.onDestroy();
        shutdownExecutor();
        binding = null;
    }

    private void shutdownExecutor() {
        if (dbExecutor == null) return;

        dbExecutor.shutdown();
        try {
            if (!dbExecutor.awaitTermination(2, TimeUnit.SECONDS)) {
                dbExecutor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            dbExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
