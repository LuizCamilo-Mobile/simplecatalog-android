package br.com.simplecatalog.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class ItemDto {

    @SerializedName("id")
    private final long id;

    @SerializedName("title")
    private final String title;

    @SerializedName("body")
    private final String body;

    public ItemDto(long id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getBody() { return body; }
}
