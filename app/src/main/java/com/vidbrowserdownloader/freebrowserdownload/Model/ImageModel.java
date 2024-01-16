package com.vidbrowserdownloader.freebrowserdownload.Model;

import android.net.Uri;

public class ImageModel {

    String imgName;
    String imgDate;
    Uri imageUri;
    String  imgPrev;
    String imgResolution;
    String imgSize;

    public String getImgSize() {
        return imgSize;
    }

    public void setImgSize(String imgSize) {
        this.imgSize = imgSize;
    }

    public String getImgResolution() {
        return imgResolution;
    }

    public void setImgResolution(String imgResolution) {
        this.imgResolution = imgResolution;
    }

    public String getImgPrev() {
        return imgPrev;
    }

    public void setImgPrev(String imgPrev) {
        this.imgPrev = imgPrev;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgDate() {
        return imgDate;
    }

    public void setImgDate(String imgDate) {
        this.imgDate = imgDate;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

}
