package com.redmky.myviewgridpicasso;

import android.view.Menu;
import android.view.MenuItem;

import com.redmky.myviewgridpicasso.R.layout;

import java.util.ArrayList;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.redmky.myviewgridpicasso.R.string.pref_movie_key;
import static com.redmky.myviewgridpicasso.R.string.pref_movie_popularity;

public class MainActivity extends android.support.v7.app.ActionBarActivity {

    private static android.content.Context mContext;
    private static ArrayList<MovieInfo> mMovieData;
    private static MyImageAdapter mGridAdapter;
    private static ArrayList<MovieByIdInfo> mTrailerData;
    private static ArrayList<MovieByIdInfo> mReviewData;
    private android.widget.GridView mGridView;

    //Get Movie Data
    public static void getMovieData(String sortBy) {
        //call to get movie data
        FetchMovieTask movieTask = new FetchMovieTask(mMovieData, mGridAdapter);
        movieTask.execute(sortBy);
    }

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(layout.activity_main);
        mGridView = (android.widget.GridView) findViewById(R.id.gridView);

        if (savedInstanceState != null) {
            mMovieData =
                    (java.util.ArrayList<MovieInfo>)
                            savedInstanceState.get("MOVIE_KEY");

            mTrailerData =
                    (java.util.ArrayList<com.redmky.myviewgridpicasso.MovieByIdInfo>)
                            savedInstanceState.get("TRAILER_KEY");

            mReviewData =
                    (java.util.ArrayList<com.redmky.myviewgridpicasso.MovieByIdInfo>)
                            savedInstanceState.get("REVIEW_KEY");

            mGridAdapter = new MyImageAdapter(this, layout.item_list, mMovieData);

        } else {

            //initialize with empty data
            mMovieData = new ArrayList<>();
            mTrailerData = new ArrayList<>();
            mReviewData = new ArrayList<>();
            mGridAdapter = new MyImageAdapter(this, layout.item_list, mMovieData);

            //Get Movie data and call movieTask
            //obtaining how to sort by
            android.content.SharedPreferences prefs =
                    android.preference.PreferenceManager.getDefaultSharedPreferences(this);

            String sortBy = prefs.getString(getString(R.string.pref_movie_key),
                    getString(R.string.pref_sort_by_default));
            getMovieData(sortBy);
        }

        mGridView.setAdapter(mGridAdapter);

        //Grid view click
        mGridView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(android.widget.AdapterView<?> parent, android.view.View v, int position, long id) {
                //Get item at position
                MovieInfo movieItem = (MovieInfo) parent.getItemAtPosition(position);

                //get the url for the movie trailer
                getTrailerUrl(movieItem.id, movieItem);

            }
        });
    }

    @Override
    protected void onSaveInstanceState(android.os.Bundle outState) {
        outState.putParcelableArrayList("MOVIE_KEY", mMovieData);
        outState.putParcelableArrayList("TRAILER_KEY", mTrailerData);
        outState.putParcelableArrayList("REVIEW_KEY", mReviewData);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new android.content.Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Get Movie trailer URL
    private void getTrailerUrl(String id, MovieInfo movieItem) {


        //call to get movie data
        FetchDataByIdTask reviewTask =
                new FetchDataByIdTask(mContext, mTrailerData, mReviewData, movieItem);

        //get data review
        //this then calls fetch trailer data
        reviewTask.execute(id, "reviews"); //reviews

        //call to get movie data
        FetchDataByIdTask trailerTask =
                new FetchDataByIdTask(mContext, mTrailerData, mReviewData, movieItem);

        trailerTask.execute(id, "videos"); //trailers


    }

    //to sort my current list instead of using API
    //currently not being use
    private void sortMovies() {

        //obtaining how to sort by
        android.content.SharedPreferences prefs =
                getDefaultSharedPreferences(this);

        String sortBy = prefs.getString(getString(pref_movie_key),
                getString(pref_movie_popularity));


        if (sortBy.equals(getString(pref_movie_popularity))) {
            java.util.Collections.sort(mMovieData, MovieInfo.moviePop);
        } else if (sortBy.equals(getString(com.redmky.myviewgridpicasso.R.string.pref_movie_rating))) {
            java.util.Collections.sort(mMovieData, MovieInfo.movieVote);
        }

        //refresh view
        mGridAdapter.setGridData(mMovieData);
    }
}

