package com.fadhifatah.omdbapp.module.detail.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MovieModel implements Serializable {
    @SerializedName("Title")
    public String title;

    @SerializedName("Year")
    public String year;

    @SerializedName("Rated")
    public String rated;

    @SerializedName("Released")
    public String released;

    @SerializedName("Runtime")
    public String runtime;

    @SerializedName("Genre")
    public String genre;

    @SerializedName("Director")
    public String director;

    @SerializedName("Writer")
    public String writer;

    @SerializedName("Actors")
    public String actors;

    @SerializedName("Plot")
    public String plot;

    @SerializedName("Language")
    public String language;

    @SerializedName("Country")
    public String country;

    @SerializedName("Awards")
    public String awards;

    @SerializedName("Poster")
    public String posterUrl;

    @SerializedName("Ratings")
    public List<RatingModel> ratingList;

    @SerializedName("Metascore")
    public String metascore;

    @SerializedName("imdbRating")
    public String imdbRating;

    @SerializedName("imdbVotes")
    public String imdbVotes;

    @SerializedName("imdbID")
    public String imdbId;

    @SerializedName("Type")
    public String type;

    @SerializedName("DVD")
    public String dvd;

    @SerializedName("BoxOffice")
    public String boxOffice;

    @SerializedName("Production")
    public String production;

    @SerializedName("Website")
    public String website;

    @SerializedName("Response")
    public String response;

    @SerializedName("Error")
    public String error;
}
