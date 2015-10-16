package com.redmky.myviewgridpicasso;

/**
 * Created by redmky on 10/4/2015.
 */


import android.view.View;

import com.squareup.picasso.Picasso;

public class MyImageAdapter extends android.widget.BaseAdapter {

    static android.widget.ArrayAdapter<MovieInfo> mMovieAdapter;
    private android.content.Context mContext;

   // private static java.util.List<String> items = new java.util.ArrayList<String>();

    public MyImageAdapter(android.content.Context context) {
        super();
        mContext = context;

        //initiate mMovieAdapter
        mMovieAdapter = new android.widget.ArrayAdapter<>(
                mContext,
                com.redmky.myviewgridpicasso.R.layout.activity_main,
                new java.util.ArrayList<MovieInfo>());



        //call to get movie data
        MyImageAdapter.FetchMovieTask movieTask = new MyImageAdapter.FetchMovieTask();
        movieTask.execute();
    }


    @Override
    public int getCount() {

        return mMovieAdapter.getCount();
        //return items.size();
    }

    @Override
    public Object getItem(int position) {

        return mMovieAdapter.getItem(position);
        //return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
       //return mMovieAdapter.get(position).drawableId;

    }

    @Override
    public android.view.View getView(int position, View convertView, android.view.ViewGroup parent) {

        android.widget.ImageView imageView;

        if (convertView == null) {

            //create imageview
            imageView = new android.widget.ImageView(mContext);

            android.content.res.Resources r = android.content.res.Resources.getSystem();
            float px = android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_DIP, 185, r.getDisplayMetrics());

            //imageView.setLayoutParams(new android.widget.GridView.LayoutParams((int)px, (int)px));
            //imageView.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);

        } else {
            imageView = (android.widget.ImageView) convertView;
        }

        //Picasso.with(mContext).load(items.get(position)).into(imageView);
        Picasso.with(mContext).load(mMovieAdapter.getItem(position).poster).into(imageView);

        return imageView;
    }

    //class item for all the movie information that will collect
    private static class MovieInfo
    {
        final String title;
        final String release_date;
        final String poster;
        final float vote;
        final String synopsis;

        MovieInfo(String title, String release_date, String poster, float vote, String synopsis) {
            this.title = title;
            this.release_date = release_date;
            this.poster = poster;
            this.vote = vote;
            this.synopsis = synopsis;
        }
    }

    private static MovieInfo[] getMovieDataFromJson(String forecastJsonStr, int numDays)
            throws org.json.JSONException {

        //final String LOG_TAG = FetchMovieTask.class.getSimpleName();
        // These are the names of the JSON objects that need to be extracted.
        final String OWM_RESULTS = "results";
        final String OWM_TITLE = "title";
        final String OWM_BACKDROP = "backdrop_path";
        final String OWM_VOTE = "vote_average";
        final String OWM_OVERVIEW = "overview";
        final String OWM_DATE = "release_date";

        org.json.JSONObject movieJson = new org.json.JSONObject(forecastJsonStr);
        org.json.JSONArray movieArray = movieJson.getJSONArray(OWM_RESULTS);

        MovieInfo[] resultStrs = new MovieInfo[movieArray.length()];

        String image;
        String title;
        String release_date;
        float vote;
        String poster;
        String synopsis;

        for (int i = 0; i < movieArray.length(); i++) {

            // Get the JSON object representing the day
            org.json.JSONObject movieResults = movieArray.getJSONObject(i);

            title = movieResults.getString(OWM_TITLE);
            release_date = movieResults.getString(OWM_DATE);
            image = movieResults.getString(OWM_BACKDROP);
            vote = Float.parseFloat(movieResults.getString(OWM_VOTE));
            poster = "http://image.tmdb.org/t/p/w185" + image;
            synopsis = movieResults.getString(OWM_OVERVIEW);

            MovieInfo movie = new MovieInfo(title, release_date, poster, vote, synopsis);

            resultStrs[i] = movie;

            //items.add(poster);
        }

        return resultStrs;
    }



    static public class FetchMovieTask extends android.os.AsyncTask<Void, Void, MovieInfo[]> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();


        @Override
        protected void onPostExecute(MovieInfo[] result) {

            if (result != null) {
                mMovieAdapter.clear();
                //for (String dayForecastStr : result)
                //{
                //mForecastAdapter.add(dayForecastStr);
                //}

                //for newer version, calls notifydatachange less frequent
                //speed up
                mMovieAdapter.addAll(result);
            }
        }

        @Override
        protected MovieInfo[] doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            java.net.HttpURLConnection urlConnection = null;
            java.io.BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;
            int numMovies = 10;

            try {
                // Construct the URL for getting the Movie info

                //http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=
                android.net.Uri.Builder builder = new android.net.Uri.Builder();

                builder.scheme("http")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("discover")
                        .appendPath("movie")
                        //.appendQueryParameter("sort_by", params[0])
                        .appendQueryParameter("sort_by", "popularity.desc")
                        .appendQueryParameter("api_key", "");

                String myUrl = builder.build().toString();

                //for debugging
                //Log.v(LOG_TAG, "URL String: " + myUrl);

                java.net.URL url = new java.net.URL(myUrl);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (java.net.HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                java.io.InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();

                //debug only
                //adding to verify the data return once refresh is press
                //Log.v(LOG_TAG, "Forecast JSON String: " + forecastJsonStr);

                try {
                    return getMovieDataFromJson(movieJsonStr, numMovies);
                } catch (org.json.JSONException exception) {

                }

            } catch (java.io.IOException e) {
                android.util.Log.e("DetailFragment", "Error ", e);
                // If the code didn't successfully get the weather data,
                //there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final java.io.IOException e) {
                        android.util.Log.e("DetailFragment", "Error closing stream", e);
                    }
                }
            }

            return null;
        }
    }

}

