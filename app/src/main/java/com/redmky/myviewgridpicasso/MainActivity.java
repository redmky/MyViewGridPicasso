package com.redmky.myviewgridpicasso;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.redmky.myviewgridpicasso.MovieListFragment.OnItemSelectedListener;


//main
public class MainActivity extends AppCompatActivity implements OnItemSelectedListener{

    public  final static String PAR_KEY = "movieInfo.par";
    public static boolean isTwoPane = false;
    private static ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        determinePaneLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (isTwoPane) {
            getMenuInflater().inflate(R.menu.menu_details, menu);

            MenuItem shareItem = menu.findItem(R.id.share);
            mShareActionProvider =
                    (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

            //mShareActionProvider.setShareIntent(setShareIntent(movieInfo));

        }
        else {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }

        // Return true to display menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new android.content.Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void determinePaneLayout() {
        FrameLayout fragmentItemDetail = (FrameLayout) findViewById(R.id.MovieDetailContainer);
        if (fragmentItemDetail != null) {
            isTwoPane = true;
            MovieListFragment fragmentItemsList =
                    (MovieListFragment)
                            getSupportFragmentManager().findFragmentById(R.id.fragmentItemsList);
            fragmentItemsList.setActivateOnItemClick(true);
        }
    }

    @Override
    public void onItemSelected(MovieInfo item) {
        if (isTwoPane) { // single activity with list and detail

            //update the share with the clicked movie
            mShareActionProvider.setShareIntent(MovieDetails.setShareIntent(item));

            // Replace frame layout with correct detail fragment
            MovieDetailFragment fragmentItem = MovieDetailFragment.newInstance(item);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.MovieDetailContainer, fragmentItem);
            ft.commit();
        } else { // separate activities
            // launch detail activity using intent
            Intent i = new Intent(this, MovieDetails.class);
            i.putExtra(PAR_KEY, item);
            startActivity(i);

            //int mRequestCode = 100;
            //((Activity) mContext).startActivityForResult(mIntent, mRequestCode);
        }
    }


}
