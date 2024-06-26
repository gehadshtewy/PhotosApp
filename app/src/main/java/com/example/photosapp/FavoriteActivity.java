package com.example.photosapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photosapp.data.Root;
import com.example.photosapp.data.imageAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    private static final String TAG = "FavoritesActivity";
    private RecyclerView recyclerView;
    private ArrayList<Root> list;
    private imageAdapter adapter;
    private GridLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorite);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        recyclerView = findViewById(R.id.recyclerviewf);
        list = new ArrayList<>();
        adapter = new imageAdapter(this, list);
        manager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        fetchFavorites();

        Button viewProfileButton = findViewById(R.id.buttonProfile);
        viewProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(FavoriteActivity.this, ProfileActivity.class);
            startActivity(intent);
        });



    }
    private void fetchFavorites() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("favorites")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Root root = document.toObject(Root.class);
                            list.add(root);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }


}