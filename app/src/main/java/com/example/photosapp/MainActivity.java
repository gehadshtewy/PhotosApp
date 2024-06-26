package com.example.photosapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photosapp.data.Root;
import com.example.photosapp.data.imageAdapter;
import com.example.photosapp.repasiyory.RootApiClient;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    //private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private ArrayList<Root> list;
    private imageAdapter adapter;
    private GridLayoutManager manager;
    private int page = 1;
    private String imageUrl;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        {/* mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user != null) {
                    Intent profileActivityIntent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(profileActivityIntent);
                } else {
                    Intent loginIntent = new Intent(MainActivity.this, login.class);
                    startActivity(loginIntent);
                }
            }
        }, 2000);*/}



        recyclerView = findViewById(R.id.recylerview);
        list = new ArrayList<>();
        adapter =new imageAdapter(this,list);
        manager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        getData();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!recyclerView.canScrollVertically(1)) { // Check if the user has scrolled to the bottom
                    page++;
                    getData();
                }
            }
        });

        Button viewFavoritesButton = findViewById(R.id.buttonfavorite);
        viewFavoritesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
            startActivity(intent);
        });
    }

    private void getData() {
       RootApiClient.getRootService().getImages(page,30)
               .enqueue(new Callback<List<Root>>() {
                   @Override
                   public void onResponse(Call<List<Root>> call, @NonNull Response<List<Root>> response) {
                    if(response.body() != null){
                        list.addAll(response.body());
                        adapter.notifyDataSetChanged();
                    }

                   }

                   @Override
                   public void onFailure(Call<List<Root>> call, @NonNull Throwable throwable) {
                       Log.e(TAG, "Failed to fetch data: " + throwable.getMessage());
                       throwable.printStackTrace();
                   }
               });


    }

}