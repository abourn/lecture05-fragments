package edu.uw.fragmentdemo;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass representing a list of Movie search results.
 */
public class MoviesFragment extends Fragment {

    private static final String TAG = "MoviesFragment";
    private static final String SEARCH_PARAM_KEY = "search_term";

    private ArrayAdapter<Movie> adapter;

    //the search we are currently showing the results of
    //private String searchTerm = null;

    private OnMovieSelectedListener callback; //context that we use for event callbacks

    //interface supported by anyone who can respond to this Fragment's clicks
    public interface OnMovieSelectedListener {
        public void onMovieSelected(Movie movie);
    }


    public MoviesFragment() {
        // Required empty public constructor
    }

    //factory method for creating the Fragment
    public static MoviesFragment newInstance(String searchTerm) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
        args.putString(SEARCH_PARAM_KEY, searchTerm);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback = (OnMovieSelectedListener)context;
        }catch(ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnMovieSelectedListend");
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        //controller
        adapter = new ArrayAdapter<Movie>(this.getActivity(),
                R.layout.list_item, R.id.txtItem, new ArrayList<Movie>());

        ListView listView = (ListView)rootView.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie)parent.getItemAtPosition(position);
                Log.v(TAG, "You clicked on: "+movie);

                //tell the activity to do stuff!
                //((OnMovieSelectedListener)getActivity()).onMovieSelected(movie);
                callback.onMovieSelected(movie);

            }
        });

        if(getArguments() != null) {
            String searchTerm = getArguments().getString(SEARCH_PARAM_KEY);
            if(searchTerm != null)
                downloadMovieData(searchTerm);
        }

        return rootView;
    }

    //helper method for downloading the data via the MovieDowloadTask
    public void downloadMovieData(String searchTerm){
        Log.v(TAG, "You searched for: "+searchTerm);
        MovieDownloadTask task = new MovieDownloadTask();
        task.execute(searchTerm);
    }

    //A task to download movie data from the internet on a background thread
    public class MovieDownloadTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            ArrayList<Movie> data = MovieDownloader.downloadMovieData(params[0]);
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);

            adapter.clear();
            for(Movie movie : movies){
                adapter.add(movie);
            }
        }
    }
}