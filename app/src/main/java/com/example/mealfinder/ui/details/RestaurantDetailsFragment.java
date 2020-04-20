package com.example.mealfinder.ui.details;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mealfinder.MainActivity;
import com.example.mealfinder.R;
import com.example.mealfinder.adapter.ReviewAdapter;
import com.example.mealfinder.model.RestaurantDetails;
import com.example.mealfinder.model.Review;
import com.example.mealfinder.network.GetDataService;
import com.example.mealfinder.network.RetrofitClientInstance;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
    RecyclerView restaurantReviews;

    private ReviewAdapter adapter;

    private FirebaseFirestore mFirestore;


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
        restaurantReviews = root.findViewById(R.id.recycler_view_reviews);


        initFirestore();
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            //get id of event that was clicked
            res_id = bundle.getString("res_id", "");
            restaurant_id=Integer.parseInt(res_id);
            Log.d("rest_id", restaurant_id+" ");
            getRestaurantDetails(restaurant_id);
            getRestaurantReviews(restaurant_id);
            Button button = root.findViewById(R.id.addBtn);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    showTextDialog(restaurant_id);
                }
            });
        }
        return root;
    }

    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
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

    private void postReview(int restaurant_id, String user, String text, float rating){
        //get current user to store the username in firestore
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        assert mFirebaseUser != null;
        String mUsername = mFirebaseUser.getDisplayName();
        Review review = new Review(restaurant_id, mUsername, text, rating);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference userReviews = mFirestore.collection("users").document(uid).collection("reviews");
        CollectionReference allReviews = mFirestore.collection("reviews").document("reviews_doc").collection(String.valueOf(restaurant_id));
        userReviews.add(review);
        allReviews.add(review);
    }

    private void getRestaurantReviews(int restaurant_id) {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        Query query = rootRef.collection("reviews").document("reviews_doc").collection(String.valueOf(restaurant_id)).whereEqualTo("restaurantID", restaurant_id);
        FirestoreRecyclerOptions<Review> options = new FirestoreRecyclerOptions.Builder<Review>()
                .setQuery(query, Review.class)
                .build();

        adapter = new ReviewAdapter(options, getContext());
        adapter.notifyDataSetChanged();
        restaurantReviews.setLayoutManager(new LinearLayoutManager(getContext()));
        restaurantReviews.setAdapter(adapter);
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

    private void showTextDialog(int restaurant_id){

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference usersRef = rootRef.collection("users");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter your review");
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.make_review, null);
        builder.setView(dialogLayout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String user = usersRef.document(uid).getId();
                String text = ((EditText)dialogLayout.findViewById(R.id.NewReviewText)).getText().toString();
                float rating =((RatingBar)dialogLayout.findViewById(R.id.NewReviewRating)).getRating();
                postReview(restaurant_id, user, text, rating);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
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
