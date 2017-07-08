package com.ilija.mygallery.core.model;

import android.net.Uri;

/**
 * Created by ikac on 7/8/17.
 */

public class ImageModel {

    private String title;
    private Uri thumbnailUri;
    private Uri imageUri;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getThumbnailUri() {
        return thumbnailUri;
    }

    public void setThumbnailUri(Uri thumbnailUri) {
        this.thumbnailUri = thumbnailUri;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
