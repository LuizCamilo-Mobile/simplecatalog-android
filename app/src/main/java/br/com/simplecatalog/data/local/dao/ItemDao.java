package br.com.simplecatalog.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import br.com.simplecatalog.data.local.entity.ItemEntity;

@Dao
public abstract class ItemDao {

    @Query("SELECT * FROM items ORDER BY id")
    public abstract List<ItemEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<ItemEntity> items);

    @Query("DELETE FROM items")
    public abstract void clear();

    // Atômico: clear + insertAll em uma única transação
    @Transaction
    public void replaceAll(List<ItemEntity> items) {
        clear();
        insertAll(items);
    }
}
