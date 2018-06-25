package com.fadhifatah.omdbapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.fadhifatah.omdbapp.R;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.search)
    SearchView searchView;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
