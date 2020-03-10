package com.example.mealfinder.ui.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealfinder.DetailsActivity;
import com.example.mealfinder.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class FavoritesFragment extends Fragment {
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> mylist;

    private FavoritesViewModel favoritesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*favoritesViewModel =
                ViewModelProviders.of(this).get(FavoritesViewModel.class);
         */
        View root = inflater.inflate(R.layout.fragment_favorites, container, false);
        final TextView textView = root.findViewById(R.id.text_favorites);
        /*favoritesViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */
        listView = root.findViewById(R.id.listView);
        mylist = new ArrayList<>();
        mylist.add("Restaurante X");
        mylist.add("Restaurante W");
        mylist.add("Restaurante A");
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mylist);
        listView.setAdapter(adapter);
        textView.setText("Favorites:");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast
                        .makeText(getContext(),
                                "Pos " + position + " clicked",
                                Toast.LENGTH_LONG)
                        .show();
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}