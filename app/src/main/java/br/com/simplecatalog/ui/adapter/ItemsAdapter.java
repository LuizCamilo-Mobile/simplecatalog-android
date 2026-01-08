package br.com.simplecatalog.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.simplecatalog.data.local.entity.ItemEntity;
import br.com.simplecatalog.databinding.ItemRowBinding;

/**
 * Adapter só exibe dados. Nada de DB/UseCase/Executor aqui.
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.VH> {

    private final List<ItemEntity> items = new ArrayList<>();

    public void submit(List<ItemEntity> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRowBinding binding = ItemRowBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        private final ItemRowBinding binding;

        VH(ItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ItemEntity item) {
            binding.txtTitle.setText(item.title);
            binding.txtSubtitle.setText(item.subtitle);
        }
    }
}
