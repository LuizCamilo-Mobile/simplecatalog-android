package br.com.simplecatalog.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.simplecatalog.databinding.ItemRowBinding;
import br.com.simplecatalog.domain.model.Item;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.VH> {

    private final List<Item> items = new ArrayList<>();

    public void submitList(List<Item> newItems) {
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
        Item item = items.get(position);
        holder.binding.tvTitle.setText(item.getTitle());
        holder.binding.tvSubtitle.setText(item.getSubtitle());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final ItemRowBinding binding;

        VH(ItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
