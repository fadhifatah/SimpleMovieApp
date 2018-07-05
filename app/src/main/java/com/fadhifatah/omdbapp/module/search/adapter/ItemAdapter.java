package com.fadhifatah.omdbapp.module.search.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fadhifatah.omdbapp.R;
import com.fadhifatah.omdbapp.module.detail.view.DetailActivity;
import com.fadhifatah.omdbapp.module.search.model.ItemModel;
import com.fadhifatah.omdbapp.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {
    private List<ItemModel> list;

    public ItemAdapter(List<ItemModel> models) {
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
        final ItemModel model = list.get(position);

        holder.title.setText(model.title);
        String s = String.valueOf(model.type.charAt(0)).toUpperCase() + model.type.substring(1) + " • " + model.year;
        holder.yearType.setText(s);

        if (!model.posterUrl.equalsIgnoreCase("n/a")) {
            holder.poster.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(holder.poster.getContext())
                    .load(model.posterUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.poster);
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                intent.putExtra(Constant.IMDB, model.imdbId);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_movie_layout)
        LinearLayout layout;

        @BindView(R.id.poster)
        ImageView poster;

        @BindView(R.id.movieTitle)
        TextView title;

        @BindView(R.id.year_type)
        TextView yearType;

        ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
