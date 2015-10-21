package com.redmky.myviewgridpicasso;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

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
    public String title;
    public String release_date;
    public String poster;
    public float vote;
    /*Comparator for sorting the list by vote rating*/
    public static Comparator<MovieInfo> movieVote = new Comparator<MovieInfo>() {

        public int compare(MovieInfo s1, MovieInfo s2) {

            float vote1 = s1.vote;
            float vote2 = s2.vote;

	        /*For Descending order*/
            return Float.compare(vote2, vote1);

        }
    };
    public String synopsis;
    public float popularity;
    /*Comparator for sorting the list by popularity*/
    public static Comparator<MovieInfo> moviePop = new Comparator<MovieInfo>() {

        public int compare(MovieInfo s1, MovieInfo s2) {

            float popularity1 = s1.popularity;
            float popularity2 = s2.popularity;

	        /*For Descending order*/
            return Float.compare(popularity2, popularity1);

        }
    };
    public String id;

    public MovieInfo(String title,
                     String release_date,
                     String poster,
                     float vote,
                     String synopsis,
                     float popularity,
                     String id) {

        this.title = title;
        this.release_date = release_date;
        this.poster = poster;
        this.vote = vote;
        this.synopsis = synopsis;
        this.popularity = popularity;
        this.id = id;
    }

    public MovieInfo(Parcel in) {
        this.title = in.readString();
        this.release_date = in.readString();
        this.poster = in.readString();
        this.vote = in.readFloat();
        this.synopsis = in.readString();
        this.popularity = in.readFloat();
        this.id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Storing the Movie data to Parcel object
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(title);
        dest.writeString(release_date);
        dest.writeString(poster);
        dest.writeFloat(vote);
        dest.writeString(synopsis);
        dest.writeFloat(popularity);
        dest.writeString(id);
    }

}

