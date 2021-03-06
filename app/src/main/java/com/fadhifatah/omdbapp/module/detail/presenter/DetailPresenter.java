package com.fadhifatah.omdbapp.module.detail.presenter;

import android.support.annotation.NonNull;

import com.fadhifatah.omdbapp.module.detail.listener.DetailListener;
import com.fadhifatah.omdbapp.module.detail.model.MovieModel;
import com.fadhifatah.omdbapp.service.API;
import com.fadhifatah.omdbapp.util.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPresenter {

    private DetailListener listener;

    public DetailPresenter(DetailListener listener) {
        this.listener = listener;
    }

    public void getMovieDetail(String imdbId) {
        API.getClientEvent()
                .create(API.Service.class)
                .getMovie(Constant.API_KEY, imdbId)
                .enqueue(new Callback<MovieModel>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieModel> call, @NonNull Response<MovieModel> response) {
                        if (response.isSuccessful()) {
                            MovieModel model = response.body();
                            if (model != null) {
                                if (model.response.equalsIgnoreCase("true")) {
                                    if (!model.posterUrl.equalsIgnoreCase("n/a"))
                                        listener.OnResultResponse(new MoviePresenter(model));
                                    else
                                        listener.OnResultResponseWithoutPoster(new MoviePresenter(model));
                                }
                                else
                                    listener.OnError(model.error);
                            }
                        }
                        else
                            listener.OnError(response.message());
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieModel> call, @NonNull Throwable t) {
                        listener.OnError("Time out! " + t.getMessage());
                    }
                });
    }
}
