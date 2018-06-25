package com.fadhifatah.omdbapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fadhifatah.omdbapp.R;
import com.fadhifatah.omdbapp.model.ItemModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {
    private Context context;
    private List<ItemModel> list;

    public ItemAdapter(Context context, List<ItemModel> models) {
        this.context = context;
        this.list = new ArrayList<>(models);
    }

    public List<ItemModel> getList() {
        return list;
    }

    public void addNewList(List<ItemModel> models) {
        list.addAll(models);
    }

    @NonNull
    @Override
    public ItemAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemHolder holder, int position) {
        ItemModel model = list.get(position);

        holder.title.setText(model.title);
        holder.yearType.setText(model.year + " | " + model.type.toUpperCase());

        if (!model.posterUrl.equalsIgnoreCase("n/a")) {
            Glide.with(context)
                    .load(model.posterUrl)
                    .into(holder.poster);
        }
        else {
            holder.poster.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.poster)
        ImageView poster;

        @BindView(R.id.movieTitle)
        TextView title;

        @BindView(R.id.year_type)
        TextView yearType;
        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
