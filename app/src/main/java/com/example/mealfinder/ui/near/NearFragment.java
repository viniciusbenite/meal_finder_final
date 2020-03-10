package com.example.mealfinder.ui.near;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mealfinder.R;
import com.example.mealfinder.ui.home.HomeViewModel;

public class NearFragment extends Fragment {

    private NearViewModel nearFragmentModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        nearFragmentModel =
                ViewModelProviders.of(this).get(NearViewModel.class);
        View root = inflater.inflate(R.layout.fragment_near, container, false);
        final TextView textView = root.findViewById(R.id.text_near);
        nearFragmentModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}