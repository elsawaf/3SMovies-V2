package com.elsawaf.thebrilliant.a3smovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by The Brilliant on 15/03/2018.
 */

public class Movie implements Parcelable {
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("overview")
    private String summary;
    @SerializedName("vote_average")
    private double userRating;
    @SerializedName("vote_count")
    private int numberOfUsers;
    @SerializedName("release_date")
    private String releaseDate;
    private int id;
    private boolean isFavourite;

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public int getId() {
        return id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getSummary() {
        return summary;
    }

    public double getUserRating() {
        return userRating;
    }

    public int getNumberOfUsers() {
        return numberOfUsers;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.originalTitle);
        dest.writeString(this.posterPath);
        dest.writeString(this.summary);
        dest.writeDouble(this.userRating);
        dest.writeInt(this.numberOfUsers);
        dest.writeString(this.releaseDate);
        dest.writeInt(this.id);
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        this.originalTitle = in.readString();
        this.posterPath = in.readString();
        this.summary = in.readString();
        this.userRating = in.readDouble();
        this.numberOfUsers = in.readInt();
        this.releaseDate = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
