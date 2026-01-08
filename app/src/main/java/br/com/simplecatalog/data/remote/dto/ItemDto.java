package br.com.simplecatalog.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class ItemDto {

    @SerializedName("id")
    public int id;

    @SerializedName("title")
    public String title;

    @SerializedName("body")
    public String body;
}
