package br.com.simplecatalog.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity = tabela do SQLite (formato de persistência).
 */
@Entity(tableName = "items")
public class ItemEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "subtitle")
    public String subtitle;

    public ItemEntity(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }
}
