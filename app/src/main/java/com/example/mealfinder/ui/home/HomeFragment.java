package com.example.mealfinder.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.mealfinder.R;
import com.example.mealfinder.adapter.FeedAdapter;
import com.example.mealfinder.model.Restaurant;
import com.example.mealfinder.model.RestaurantList;
import com.example.mealfinder.network.GetDataService;
import com.example.mealfinder.network.RetrofitClientInstance;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class HomeFragment extends Fragment{

    private ListView listView;
    View root;
    private FirebaseFirestore mFirestore;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);
        initFirestore();
        getRestaurants();
        return root;
    }

    private void getRestaurants(){
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<RestaurantList> getRestaurants=service.getRestaurants(10, 40.64427, 8.64554, "308", "rating", "desc");
        getRestaurants.enqueue(new Callback<RestaurantList>() {
            @Override
            public void onResponse(Call<RestaurantList> call, Response<RestaurantList> response) {

                //put data in adapter
                for (Restaurant r: response.body().getRestaurantList())
                    Log.d("location", r.getRestaurantInfo().getLocation().getLocality());
                if (response.body().getRestaurantList().isEmpty()){
                    Toast.makeText(getContext(), "We have nothing to show you", Toast.LENGTH_SHORT).show();
                }
                else {
                    generateFeed(response.body().getRestaurantList());
                }
            }
            @Override
            public void onFailure(Call<RestaurantList> call, Throwable t) {

            }
        });
}

    private void generateFeed(final List<Restaurant> restaurantList){
        RecyclerView recyclerView = root.findViewById(R.id.feed_recycler_view);
        FeedAdapter adapter = new FeedAdapter(getContext(), restaurantList);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FeedAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("event_id", restaurantList.get(position).getRestaurantInfo().getId());
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.feed_to_restaurant_details,bundle);
            }

            @Override
            public void onFavoriteClick(int position) {
                Toast.makeText(getContext(), "adding to Favorites", Toast.LENGTH_SHORT).show();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                CollectionReference favorite_restaurants = mFirestore.collection("users").document(uid).collection("favorite_restaurants");
                favorite_restaurants.add(restaurantList.get(position).getRestaurantInfo());
            }
        });
    }
    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
    }

}