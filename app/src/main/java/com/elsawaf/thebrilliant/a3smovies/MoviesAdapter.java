package com.elsawaf.thebrilliant.a3smovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.elsawaf.thebrilliant.a3smovies.model.Movie;
import com.elsawaf.thebrilliant.a3smovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by The Brilliant on 27/02/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder>
         {

    private ArrayList<Movie> movies;
    private Context context;
    private MyOnClickListener myOnClickListener;

    public MoviesAdapter(ArrayList<Movie> movies, Context context, MyOnClickListener listener) {
        this.movies = movies;
        this.context = context;
        myOnClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item, null);
        ViewHolder viewHolder = new ViewHolder(myView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = movies.get(position);

        String imageUrl = NetworkUtils.buildImageUrl(movie.getPosterPath()).toString();
        Picasso.with(context).load(imageUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void updateData(ArrayList<Movie> list) {
        movies = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
//            imageView = (ImageView) itemView;
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
