package com.redmky.myviewgridpicasso;

/**
 * Created by redmky on 10/4/2015.
 */

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.redmky.myviewgridpicasso.Data.ArchivedMovieColumns;
import com.redmky.myviewgridpicasso.Data.MovieProvider;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyImageAdapter extends android.widget.ArrayAdapter<MovieInfo> {

    private Context mContext;
    private int layoutResourceId;

    //arrayadapter vs arraylist????
    //private ArrayAdapter<MovieInfo> mMovieAdapter;
    private ArrayList<MovieInfo> mMovieData = new ArrayList<MovieInfo>();

    public MyImageAdapter(Context context, int layoutResourceId, ArrayList<MovieInfo> mMovieData) {
        super(context, layoutResourceId, mMovieData);
        this.mContext = context;
        this.layoutResourceId = layoutResourceId;
        this.mMovieData = mMovieData;
    }

    /**
     * Updates grid data and refresh grid items.
     */
    public void setGridData(ArrayList<MovieInfo> mGridData) {

        this.mMovieData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return mMovieData.size();
    }

    @Override
    public MovieInfo getItem(int position) {

        return mMovieData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View imageView = convertView;
        ImageView holder;

        if (imageView == null) {

            android.view.LayoutInflater inflater = ((android.app.Activity) mContext).getLayoutInflater();
            imageView = inflater.inflate(layoutResourceId, parent, false);

            holder = (android.widget.ImageView) imageView.findViewById(com.redmky.myviewgridpicasso.R.id.imageView1);
            imageView.setTag(holder);

        } else {
            holder = (android.widget.ImageView) imageView.getTag();
        }

        MovieInfo movieItem = getItem(position);

        Picasso.with(mContext)
                .load(movieItem.poster)
                .placeholder(com.redmky.myviewgridpicasso.R.mipmap.ic_downloading)
                .error(com.redmky.myviewgridpicasso.R.mipmap.ic_error)
                .into(holder);

        return imageView;
    }

    public MovieInfo[] getDBContent()
    {
        Cursor cursor =
                mContext.getContentResolver().query(MovieProvider.ArchivedMovies.CONTENT_URI,
                        null, null, null, null);

        mMovieData.clear();

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

            MovieInfo movieItem =
                    new MovieInfo(i, title, release_date,
                            poster, vote, synopsys, 0,id,favorite,null, null);

            resultStrs[i++] = movieItem;
        }
        return resultStrs;
    }
}

