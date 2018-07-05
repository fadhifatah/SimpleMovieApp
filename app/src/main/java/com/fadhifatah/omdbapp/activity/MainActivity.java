package com.fadhifatah.omdbapp.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.fadhifatah.omdbapp.R;
import com.fadhifatah.omdbapp.adapter.ItemAdapter;
import com.fadhifatah.omdbapp.listener.ItemListener;
import com.fadhifatah.omdbapp.model.ItemModel;
import com.fadhifatah.omdbapp.model.SearchModel;
import com.fadhifatah.omdbapp.presenter.ItemPresenter;
import com.fadhifatah.omdbapp.util.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ItemListener {
    private final String STATE_LIST = "state_list";
    private final String STATE_INDEX = "state_index";
    private final String STATE_LOADING = "state_loading";
    private final String STATE_QUERY = "state_query";
    private final String STATE_TYPE = "state_type";
    private final String STATE_YEAR = "state_year";

    private int INDEX = 1;
    private RecyclerView.LayoutManager layoutManager;
    private ItemPresenter presenter;
    private String query = "", type = "", year = "";
    private int pastVisibleItem, visibleItemCount, totalItemCount;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new ItemPresenter(this);

        searchET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    submitIB.performClick();
                    return true;
                }
                return false;
            }
        });
        setUpLayoutManager();

        if (savedInstanceState != null) {
            Log.d("SAVED_INSTANCE_BEFORE", "INDEX -> " + INDEX);
            Log.d("SAVED_INSTANCE_BEFORE", "list -> " + list.size());
            Log.d("SAVED_INSTANCE_BEFORE", "isLoading -> " + isLoading);

            list = new ArrayList<>((List<ItemModel>) savedInstanceState.getSerializable(STATE_LIST));
            ItemAdapter adapter = new ItemAdapter(this, list);
            recyclerView.setAdapter(adapter);

            INDEX = savedInstanceState.getInt(STATE_INDEX);
            isLoading = savedInstanceState.getBoolean(STATE_LOADING);
            query = savedInstanceState.getString(STATE_QUERY);
            type = savedInstanceState.getString(STATE_TYPE);
            year = savedInstanceState.getString(STATE_YEAR);

            Log.d("SAVED_INSTANCE_AFTER", "INDEX -> " + INDEX);
            Log.d("SAVED_INSTANCE_AFTER", "list -> " + list.size());
            Log.d("SAVED_INSTANCE_AFTER", "isLoading -> " + isLoading);
        }
    }

    private void setUpLayoutManager() {
        int spanCount;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            spanCount = 2;
        }
        else {
            spanCount = 4;
        }
        layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, dpToPx(), true));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    Log.d("ON_SCROLLED", "Down");
                    GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                    visibleItemCount = gridLayoutManager.getChildCount();
                    totalItemCount = gridLayoutManager.getItemCount();
                    pastVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                    if (isLoading) {
                        if ((visibleItemCount + pastVisibleItem) >= totalItemCount) {
                            Log.d("ON_SCROLLED", "End of list");
                            isLoading = false;
                            Log.d("ON_SCROLLED", "Run Presenter");
                            Log.d("ON_SCROLLED", "Page: " + INDEX);
                            presenter.loadMoreItems(query, year, type, INDEX);
                        }
                    }
                }
            }
        });
    }

    private int dpToPx() {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));
    }

    @OnClick(R.id.submit)
    public void onSubmit() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        INDEX = 1;
        query = searchET.getText().toString().trim();
        year = yearET.getText().toString().trim();
        type = typeS.getSelectedItem().toString().equalsIgnoreCase("all") ?
                null : typeS.getSelectedItem().toString().toLowerCase();
        if (TextUtils.isEmpty(query)) {
            Snackbar.make(recyclerView, "Search field can't be empty", Snackbar.LENGTH_SHORT).show();
        }
        else {
            Log.d("ON_SUBMIT", "Run Presenter");
            Log.d("ON_SUBMIT", "Page: " + INDEX);
            presenter.searchItem(query, year, type, INDEX);
        }
    }

    @Override
    public void OnSearchResponse(SearchModel model) {
        if (model.response.equalsIgnoreCase("true")) {
            Log.d("SEARCH_RESPONSE", "Success");
            list = new ArrayList<>(model.searchList);
            Log.d("SEARCH_RESPONSE", "List size: " + list.size());
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
            Log.d("LOAD_MORE_RESPONSE", "Success");
            ((ItemAdapter) recyclerView.getAdapter()).addNewList(model.searchList);
            recyclerView.getAdapter().notifyDataSetChanged();

            list = new ArrayList<>(((ItemAdapter) recyclerView.getAdapter()).getList());
            Log.d("LOAD_MORE_RESPONSE", "New items added");
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
    }
}
