package com.example.mealfinder.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealfinder.R;
import com.example.mealfinder.adapter.FavoritesAdapter;
import com.example.mealfinder.model.RestaurantInfo;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavoritesFragment extends Fragment {

    View root;
    private FirebaseFirestore mFirestore;
    private FavoritesAdapter adapter;
    RecyclerView favorites_recycler_view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_favorites, container, false);
        favorites_recycler_view=root.findViewById(R.id.favorites_recycler_view);
        getFavoriteRestaurants();

        return root;
    }


    private void getFavoriteRestaurants(){
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = rootRef.collection("users").document(uid).collection("favorite_restaurants");

        FirestoreRecyclerOptions<RestaurantInfo> options = new FirestoreRecyclerOptions.Builder<RestaurantInfo>()
                .setQuery(query, RestaurantInfo.class)
                .build();
        adapter= new FavoritesAdapter(options, getContext());
        adapter.notifyDataSetChanged();
        favorites_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        favorites_recycler_view.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}