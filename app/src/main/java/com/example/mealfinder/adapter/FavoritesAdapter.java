package com.example.mealfinder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mealfinder.R;
import com.example.mealfinder.model.RestaurantInfo;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FavoritesAdapter extends FirestoreRecyclerAdapter<RestaurantInfo, FavoritesAdapter.FavoritesViewHolder> {

    private Context context;
    public FavoritesAdapter(@NonNull FirestoreRecyclerOptions<RestaurantInfo> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    public void onBindViewHolder(FavoritesViewHolder holder, int position, RestaurantInfo model) {
        Glide.with(context).load(model.getThumb()).into(holder.restaurantImage);
        holder.restaurantName.setText(model.getName());
        holder.restaurantLocation.setText(model.getLocation().getLocality());
    }
    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.restaurant_favorites, group, false);
        return new FavoritesViewHolder(view);
    }
    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    static class FavoritesViewHolder extends RecyclerView.ViewHolder{

        public final View mView;

        ImageView restaurantImage;
        TextView restaurantName;
        TextView restaurantLocation;

        FavoritesViewHolder(View itemView) {

            super(itemView);
            mView=itemView;
            restaurantImage=mView.findViewById(R.id.Restaurant_Image_Favorites);
            restaurantName=mView.findViewById(R.id.Restaurant_Name_Favorites);
            restaurantLocation=mView.findViewById(R.id.Restaurant_Location_Favorites);
        }
    }
}
