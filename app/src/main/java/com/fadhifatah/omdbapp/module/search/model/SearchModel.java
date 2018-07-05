package com.fadhifatah.omdbapp.module.search.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SearchModel implements Serializable {
    @SerializedName("Search")
    public List<ItemModel> searchList;

    @SerializedName("totalResults")
    public String total;

    @SerializedName("Response")
    public String response;

    @SerializedName("Error")
    public String error;
}
