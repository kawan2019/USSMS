package com.example.ussms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ussms.Model.AddPost;
import com.example.ussms.R;

import java.util.List;

public class PickedImagesAdapter extends RecyclerView.Adapter<PickedImagesAdapter.ImageViewHolder> {

    Context context;
    List<AddPost> imagesList;

    public PickedImagesAdapter(Context context, List<AddPost> imagesList) {
        this.context = context;
        this.imagesList = imagesList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.l_piked_image_news_add, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, final int position) {
        holder.imageView.setImageURI(imagesList.get(position).getImageURI());
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, imagesList.get(position).getImageName()+" removed!", Toast.LENGTH_SHORT).show();
                imagesList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageButton btnRemove;
        ImageView imageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            btnRemove = itemView.findViewById(R.id.btnRemoveImage);
            imageView = itemView.findViewById(R.id.igv_card_pick);
        }
    }
}
