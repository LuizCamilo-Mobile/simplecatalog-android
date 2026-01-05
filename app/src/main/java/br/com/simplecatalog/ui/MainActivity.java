package br.com.simplecatalog.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.simplecatalog.databinding.ActivityMainBinding;
import br.com.simplecatalog.databinding.ItemRowBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    // Executor para simular trabalho pesado fora da UI
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // Dados locais (hardcoded)
    private final List<String> items = new ArrayList<>();
    private ItemsAdapter adapter;

    // Estados de UI manuais
    private enum UiState { LOADING, CONTENT, EMPTY, ERROR }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewBinding: infla sem findViewById
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupRecycler();

        // Começa em vazio (lista vazia)
        render(UiState.EMPTY);

        // Botões controlam estado
        binding.btnLoad.setOnClickListener(v -> onLoadClicked());
        binding.btnError.setOnClickListener(v -> onErrorClicked());
        binding.btnEmpty.setOnClickListener(v -> onEmptyClicked());
    }

    private void setupRecycler() {
        adapter = new ItemsAdapter(items);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setAdapter(adapter);
    }

    private void onLoadClicked() {
        render(UiState.LOADING);

        executor.execute(() -> {
            // Simula carga pesada (não pode travar UI)
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Dados hardcoded (5 itens)
            List<String> loaded = Arrays.asList(
                    "Item 1",
                    "Item 2",
                    "Item 3",
                    "Item 4",
                    "Item 5"
            );

            // Volta para a UI thread para atualizar views/adapter
            runOnUiThread(() -> {
                items.clear();
                items.addAll(loaded);
                adapter.notifyDataSetChanged();
                render(UiState.CONTENT);
            });
        });
    }

    private void onErrorClicked() {
        // Estado de erro: limpa lista e mostra mensagem
        items.clear();
        adapter.notifyDataSetChanged();
        render(UiState.ERROR);
    }

    private void onEmptyClicked() {
        // Estado vazio: limpa lista e mostra empty
        items.clear();
        adapter.notifyDataSetChanged();
        render(UiState.EMPTY);
    }

    private void render(@NonNull UiState state) {
        // Regra simples: só um “bloco” visível por vez
        binding.progress.setVisibility(state == UiState.LOADING ? View.VISIBLE : View.GONE);
        binding.recycler.setVisibility(state == UiState.CONTENT ? View.VISIBLE : View.GONE);
        binding.tvEmpty.setVisibility(state == UiState.EMPTY ? View.VISIBLE : View.GONE);
        binding.tvError.setVisibility(state == UiState.ERROR ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Evita thread viva após fechar a Activity
        executor.shutdownNow();
        binding = null;
    }

    // --------------------------------------------
    // Adapter + ViewHolder (RecyclerView básico)
    // --------------------------------------------
    private static class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemVH> {

        private final List<String> data;

        ItemsAdapter(List<String> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public ItemVH onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
            ItemRowBinding rowBinding = ItemRowBinding.inflate(
                    android.view.LayoutInflater.from(parent.getContext()),
                    parent,
                    false
            );
            return new ItemVH(rowBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemVH holder, int position) {
            holder.bind(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class ItemVH extends RecyclerView.ViewHolder {
            private final ItemRowBinding binding;

            ItemVH(ItemRowBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            void bind(String title) {
                binding.tvTitle.setText(title);
            }
        }
    }
}
