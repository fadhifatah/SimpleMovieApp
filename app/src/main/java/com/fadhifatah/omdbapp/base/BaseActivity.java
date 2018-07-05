package com.fadhifatah.omdbapp.base;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;

import com.fadhifatah.omdbapp.util.GridSpacingItemDecoration;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(findLayoutById());
        ButterKnife.bind(this);
        init(savedInstanceState);
    }

    protected abstract void init(@Nullable Bundle savedInstanceState);

    protected abstract int findLayoutById();

    public void setUpGridRecyclerView(RecyclerView view) {
        int spanCount;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            spanCount = 2;
        }
        else {
            spanCount = 4;
        }
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        view.addItemDecoration(new GridSpacingItemDecoration(spanCount, dpToPx(), true));

    }

    private int dpToPx() {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));
    }

    public void hideKeyboardInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
