package com.fadhifatah.omdbapp.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.fadhifatah.omdbapp.service.API;
import com.fadhifatah.omdbapp.util.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private ProgressDialog dialog;

    @BindView(R.id.poster_detail)
    ImageView poster;

    @BindView(R.id.title_detail)
    TextView title;

    @BindView(R.id.information_detail)
    TextView information;

    @BindView(R.id.additional_detail)
    TextView additional;

    @BindView(R.id.scores)
    TextView scores;

    @BindView(R.id.directed)
    TextView director;

    @BindView(R.id.awards)
    TextView awards;

    @BindView(R.id.writer)
    TextView writer;

    @BindView(R.id.actor)
    TextView actor;

    @BindView(R.id.rating_detail)
    RecyclerView ratingView;

    @BindView(R.id.plot_detail)
    TextView plot;

    @BindView(R.id.dvd)
    TextView dvd;

    @BindView(R.id.website)
    TextView website;

    @BindView(R.id.box_office)
    TextView boxOffice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        String imdbId = (String) getIntent().getSerializableExtra(Constant.IMDB);
        getSupportActionBar().setTitle(imdbId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.show();

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
                                    model.language + ") • " + model.production;
                            additional.setText(s);

                            s = "Metascore - " + model.metascore + " • IMDb Rating - " + model.imdbRating +
                                    " • " + model.imdbVotes + " votes";
                            scores.setText(s);

                            s = "Directed by " + model.director;
                            director.setText(s);

                            s = "Awards - " + model.awards;
                            awards.setText(s);

                            writer.setText(model.writer);
                            actor.setText(model.actors);
                            dvd.setText(model.dvd);
                            website.setText(model.website);
                            boxOffice.setText(model.boxOffice);

                            s = "\"" + model.plot + "\"";
                            plot.setText(s);

                            RatingAdapter adapter = new RatingAdapter(getBaseContext(), model.ratingList);
                            ratingView.setAdapter(adapter);
                        }
                        else {
                            Snackbar.make(ratingView, model.error, Snackbar.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<MovieModel> call, Throwable t) {
                        dialog.dismiss();
                        Snackbar.make(ratingView, "Time out! " + t.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
