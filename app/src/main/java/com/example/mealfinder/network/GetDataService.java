package com.example.mealfinder.network;

import com.example.mealfinder.model.RestaurantInfo;
import com.example.mealfinder.model.RestaurantList;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface GetDataService {

    @Headers("user-key: 00469c39896ef18cd0fcbe0bf5111171")
    @GET("search")
    Call<RestaurantList> getRestaurants(@Query("count") int count, @Query("lat") double lat, @Query("lon") double lon, @Query("cuisines") String cuisines,
                                        @Query("sort") String sort, @Query("order") String desc);

    @Headers("user-key: 00469c39896ef18cd0fcbe0bf5111171")
    @GET("restaurant")
    Call<RestaurantInfo> getRestaurantById(@Query("res_id") int res_id);


}
