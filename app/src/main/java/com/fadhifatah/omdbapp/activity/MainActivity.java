package com.fadhifatah.omdbapp.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.fadhifatah.omdbapp.R;
import com.fadhifatah.omdbapp.model.ItemModel;
import com.fadhifatah.omdbapp.model.SearchModel;
import com.fadhifatah.omdbapp.service.API;
import com.fadhifatah.omdbapp.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private int INDEX = 1;
    private List<ItemModel> list = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;

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

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(this, 2);
        }
        else {
            layoutManager = new GridLayoutManager(this, 3);
        }
        recyclerView.setLayoutManager(layoutManager);
    }

    @OnClick(R.id.submit)
    public void onSubmit() {
        INDEX = 1;
        list = new ArrayList<>();

        String query = searchET.getText().toString().trim();
        String year = yearET.getText().toString().trim();
        String type = typeS.getSelectedItem().toString().equalsIgnoreCase("all") ?
                null : typeS.getSelectedItem().toString();
        if (TextUtils.isEmpty(query)) {
            Snackbar.make(recyclerView, "Search field can't be empty", Snackbar.LENGTH_SHORT).show();
        }
        else {
            API.getClientEvent()
                    .create(API.Service.class)
                    .search(Constant.API_KEY, query, type, year, INDEX)
                    .enqueue(new Callback<SearchModel>() {
                        @Override
                        public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                            if (response.body().response.equalsIgnoreCase("true")) {
                                list.addAll(response.body().searchList);
                            }
                        }

                        @Override
                        public void onFailure(Call<SearchModel> call, Throwable t) {

                        }
                    });
        }
    }
}
