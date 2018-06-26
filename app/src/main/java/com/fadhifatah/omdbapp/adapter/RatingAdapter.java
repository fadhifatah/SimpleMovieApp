package com.fadhifatah.omdbapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fadhifatah.omdbapp.R;
import com.fadhifatah.omdbapp.model.RatingModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingHolder> {
    private Context context;
    private List<RatingModel> list;

    public RatingAdapter(Context context, List<RatingModel> models) {
        this.context = context;
        this.list = new ArrayList<>(models);
    }

    @NonNull
    @Override
    public RatingAdapter.RatingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating, parent, false);
        return new RatingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingAdapter.RatingHolder holder, int position) {
        RatingModel model = list.get(position);

        holder.source.setText(model.source);
        holder.value.setText(model.value);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RatingHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.source)
        TextView source;

        @BindView(R.id.value)
        TextView value;

        public RatingHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
