package br.com.simplecatalog.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import br.com.simplecatalog.databinding.ActivityMainBinding;
import br.com.simplecatalog.ui.adapter.ItemsAdapter;
import br.com.simplecatalog.ui.viewmodel.ItemsViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private ItemsAdapter adapter;
    private ItemsViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new ItemsAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ItemsViewModel.class);

        observeViewModel();

        // Dispara carga inicial
        viewModel.loadItems();

        // Botão opcional (se existir no layout) para repetir o ciclo
        binding.retryButton.setOnClickListener(v -> viewModel.loadItems());
    }

    private void observeViewModel() {
        viewModel.getLoading().observe(this, isLoading -> {
            boolean loading = isLoading != null && isLoading;
            binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);

            // Durante loading, você pode esconder lista/erro para ficar claro
            binding.recyclerView.setVisibility(loading ? View.GONE : View.VISIBLE);
        });

        viewModel.getError().observe(this, message -> {
            boolean hasError = message != null && !message.trim().isEmpty();
            binding.errorText.setText(hasError ? message : "");
            binding.errorText.setVisibility(hasError ? View.VISIBLE : View.GONE);
            binding.retryButton.setVisibility(hasError ? View.VISIBLE : View.GONE);
        });

        viewModel.getItems().observe(this, items -> {
            adapter.submitList(items);
            // Você pode optar por mostrar "empty" aqui, mas não foi exigido neste exercício.
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // evita segurar referência desnecessária
    }
}
