package edu.uw.fragmentdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements MoviesFragment.OnMovieSelectedListener, SearchFragment.OnSearchListener {

    private static final String TAG = "MainActivity";
    private SearchFragment searchFragment;
    private MoviesFragment moviesFragment;
    private DetailFragment detailFragment;
    private MoviePagerAdapter mpa;
    private ViewPager vp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vp = (ViewPager)findViewById(R.id.viewPager);
//        FragmentManager manager = new FragmentManager();
        mpa = new MoviePagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mpa);
    }


    //respond to search button clicking
    public void handleSearchClick(View v){
        EditText text = (EditText)findViewById(R.id.txtSearch);
        String searchTerm = text.getText().toString();

        //add a new results fragment to the page
        MoviesFragment fragment = MoviesFragment.newInstance(searchTerm);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment, "MoviesFragment");
        ft.addToBackStack(null); //remember for the back button
        ft.commit();
    }

    @Override
    public void onMovieSelected(Movie movie) {
        detailFragment = DetailFragment.newInstance(movie.toString(), movie.imdbId);
        mpa.notifyDataSetChanged();
        vp.setCurrentItem(R.id.txtItem);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, detailFragment, null)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onSearchSubmitted(String searchTerm) {
        // if results.length  0 then:
        moviesFragment = MoviesFragment.newInstance(searchTerm);
        mpa.notifyDataSetChanged();
        vp.setCurrentItem(R.id.listView);
    }


    public class MoviePagerAdapter extends FragmentStatePagerAdapter {
        public MoviePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                searchFragment = new SearchFragment();
                return searchFragment;
            } else if (position == 1) {
                moviesFragment = new MoviesFragment();
                return moviesFragment;
            } else {
                detailFragment = new DetailFragment();
                return detailFragment;
            }
        }

        @Override
        public int getCount() {
            if (detailFragment != null) {
                return 3;
            } else if (moviesFragment != null) {
                return 2;
            } else {
                return 1;
            }
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
