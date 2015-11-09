package com.redmky.myviewgridpicasso;

import android.content.Context;
import android.database.Cursor;

import com.redmky.myviewgridpicasso.Data.MovieDatabase;

import java.util.ArrayList;

/**
 * Created by redmky on 10/18/2015.
 */
public class FetchMovieTask extends android.os.AsyncTask<String, Void, MovieInfo[]> {

    //private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

    private ArrayList<MovieInfo> mMovieData;
    private Context mContex;
    private MyImageAdapter mGridAdapter;

    public FetchMovieTask(Context mContex, ArrayList<MovieInfo> mMovieData, MyImageAdapter mGridAdapter) {
        this.mMovieData = mMovieData;
        this.mContex = mContex;
        this.mGridAdapter = mGridAdapter;
    }

    @Override
        protected void onPostExecute(MovieInfo[] result) {

            if (result != null) {
                // mMovieAdapter.clear();
                mMovieData.clear();

                for (MovieInfo movieItem : result)
                {

                    mMovieData.add(movieItem);
                }

                //to fresh the activity
                mGridAdapter.setGridData(mMovieData);

                //for newer version, calls notifydatachange less frequent
                //speed up
                // does not work???
                //mMovieData.addAll(result);
            }
        }

        @Override
        protected MovieInfo[] doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            java.net.HttpURLConnection urlConnection = null;
            java.io.BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String movieJsonStr;

            if(params[0].compareTo("favorite") == 0 )
            {
                return MovieDatabase.getDBContent(mContex);
            }
            else {

                try {
                    // Construct the URL for getting the Movie info

                    //http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=
                    android.net.Uri.Builder builder = new android.net.Uri.Builder();

                    builder.scheme("http")
                            .authority("api.themoviedb.org")
                            .appendPath("3")
                            .appendPath("discover")
                            .appendPath("movie")
                            .appendQueryParameter("sort_by", params[0])
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
                        return getMovieDataFromJson(movieJsonStr);
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
            }

            return null;
        }

    private MovieInfo[] getMovieDataFromJson(String forecastJsonStr)
            throws org.json.JSONException {

        //final String LOG_TAG = FetchMovieTask.class.getSimpleName();
        // These are the names of the JSON objects that need to be extracted.
        final String OWM_RESULTS = "results";
        final String OWM_TITLE = "title";
        final String OWM_BACKDROP = "backdrop_path";
        final String OWM_VOTE = "vote_average";
        final String OWM_OVERVIEW = "overview";
        final String OWM_DATE = "release_date";
        final String OWM_POPULARITY = "popularity";
        final String OWM_ID = "id";

        org.json.JSONObject movieJson = new org.json.JSONObject(forecastJsonStr);
        org.json.JSONArray movieArray = movieJson.getJSONArray(OWM_RESULTS);

        MovieInfo[] resultStrs = new MovieInfo[movieArray.length()];

        String image;
        String title;
        String release_date;
        float vote;
        String poster;
        String synopsis;
        float popularity;
        String id;

        for (int i = 0; i < movieArray.length(); i++) {

            // Get the JSON object representing the day
            org.json.JSONObject movieResults = movieArray.getJSONObject(i);

            title = movieResults.getString(OWM_TITLE);
            release_date = movieResults.getString(OWM_DATE);
            image = movieResults.getString(OWM_BACKDROP);
            vote = Float.parseFloat(movieResults.getString(OWM_VOTE));
            poster = "http://image.tmdb.org/t/p/w185" + image;
            synopsis = movieResults.getString(OWM_OVERVIEW);
            popularity = Float.parseFloat(movieResults.getString(OWM_POPULARITY));
            id = movieResults.getString(OWM_ID);
            boolean favorite = false;
            //TODO: retrieve db for this movie to see if its a favorite

            MovieInfo movie =
                    new MovieInfo(i, title,
                            release_date, poster, vote,
                            synopsis, popularity, id, favorite,null,null);

            resultStrs[i] = movie;
        }

        return resultStrs;
    }
}
