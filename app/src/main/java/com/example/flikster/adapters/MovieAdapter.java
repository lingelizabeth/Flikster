package com.example.flikster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flikster.R;
import com.example.flikster.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.HashMap;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    Context context; // context where the adapter is being constructed from
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;

    }

    // this inflates a layout from XML and returns it inside a viewholder
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        // wrap view inside viewholder
        return new ViewHolder(movieView);
    }

    // populate data into the view within the viewholder
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        // Get the movie at position position
        Movie movie = movies.get(position);

        // Bind the movie data into the viewholder
        holder.bind(movie);
    }

    // return the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // adapter extends the base adapter and is parameterized by the viewholder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //view holder is a representation of the view (1 row) in the recycler view
        // that's whats passed in below, we define each part of the view as var
        TextView tvTitle, tvOverview, tvYear, tvGenres;
        ImageView ivPoster;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            // tell the super constructor where each part is coming from
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvYear = itemView.findViewById(R.id.tvYear);
            tvGenres = itemView.findViewById(R.id.tvGenres);

            // add this as the itemView's OnClickListener
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            // use the getter method to populate the viewholder views
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            tvYear.setText(movie.getYear());
            tvGenres.setText(movie.getGenres());

            // render images w Glide into the image view
            // check if orientation is landscape or portrait and render images conditionally
            int radius = 30; // corner radius, higher value = more rounded
            int margin = 0; // crop margin, set to 0 for corners with no crop
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                Log.i("MovieAdapter", "loading landscape images "+ movie.getBackdropPath());
                Glide.with(context)
                        .load(movie.getBackdropPath())
                        .placeholder(R.drawable.flicks_backdrop_placeholder)
                        .transform(new RoundedCornersTransformation(radius, margin))
                        .into(ivPoster);
            } else {
                Log.i("MovieAdapter", "loading portrait images "+movie.getPosterPath());
                Glide.with(context)
                        .load(movie.getPosterPath())
                        .placeholder(R.drawable.flicks_movie_placeholder)
                        .transform(new RoundedCornersTransformation(radius, margin))
                        .into(ivPoster);
            }


        }

        @Override
        public void onClick(View v) {
            // get item position and check if its valid i.e. actually exists in the view
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class); // why .class?
                // serialize the movie using parceler, use its short name as a key
                // send the movie data as a parcel to the other activity
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the activity
                context.startActivity(intent);
            }
        }
    }
}
