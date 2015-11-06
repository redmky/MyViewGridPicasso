package com.redmky.myviewgridpicasso;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by redmky on 10/18/2015.
 */
//class item for all the movie information that will collect
public class MovieInfo implements Parcelable {

    public static final Parcelable.Creator<MovieInfo>
            CREATOR = new Parcelable.Creator<MovieInfo>() {

        @Override
        public MovieInfo createFromParcel(Parcel source) {
            return new MovieInfo(source);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };
    public int position;
    public String title;
    public String release_date;
    public String poster;
    public float vote;
    public boolean favorite;
    public String synopsis;
    public float popularity;
    public String id;
    public ArrayList<MovieByIdInfo> trailerData;
    public ArrayList<MovieByIdInfo> reviewData;

    public MovieInfo(int position,
                     String title,
                     String release_date,
                     String poster,
                     float vote,
                     String synopsis,
                     float popularity,
                     String id,
                     boolean favorite,
                     ArrayList<MovieByIdInfo> trailerData,
                     ArrayList<MovieByIdInfo> reviewData) {

        this.position = position;
        this.title = title;
        this.release_date = release_date;
        this.poster = poster;
        this.vote = vote;
        this.synopsis = synopsis;
        this.popularity = popularity;
        this.id = id;
        this.favorite = favorite;
        this.trailerData = trailerData;
        this.reviewData = reviewData;
    }

    public MovieInfo(Parcel in) {
        this.position = in.readInt();
        this.title = in.readString();
        this.release_date = in.readString();
        this.poster = in.readString();
        this.vote = in.readFloat();
        this.synopsis = in.readString();
        this.popularity = in.readFloat();
        this.id = in.readString();
        this.favorite = in.readByte() != 0;

        this.trailerData = in.readArrayList(null);
        this.reviewData = in.readArrayList(null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Storing the Movie data to Parcel object
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(position);
        dest.writeString(title);
        dest.writeString(release_date);
        dest.writeString(poster);
        dest.writeFloat(vote);
        dest.writeString(synopsis);
        dest.writeFloat(popularity);
        dest.writeString(id);
        dest.writeInt(favorite ? 1 : 0);
        dest.writeList(trailerData);
        dest.writeList(reviewData);
    }
}

