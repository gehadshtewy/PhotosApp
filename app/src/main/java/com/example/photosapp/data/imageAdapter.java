package com.example.photosapp.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.photosapp.MainActivity;
import com.example.photosapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class imageAdapter extends RecyclerView.Adapter<imageAdapter.ImageViewHolder> {
 private Context context;
 private ArrayList<Root> list;

    public imageAdapter(Context context, ArrayList<Root> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.image_item,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Root root = list.get(position);

        Glide.with(context).load(list.get(position).urls.regular)
                .into(holder.imageView);
        holder.heartIcon.setOnClickListener(v -> {
            checkAndAddToFavorites(root);

        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView heartIcon;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            heartIcon = itemView.findViewById(R.id.heart_icon);
        }
    }

    public void addItems(List<Root> newItems) {
        list.addAll(newItems);
        notifyDataSetChanged();
    }

    private void checkAndAddToFavorites(Root root) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("favorites")
                .whereEqualTo("id", root.id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            // Photo is not in favorites, add it
                            db.collection("favorites").add(root)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(context,"Success added",Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle failure
                                    });
                        } else {
                            Toast.makeText(context,"Photo is already in favorites",Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(context,"error",Toast.LENGTH_SHORT).show();
                        // Handle the error
                    }});
                }


}
