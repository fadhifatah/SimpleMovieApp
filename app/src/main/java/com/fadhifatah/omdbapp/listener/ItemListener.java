package com.fadhifatah.omdbapp.listener;

import com.fadhifatah.omdbapp.model.SearchModel;

public interface ItemListener {
    void OnSearchResponse(SearchModel model);
    void OnLoadMoreResponse(SearchModel model);
    void OnError(String error);
}
