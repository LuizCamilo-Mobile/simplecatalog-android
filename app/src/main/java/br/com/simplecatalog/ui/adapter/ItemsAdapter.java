package br.com.simplecatalog.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.simplecatalog.databinding.ItemRowBinding;
import br.com.simplecatalog.domain.model.Item;

/**
 * Adapter do RecyclerView: apenas exibe dados do domínio em lista.
 * Utiliza ViewHolder + ViewBinding para performance e segurança de null.
 *
 * O Adapter é responsável por exibir os modelos do domínio no RecyclerView.
 * Ele não acessa rede nem banco, apenas consome dados prontos e gerencia a camada de apresentação da lista.
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> items;

    public ItemsAdapter(List<Item> items) {
        this.items = items;
    }

    /**
     * Atualiza a lista do Adapter (novo dataset vindo do ViewModel/Presenter)
     */
    public void updateItems(List<Item> newItems) {
        this.items = newItems;
        notifyDataSetChanged(); // notifica o RecyclerView para redesenhar
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout da linha usando ViewBinding
        ItemRowBinding binding = ItemRowBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Obtém o item do domínio e preenche a UI
        Item item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    /**
     * ViewHolder segura as views de cada linha e faz o bind dos dados.
     * Usa binding para evitar findViewById e melhorar performance.
     */
    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private final ItemRowBinding binding;

        public ItemViewHolder(ItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /**
         * Faz o preenchimento da linha com os dados do item
         */
        public void bind(Item item) {
            binding.itemTitle.setText(item.getTitle());       // ← referência do domínio
            binding.itemSubtitle.setText(item.getSubtitle()); // ← referência do domínio
        }
    }
}

