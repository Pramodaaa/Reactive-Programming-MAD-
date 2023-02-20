package com.example.madasspartb.adaptors;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.madasspartb.R;
import com.example.madasspartb.utility.GlobalClass;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>{

    private List<Bitmap> imageList;
    private List<Bitmap> selectedList;
    GlobalClass sharedData;

    public ImageAdapter(List<Bitmap> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_dispaly_item, parent, false);
        sharedData = GlobalClass.getInstance();
        selectedList = new ArrayList<>();
        return new ImageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.picture.setImageBitmap(imageList.get(position));

        if (!selectedList.isEmpty()) {
            boolean selected = selectedList.contains(position);
            if(selected) {
                holder.checkBtnImage.setVisibility(View.VISIBLE);
            } else {
                holder.checkBtnImage.setVisibility(View.INVISIBLE);
            }
        }

        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBtnImage.getVisibility() == View.INVISIBLE) {
                    holder.checkBtnImage.setVisibility(View.VISIBLE);
                    selectedList.add(imageList.get(position));
                    sharedData.setToUploadList(selectedList);
                } else {
                    selectedList.remove(imageList.get(position));
                    sharedData.setToUploadList(selectedList);
                    holder.checkBtnImage.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(imageList == null) {
            imageList = new ArrayList<>();
        }
        return imageList.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView checkBtnImage;
        ImageView picture;


        public ImageViewHolder(View view) {
            super(view);
            checkBtnImage = view.findViewById(R.id.checkBoxImage);
            picture = view.findViewById(R.id.pictureId);

        }
    }

    public interface ImageListClickListener {
        public void onItemClick(int position);
    }

}
