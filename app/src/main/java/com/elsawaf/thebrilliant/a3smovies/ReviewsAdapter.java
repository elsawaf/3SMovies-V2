package com.elsawaf.thebrilliant.a3smovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elsawaf.thebrilliant.a3smovies.model.MovieReview;
import com.elsawaf.thebrilliant.a3smovies.model.MovieTrailer;

import java.util.ArrayList;

/**
 * Created by The Brilliant on 16/04/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private ArrayList<MovieReview> reviews;
    private Boolean isEllipsize;

    public ReviewsAdapter(ArrayList<MovieReview> movies) {
        this.reviews = movies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.review_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(myView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MovieReview movieReview = reviews.get(position);
        holder.author.setText(movieReview.getAuthor());
        holder.content.setText(movieReview.getContent());
        isEllipsize = true;
        holder.content.setTag(isEllipsize);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView author;
        public TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.review_author_text_view);
            content = itemView.findViewById(R.id.review_content_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            /*the content of review is long so in the begging the app show only 4 lines
            * then if user click on the item the app show the hall of content
            * finally if user click again on the same item it will back to show only 4 lines*/

            isEllipsize = (Boolean) content.getTag();
            if (isEllipsize){
                content.setEllipsize(null);
                content.setMaxLines(Integer.MAX_VALUE);
            }
            else {
                content.setEllipsize(TextUtils.TruncateAt.END);
                content.setMaxLines(4);
            }
            /*to make the review content is expand and collapse when user click on it
            * we should save the last condition after execute the action*/
            isEllipsize = !isEllipsize;
            content.setTag(isEllipsize);
        }
    }

}
