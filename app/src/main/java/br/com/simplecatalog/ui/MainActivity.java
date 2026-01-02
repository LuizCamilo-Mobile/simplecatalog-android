package br.com.simplecatalog.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import br.com.simplecatalog.databinding.ActivityMainBinding;
import br.com.simplecatalog.di.AppContainer;
import br.com.simplecatalog.ui.adapter.ItemsAdapter;
import br.com.simplecatalog.ui.viewmodel.ItemsViewModel;
import br.com.simplecatalog.ui.viewmodel.ItemsViewModelFactory;

/**
 * Activity (UI):
 * - Configura layout, RecyclerView e observa estados do ViewModel
 * - Não faz chamadas de rede ou banco diretamente (isso fica no repository/usecase)
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ItemsAdapter adapter;
    private ItemsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewBinding: evita findViewById e dá segurança de null
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 1) RecyclerView + Adapter
        setupRecyclerView();

        // 2) Monta dependências via AppContainer e cria ViewModel via Factory
        setupViewModel();

        // 3) Observa estados do ViewModel e atualiza UI
        observeViewModel();

        // 4) Dispara carregamento inicial (pode ser cache-first ou refresh)
        viewModel.loadItems();
    }

    private void setupRecyclerView() {
        adapter = new ItemsAdapter(new ArrayList<>());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        // AppContainer fornece as dependências (UseCase já composto com Repository)
        AppContainer container = AppContainer.getInstance(this);

        ItemsViewModelFactory factory = new ItemsViewModelFactory(container.getItemsUseCase);
        viewModel = new ViewModelProvider(this, factory).get(ItemsViewModel.class);
    }

    private void observeViewModel() {
        viewModel.getItems().observe(this, items -> {
            // Atualiza lista
            adapter.updateItems(items);

            // Se tem itens, mostra lista e esconde estado vazio (se existir)
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.emptyState.setVisibility(items == null || items.isEmpty() ? View.VISIBLE : View.GONE);
        });

        viewModel.getLoading().observe(this, isLoading -> {
            // Mostra/oculta progress
            binding.progressBar.setVisibility(Boolean.TRUE.equals(isLoading) ? View.VISIBLE : View.GONE);
        });

        viewModel.getError().observe(this, message -> {
            if (message != null) {
                binding.errorText.setText(message);
                binding.errorText.setVisibility(View.VISIBLE);
            } else {
                binding.errorText.setVisibility(View.GONE);
            }
        });
    }
}
