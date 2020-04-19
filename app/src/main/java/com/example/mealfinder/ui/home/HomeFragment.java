package com.example.mealfinder.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.mealfinder.R;
import com.example.mealfinder.adapter.FeedAdapter;
//import com.example.mealfinder.model.Location;
import com.example.mealfinder.model.Diet;
import com.example.mealfinder.model.Restaurant;
import com.example.mealfinder.model.RestaurantList;
import com.example.mealfinder.network.GetDataService;
import com.example.mealfinder.network.RetrofitClientInstance;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static androidx.core.content.ContextCompat.getSystemService;

@RuntimePermissions
public class HomeFragment extends Fragment {

    ProgressDialog progressDialog;
    private ListView listView;
    View root;
    private FirebaseFirestore mFirestore;
    List<Diet> dietsReceived= new ArrayList<>();
//    private double lat, lon;

    private FusedLocationProviderClient mFusedLocationClient;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);
        initFirestore();
//        getRestaurants(lat, lon);
        getDiets();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching restaurants...");
        progressDialog.show();
        requestLocationPermission();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        return root;
    }

    private void getDiets(){
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        Query query = rootRef.collection("users").document(uid).collection("diets");
        Source source = Source.CACHE;
        query.get(source).addOnSuccessListener(documentSnapshots -> {
            if (documentSnapshots.isEmpty()) {
                Log.d("empty", "onSuccess: LIST EMPTY");
                return;
            } else {
                List<Diet> diets = documentSnapshots.toObjects(Diet.class);
                dietsReceived.addAll(diets);
            }
        });
    }
    private void getRestaurants(double lat, double lon){

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        Log.e("Latitude: ", Double.toString(lat));
        Log.e("Longitude", Double.toString(lon));

        String keyword="";
        for (Diet d : dietsReceived)
            keyword+=d.toString()+" ";
        Log.d("keyword", keyword);
        Call<RestaurantList> getRestaurants=service.getRestaurants(keyword,10, lat, -lon,"rating", "desc");
        dietsReceived.clear();
        getRestaurants.enqueue(new Callback<RestaurantList>() {
            @Override
            public void onResponse(Call<RestaurantList> call, Response<RestaurantList> response) {
                progressDialog.dismiss();
                //put data in adapter
                assert response.body() != null;
                for (Restaurant r: response.body().getRestaurantList()) {
                        Log.d("location", r.getRestaurantInfo().getLocation().getLocality());
                    if (response.body().getRestaurantList().isEmpty()){
                        Toast.makeText(getContext(), "We have nothing to show you", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        generateFeed(response.body().getRestaurantList());
                    }
                }
            }
            @Override
            public void onFailure(Call<RestaurantList> call, Throwable t) {

            }
        });
}

    private void generateFeed(final List<Restaurant> restaurantList){
        final RecyclerView recyclerView = root.findViewById(R.id.feed_recycler_view);
        final FeedAdapter adapter = new FeedAdapter(getContext(), restaurantList);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FeedAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("res_id", restaurantList.get(position).getRestaurantInfo().getId());
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.feed_to_restaurant_details,bundle);
            }

            @Override
            public void onFavoriteClick(int position) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                CollectionReference favorite_restaurants = mFirestore.collection("users").document(uid).collection("favorite_restaurants");
                favorite_restaurants.add(restaurantList.get(position).getRestaurantInfo());
                recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.add_to_favorites).setBackgroundResource(R.drawable.ic_favorite_feed);
             }

        });
    }
    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean locationEnabled () {
        LocationManager lm = (LocationManager)
               getActivity().getSystemService(Context. LOCATION_SERVICE ) ;
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager. GPS_PROVIDER ) ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager. NETWORK_PROVIDER ) ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }
        if (gps_enabled && network_enabled) { return true; }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(getActivity())
                    .setMessage( "Your GPS is disabled. Please, turn it on." )
                    .setPositiveButton( "Settings" , new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick (DialogInterface paramDialogInterface , int paramInt) {
                                    startActivity( new Intent(Settings. ACTION_LOCATION_SOURCE_SETTINGS )) ;
                                }
                            })
                    .setNegativeButton( "Cancel" , null )
                    .show() ;
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void requestLocationPermission() {
        Log.e("Permission", Manifest.permission.ACCESS_FINE_LOCATION);
        if (locationEnabled()) {
            Log.e("location", "enabled");
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.e("bla", "blabla");
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, PackageManager.PERMISSION_GRANTED);
                Log.e("Permission", Manifest.permission.ACCESS_FINE_LOCATION);
                getLastLocation();
            } else {
                Log.d("Call to getLastLocation", "OK");
                getLastLocation();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (mFusedLocationClient != null) {
                Log.e("Fused location", "not null");
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                    if (location == null) {
                                        requestNewLocationData();
                                    } else {
                                        getRestaurants(location.getLatitude(), location.getLongitude());
                                    }
                            }
                        }
                    );
                }
            Log.e("fuse", "null");
            requestNewLocationData();
            }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            android.location.Location mLastLocation = locationResult.getLastLocation();
            Log.d("latitude", ""+mLastLocation.getLatitude()+ ", "+mLastLocation.getLongitude());
            getRestaurants(mLastLocation.getLatitude(),  mLastLocation.getLongitude());
        }
    };


}