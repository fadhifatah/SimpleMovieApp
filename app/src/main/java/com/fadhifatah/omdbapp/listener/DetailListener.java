package com.fadhifatah.omdbapp.listener;

import com.fadhifatah.omdbapp.util.MoviePresenter;

public interface DetailListener {

    void OnResultResponse(MoviePresenter moviePresenter);

    void OnError(String error);
}
