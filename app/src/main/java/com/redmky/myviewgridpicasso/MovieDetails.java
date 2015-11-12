package com.redmky.myviewgridpicasso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by redmky on 10/19/2015.
 */
public class MovieDetails extends AppCompatActivity {
    private static final String LOG_TAG = MovieDetails.class.getSimpleName();

    MovieDetailFragment fragmentItemDetail;
    MovieInfo movieInfo;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        movieInfo = (MovieInfo) getIntent().getParcelableExtra(MainActivity.PAR_KEY);
        if (savedInstanceState == null) {
            fragmentItemDetail = MovieDetailFragment.newInstance(movieInfo);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.MovieDetailContainer, fragmentItemDetail);
            ft.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.redmky.myviewgridpicasso.R.menu.menu_details, menu);

        //sharing
        // Fetch and store ShareActionProvider
        MenuItem shareItem = menu.findItem(R.id.share);
        mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        mShareActionProvider.setShareIntent(setShareIntent(movieInfo));

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

    // Call to update the share intent
    public static Intent setShareIntent(MovieInfo movieInfo) {

        Intent shareIntent=null;

        if(movieInfo != null && movieInfo.trailerData != null) {
            MovieByIdInfo tempMovieInfo = movieInfo.trailerData.get(0);
            final String trailerUrl = tempMovieInfo.key;
            if (!trailerUrl.equals("none")) {
                shareIntent = new Intent();

                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, trailerUrl);
                shareIntent.setType("text/plain");
            }
        }

        return shareIntent;
    }
}

