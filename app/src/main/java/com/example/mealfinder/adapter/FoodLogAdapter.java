package com.example.mealfinder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mealfinder.MainActivity;
import com.example.mealfinder.R;
import com.example.mealfinder.model.FoodLog;
import com.example.mealfinder.ui.food_log.FoodLogFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class FoodLogAdapter extends FirestoreRecyclerAdapter<FoodLog, FoodLogAdapter.FoodLogViewHolder> {

    private Context context;
    public FoodLogAdapter(@NonNull FirestoreRecyclerOptions<FoodLog> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    public void onBindViewHolder(FoodLogViewHolder holder, int position, FoodLog model) {
        holder.logName.setText(model.getLogName());
        holder.mealName.setText(model.getMealName());
        holder.date.setText(model.getDate());
        Glide.with(context).load(model.getPicture()).into(holder.foodLogImage);

    }
    @Override
    public FoodLogViewHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.item_food_log, group, false);
        return new FoodLogViewHolder(view);
    }
    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    static class FoodLogViewHolder extends RecyclerView.ViewHolder{

        public final View mView;

        ImageView foodLogImage;
        TextView logName;
        TextView mealName;
        TextView date;

        FoodLogViewHolder(View itemView) {

            super(itemView);
            mView=itemView;
            foodLogImage=mView.findViewById(R.id.food_log_item_image);
            logName=mView.findViewById(R.id.food_log_item_log_name);
            mealName=mView.findViewById(R.id.food_log_item_meal_name);
            date=mView.findViewById(R.id.food_log_item_date);
        }
    }
}
