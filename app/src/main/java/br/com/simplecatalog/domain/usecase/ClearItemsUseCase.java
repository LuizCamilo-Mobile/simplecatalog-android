package br.com.simplecatalog.domain.usecase;

import br.com.simplecatalog.data.local.dao.ItemDao;

public class ClearItemsUseCase {

    private final ItemDao dao;

    public ClearItemsUseCase(ItemDao dao) {
        this.dao = dao;
    }

    public void execute() {
        dao.clear();
    }
}
