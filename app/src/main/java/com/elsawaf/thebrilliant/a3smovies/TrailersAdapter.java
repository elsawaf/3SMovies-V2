package com.elsawaf.thebrilliant.a3smovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elsawaf.thebrilliant.a3smovies.model.Movie;
import com.elsawaf.thebrilliant.a3smovies.model.MovieTrailer;
import com.elsawaf.thebrilliant.a3smovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by The Brilliant on 27/02/2018.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder>
         {

    private ArrayList<MovieTrailer> movieTrailers;
    private Context context;
    private MyOnClickListener myOnClickListener;

    public TrailersAdapter(ArrayList<MovieTrailer> movies, Context context, MyOnClickListener listener) {
        this.movieTrailers = movies;
        this.context = context;
        myOnClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.trailer_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(myView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MovieTrailer movieTrailer = movieTrailers.get(position);
        holder.videoName.setText(movieTrailer.getName());
    }

    @Override
    public int getItemCount() {
        return movieTrailers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView videoName;

        public ViewHolder(View itemView) {
            super(itemView);
            videoName = itemView.findViewById(R.id.video_name_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            myOnClickListener.onItemClicked(getAdapterPosition());
        }
    }

    public interface MyOnClickListener{
        void onItemClicked(int position);
    }
}
