package com.fadhifatah.omdbapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

class RatingModel implements Serializable {
    @SerializedName("Source")
    public String source;

    @SerializedName("Value")
    public String value;
}
