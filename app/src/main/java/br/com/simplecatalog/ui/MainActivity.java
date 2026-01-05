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

public final class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ItemsAdapter adapter;
    private ItemsViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        adapter = new ItemsAdapter();
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ItemsViewModel.class);

        viewModel.getLoading().observe(this, isLoading -> {
            boolean show = Boolean.TRUE.equals(isLoading);
            binding.progress.setVisibility(show ? View.VISIBLE : View.GONE);
        });

        viewModel.getItems().observe(this, list -> adapter.submitList(list));

        // dispara carga simulada
        viewModel.loadItems();
    }
}
