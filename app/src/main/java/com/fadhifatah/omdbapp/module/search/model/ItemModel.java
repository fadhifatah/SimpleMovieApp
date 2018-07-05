package com.fadhifatah.omdbapp.module.search.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemModel implements Serializable{
    @SerializedName("Title")
    public String title;

    @SerializedName("Year")
    public String year;

    @SerializedName("imdbID")
    public String imdbId;

    @SerializedName("Type")
    public String type;

    @SerializedName("Poster")
    public String posterUrl;
}
