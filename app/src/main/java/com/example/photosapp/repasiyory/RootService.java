package com.example.photosapp.repasiyory;


import com.example.photosapp.data.Root;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RootService {
   @GET("/photos?client_id=GiIws0RzuMVDo5k73-JGDrBTo3KUkHG3os7eAIkS7Ww")
   public Call<List<Root>> getImages(
           @Query("page") int page,
           @Query("per_page") int perPage
   );
}
