package com.redmky.myviewgridpicasso;

import static java.util.Collections.addAll;

/**
 * Created by redmky on 10/24/2015.
 */
public class FetchDataByIdTask extends android.os.AsyncTask<String, Void, MovieByIdInfo[]> {


    //private final String LOG_TAG = FetchDataByIdTask.class.getSimpleName();

    private java.util.ArrayList<MovieByIdInfo> mTrailerData;
    private java.util.ArrayList<MovieByIdInfo> mReviewData;
    private android.content.Context mContext;

    //for holding info of what item the user clicked
    private MovieInfo movieItem;

    public FetchDataByIdTask(android.content.Context mContext,
                             java.util.ArrayList<MovieByIdInfo> TrailerData,
                             java.util.ArrayList<MovieByIdInfo> ReviewData,
                             MovieInfo movieItem) {
        this.mContext = mContext;
        this.mTrailerData = TrailerData;
        this.mReviewData = ReviewData;
        this.movieItem = movieItem;
    }

    public void movieDetailActivity(String trailerUrl) {
        //Pass the image title and url to DetailsActivity
        android.content.Intent intent = new android.content.Intent(mContext, com.redmky.myviewgridpicasso.MovieDetails.class);
        intent.putExtra("title", movieItem.title);
        intent.putExtra("image", movieItem.poster);
        intent.putExtra("vote", movieItem.vote);
        intent.putExtra("pop", movieItem.popularity);
        intent.putExtra("synopsis", movieItem.synopsis);
        intent.putExtra("releaseDate", movieItem.release_date);
        intent.putExtra("trailerUrl", trailerUrl);
        intent.putExtra("reviews", mReviewData);

        //Start details activity
        mContext.startActivity(intent);


    }

    @Override
    protected void onPostExecute(MovieByIdInfo[] result) {

        if (result != null) {
            if (result[0].mode.equals("movies")) {
                mTrailerData.clear();

                addAll(mTrailerData, result);
                //call activity to display movie details
                //at the moment we only care about one trailer
                movieDetailActivity(result[0].key);
            } else if (result[0].mode.equals("reviews")) {
                mReviewData.clear();

                addAll(mReviewData, result);
            }
        }
    }


    @Override
    protected MovieByIdInfo[] doInBackground(String... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        java.net.HttpURLConnection urlConnection = null;
        java.io.BufferedReader reader = null;
        // Will contain the raw JSON response as a string.
        String movieJsonStr;

        try {
            // Construct the URL for getting the Movie info
            android.net.Uri.Builder builder = new android.net.Uri.Builder();

            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(params[0])
                    .appendPath(params[1])
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
                if (params[1].equals("videos")) {
                    return getMovieTrailerDataFromJson(params[0], movieJsonStr);
                } else if (params[1].equals("reviews")) {
                    return getMovieReviewDataFromJson(params[0], movieJsonStr);
                }
            } catch (org.json.JSONException exception) {

            }

        } catch (java.io.IOException e) {
            android.util.Log.e("DetailFragment", "Error ", e);
            // If the code didn't successfully get the Trailer data,
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

    private MovieByIdInfo[] getMovieTrailerDataFromJson(String id, String forecastJsonStr)
            throws org.json.JSONException {

        //final String LOG_TAG = FetchMovieTask.class.getSimpleName();
        // These are the names of the JSON objects that need to be extracted.
        final String OWM_RESULTS = "results";
        final String OWM_KEY = "key";

        org.json.JSONObject movieJson = new org.json.JSONObject(forecastJsonStr);
        org.json.JSONArray movieArray = movieJson.getJSONArray(OWM_RESULTS);

        MovieByIdInfo[] resultStrs = null;

        if (movieArray.length() > 0) {

            resultStrs = new MovieByIdInfo[movieArray.length()];

            String key;

            for (int i = 0; i < movieArray.length(); i++) {

                // Get the JSON object representing the day
                org.json.JSONObject movieResults = movieArray.getJSONObject(i);

                key = movieResults.getString(OWM_KEY);

                MovieByIdInfo trailer =
                        new MovieByIdInfo(id, "https://www.youtube.com/watch?v=" + key, "movies");

                resultStrs[i] = trailer;
            }
        } else if (movieArray.length() == 0) {
            //no trailer available
            resultStrs = new MovieByIdInfo[1];
            resultStrs[0] = new MovieByIdInfo(id, "No Trailers Available.", "movies");
        }

        return resultStrs;
    }

    private MovieByIdInfo[] getMovieReviewDataFromJson(String id, String forecastJsonStr)
            throws org.json.JSONException {

        //final String LOG_TAG = FetchMovieTask.class.getSimpleName();
        // These are the names of the JSON objects that need to be extracted.
        final String OWM_RESULTS = "results";
        final String OHM_AUTHOR = "author";
        final String OWM_CONTENT = "content";

        org.json.JSONObject movieJson = new org.json.JSONObject(forecastJsonStr);
        org.json.JSONArray movieArray = movieJson.getJSONArray(OWM_RESULTS);

        MovieByIdInfo[] resultStrs = null;

        if (movieArray.length() > 0) {

            resultStrs = new MovieByIdInfo[movieArray.length()];

            String content;
            String authour;

            for (int i = 0; i < movieArray.length(); i++) {

                // Get the JSON object representing the day
                org.json.JSONObject movieResults = movieArray.getJSONObject(i);

                content = movieResults.getString(OWM_CONTENT);
                authour = movieResults.getString(OHM_AUTHOR);

                MovieByIdInfo review =
                        new MovieByIdInfo(id, authour + ": " + content, "reviews");

                resultStrs[i] = review;
            }
        } else if (movieArray.length() == 0) {
            //no review available
            resultStrs = new MovieByIdInfo[1];
            resultStrs[0] = new MovieByIdInfo(id, "No Reviews Available", "reviews");
        }

        return resultStrs;
    }
}
