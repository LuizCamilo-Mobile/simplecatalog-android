package br.com.simplecatalog.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import br.com.simplecatalog.data.local.dao.ItemDao;
import br.com.simplecatalog.data.local.entity.ItemEntity;

/**
 * RoomDatabase = schema + DAOs.
 */
@Database(
        entities = {ItemEntity.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract ItemDao itemDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "simplecatalog.db"
                            )
                            // Sem allowMainThreadQueries (regra do exercício)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
