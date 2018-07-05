package com.fadhifatah.omdbapp.module.detail.listener;

import com.fadhifatah.omdbapp.module.detail.presenter.MoviePresenter;

public interface DetailListener {

    void OnResultResponse(MoviePresenter moviePresenter);

    void OnResultResponseWithoutPoster(MoviePresenter moviePresenter);

    void OnError(String error);
}
