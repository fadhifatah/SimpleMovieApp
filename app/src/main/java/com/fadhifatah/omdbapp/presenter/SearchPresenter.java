package com.fadhifatah.omdbapp.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.fadhifatah.omdbapp.adapter.ItemAdapter;
import com.fadhifatah.omdbapp.listener.SearchListener;
import com.fadhifatah.omdbapp.model.SearchModel;
import com.fadhifatah.omdbapp.service.API;
import com.fadhifatah.omdbapp.util.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresenter {

    private SearchListener listener;

    public SearchPresenter(SearchListener listener) {
        this.listener = listener;
    }

    public void searchItem(SearchInstance instance) {
        if (TextUtils.isEmpty(instance.getQuery())) {
            listener.OnError("Search field can't empty");
        }
        else {
            API.getClientEvent()
                    .create(API.Service.class)
                    .search(Constant.API_KEY, instance.getQuery(), instance.getType(), instance.getYear(), instance.getPage())
                    .enqueue(new Callback<SearchModel>() {
                        @Override
                        public void onResponse(@NonNull Call<SearchModel> call, @NonNull Response<SearchModel> response) {
                            if (response.isSuccessful()) {
                                SearchModel model = response.body();
                                if (model != null) {
                                    if (model.response.equalsIgnoreCase("true"))
                                        listener.OnSearchResponse(new ItemAdapter(model.searchList));
                                    else
                                        listener.OnError(model.error);
                                }
                            }
                            else
                                listener.OnError(response.message());
                        }

                        @Override
                        public void onFailure(@NonNull Call<SearchModel> call, @NonNull Throwable t) {
                            listener.OnError("Time out! " + t.getMessage());
                        }
                    });
        }
    }

    public void loadMoreItems(SearchInstance instance) {
        API.getClientEvent()
                .create(API.Service.class)
                .search(Constant.API_KEY, instance.getQuery(), instance.getType(), instance.getYear(), instance.getPage())
                .enqueue(new Callback<SearchModel>() {
                    @Override
                    public void onResponse(@NonNull Call<SearchModel> call, @NonNull Response<SearchModel> response) {
                        if (response.isSuccessful()) {
                            SearchModel model = response.body();
                            if (model != null) {
                                if (model.response.equalsIgnoreCase("true"))
                                    listener.OnLoadMoreResponse(model);
                                else
                                    listener.OnError("There are no more items! " + model.error);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SearchModel> call, @NonNull Throwable t) {
                        listener.OnError("Time out! " + t.getMessage());
                    }
                });
    }
}
