package com.example.mealfinder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mealfinder.R;
import com.example.mealfinder.model.FoodLog;
import com.example.mealfinder.model.Review;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends FirestoreRecyclerAdapter<Review, ReviewAdapter.ReviewViewHolder> {

    private Context context;
    public ReviewAdapter(@NonNull FirestoreRecyclerOptions<Review> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, int position, @NonNull Review model) {
        holder.userNameReview.setText(model.getUser());
        holder.userCommentReview.setText(model.getText());
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder{

        public final View mView;

        TextView userNameReview;
        TextView userCommentReview;

        ReviewViewHolder(View itemView) {

            super(itemView);
            mView=itemView;
            userNameReview=mView.findViewById(R.id.userNameReview);
            userCommentReview=mView.findViewById(R.id.userCommentReview);
        }
    }
}
