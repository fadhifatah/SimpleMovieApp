package com.fadhifatah.omdbapp.module.detail.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fadhifatah.omdbapp.R;
import com.fadhifatah.omdbapp.module.detail.adapter.RatingAdapter;
import com.fadhifatah.omdbapp.base.BaseActivity;
import com.fadhifatah.omdbapp.module.detail.listener.DetailListener;
import com.fadhifatah.omdbapp.module.detail.presenter.DetailPresenter;
import com.fadhifatah.omdbapp.util.Constant;
import com.fadhifatah.omdbapp.module.detail.presenter.MoviePresenter;

import butterknife.BindView;

public class DetailActivity extends BaseActivity implements DetailListener{

    private final static String TAG = "ActivityDetail";

    private ProgressDialog dialog;
    private DetailPresenter presenter = new DetailPresenter(this);

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
    protected void init(@Nullable Bundle savedInstanceState) {
//        Set-up toolbar
        Log.d(TAG, "Get intent");
        String imdbId = (String) getIntent().getSerializableExtra(Constant.IMDB);
        setTitle(imdbId);
        showNavigateUp();

        setUpLinearRecyclerView(ratingView, LinearLayoutManager.HORIZONTAL);
        Log.d(TAG, "Set up complete");

        dialog = setUpProgressDialog("Loading...");
        dialog.show();
        Log.d(TAG, "Loading...");

        presenter.getMovieDetail(imdbId);
    }

    @Override
    protected int findLayoutById() {
        return R.layout.activity_detail;
    }

    @Override
    public void OnResultResponse(MoviePresenter moviePresenter) {
        Log.d(TAG, "Get result");
        poster.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(poster.getContext())
                .load(moviePresenter.getPosterUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(poster);

        setUpAllTextView(moviePresenter);

        ratingView.setAdapter(new RatingAdapter(getBaseContext(), moviePresenter.getRatingList()));
        dialog.dismiss();
    }

    @Override
    public void OnResultResponseWithoutPoster(MoviePresenter moviePresenter) {
        Log.d(TAG, "Get result");
        setUpAllTextView(moviePresenter);

        ratingView.setAdapter(new RatingAdapter(getBaseContext(), moviePresenter.getRatingList()));
        dialog.dismiss();
    }

    private void setUpAllTextView(MoviePresenter moviePresenter) {
        title.setText(moviePresenter.getTitle());
        information.setText(moviePresenter.getInformation());
        additional.setText(moviePresenter.getAdditionalInfo());
        scores.setText(moviePresenter.getScores());
        director.setText(moviePresenter.getDirectors());
        awards.setText(moviePresenter.getAwards());
        writer.setText(moviePresenter.getWriters());
        actor.setText(moviePresenter.getActors());
        dvd.setText(moviePresenter.getDVD());
        website.setText(moviePresenter.getWebsite());
        boxOffice.setText(moviePresenter.getBoxOffce());
        plot.setText(moviePresenter.getPlot());
    }

    @Override
    public void OnError(String error) {
        dialog.dismiss();
        showSnackbar(error);
    }
}
