package com.example.flikster.adapters; // not sure if this is right? or what it does?

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flikster.R;
import com.example.flikster.models.Movie;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie; // current movie

    // layout objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView ivBackdrop;
    TextView tvRatingNum;
    TextView tvSelectedCast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // get view objects
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        ivBackdrop = (ImageView) findViewById(R.id.ivBackdrop);
        tvRatingNum = (TextView) findViewById(R.id.tvRatingNum);
        tvSelectedCast = (TextView) findViewById(R.id.tvSelectedCast);

        // unwrap the movie passed in via intent, using its simple name as a key
        // get parcelable extra fetches the extra data, which we put in w simple name as key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        //set values of view objects
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvSelectedCast.setText(movie.getSelectedCast());
        // divide by 2 to take vote avg out of 10 to out of 5
        rbVoteAverage.setRating(movie.getVoteAverage().floatValue() / 2.0f);
        tvRatingNum.setText((movie.getVoteAverage().floatValue()/ 2.0f)+"");

        // Use glide to upload backdrop image
        Glide.with(this)
                .load(movie.getBackdropPath())
                .placeholder(R.drawable.flicks_backdrop_placeholder)
                .into(ivBackdrop);


    }
}