package com.fadhifatah.omdbapp.module.detail.presenter;

import com.fadhifatah.omdbapp.module.detail.model.MovieModel;
import com.fadhifatah.omdbapp.module.detail.model.RatingModel;

import java.util.List;

public class MoviePresenter {

    private MovieModel model;

    public MoviePresenter(MovieModel model) {
        this.model = model;
    }

    public String getTitle() {
        return model.title + " (" + model.year + ")";
    }

    public String getInformation() {
        return model.rated + " • " + model.runtime + " • " + model.genre;
    }

    public String getAdditionalInfo() {
        return "Released at " + model.released + " from " + model.country + " (" +
                model.language + ") • " + model.production;
    }

    public String getScores() {
        return "Metascore - " + model.metascore + " • IMDb Rating - " + model.imdbRating +
                " • " + model.imdbVotes + " votes";
    }

    public String getDirectors() {
        return "Directed by " + model.director;
    }

    public String getAwards() {
        return "Awards - " + model.awards;
    }

    public String getWriters() {
        return model.writer;
    }

    public String getActors() {
        return model.actors;
    }

    public String getDVD() {
        return model.dvd;
    }

    public String getWebsite() {
        return model.website;
    }

    public String getBoxOffce() {
        return model.boxOffice;
    }

    public String getPlot() {
        return "\"" + model.plot + "\"";
    }

    public String getPosterUrl() {
        return model.posterUrl;
    }

    public List<RatingModel> getRatingList() {
        return model.ratingList;
    }
}
