package br.com.simplecatalog.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import br.com.simplecatalog.data.local.entity.ItemEntity;

/**
 * Dao = encapsula SQL (UI não faz query).
 */
@Dao
public interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ItemEntity> items);

    @Query("SELECT * FROM items ORDER BY id DESC")
    List<ItemEntity> getAll();

    @Query("DELETE FROM items")
    void clear();
}
