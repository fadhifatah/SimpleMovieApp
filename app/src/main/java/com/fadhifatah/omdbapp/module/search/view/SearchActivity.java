package com.fadhifatah.omdbapp.module.search.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.fadhifatah.omdbapp.R;
import com.fadhifatah.omdbapp.module.search.adapter.ItemAdapter;
import com.fadhifatah.omdbapp.base.BaseActivity;
import com.fadhifatah.omdbapp.module.search.listener.SearchListener;
import com.fadhifatah.omdbapp.module.search.model.ItemModel;
import com.fadhifatah.omdbapp.module.search.model.SearchModel;
import com.fadhifatah.omdbapp.module.search.presenter.SearchInstance;
import com.fadhifatah.omdbapp.module.search.presenter.SearchPresenter;
import com.fadhifatah.omdbapp.util.LoadMoreScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements SearchListener {

    private final String TAG = "ActivitySearch";
    private final String STATE_LIST = "listState";
    private final String STATE_INSTANCE = "instanceState";

    private SearchPresenter presenter;
    private SearchInstance instance;
    private List<ItemModel> list = new ArrayList<>();
    private LoadMoreScrollListener scrollListener;

    @BindView(R.id.year)
    EditText yearField;

    @BindView(R.id.type)
    Spinner typeSelector;

    @BindView(R.id.query)
    EditText queryField;

    @BindView(R.id.submit)
    ImageButton submit;

    @BindView(R.id.list)
    RecyclerView resultRecyclerView;

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        presenter = new SearchPresenter(this);
        setUpGridRecyclerView(resultRecyclerView);

        scrollListener = new LoadMoreScrollListener((GridLayoutManager) resultRecyclerView.getLayoutManager()) {
            @Override
            protected void onLoadMore() {
                presenter.loadMoreItems(instance);
            }
        };
        resultRecyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    protected int findLayoutById() {
        return R.layout.activity_search;
    }

    @OnClick(R.id.submit)
    public void onSubmit() {
        hideKeyboardInput();
        String query = queryField.getText().toString().trim();
        String year = yearField.getText().toString().trim();
        String type = typeSelector.getSelectedItem().toString().equalsIgnoreCase("all") ?
                null : typeSelector.getSelectedItem().toString().toLowerCase();

        scrollListener.resetState();
        instance = new SearchInstance(query, year, type, 1);
        presenter.searchItem(instance);
    }

    @Override
    public void OnSearchResponse(ItemAdapter adapter) {
        Log.d(TAG, "Get search result");
        resultRecyclerView.setAdapter(adapter);

//        Update instance
        list = new ArrayList<>(adapter.getList());
        instance.nextPage();
    }

    @Override
    public void OnLoadMoreResponse(SearchModel model) {
        Log.d(TAG, "Get more items");
        ((ItemAdapter) resultRecyclerView.getAdapter()).addNewList(model.searchList);
        resultRecyclerView.getAdapter().notifyDataSetChanged();

//            Update instance
        list = new ArrayList<>(((ItemAdapter) resultRecyclerView.getAdapter()).getList());
        instance.nextPage();
    }

    @Override
    public void OnError(String error) {
        showSnackbar(error);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_LIST, new ArrayList<>(list));
        outState.putParcelable(STATE_INSTANCE, instance);
        Log.d(TAG, "State saved");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            instance = savedInstanceState.getParcelable(STATE_INSTANCE);
            list = new ArrayList<>((List<ItemModel>) savedInstanceState.getSerializable(STATE_LIST));
            resultRecyclerView.setAdapter(new ItemAdapter(list));
            Log.d(TAG, "State loaded");
        }
    }
}
