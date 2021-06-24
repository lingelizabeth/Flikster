package com.example.flikster.models;

import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Headers;

@Parcel // annotation indicates class is Parcelable
public class Movie {
    Integer id;
    String posterPath;
    String title;
    String overview;
    String backdropPath;
    Double voteAverage;
    String selectedCast;
    String year;
    ArrayList<Integer> genreIds;
    HashMap<Integer, String> genres;

    // no-arg, empty constructor required for Parceler
    public Movie() {}

    public Movie(JSONObject jsonObject) throws JSONException{
        // constructor must throw JSON Exception because any of these gets could fail
            // whoever calls this method must handle the execption
        // each of these get_string fields comes from the json obj keys
        id = jsonObject.getInt("id");
        posterPath = jsonObject.getString("poster_path");
        backdropPath = jsonObject.getString("backdrop_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        voteAverage = jsonObject.getDouble("vote_average");
        selectedCast = "";
        year = jsonObject.getString("release_date").substring(0, 4);
        genreIds = toArrayList(jsonObject.getJSONArray("genre_ids"));
        genres = new HashMap<>();


        // implement API call for movie credits
        String CREDITS_URL=String.format("https://api.themoviedb.org/3/movie/%s/credits?api_key=ce89b5138cf03d564d758ba27d4b6e94&language=en-US", id+"");
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(CREDITS_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d("Movie", "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try{
                    JSONArray cast = jsonObject.getJSONArray("cast"); //use getJSONArray because results maps to an array value
                    Log.i("Movie", "cast: "+cast.toString());

                    for(int k=0;k<3 && k<cast.length();k++){
                        Log.i("Movie", cast.get(k).toString());
                        JSONObject cast_member = (JSONObject) cast.get(k);
                        if (k != 0)
                            selectedCast += ", ";
                        selectedCast += cast_member.getString("name");

                    }


                } catch(JSONException e) {
                    Log.e("Movie", "JSON exception", e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d("Movie", "onFailure");
            }
        });


        // API call for movies genre list
        final String GENRE_URL = "https://api.themoviedb.org/3/genre/movie/list?api_key=ce89b5138cf03d564d758ba27d4b6e94&language=en-US";
        client.get(GENRE_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d("MovieAdapter", "Genres onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray genres_array = jsonObject.getJSONArray("genres");
                    for(int j=0;j<genres_array.length();j++){
                        JSONObject curr_genre = (JSONObject) genres_array.get(j);
                        genres.put(curr_genre.getInt("id"), curr_genre.getString("name"));
                    }
                    Log.i("MovieAdapter", genres.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d("MovieAdapter", "Genres onFailure");
            }
        });
    }

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException{
        // generate list of movies from an array of movie JSONs
        // (since the api response is in the form of a list of jsons)
        List<Movie> movies = new ArrayList<>();
        for (int i=0;i<movieJsonArray.length();i++){
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public static ArrayList<Integer> toArrayList(JSONArray jsonArray){
        ArrayList<Integer> listdata = new ArrayList<Integer>();
        if (jsonArray != null) {
            for (int i=0;i<jsonArray.length();i++){
                try {
                    listdata.add(jsonArray.getInt(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return listdata;
    }

    public String getPosterPath() {
        // hardcoded, should fetch all available sizes and base url from configurations api
        // then append relative path
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getSelectedCast() {
        return selectedCast;
    }

    public String getYear() {
        return year;
    }

    public String getGenres() {
        String genreList = "";
        // get first 2 genres if they exist, otherwise get 1
        for(int i=0;i<genreIds.size() && i<2;i++){
            genreList += genres.get(genreIds.get(i));
            if(genreIds.size() != 1 && i==0){
                genreList += ", "; //max two genres aka max one comma
            }
        }
        return genreList;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }
}
