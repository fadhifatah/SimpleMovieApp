package com.fadhifatah.omdbapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.fadhifatah.omdbapp.R;
import com.fadhifatah.omdbapp.adapter.ItemAdapter;
import com.fadhifatah.omdbapp.base.BaseActivity;
import com.fadhifatah.omdbapp.listener.SearchListener;
import com.fadhifatah.omdbapp.model.ItemModel;
import com.fadhifatah.omdbapp.model.SearchModel;
import com.fadhifatah.omdbapp.presenter.SearchPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements SearchListener {

    private final String TAG = "ActivitySearch";
    private final String STATE_LIST = "state_list";
    private final String STATE_INDEX = "state_index";
    private final String STATE_LOADING = "state_loading";
    private final String STATE_QUERY = "state_query";
    private final String STATE_TYPE = "state_type";
    private final String STATE_YEAR = "state_year";

    private int INDEX = 1;
    private RecyclerView.LayoutManager layoutManager;
    private SearchPresenter presenter;
    private String query = "", type = "", year = "";
    private boolean isLoading = true;
    private List<ItemModel> list = new ArrayList<>();

    @BindView(R.id.year)
    EditText yearET;

    @BindView(R.id.type)
    Spinner typeS;

    @BindView(R.id.query)
    EditText searchET;

    @BindView(R.id.submit)
    ImageButton submitIB;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        presenter = new SearchPresenter(this);

        setUpLayoutManager();

        if (savedInstanceState != null) {
            list = new ArrayList<>((List<ItemModel>) savedInstanceState.getSerializable(STATE_LIST));
            ItemAdapter adapter = new ItemAdapter(this, list);
            recyclerView.setAdapter(adapter);

            INDEX = savedInstanceState.getInt(STATE_INDEX);
            isLoading = savedInstanceState.getBoolean(STATE_LOADING);
            query = savedInstanceState.getString(STATE_QUERY);
            type = savedInstanceState.getString(STATE_TYPE);
            year = savedInstanceState.getString(STATE_YEAR);
            Log.d(TAG, "State loaded");
        }
    }

    @Override
    protected int findLayoutById() {
        return R.layout.activity_search;
    }

    private void setUpLayoutManager() {
        setUpGridRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    Log.d(TAG, "Scroll down");
                    GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                    int visibleItemCount = gridLayoutManager.getChildCount();
                    int totalItemCount = gridLayoutManager.getItemCount();
                    int firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                    if (isLoading) {
                        if ((visibleItemCount + firstVisibleItem) >= totalItemCount) {
                            Log.d(TAG, "Scroll to end of list");
                            isLoading = false;
                            presenter.loadMoreItems(query, year, type, INDEX);
                        }
                    }
                }
            }
        });
    }

    @OnClick(R.id.submit)
    public void onSubmit() {
        hideKeyboardInput();
        INDEX = 1;
        query = searchET.getText().toString().trim();
        year = yearET.getText().toString().trim();
        type = typeS.getSelectedItem().toString().equalsIgnoreCase("all") ?
                null : typeS.getSelectedItem().toString().toLowerCase();
        if (TextUtils.isEmpty(query)) {
            Snackbar.make(recyclerView, "Search field can't be empty", Snackbar.LENGTH_SHORT).show();
        }
        else {
            presenter.searchItem(query, year, type, INDEX);
        }
    }

    @Override
    public void OnSearchResponse(SearchModel model) {
        if (model.response.equalsIgnoreCase("true")) {
            Log.d(TAG, "Get search result: " + model.searchList.size());
            list = new ArrayList<>(model.searchList);
            ItemAdapter adapter = new ItemAdapter(this, list);
            recyclerView.setAdapter(adapter);

            isLoading = true;
            INDEX++;
        }
        else {
            Snackbar.make(recyclerView, model.error, Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    public void OnLoadMoreResponse(SearchModel model) {
        if (model.response.equalsIgnoreCase("true")) {
            Log.d(TAG, "Get load more result");
            ((ItemAdapter) recyclerView.getAdapter()).addNewList(model.searchList);
            recyclerView.getAdapter().notifyDataSetChanged();

            list = new ArrayList<>(((ItemAdapter) recyclerView.getAdapter()).getList());
            isLoading = true;
            INDEX++;
        }
        else {
            Snackbar.make(recyclerView, "There are no more items", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnError(String error) {
        Snackbar.make(recyclerView, "Time out! " + error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_LIST, new ArrayList<>(list));
        outState.putInt(STATE_INDEX, INDEX);
        outState.putBoolean(STATE_LOADING, isLoading);
        outState.putString(STATE_QUERY, query);
        outState.putString(STATE_TYPE, type);
        outState.putString(STATE_YEAR, year);
        Log.d(TAG, "State saved");
    }
}
