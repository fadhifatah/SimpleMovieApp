package com.fadhifatah.omdbapp.service;

import com.fadhifatah.omdbapp.model.MovieModel;
import com.fadhifatah.omdbapp.model.SearchModel;
import com.fadhifatah.omdbapp.util.Constant;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class API {
    private static Retrofit retrofit = null;

    public static Retrofit getClientEvent() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public interface Service {
        @GET("/")
        Call<SearchModel> search(
                @Query("apikey") String apiKey,
                @Query("s") String search,
                @Query("type") String type,
                @Query("y") String year,
                @Query("page") Integer page
        );

        @GET("/")
        Call<MovieModel> getMovie(
                @Query("apikey") String apiKey,
                @Query("i") String imdbId
        );
    }
}
