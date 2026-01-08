package br.com.simplecatalog.domain.usecase;

import java.util.List;

import br.com.simplecatalog.domain.model.Item;
import br.com.simplecatalog.repository.ItemRepository;

public class GetItemsUseCase {

    private final ItemRepository repository;

    public GetItemsUseCase(ItemRepository repository) {
        this.repository = repository;
    }

    public List<Item> executeCacheFirst() {
        return repository.getItems();
    }

    public List<Item> executeRefresh() throws Exception {
        return repository.refresh();
    }
}
