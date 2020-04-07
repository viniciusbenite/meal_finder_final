package com.example.mealfinder.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.mealfinder.DetailsActivity;
import com.example.mealfinder.R;
import com.example.mealfinder.model.Restaurant;
import com.example.mealfinder.model.RestaurantList;
import com.example.mealfinder.network.GetDataService;
import com.example.mealfinder.network.RetrofitClientInstance;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment{

    private ListView listView;

    private ArrayList<String> mylist= new ArrayList<>();
    private ArrayAdapter adapter;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        getRestaurants();
        return root;
    }

    private void getRestaurants(){
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<RestaurantList> getRestaurants=service.getRestaurants(10, 40.64427, 8.64554, "308", "rating", "desc");
        getRestaurants.enqueue(new Callback<RestaurantList>() {
            @Override
            public void onResponse(Call<RestaurantList> call, Response<RestaurantList> response) {
                Log.d("Name of the restaurant", response.body().getRestaurantList().toString());

            }

            @Override
            public void onFailure(Call<RestaurantList> call, Throwable t) {

            }
        });
}





}