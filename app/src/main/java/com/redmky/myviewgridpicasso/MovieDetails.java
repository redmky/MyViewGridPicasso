package com.redmky.myviewgridpicasso;

import android.view.Menu;

/**
 * Created by redmky on 10/19/2015.
 */
public class MovieDetails extends android.support.v7.app.ActionBarActivity {

    String trailerUrl;
    java.util.ArrayList<MovieByIdInfo> mReviewData;
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
            trailerUrl = intent.getStringExtra("trailerUrl");
            mReviewData =
                    (java.util.ArrayList<MovieByIdInfo>) intent.getSerializableExtra("reviews");

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
            com.squareup.picasso.Picasso.with(this)
                    .load(image)
                    .placeholder(com.redmky.myviewgridpicasso.R.mipmap.ic_downloading)
                    .error(com.redmky.myviewgridpicasso.R.mipmap.ic_error)
                    .into(imageView);
            if (!trailerUrl.equals("none")) {
                android.widget.LinearLayout TrailerLayout = (android.widget.LinearLayout) findViewById(com.redmky.myviewgridpicasso.R.id.trailer_layout);
                TrailerLayout.setOnClickListener(new android.view.View.OnClickListener() {
                    public void onClick(android.view.View v) {
                        android.content.Intent intentTrailer = new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(trailerUrl));
                        startActivity(intentTrailer);
        }
                });
            }
        //if (savedInstanceState == null) {
        //    getSupportFragmentManager().beginTransaction()
        //            .add(com.redmky.myviewgridpicasso.R.id.gridView, new DetailFragment())
            ReviewsAdapter adapter = new ReviewsAdapter(this, mReviewData);
        //            .commit();
            android.widget.ListView RlistView =
                    (android.widget.ListView) findViewById(R.id.listview_reviews);
            RlistView.setAdapter(adapter);
    }
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
