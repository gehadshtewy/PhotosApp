package com.example.photosapp.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.photosapp.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class favoriteAdapter extends RecyclerView.Adapter<favoriteAdapter.ImageViewHolder> {
    private Context context;
    private ArrayList<Root> list;

    public favoriteAdapter(Context context, ArrayList<Root> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.favorite_photos,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder,  int position) {
        Glide.with(context).load(list.get(position).urls.regular)
                .into(holder.imageView);

        holder.delete.setOnClickListener(v -> {
            deletePhoto(list.get(position), position);
        });
       


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView delete;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            delete = itemView.findViewById(R.id.delete_icon);

        }
    }

    private void deletePhoto(Root photo, int position) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Assuming 'photo' object contains a reference to the photo URL or path in Firebase Storage
        StorageReference photoRef = storage.getReferenceFromUrl(photo.urls.regular);

        // Delete the photo from Firebase Storage
        photoRef.delete().addOnSuccessListener(aVoid -> {
            // Photo deleted successfully from Storage, now delete from Firestore
            db.collection("favorites").document(photo.id)
                    .delete()
                    .addOnSuccessListener(aVoid2 -> {
                        // Photo deleted successfully from Firestore
                        list.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, list.size());
                        Toast.makeText(context, "Photo deleted", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(exception -> {
                        // An error occurred while deleting from Firestore
                        Toast.makeText(context, "Failed to delete photo from Firestore", Toast.LENGTH_SHORT).show();
                    });
        }).addOnFailureListener(exception -> {
            // An error occurred while deleting from Storage
            Toast.makeText(context, "Failed to delete photo from Storage", Toast.LENGTH_SHORT).show();
        });
    }

}
