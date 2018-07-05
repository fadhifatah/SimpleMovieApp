package com.fadhifatah.omdbapp.listener;

import com.fadhifatah.omdbapp.adapter.ItemAdapter;
import com.fadhifatah.omdbapp.model.SearchModel;

public interface SearchListener {

    void OnSearchResponse(ItemAdapter adapter);

    void OnLoadMoreResponse(SearchModel model);

    void OnError(String error);
}
