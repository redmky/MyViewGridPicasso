package com.redmky.myviewgridpicasso.Data;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.redmky.myviewgridpicasso.MovieByIdInfo;
import com.redmky.myviewgridpicasso.MovieInfo;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

import java.util.ArrayList;

/**
 * Created by redmky on 11/1/2015.
 */
@Database(version = MovieDatabase.VERSION)
public final class MovieDatabase {
    private static final String LOG_TAG = MovieDatabase.class.getSimpleName();

    private MovieDatabase() {
    }

    public static final int VERSION = 1;

    @Table(ArchivedMovieColumns.class)
    public static final String ARCHIVED_MOVIES =
            "archived_movies";

    @Table(ArchivedChildColumns.class)
    public static final String ARCHIVED_REVIEWS =
            "archived_reviews";

    @Table(ArchivedChildColumns.class)
    public static final String ARCHIVED_TRAILERS =
            "archived_trailers";


    //insert to DB
    public static void insertToFavorites(Context mC, MovieInfo movieInfo) {

        ContentValues cv = new ContentValues();

        //cv.put(ArchivedMovieColumns._ID, 1);
        cv.put(ArchivedMovieColumns.NAME, movieInfo.title);
        cv.put(ArchivedMovieColumns.IMAGE_RESOURCE, movieInfo.poster);
        cv.put(ArchivedMovieColumns.VOTES, movieInfo.vote);
        cv.put(ArchivedMovieColumns.SYNOPSYS, movieInfo.synopsis);
        cv.put(ArchivedMovieColumns.RELEASE_DATE, movieInfo.release_date);
        cv.put(ArchivedMovieColumns.MOVIE_ID, movieInfo.id);

        Uri uri = mC.getContentResolver().insert(MovieProvider.ArchivedMovies.CONTENT_URI, cv);
        insertReviewsToFavorites(mC, movieInfo);
        insertTrailersToFavorites(mC, movieInfo);

        Toast.makeText(((Activity) mC).getBaseContext(),
                uri.toString(), Toast.LENGTH_LONG).show();

    }

    public static void insertReviewsToFavorites(Context mC, MovieInfo movieInfo) {
        Log.d(LOG_TAG, "insert");

        ArrayList<ContentProviderOperation> batchOperations =
                new ArrayList<>(movieInfo.reviewData.size());

        for (MovieByIdInfo review : movieInfo.reviewData) {
            ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                    MovieProvider.ArchivedReviews.CONTENT_URI);

            builder.withValue(ArchivedChildColumns.MOVIE_ID, movieInfo.id);
            builder.withValue(ArchivedChildColumns.review, review.key);
            batchOperations.add(builder.build());
        }

        try {
            mC.getContentResolver().applyBatch(MovieProvider.AUTHORITY, batchOperations);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e(LOG_TAG, "Error applying batch insert", e);
        }
    }

    public static void insertTrailersToFavorites(Context mC, MovieInfo movieInfo) {
        Log.d(LOG_TAG, "insert");

        ArrayList<ContentProviderOperation> batchOperations =
                new ArrayList<>(movieInfo.trailerData.size());

        for (MovieByIdInfo trailer : movieInfo.trailerData) {
            ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                    MovieProvider.ArchivedTrailers.CONTENT_URI);

            builder.withValue(ArchivedChildColumns.MOVIE_ID, movieInfo.id);
            builder.withValue(ArchivedChildColumns.review, trailer.key);
            batchOperations.add(builder.build());
        }

        try {
            mC.getContentResolver().applyBatch(MovieProvider.AUTHORITY, batchOperations);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e(LOG_TAG, "Error applying batch insert", e);
        }
    }

    //get database contents
    public static MovieInfo[] getDBContent(Context mC) {
        Cursor cursor =
                mC.getContentResolver().query(MovieProvider.ArchivedMovies.CONTENT_URI,
                        null, null, null, null);

        MovieInfo[] resultStrs = new MovieInfo[cursor.getCount()];
        int i = 0;

        while (cursor.moveToNext()) {
            // Extract data.
            String title = cursor.getString(cursor.getColumnIndex(ArchivedMovieColumns.NAME));
            String release_date =
                    cursor.getString(cursor.getColumnIndex(ArchivedMovieColumns.RELEASE_DATE));
            String poster =
                    cursor.getString(cursor.getColumnIndex(ArchivedMovieColumns.IMAGE_RESOURCE));
            float vote = cursor.getFloat(cursor.getColumnIndex(ArchivedMovieColumns.VOTES));
            String synopsys =
                    cursor.getString(cursor.getColumnIndex(ArchivedMovieColumns.SYNOPSYS));
            String id = cursor.getString(cursor.getColumnIndex(ArchivedMovieColumns.MOVIE_ID));
            boolean favorite = true;

            //get trailer info

            Cursor cursorReview =
                    mC.getContentResolver().query(MovieProvider.ArchivedReviews.CONTENT_URI,
                            null, null, null, null);

            ArrayList<MovieByIdInfo> resultReviewStrs = new ArrayList<MovieByIdInfo>();

            MovieByIdInfo reviewItem = null;
            while (cursorReview.moveToNext()) {
                // Extract data.
                String review = cursorReview.getString(cursorReview.getColumnIndex(ArchivedChildColumns.review));

                reviewItem = new MovieByIdInfo(id, review, "review");
                resultReviewStrs.add(reviewItem);
            }

            // end of trailer

            //get review info

            Cursor cursorTrailer =
                    mC.getContentResolver().query(MovieProvider.ArchivedTrailers.CONTENT_URI,
                            null, null, null, null);

            ArrayList<MovieByIdInfo> resultTrailerStrs = new ArrayList<MovieByIdInfo>();

            MovieByIdInfo trailerItem = null;
            while (cursorTrailer.moveToNext()) {
                // Extract data.
                String review = cursorTrailer.getString(cursorTrailer.getColumnIndex(ArchivedChildColumns.review));

                trailerItem = new MovieByIdInfo(id, review, "trailer");
                resultTrailerStrs.add(trailerItem);
            }

            //end of review

            MovieInfo movieItem =
                    new MovieInfo(i, title, release_date,
                            poster, vote, synopsys, 0, id, favorite, resultTrailerStrs, resultReviewStrs);

            resultStrs[i++] = movieItem;
        }
        return resultStrs;
    }

}
