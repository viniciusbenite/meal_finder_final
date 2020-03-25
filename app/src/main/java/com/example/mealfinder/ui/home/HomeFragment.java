package com.example.mealfinder.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealfinder.DetailsActivity;
import com.example.mealfinder.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment{

    private ListView listView;

    private ArrayList<String> mylist= new ArrayList<>();
    private ArrayAdapter adapter;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel =
          //      ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        /*
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */
         setHasOptionsMenu(true);
        listView = root.findViewById(R.id.listView);
        mylist = new ArrayList<>();
        mylist.add("Restaurante A");
        mylist.add("Restaurante B");
        mylist.add("Restaurante C");
        mylist.add("Restaurante D");
        mylist.add("Restaurante E");
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mylist);
        listView.setAdapter(adapter);
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
        textView.setText("Restaurantes Recomendados para si:");
        return root;
    }





}