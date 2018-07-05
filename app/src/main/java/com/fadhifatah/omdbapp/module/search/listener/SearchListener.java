package com.fadhifatah.omdbapp.module.search.listener;

import com.fadhifatah.omdbapp.module.search.adapter.ItemAdapter;
import com.fadhifatah.omdbapp.module.search.model.SearchModel;

public interface SearchListener {

    void OnSearchResponse(ItemAdapter adapter);

    void OnLoadMoreResponse(SearchModel model);

    void OnError(String error);
}
