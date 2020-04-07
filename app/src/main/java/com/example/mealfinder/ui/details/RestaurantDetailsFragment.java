package com.example.mealfinder.ui.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealfinder.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class RestaurantDetailsFragment extends Fragment {
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_restaurant_details, container, false);
        return root;
    }
}
