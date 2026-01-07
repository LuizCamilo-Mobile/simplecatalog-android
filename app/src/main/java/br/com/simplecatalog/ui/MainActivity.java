package br.com.simplecatalog.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.simplecatalog.data.local.AppDatabase;
import br.com.simplecatalog.data.local.dao.ItemDao;
import br.com.simplecatalog.data.local.entity.ItemEntity;
import br.com.simplecatalog.databinding.ActivityMainBinding;
import br.com.simplecatalog.ui.adapter.ItemsAdapter;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ItemsAdapter adapter;

    private AppDatabase db;
    private ItemDao dao;

    private final ExecutorService dbExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = AppDatabase.getInstance(this);
        dao = db.itemDao();

        adapter = new ItemsAdapter();
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setAdapter(adapter);

        // Começa “vazio”
        showEmpty();

        binding.btnInsert.setOnClickListener(v -> insertHardcoded());
        binding.btnRead.setOnClickListener(v -> readFromDb());
        binding.btnClear.setOnClickListener(v -> clearDb());
    }

    private void insertHardcoded() {
        showLoading();

        dbExecutor.execute(() -> {
            try {
                dao.insertAll(buildHardcodedItems());

                runOnUiThread(() -> {
                    hideLoading();
                    Toast.makeText(this, "Inserido! Agora clique em 'Ler Banco'.", Toast.LENGTH_SHORT).show();
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    hideLoading();
                    showError("Falha ao inserir: " + e.getMessage());
                });
            }
        });
    }

    private void readFromDb() {
        showLoading();

        dbExecutor.execute(() -> {
            try {
                List<ItemEntity> items = dao.getAll();

                runOnUiThread(() -> {
                    hideLoading();
                    if (items == null || items.isEmpty()) {
                        adapter.submit(new ArrayList<>());
                        showEmpty();
                    } else {
                        adapter.submit(items);
                        showList();
                    }
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    hideLoading();
                    showError("Falha ao ler: " + e.getMessage());
                });
            }
        });
    }

    private void clearDb() {
        showLoading();

        dbExecutor.execute(() -> {
            try {
                dao.clear();

                runOnUiThread(() -> {
                    hideLoading();
                    adapter.submit(new ArrayList<>());
                    showEmpty();
                    Toast.makeText(this, "Banco limpo.", Toast.LENGTH_SHORT).show();
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    hideLoading();
                    showError("Falha ao limpar: " + e.getMessage());
                });
            }
        });
    }

    private List<ItemEntity> buildHardcodedItems() {
        List<ItemEntity> result = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            result.add(new ItemEntity("Item " + i, "Subtitle do item " + i));
        }
        return result;
    }

    // ---------- UI States ----------
    private void showLoading() {
        binding.progress.setVisibility(android.view.View.VISIBLE);
        binding.recycler.setVisibility(android.view.View.GONE);
        binding.tvEmpty.setVisibility(android.view.View.GONE);
        binding.tvError.setVisibility(android.view.View.GONE);
    }

    private void hideLoading() {
        binding.progress.setVisibility(android.view.View.GONE);
    }

    private void showEmpty() {
        binding.recycler.setVisibility(android.view.View.GONE);
        binding.tvEmpty.setVisibility(android.view.View.VISIBLE);
        binding.tvError.setVisibility(android.view.View.GONE);
    }

    private void showList() {
        binding.recycler.setVisibility(android.view.View.VISIBLE);
        binding.tvEmpty.setVisibility(android.view.View.GONE);
        binding.tvError.setVisibility(android.view.View.GONE);
    }

    private void showError(String message) {
        binding.recycler.setVisibility(android.view.View.GONE);
        binding.tvEmpty.setVisibility(android.view.View.GONE);
        binding.tvError.setVisibility(android.view.View.VISIBLE);
        binding.tvError.setText("Error: " + message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbExecutor.shutdown();
        binding = null;
    }
}
