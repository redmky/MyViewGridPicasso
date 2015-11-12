package com.redmky.myviewgridpicasso;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MovieListFragment extends Fragment {
    private static android.content.Context mContext;
    private static ArrayList<MovieInfo> mMovieData;
    private static MyImageAdapter mGridAdapter;
    protected android.widget.GridView mGridView;
    static MovieInfo mMovieItem;
    public static OnItemSelectedListener listener;

    public interface OnItemSelectedListener {
        void onItemSelected(MovieInfo i);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement ItemsListFragment.OnItemSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this is needed to show menu options when
        //using fragments
        setHasOptionsMenu(true);

        mContext = getActivity();

        if (savedInstanceState != null) {
            mMovieData =
                    (java.util.ArrayList<MovieInfo>)
                            savedInstanceState.get("MOVIE_KEY");

            mMovieItem = (MovieInfo)savedInstanceState.get("MOVIE_ITEM");

            mGridAdapter = new MyImageAdapter(mContext, R.layout.item_list, mMovieData);

        } else {
            //initialize with empty data
            mMovieData = new ArrayList<>();
            mGridAdapter = new MyImageAdapter(mContext, R.layout.item_list, mMovieData);

            //Get Movie data and call movieTask
            //obtaining how to sort by
            android.content.SharedPreferences prefs =
                    android.preference.PreferenceManager.getDefaultSharedPreferences(mContext);

            String sortBy = prefs.getString(getString(R.string.pref_movie_key),
                    getString(R.string.pref_sort_by_default));
            getMovieData(sortBy);
        }
    }

    @Override
    public void onSaveInstanceState(android.os.Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList("MOVIE_KEY", mMovieData);
        savedInstanceState.putParcelable("MOVIE_ITEM", mMovieItem);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }

    //Get Movie Data
    public static void getMovieData(String sortBy) {
        //call to get movie data
        FetchMovieTask movieTask = new FetchMovieTask(mContext, mMovieData, mGridAdapter);
        movieTask.execute(sortBy);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item_list, container,
                false);
        mGridView = (android.widget.GridView) view.findViewById(R.id.gridView);

        mGridView.setAdapter(mGridAdapter);

        //Grid view click
        mGridView.setOnItemClickListener(new OnItemClickListener() {
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
                        getReviewsAndTrailerUrl();
                    } else {
                        Toast.makeText(((Activity) mContext).getBaseContext(),
                                "Please Connect To The Internet!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    //get data from db
                    //movieDetailActivity();
                    // Fire selected event for item
                    listener.onItemSelected(mMovieItem);
                }
            }
        });
        return view;
    }

    //Get Movie trailer URL
    private void getReviewsAndTrailerUrl() {

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

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        mGridView.setChoiceMode(
                activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
                        : ListView.CHOICE_MODE_NONE);
    }
}
