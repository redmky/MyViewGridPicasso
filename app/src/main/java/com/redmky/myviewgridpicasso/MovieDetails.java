package com.redmky.myviewgridpicasso;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.redmky.myviewgridpicasso.Data.MovieDatabase;

/**
 * Created by redmky on 10/19/2015.
 */
public class MovieDetails extends android.support.v7.app.ActionBarActivity {
    private static final String LOG_TAG = MovieDetails.class.getSimpleName();

    String trailerUrl;
    java.util.ArrayList<MovieByIdInfo> mReviewData;
    MovieInfo movieInfo;

    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add this line in order for this fragment to handle menu events.
        setContentView(com.redmky.myviewgridpicasso.R.layout.activity_details);


        movieInfo = getIntent().getParcelableExtra(MainActivity.PAR_KEY);
        if (movieInfo != null) {

            final int position = movieInfo.position;
            final String image = movieInfo.poster;
            final String title = movieInfo.title;
            final float vote = movieInfo.vote;
            float popularity = movieInfo.popularity;
            final String synopsis = movieInfo.synopsis;
            final String releaseDate = movieInfo.release_date;
            final String id = movieInfo.id;
            final boolean favorite = movieInfo.favorite;

            MovieByIdInfo trailerData = movieInfo.trailerData.get(0);
            trailerUrl = trailerData.key;

            mReviewData = movieInfo.reviewData;

            //initialize and set the image description
            android.widget.TextView titleTextView = (android.widget.TextView)
                    findViewById(com.redmky.myviewgridpicasso.R.id.list_item_title_textview);
            titleTextView.setText(title);
            android.widget.TextView voteTextView = (android.widget.TextView)
                    findViewById(com.redmky.myviewgridpicasso.R.id.list_item_vote_textview);
            voteTextView.setText("Rating: " + String.valueOf(vote));
            android.widget.TextView popTextView = (android.widget.TextView)
                    findViewById(com.redmky.myviewgridpicasso.R.id.list_item_popularity_textview);
            popTextView.setText("Popularity: " + String.valueOf(popularity));
            android.widget.TextView rDateTextView = (android.widget.TextView)
                    findViewById(com.redmky.myviewgridpicasso.R.id.list_item_releaseDate_textview);
            rDateTextView.setText("Release Date: " + String.valueOf(releaseDate));
            android.widget.TextView synopsisTextView = (android.widget.TextView)
                    findViewById(com.redmky.myviewgridpicasso.R.id.list_item_synopsis_textview);
            synopsisTextView.setText(synopsis);

            //Set image url
            android.widget.ImageView imageView =
                    (android.widget.ImageView) findViewById(R.id.movie_item_poster);
            com.squareup.picasso.Picasso.with(this)
                    .load(image)
                    .placeholder(com.redmky.myviewgridpicasso.R.mipmap.ic_downloading)
                    .error(com.redmky.myviewgridpicasso.R.mipmap.ic_error)
                    .into(imageView);


            //intent to play trailer
            if (!trailerUrl.equals("none")) {
                android.widget.LinearLayout TrailerLayout = (android.widget.LinearLayout)
                        findViewById(com.redmky.myviewgridpicasso.R.id.trailer_layout);
                TrailerLayout.setOnClickListener(new android.view.View.OnClickListener() {
                    public void onClick(android.view.View v) {
                        android.content.Intent intentTrailer =
                                new android.content.Intent(android.content.Intent.ACTION_VIEW,
                                        android.net.Uri.parse(trailerUrl));
                        startActivity(intentTrailer);
                    }
                });
            }

            // Create the adapter for the movie reviews data
            ReviewsAdapter adapter = new ReviewsAdapter(this, mReviewData);
            // Attach the adapter to a ListView
            android.widget.ListView RlistView =
                    (android.widget.ListView) findViewById(R.id.listview_reviews);
            RlistView.setAdapter(adapter);

            //if button click add to favorite
            //save movie details to database
            final Button button = (Button) findViewById(R.id.buttonFavorite);
            final boolean[] favChange = {favorite};

            if (favorite) {
                button.setText("Remove From Favorites");
            }
            else {
                button.setText("Add to Favorites");
            }

            final Context mC = this;
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    if (favorite) {
                        button.setText("Removed From Favorites");
                        favChange[0] = false;
                        //TODO: remove from favorites
                    } else {
                        MovieDatabase.insertToFavorites(mC, movieInfo);
                        button.setText("Added to Favorites");
                        favChange[0] = true;

                        //change fav back to main activity
                        Intent intentBack = new Intent();
                        intentBack.putExtra("position", position);
                        intentBack.putExtra("favorite", favChange[0]);
                        setResult(RESULT_OK, intentBack);
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.redmky.myviewgridpicasso.R.menu.menu_main, menu);

        //sharing
        // Fetch and store ShareActionProvider

        MenuItem shareItem = menu.findItem(R.id.share);
        ShareActionProvider myShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        myShareActionProvider.setShareIntent(MainActivity.setShareIntent(movieInfo));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

        // Respond to the action bar's back button
        case android.R.id.home:
        onBackPressed();
        return true;
    }

        return super.onOptionsItemSelected(item);
    }
}
