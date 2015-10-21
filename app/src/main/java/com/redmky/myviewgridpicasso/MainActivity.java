package com.redmky.myviewgridpicasso;

import android.view.Menu;
import android.view.MenuItem;

import com.redmky.myviewgridpicasso.R.layout;

import java.util.ArrayList;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.redmky.myviewgridpicasso.R.string.pref_movie_key;
import static com.redmky.myviewgridpicasso.R.string.pref_movie_popularity;

public class MainActivity extends android.support.v7.app.ActionBarActivity {

    private android.widget.GridView mGridView;

    private ArrayList<MovieInfo> mMovieData;
    private MyImageAdapter mGridAdapter;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        mGridView = (android.widget.GridView) findViewById(R.id.gridView);

        //initialize with empty data
        mMovieData = new ArrayList<>();
        mGridAdapter = new MyImageAdapter(this, layout.item_list, mMovieData);
        mGridView.setAdapter(mGridAdapter);

        //Grid view click
        mGridView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(android.widget.AdapterView<?> parent, android.view.View v, int position, long id) {
                //Get item at position
                MovieInfo item = (MovieInfo) parent.getItemAtPosition(position);
                //Pass the image title and url to DetailsActivity
                android.content.Intent intent = new android.content.Intent(MainActivity.this, com.redmky.myviewgridpicasso.MovieDetails.class);
                intent.putExtra("title", item.title);
                intent.putExtra("image", item.poster);
                intent.putExtra("vote", item.vote);
                intent.putExtra("pop", item.popularity);
                intent.putExtra("synopsis", item.synopsis);
                intent.putExtra("releaseDate", item.release_date);
                //Start details activity
                startActivity(intent);
            }
        });

        //Get Movie data and call movieTask
        getMovieData();
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

            //mGridAdapter.setGridData(mMovieData);
            //instead of the above, using below to hope to refresh faster but no luck ????
            getMovieData();

            //sortMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Get Movie Data
    private void getMovieData() {
        //obtaining how to sort by
        android.content.SharedPreferences prefs =
                android.preference.PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        String sortBy = prefs.getString(getString(R.string.pref_movie_key),
                getString(R.string.pref_sort_by_default));

        //call to get movie data
        FetchMovieTask movieTask = new FetchMovieTask(mMovieData, mGridAdapter);
        movieTask.execute(sortBy);
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
        }
        else if (sortBy.equals(getString(com.redmky.myviewgridpicasso.R.string.pref_movie_rating))) {
            java.util.Collections.sort(mMovieData, MovieInfo.movieVote);
        }

        //refresh view
        mGridAdapter.setGridData(mMovieData);
    }
}

