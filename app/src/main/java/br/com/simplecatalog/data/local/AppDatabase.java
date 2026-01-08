package br.com.simplecatalog.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import br.com.simplecatalog.data.local.dao.ItemDao;
import br.com.simplecatalog.data.local.entity.ItemEntity;

@Database(entities = {ItemEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ItemDao itemDao();
}
