package br.com.simplecatalog.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.simplecatalog.databinding.ItemRowBinding;
import br.com.simplecatalog.domain.model.Item;

public final class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.VH> {

    private final List<Item> data = new ArrayList<>();

    public void submitList(@NonNull List<Item> newList) {
        data.clear();
        data.addAll(newList);
        notifyDataSetChanged(); // suficiente para o exercício 1
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
        Item item = data.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static final class VH extends RecyclerView.ViewHolder {
        private final ItemRowBinding binding;

        VH(@NonNull ItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(@NonNull Item item) {
            binding.title.setText(item.getTitle());
            binding.subtitle.setText(item.getSubtitle());
        }
    }
}
