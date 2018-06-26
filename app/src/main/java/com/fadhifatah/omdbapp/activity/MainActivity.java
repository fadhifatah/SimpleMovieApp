package com.fadhifatah.omdbapp.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.fadhifatah.omdbapp.R;
import com.fadhifatah.omdbapp.adapter.ItemAdapter;
import com.fadhifatah.omdbapp.listener.ItemListener;
import com.fadhifatah.omdbapp.model.ItemModel;
import com.fadhifatah.omdbapp.model.SearchModel;
import com.fadhifatah.omdbapp.presenter.ItemPresenter;
import com.fadhifatah.omdbapp.util.Constant;
import com.fadhifatah.omdbapp.util.GridSpacingItemDecoration;
import com.fadhifatah.omdbapp.util.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ItemListener {
    private int INDEX = 1;
    private RecyclerView.LayoutManager layoutManager;
    private ItemPresenter presenter;
    private String query, type, year;
    private int pastVisibleItem, visibleItemCount, totalItemCount;
    private boolean isLoading = true;

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
        setUpLayoutManager();
    }

    private void setUpLayoutManager() {
        int spanCount;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            spanCount = 2;
        }
        else {
            spanCount = 3;
        }
        layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, dpToPx(), true));

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//                Toast.makeText(getApplicationContext(), ((ItemAdapter) recyclerView.getAdapter()).getList().get(position).imdbId, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra(Constant.IMDB, ((ItemAdapter) recyclerView.getAdapter()).getList().get(position).imdbId);
                startActivity(intent);
            }
        });

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
            List<ItemModel> list = new ArrayList<>(model.searchList);

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
        Snackbar.make(recyclerView, error, Snackbar.LENGTH_SHORT).show();
    }
}
