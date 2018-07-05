package com.fadhifatah.omdbapp.module.detail.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RatingModel implements Serializable {
    @SerializedName("Source")
    public String source;

    @SerializedName("Value")
    public String value;
}
