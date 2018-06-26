package com.fadhifatah.omdbapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fadhifatah.omdbapp.R;
import com.fadhifatah.omdbapp.adapter.RatingAdapter;
import com.fadhifatah.omdbapp.model.MovieModel;
import com.fadhifatah.omdbapp.model.RatingModel;
import com.fadhifatah.omdbapp.service.API;
import com.fadhifatah.omdbapp.util.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private List<RatingModel> list;

    @BindView(R.id.poster_detail)
    ImageView poster;

    @BindView(R.id.title_detail)
    TextView title;

    @BindView(R.id.information_detail)
    TextView information;

    @BindView(R.id.additional_detail)
    TextView additional;

    @BindView(R.id.directed)
    TextView director;

    @BindView(R.id.writer)
    TextView writer;

    @BindView(R.id.rating_detail)
    RecyclerView ratingView;

    @BindView(R.id.plot_detail)
    TextView plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        String imdbId = (String) getIntent().getSerializableExtra(Constant.IMDB);
        getSupportActionBar().setTitle(imdbId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ratingView.setLayoutManager(layoutManager);

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

                            s = model.rated + " • " + model.runtime + " • " + model.genre + " • ";
                            information.setText(s);

                            s = "Released at " + model.released + " from " + model.country + " (" +
                                    model.language + ")";
                            additional.setText(s);

                            s = "Directed by " + model.director;
                            director.setText(s);

                            s = "Writer: " + model.writer;
                            writer.setText(s);

                            s = "\"" + model.plot + "\"";
                            plot.setText(s);

                            RatingAdapter adapter = new RatingAdapter(getBaseContext(), model.ratingList);
                            ratingView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieModel> call, Throwable t) {

                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
