package com.fadhifatah.omdbapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fadhifatah.omdbapp.R;
import com.fadhifatah.omdbapp.model.MovieModel;
import com.fadhifatah.omdbapp.service.API;
import com.fadhifatah.omdbapp.util.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private String imdbId;

    @BindView(R.id.poster_detail)
    ImageView poster;

    @BindView(R.id.title_detail)
    TextView title;

    @BindView(R.id.information_detail)
    TextView information;

    @BindView(R.id.rating_detail)
    RecyclerView ratingView;

    @BindView(R.id.plot_detail)
    TextView plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        imdbId = (String) getIntent().getSerializableExtra(Constant.IMDB);

        API.getClientEvent()
                .create(API.Service.class)
                .getMovie(Constant.API_KEY, imdbId)
                .enqueue(new Callback<MovieModel>() {
                    @Override
                    public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                        MovieModel model = response.body();
                        if (model.response.equalsIgnoreCase("true")) {
                            if (!model.posterUrl.equalsIgnoreCase("n/a")) {
                                poster.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                Glide.with(getApplicationContext())
                                        .load(model.posterUrl)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(poster);
                            }

                            String s = model.title + " (" + model.year + ")";
                            title.setText(s);

                            s = model.rated + " • " + model.runtime + " • " + model.genre + " • " +
                                    model.released + " • " + model.language + " • " + model.country;
                            information.setText(s);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieModel> call, Throwable t) {

                    }
                });
    }
}
