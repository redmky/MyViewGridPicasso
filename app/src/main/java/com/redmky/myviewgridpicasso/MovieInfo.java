package com.redmky.myviewgridpicasso;

import java.util.Comparator;

/**
 * Created by redmky on 10/18/2015.
 */
//class item for all the movie information that will collect
public class MovieInfo {

    public String title;
    public String release_date;
    public String poster;
    public float vote;
    public String synopsis;
    public float popularity;

        MovieInfo(String title,
                  String release_date,
                  String poster,
                  float vote,
                  String synopsis,
                  float popularity) {

            super();
            this.title = title;
            this.release_date = release_date;
            this.poster = poster;
            this.vote = vote;
            this.synopsis = synopsis;
            this.popularity = popularity;
        }


    /*Comparator for sorting the list by vote rating*/
    public static Comparator<MovieInfo> movieVote = new Comparator<MovieInfo>() {

        public int compare(MovieInfo s1, MovieInfo s2) {

            float vote1 = s1.vote;
            float vote2 = s2.vote;

	        /*For Descending order*/
            return Float.compare(vote2, vote1);

        }};

    /*Comparator for sorting the list by popularity*/
    public static Comparator<MovieInfo> moviePop = new Comparator<MovieInfo>() {

        public int compare(MovieInfo s1, MovieInfo s2) {

            float popularity1 = s1.popularity;
            float popularity2 = s2.popularity;

	        /*For Descending order*/
            return Float.compare(popularity2, popularity1);

        }};
}

