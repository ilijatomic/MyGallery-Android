package com.ilija.mygallery.ui.main.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.ilija.mygallery.core.model.ImageModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ikac on 7/8/17.
 */

public class MainModel implements IMainModel {

    private Context mContext;

    public MainModel(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Observable<List<ImageModel>> getImages() {
        return Observable.create(subscriber -> {

            final String[] projection = {MediaStore.Images.Thumbnails.DATA,
                    MediaStore.Images.Thumbnails.IMAGE_ID,
                    MediaStore.Images.Thumbnails.DATA};

            Cursor thumbnailsCursor = mContext.getContentResolver().query(
                    MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                    projection, // Which columns to return
                    null,       // Return all rows
                    null,
                    null);

            // Extract the proper column thumbnails
            int thumbnailColumnIndex = thumbnailsCursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
            ArrayList<ImageModel> result = new ArrayList<>(thumbnailsCursor.getCount());

            if (thumbnailsCursor.moveToFirst()) {
                do {
                    // Generate a tiny thumbnail version.
                    int thumbnailImageID = thumbnailsCursor.getInt(thumbnailColumnIndex);

                    int titleIndex = thumbnailsCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    String title = thumbnailsCursor.getString(titleIndex);
                    int lastIndex = title.lastIndexOf('/');
                    title = title.substring(lastIndex + 1);

                    String thumbnailPath = thumbnailsCursor.getString(thumbnailImageID);
                    Uri thumbnailUri = Uri.parse(thumbnailPath);
                    Uri fullImageUri = fullImageUri(thumbnailsCursor, mContext);

                    // Create the list item.
                    ImageModel newItem = new ImageModel();
                    newItem.setTitle(title);
                    newItem.setThumbnailUri(thumbnailUri);
                    newItem.setImageUri(fullImageUri);
                    result.add(0, newItem);
                } while (thumbnailsCursor.moveToNext());
            }
            thumbnailsCursor.close();

            subscriber.onNext(result);
            subscriber.onComplete();
        });
    }

    private Uri fullImageUri(Cursor cursor, Context context) {
        String imageId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID));

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor imagesCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, filePathColumn, MediaStore.Images.Media._ID + "=?", new String[]{imageId}, null);

        if (imagesCursor != null && imagesCursor.moveToFirst()) {
            int columnIndex = imagesCursor.getColumnIndex(filePathColumn[0]);
            String filePath = imagesCursor.getString(columnIndex);
            imagesCursor.close();
            return Uri.parse(filePath);
        } else {
            imagesCursor.close();
            return Uri.parse("");
        }
    }
}
