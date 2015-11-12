package com.redmky.myviewgridpicasso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.redmky.myviewgridpicasso.Data.MovieDatabase;

/**
 * Created by redmky on 11/9/2015.
 */
public class MovieDetailFragment extends Fragment {
    private MovieInfo mMovieItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovieItem = (MovieInfo) getArguments().getParcelable(MainActivity.PAR_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_details,
                container, false);

        setDetailActivity(view);

        return view;
    }

    public void setDetailActivity(View view)
    {
        //final int position = mMovieItem.position;
        final String image = mMovieItem.poster;
        final String title = mMovieItem.title;
        final float vote = mMovieItem.vote;
        float popularity = mMovieItem.popularity;
        final String synopsis = mMovieItem.synopsis;
        final String releaseDate = mMovieItem.release_date;
        final String id = mMovieItem.id;
        final boolean favorite = mMovieItem.favorite;

        //initialize and set the image description
        android.widget.TextView titleTextView = (android.widget.TextView)
                view.findViewById
                        (com.redmky.myviewgridpicasso.R.id.list_item_title_textview);
        titleTextView.setText(title);
        android.widget.TextView voteTextView = (android.widget.TextView)
                view.findViewById
                        (com.redmky.myviewgridpicasso.R.id.list_item_vote_textview);
        voteTextView.setText("Rating: " + String.valueOf(vote));
        android.widget.TextView popTextView = (android.widget.TextView)
                view.findViewById
                        (com.redmky.myviewgridpicasso.R.id.list_item_popularity_textview);
        popTextView.setText("Popularity: " + String.valueOf(popularity));
        android.widget.TextView rDateTextView = (android.widget.TextView)
                view.findViewById
                        (com.redmky.myviewgridpicasso.R.id.list_item_releaseDate_textview);
        rDateTextView.setText("Release Date: " + String.valueOf(releaseDate));
        android.widget.TextView synopsisTextView = (android.widget.TextView)
                view.findViewById
                        (com.redmky.myviewgridpicasso.R.id.list_item_synopsis_textview);
        synopsisTextView.setText(synopsis);

        //Set image url
        android.widget.ImageView imageView =
                (android.widget.ImageView)view.findViewById(R.id.movie_item_poster);
        com.squareup.picasso.Picasso.with(getActivity())
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
                        view.findViewById
                                (com.redmky.myviewgridpicasso.R.id.trailer_layout);
                TrailerLayout.setOnClickListener(new android.view.View.OnClickListener() {
                    public void onClick(android.view.View v) {
                        android.content.Intent intentTrailer = new android.content.Intent
                                (android.content.Intent.ACTION_VIEW,
                                        android.net.Uri.parse(trailerUrl));
                        getActivity().startActivity(intentTrailer);
                    }
                });
            }

            // Create the adapter for the movie reviews data
            ReviewsAdapter adapter = new ReviewsAdapter(getActivity(), mMovieItem.reviewData);
            // Attach the adapter to a ListView
            android.widget.ListView RlistView =
                    (android.widget.ListView)
                            view.findViewById(R.id.listview_reviews);
            RlistView.setAdapter(adapter);

            //if button click add to favorite
            //save movie details to database
            final Button button = (Button) view.findViewById(R.id.buttonFavorite);
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
                        //insert data to db
                        MovieDatabase.insertToFavorites(getActivity(), mMovieItem);
                        button.setText("Added to Favorites");

                        //MovieInfo tempInfo = mMovieData.get(position);
                        //tempInfo.favorite = true;
                        //mMovieData.set(position, tempInfo);
                    }
                }
            });
        }
    }

    // ItemDetailFragment.newInstance(item)
    public static MovieDetailFragment newInstance(MovieInfo item) {
        MovieDetailFragment fragmentDemo = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(MainActivity.PAR_KEY, item);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

}
