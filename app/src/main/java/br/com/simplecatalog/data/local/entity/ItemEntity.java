package br.com.simplecatalog.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "items")
public class ItemEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String title;
    public String subtitle;

    public ItemEntity(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }
}
