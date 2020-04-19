package com.example.mealfinder.ui.details;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mealfinder.MainActivity;
import com.example.mealfinder.R;
import com.example.mealfinder.model.RestaurantDetails;
import com.example.mealfinder.model.RestaurantInfo;
import com.example.mealfinder.network.GetDataService;
import com.example.mealfinder.network.RetrofitClientInstance;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDetailsFragment extends Fragment {
    View root;
    ImageView restaurantImage;
    TextView restaurantName;
    TextView restaurantLocation;
    TextView restaurantTiming;
    TextView restaurantCuisines;
    TextView restaurantAverageForTwo;
    RatingBar ratingBar;
    TextView ratingVotes;
    String res_id;
    int restaurant_id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_restaurant_details, container, false);
        restaurantImage=root.findViewById(R.id.restaurant_image_details);
        restaurantName=root.findViewById(R.id.restaurant_name_details);
        restaurantLocation=root.findViewById(R.id.restaurant_location_details);
        ratingBar=root.findViewById(R.id.restRating);
        ratingVotes=root.findViewById(R.id.textNoEvaluations);
        restaurantTiming=root.findViewById(R.id.restaurant_timings);
        restaurantCuisines=root.findViewById(R.id.restaurantCuisines);
        restaurantAverageForTwo=root.findViewById(R.id.restaurantAverageCost);

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            //get id of event that was clicked
            res_id = bundle.getString("res_id", "");
            restaurant_id=Integer.parseInt(res_id);
            Log.d("rest_id", restaurant_id+" ");
            getRestaurantDetails(restaurant_id);
        }
        return root;
    }

    private void getRestaurantDetails(int restaurant_id){
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<RestaurantDetails> call_rest_details=service.getRestaurantById(restaurant_id);
        call_rest_details.enqueue(new Callback<RestaurantDetails>() {
            @Override
            public void onResponse(Call<RestaurantDetails> call, Response<RestaurantDetails> response) {
                putData(response.body());
            }

            @Override
            public void onFailure(Call<RestaurantDetails> call, Throwable t) {

            }
        });
    }

    private void putData(RestaurantDetails restaurantDetails){
        ((MainActivity) getActivity()).setActionBarTitle(restaurantDetails.getName());
        Glide.with(getContext()).load(restaurantDetails.getThumb()).into(restaurantImage);
        restaurantName.setText(restaurantDetails.getName());
        ratingBar.setRating(Float.parseFloat(restaurantDetails.getUserRating().getAggregateRating()));
        ratingVotes.setText(restaurantDetails.getUserRating().getVotes() + " votes");
        restaurantLocation.setText(restaurantDetails.getLocation().getAddress());
        restaurantTiming.setText(restaurantDetails.getTimings());
        restaurantCuisines.setText(restaurantDetails.getCuisines());
        restaurantAverageForTwo.setText(restaurantDetails.getAverageCostForTwo()+" "+restaurantDetails.getCurrency());

    }
}
