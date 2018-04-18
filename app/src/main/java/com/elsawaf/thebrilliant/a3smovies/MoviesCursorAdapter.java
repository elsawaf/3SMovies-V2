package com.elsawaf.thebrilliant.a3smovies;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.elsawaf.thebrilliant.a3smovies.data.MovieContract;
import com.elsawaf.thebrilliant.a3smovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

public class MoviesCursorAdapter extends RecyclerView.Adapter<MoviesCursorAdapter.MovieViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public MoviesCursorAdapter(Cursor mCursor, Context mContext) {
        this.mCursor = mCursor;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        int imageIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
        String imagePath = mCursor.getString(imageIndex);

        String imageUrl = NetworkUtils.buildImageUrl(imagePath).toString();
        Picasso.with(mContext).load(imageUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;

        public MovieViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
        }
    }

}
