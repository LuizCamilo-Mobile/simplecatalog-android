package br.com.simplecatalog.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import br.com.simplecatalog.databinding.ActivityMainBinding;
import br.com.simplecatalog.di.AppContainer;
import br.com.simplecatalog.ui.adapter.ItemsAdapter;
import br.com.simplecatalog.ui.viewmodel.ItemsViewModel;
import br.com.simplecatalog.ui.viewmodel.ItemsViewModelFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ItemsViewModel viewModel;
    private ItemsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupRecycler();
        setupViewModel();
        setupObservers();
        setupActions();

        viewModel.loadItems();
    }

    private void setupRecycler() {
        adapter = new ItemsAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        AppContainer container = AppContainer.getInstance(getApplicationContext());
        ItemsViewModelFactory factory = new ItemsViewModelFactory(container.getGetItemsUseCase());
        viewModel = new ViewModelProvider(this, factory).get(ItemsViewModel.class);
    }

    private void setupObservers() {
        viewModel.getLoading().observe(this, isLoading -> {
            boolean showing = isLoading != null && isLoading;
            binding.progressBar.setVisibility(showing ? View.VISIBLE : View.GONE);
            // Enquanto carrega, não precisa esconder a lista; UX fica melhor mantendo cache visível.
        });

        viewModel.getError().observe(this, msg -> {
            if (msg != null && !msg.trim().isEmpty()) {
                binding.txtError.setText(msg);
                binding.txtError.setVisibility(View.VISIBLE);
            } else {
                binding.txtError.setVisibility(View.GONE);
            }
        });

        viewModel.getItems().observe(this, items -> {
            adapter.submit(items);

            boolean empty = (items == null || items.isEmpty());
            binding.txtEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
            binding.recyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
        });
    }

    private void setupActions() {
        binding.btnRefresh.setOnClickListener(v -> viewModel.refreshItems());
    }
}
