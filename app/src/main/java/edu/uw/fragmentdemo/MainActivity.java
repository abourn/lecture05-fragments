package edu.uw.fragmentdemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviesFragment.OnMovieSelectedListener {

    private static final String TAG = "MainActivity";

    private ArrayAdapter<Movie> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    //respond to search button clicking
    public void handleSearchClick(View v){
        EditText text = (EditText)findViewById(R.id.txtSearch);
        String searchTerm = text.getText().toString();

        MoviesFragment fragment = MoviesFragment.newInstance(searchTerm);

        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction(); // like getting deposit slip from the bank
        // add(), remove(), replace() <--three common methods for FragmentTransaction
        ft.replace(R.id.container, fragment, "MovieFragment");
        ft.commit(); // in order to commit the set of changes to my fragment

//        MoviesFragment myFragment = (MoviesFragment)fm.findFragmentById(R.id.fragment);
//
//        //downloadMovieData(searchTerm);
//        myFragment.downloadMovieData(searchTerm);
    }

    @Override
    public void onMovieSelected(Movie movie) {
        DetailFragment fragment = DetailFragment.newInstance(movie);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment, "DetailFragment")
                .commit();
    }
}
