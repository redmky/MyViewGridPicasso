package com.redmky.myviewgridpicasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.redmky.myviewgridpicasso.Data.MovieDatabase;
import com.redmky.myviewgridpicasso.R.layout;

import java.util.ArrayList;

public class MainActivity extends android.support.v7.app.ActionBarActivity {

    private static android.content.Context mContext;
    private static ArrayList<MovieInfo> mMovieData;
    private static MyImageAdapter mGridAdapter;
    protected android.widget.GridView mGridView;
    static MovieInfo mMovieItem;
    ShareActionProvider myShareActionProvider;
    public  final static String PAR_KEY = "movieInfo.par";

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

            mMovieItem = (MovieInfo)savedInstanceState.get("MOVIE_ITEM");

            mGridAdapter = new MyImageAdapter(this, layout.item_list, mMovieData);

            //todo clean get detail activity if change from popular to fav
            //Start details activity
            if(isTwoPane()) {

                setDetailActivity();
            }

        } else {

            //initialize with empty data
            mMovieData = new ArrayList<>();
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
                mMovieItem = (MovieInfo) parent.getItemAtPosition(position);
                mMovieItem.position = position;

                //todo: search db 1st

                if (mMovieItem.trailerData == null) {
                    //only get data if there is internet connection
                    ConnectivityManager cm =
                            (ConnectivityManager) mContext.getSystemService
                                    (Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    if (activeNetwork != null) { // connected to the internet

                        //get the url for the movie trailer
                        getReviewsAndTrailerUrl(mMovieItem.id, mMovieItem);
                    } else {
                        Toast.makeText(((Activity) mContext).getBaseContext(),
                                "Please Connect To The Internet!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    //get data from db
                    movieDetailActivity();
                }
            }
        });
    }
    // Call to update the share intent
    public static Intent setShareIntent(MovieInfo movieInfo) {

        Intent shareIntent=null;

        if(movieInfo != null && movieInfo.trailerData != null) {
            MovieByIdInfo tempMovieInfo = movieInfo.trailerData.get(0);
            final String trailerUrl = tempMovieInfo.key;
            if (!trailerUrl.equals("none")) {
                shareIntent = new Intent();
                //shareIntent.setAction(Intent.ACTION_SEND);
                //shareIntent.putExtra(Intent.EXTRA_STREAM, android.net.Uri.parse(trailerUrl));
                //shareIntent.setType("video");
                //mContext.startActivity(Intent.createChooser(shareIntent, "share"));

                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, trailerUrl);
                shareIntent.setType("text/plain");
            }
        }

        return shareIntent;
    }

    //Get Movie Data
    public static void getMovieData(String sortBy) {
        //call to get movie data
        FetchMovieTask movieTask = new FetchMovieTask(mContext, mMovieData, mGridAdapter);
        movieTask.execute(sortBy);
    }

    @Override
    protected void onSaveInstanceState(android.os.Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList("MOVIE_KEY", mMovieData);
        savedInstanceState.putParcelable("MOVIE_ITEM", mMovieItem);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //sharing
        // Fetch and store ShareActionProvider
        MenuItem shareItem = menu.findItem(R.id.share);
        // Fetch and store ShareActionProvider
        myShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        myShareActionProvider.setShareIntent(setShareIntent(mMovieItem));

        // Return true to display menu
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
    private void getReviewsAndTrailerUrl(String id, MovieInfo movieItem) {


        //call to get movie data
        FetchDataByIdTask reviewTask =
                new FetchDataByIdTask(mMovieItem);

        //get data review
        //this then calls fetch trailer data
        reviewTask.execute(mMovieItem.id, "reviews"); //reviews

        //call to get movie data
        FetchDataByIdTask trailerTask =
                new FetchDataByIdTask(mMovieItem);

        trailerTask.execute(mMovieItem.id, "videos"); //trailers
    }

    private static boolean isTwoPane() {

        boolean twoPane;
        int screenSize = mContext.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;
        twoPane = screenSize > Configuration.SCREENLAYOUT_SIZE_NORMAL;
        return twoPane;
    }

    public static void movieDetailActivity() {

        //Start details activity
        if(isTwoPane()) {

            setDetailActivity();
        }
        else {
            //Pass the image title and url to DetailsActivity

           Intent mIntent = new Intent(mContext, MovieDetails.class);
           mIntent.putExtra(PAR_KEY, mMovieItem);

            //((Activity) mContext).startActivity(mIntent);

            int mRequestCode = 100;
            ((Activity) mContext).startActivityForResult(mIntent, mRequestCode);
        }
    }

    public static void setDetailActivity()
    {
        final int position = mMovieItem.position;
        final String image = mMovieItem.poster;
        final String title = mMovieItem.title;
        final float vote = mMovieItem.vote;
        float popularity = mMovieItem.popularity;
        final String synopsys = mMovieItem.synopsis;
        final String releaseDate = mMovieItem.release_date;
        final String id = mMovieItem.id;
        final boolean favorite = mMovieItem.favorite;

        //initialize and set the image description
        android.widget.TextView titleTextView = (android.widget.TextView)
                ((Activity)mContext).findViewById
                        (com.redmky.myviewgridpicasso.R.id.list_item_title_textview);
        titleTextView.setText(title);
        android.widget.TextView voteTextView = (android.widget.TextView)
                ((Activity)mContext).findViewById
                        (com.redmky.myviewgridpicasso.R.id.list_item_vote_textview);
        voteTextView.setText("Rating: " + String.valueOf(vote));
        android.widget.TextView popTextView = (android.widget.TextView)
                ((Activity)mContext).findViewById
                        (com.redmky.myviewgridpicasso.R.id.list_item_popularity_textview);
        popTextView.setText("Popularity: " + String.valueOf(popularity));
        android.widget.TextView rDateTextView = (android.widget.TextView)
                ((Activity)mContext).findViewById
                        (com.redmky.myviewgridpicasso.R.id.list_item_releaseDate_textview);
        rDateTextView.setText("Release Date: " + String.valueOf(releaseDate));
        android.widget.TextView synopsisTextView = (android.widget.TextView)
                ((Activity)mContext).findViewById
                        (com.redmky.myviewgridpicasso.R.id.list_item_synopsis_textview);
        synopsisTextView.setText(synopsys);

        //Set image url
        android.widget.ImageView imageView =
                (android.widget.ImageView)((Activity)mContext).findViewById(R.id.movie_item_poster);
        com.squareup.picasso.Picasso.with(mContext)
                .load(image)
                .placeholder(com.redmky.myviewgridpicasso.R.mipmap.ic_downloading)
                .error(com.redmky.myviewgridpicasso.R.mipmap.ic_error)
                .into(imageView);

        //display data if data is not null
        //if null then don't even try to add it to favorites
        if(mMovieItem.trailerData != null && mMovieItem.reviewData != null) {
            //intent to play trailer
            MovieByIdInfo tempMovieInfo = mMovieItem.trailerData.get(0);
            final String trailerUrl = tempMovieInfo.key;
            //todo use the array instead of one item
            if (!trailerUrl.equals("none")) {
                android.widget.LinearLayout TrailerLayout = (android.widget.LinearLayout)
                        ((Activity) mContext).findViewById
                                (com.redmky.myviewgridpicasso.R.id.trailer_layout);
                TrailerLayout.setOnClickListener(new android.view.View.OnClickListener() {
                    public void onClick(android.view.View v) {
                        android.content.Intent intentTrailer = new android.content.Intent
                                (android.content.Intent.ACTION_VIEW,
                                        android.net.Uri.parse(trailerUrl));
                        mContext.startActivity(intentTrailer);
                    }
                });
            }

            // Create the adapter for the movie reviews data
            ReviewsAdapter adapter = new ReviewsAdapter(mContext, mMovieItem.reviewData);
            // Attach the adapter to a ListView
            android.widget.ListView RlistView =
                    (android.widget.ListView)
                            ((Activity) mContext).findViewById(R.id.listview_reviews);
            RlistView.setAdapter(adapter);

            //if button click add to favorite
            //save movie details to database
            final Button button = (Button) ((Activity) mContext).findViewById(R.id.buttonFavorite);
            final boolean[] favChange = {favorite};

            if (favorite) {
                button.setText("Remove From Favorites");
            } else {
                button.setText("Add to Favorites");
            }

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    if (favorite) {
                        button.setText("Removed From Favorites");
                        favChange[0] = false;
                        //TODO: remove from favorites
                    } else {
                        MovieDatabase.insertToFavorites(mContext, mMovieItem);
                        button.setText("Added to Favorites");

                        MovieInfo tempInfo = mMovieData.get(position);
                        tempInfo.favorite = true;
                        mMovieData.set(position, tempInfo);
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                int position = data.getIntExtra("position",0);
                boolean fav = data.getBooleanExtra("favorite", false);

                MovieInfo tempInfo = mMovieData.get(position);
                tempInfo.favorite = fav;
                mMovieData.set(position, tempInfo);
            }
        }
    }
}
