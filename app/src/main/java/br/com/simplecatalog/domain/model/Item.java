package br.com.simplecatalog.domain.model;

public class Item {

    private final long id;
    private final String title;
    private final String subtitle;

    public Item(long id, String title, String subtitle) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }
}
