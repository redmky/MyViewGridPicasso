package com.redmky.myviewgridpicasso.Data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by redmky on 11/2/2015.
 */
@ContentProvider(authority = MovieProvider.AUTHORITY, database = MovieDatabase.class)
public final class MovieProvider {
    public static final String AUTHORITY =
            "com.redmky.myviewgridpicasso.Data.MovieProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path{
        String ARCHIVED_MOVIES ="archived_movies";
        String ARCHIVED_REVIEWS ="archived_reviews";
        String ARCHIVED_TRAILERS ="archived_trailers";
    }
    private static Uri buildUri(String ... paths){
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = MovieDatabase.ARCHIVED_MOVIES) public static class ArchivedMovies {
        @ContentUri(
                path = Path.ARCHIVED_MOVIES,
                type = "vnd.android.cursor.dir/archived_movies",
                defaultSort = ArchivedMovieColumns.VOTES + " ASC"
        )
        public static final Uri CONTENT_URI = buildUri(Path.ARCHIVED_MOVIES);

        @InexactContentUri(
                name = "ARCHIVED_MOVIE_ID",
                path = Path.ARCHIVED_MOVIES + "/#",
                type = "vnd.android.cursor.item/archived_movies",
                whereColumn = ArchivedMovieColumns._ID,
                pathSegment = 1
        )
        public static Uri withId(long id){
            return buildUri(Path.ARCHIVED_MOVIES, String.valueOf(id));
        }
    }

    @TableEndpoint(table = MovieDatabase.ARCHIVED_REVIEWS) public static class ArchivedReviews {
        @ContentUri(
                path = Path.ARCHIVED_REVIEWS,
                type = "vnd.android.cursor.dir/archived_reviews",
                defaultSort = ArchivedChildColumns._ID + " ASC"
        )
        public static final Uri CONTENT_URI = buildUri(Path.ARCHIVED_REVIEWS);

        @InexactContentUri(
                name = "ARCHIVED_MOVIE_ID",
                path = Path.ARCHIVED_REVIEWS + "/#",
                type = "vnd.android.cursor.item/archived_reviews",
                whereColumn = ArchivedChildColumns.MOVIE_ID,
                pathSegment = 1
        )
        public static Uri withId(long id){
            return buildUri(Path.ARCHIVED_REVIEWS, String.valueOf(id));
        }
    }

    @TableEndpoint(table = MovieDatabase.ARCHIVED_TRAILERS) public static class ArchivedTrailers {
        @ContentUri(
                path = Path.ARCHIVED_TRAILERS,
                type = "vnd.android.cursor.dir/archived_trailers",
                defaultSort = ArchivedChildColumns._ID + " ASC"
        )
        public static final Uri CONTENT_URI = buildUri(Path.ARCHIVED_TRAILERS);

        @InexactContentUri(
                name = "ARCHIVED_MOVIE_ID",
                path = Path.ARCHIVED_TRAILERS + "/#",
                type = "vnd.android.cursor.item/archived_trailers",
                whereColumn = ArchivedChildColumns.MOVIE_ID,
                pathSegment = 1
        )
        public static Uri withId(long id){
            return buildUri(Path.ARCHIVED_TRAILERS, String.valueOf(id));
        }
    }
}