package com.fadhifatah.omdbapp.presenter;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

public class SearchInstance implements Parcelable{
    private String query;
    private String year;
    private String type;
    private int page;

    public SearchInstance(String query, @Nullable String year, String type, int page) {
        this.query = query;
        this.year = year;
        this.type = type;
        this.page = page;
    }

    private SearchInstance(Parcel in) {
        query = in.readString();
        year = in.readString();
        type = in.readString();
        page = in.readInt();
    }

    public static final Creator<SearchInstance> CREATOR = new Creator<SearchInstance>() {
        @Override
        public SearchInstance createFromParcel(Parcel in) {
            return new SearchInstance(in);
        }

        @Override
        public SearchInstance[] newArray(int size) {
            return new SearchInstance[size];
        }
    };

    public int getPage() {
        return page;
    }

    public String getQuery() {
        return query;
    }

    public String getType() {
        return type;
    }

    public String getYear() {
        return year;
    }

    public void nextPage() {
        page++;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(query);
        parcel.writeString(year);
        parcel.writeString(type);
        parcel.writeInt(page);
    }
}
