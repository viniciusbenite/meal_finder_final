package com.example.mealfinder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mealfinder.R;
import com.example.mealfinder.model.Restaurant;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private List<Restaurant> dataList;
    private Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onFavoriteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public FeedAdapter(Context context, List<Restaurant> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    static class FeedViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        ImageView restaurantImage;
        TextView restaurantName;
        TextView restaurantLocation;
        Button addToFavorites;

        FeedViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mView = itemView;
            restaurantImage=mView.findViewById(R.id.Restaurant_Image);
            restaurantName=mView.findViewById(R.id.Restaurant_Name);
            restaurantLocation=mView.findViewById(R.id.Restaurant_Location);
            addToFavorites=mView.findViewById(R.id.add_to_favorites);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener !=null){
                        int position=getAdapterPosition();
                        if (position !=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            addToFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener !=null){
                        int position=getAdapterPosition();
                        if (position !=RecyclerView.NO_POSITION){
                            listener.onFavoriteClick(position);
                        }
                    }
                }
            });
        }
    }
    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.restaurant_feed, parent, false);
        return new FeedViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getRestaurantInfo().getThumb()).into(holder.restaurantImage);
        holder.restaurantName.setText(dataList.get(position).getRestaurantInfo().getName());
        holder.restaurantLocation.setText(dataList.get(position).getRestaurantInfo().getLocation().getLocality());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
