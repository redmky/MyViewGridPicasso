package com.redmky.myviewgridpicasso;

import android.view.Menu;

/**
 * Created by redmky on 10/19/2015.
 */
public class MovieDetails extends android.support.v7.app.ActionBarActivity {

    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add this line in order for this fragment to handle menu events.
        setContentView(com.redmky.myviewgridpicasso.R.layout.activity_details);

        android.content.Intent intent = getIntent();
        if (intent != null) {

            String image = intent.getStringExtra("image");
            String title = intent.getStringExtra("title");
            float vote = intent.getFloatExtra("vote", 0);
            float popularity = intent.getFloatExtra("pop", 0);
            String synopsis = intent.getStringExtra("synopsis");
            String releaseDate = intent.getStringExtra("releaseDate");

            //initialize and set the image description
            android.widget.TextView titleTextView = (android.widget.TextView) findViewById(com.redmky.myviewgridpicasso.R.id.list_item_title_textview);
            titleTextView.setText(title);
            android.widget.TextView voteTextView = (android.widget.TextView) findViewById(com.redmky.myviewgridpicasso.R.id.list_item_vote_textview);
            voteTextView.setText("Rating: " + String.valueOf(vote));
            android.widget.TextView popTextView = (android.widget.TextView) findViewById(com.redmky.myviewgridpicasso.R.id.list_item_popularity_textview);
            popTextView.setText("Popularity: " + String.valueOf(popularity));
            android.widget.TextView rDateTextView = (android.widget.TextView) findViewById(com.redmky.myviewgridpicasso.R.id.list_item_releaseDate_textview);
            rDateTextView.setText("Release Date: " + String.valueOf(releaseDate));
            android.widget.TextView synopsisTextView = (android.widget.TextView) findViewById(com.redmky.myviewgridpicasso.R.id.list_item_synopsis_textview);
            synopsisTextView.setText(synopsis);

            //Set image url
            android.widget.ImageView imageView = (android.widget.ImageView) findViewById(R.id.movie_item_poster);
            com.squareup.picasso.Picasso.with(this).load(image).into(imageView);
        }


        //if (savedInstanceState == null) {
        //    getSupportFragmentManager().beginTransaction()
        //            .add(com.redmky.myviewgridpicasso.R.id.gridView, new DetailFragment())
        //            .commit();
        //}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.redmky.myviewgridpicasso.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

}
