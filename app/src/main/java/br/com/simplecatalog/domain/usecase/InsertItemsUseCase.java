package br.com.simplecatalog.domain.usecase;

import java.util.List;

import br.com.simplecatalog.data.local.dao.ItemDao;
import br.com.simplecatalog.data.local.entity.ItemEntity;

/**
 * UseCase = ação (UI chama UseCase, não Dao).
 */
public class InsertItemsUseCase {

    private final ItemDao dao;

    public InsertItemsUseCase(ItemDao dao) {
        this.dao = dao;
    }

    public void execute(List<ItemEntity> items) {
        dao.insertAll(items);
    }
}

