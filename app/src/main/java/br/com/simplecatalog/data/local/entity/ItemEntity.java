package br.com.simplecatalog.data.local.entity;

public class ItemEntity {

    private final long id;
    private final String title;
    private final String subtitle;

    public ItemEntity(long id, String title, String subtitle) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }
}
