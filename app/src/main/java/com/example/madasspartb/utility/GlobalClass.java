package com.example.madasspartb.utility;

import android.graphics.Bitmap;

import java.util.List;

public class GlobalClass {

    private static GlobalClass instance = new GlobalClass();

    public static GlobalClass getInstance() {
        return instance;
    }

    public static void setInstance(GlobalClass instance) {
        GlobalClass.instance = instance;
    }

    private List<Bitmap> imageList;
    private List<Bitmap> toUploadList;
    private List<Integer> selectedPositions;

    public GlobalClass() {

    }

    public List<Bitmap> getImageList() {
        return imageList;
    }

    public void setImageList(List<Bitmap> imageList) {
        this.imageList = imageList;
    }

    public List<Bitmap> getToUploadList() {
        return toUploadList;
    }

    public void setToUploadList(List<Bitmap> toUploadList) {
        this.toUploadList = toUploadList;
    }

    public List<Integer> getSelectedPositions() {
        return selectedPositions;
    }

    public void setSelectedPositions(List<Integer> selectedPositions) {
        this.selectedPositions = selectedPositions;
    }
}
