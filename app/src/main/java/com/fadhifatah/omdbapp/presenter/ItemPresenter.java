package com.fadhifatah.omdbapp.presenter;

import com.fadhifatah.omdbapp.listener.ItemListener;
import com.fadhifatah.omdbapp.model.SearchModel;
import com.fadhifatah.omdbapp.service.API;
import com.fadhifatah.omdbapp.util.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemPresenter {
    private ItemListener listener;

    public ItemPresenter(ItemListener itemListener) {
        this.listener = itemListener;
    }

    public void searchItem(String query, String year, String type, int index) {
        API.getClientEvent()
                .create(API.Service.class)
                .search(Constant.API_KEY, query, type, year, index)
                .enqueue(new Callback<SearchModel>() {
                    @Override
                    public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                        listener.OnSearchResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<SearchModel> call, Throwable t) {
                        listener.OnError(t.getMessage());
                    }
                });
    }

    public void loadMoreItems(String query, String year, String type, int index) {
        API.getClientEvent()
                .create(API.Service.class)
                .search(Constant.API_KEY, query, type, year, index)
                .enqueue(new Callback<SearchModel>() {
                    @Override
                    public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                        listener.OnLoadMoreResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<SearchModel> call, Throwable t) {
                        listener.OnError(t.getMessage());
                    }
                });
    }
}
