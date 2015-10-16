package com.redmky.myviewgridpicasso;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //MyImageAdapter.FetchMovieTask movieTask = new MyImageAdapter.FetchMovieTask();
       // movieTask.execute();

        android.widget.GridView gridView = (android.widget.GridView) findViewById(R.id.gridView1);
        //gridView.setAdapter(new MyImageAdapter(getApplicationContext(), getResources().getStringArray(com.redmky.myviewgridpicasso.R.array.urlImages)));


        gridView.setAdapter(new MyImageAdapter(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}

