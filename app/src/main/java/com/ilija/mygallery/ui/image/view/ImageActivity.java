package com.ilija.mygallery.ui.image.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ilija.mygallery.R;
import com.ilija.mygallery.ui.histogram.view.HistogramActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ikac on 7/8/17.
 */

public class ImageActivity extends AppCompatActivity {

    private static final String TAG = ImageActivity.class.getSimpleName();
    public static final String IMAGE_URI_EXTRA = "AbsolutePath";

    @BindView(R.id.image_full)
    ImageView mFullImage;
    @BindView(R.id.image_negative)
    Button mNegative;
    @BindView(R.id.image_grayscale)
    Button mGrayscale;
    @BindView(R.id.image_save)
    Button mDownload;
    @BindView(R.id.image_histogram)
    Button mHistogram;

    private String mImageUri;
    private Bitmap currentBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null && intent.getAction().equals(Intent.ACTION_VIEW)) {
            if (intent.getExtras() != null && intent.hasExtra(IMAGE_URI_EXTRA)) {
                mImageUri = intent.getStringExtra(IMAGE_URI_EXTRA);
                mImageUri = "file://" + Uri.parse(mImageUri);
            } else {
                return;
            }
        }

        if (mImageUri != null) {
            Picasso.with(this)
                    .load(mImageUri)
                    .into(mFullImage);
        }

    }

    @OnClick({R.id.image_negative, R.id.image_grayscale, R.id.image_histogram, R.id.image_save})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_negative:
                negative();
                break;
            case R.id.image_grayscale:
                greyscale();
                break;
            case R.id.image_histogram:
                Intent intent = new Intent(this, HistogramActivity.class);
                intent.putExtra(ImageActivity.IMAGE_URI_EXTRA, mImageUri);
                startActivity(intent);
                break;
            case R.id.image_save:
                save();
                break;

        }
    }

    private void negative() {

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap result, Picasso.LoadedFrom from) {
                int[] pix = new int[result.getWidth() * result.getHeight()];
                result.getPixels(pix, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());

                for (int y = 0; y < result.getHeight(); y++) {
                    for (int x = 0; x < result.getWidth(); x++) {
                        int index = y * result.getWidth() + x;

                        int R = (pix[index] >> 16) & 0xff;
                        int G = (pix[index] >> 8) & 0xff;
                        int B = pix[index] & 0xff;

                        int invR = 255 - R;
                        int invG = 255 - G;
                        int invB = 255 - B;

                        pix[index] = 0xFF000000 | (invR << 16) | (invG << 8) | (invB);
                    }
                }
                Bitmap bitmap = Bitmap.createBitmap(result.getWidth(), result.getHeight(), Bitmap.Config.ARGB_8888);
                bitmap.setPixels(pix, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
                mFullImage.setImageBitmap(bitmap);

                currentBitmap = bitmap;
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(this)
                .load(mImageUri)
                .into(target);
    }

    private void greyscale() {

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap result, Picasso.LoadedFrom from) {

                int[] pix = new int[result.getWidth() * result.getHeight()];
                result.getPixels(pix, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());

                for (int y = 0; y < result.getHeight(); y++) {
                    for (int x = 0; x < result.getWidth(); x++) {
                        int index = y * result.getWidth() + x;

                        int R = (pix[index] >> 16) & 0xff;
                        int G = (pix[index] >> 8) & 0xff;
                        int B = pix[index] & 0xff;

                        double Y = R * 0.21 + G * 0.72 + B * 0.07;
                        int intY = (int) Y;
                        pix[index] = 0xFF000000 | (intY << 16) | (intY << 8) | (intY);

                    }
                }
                Bitmap bitmap = Bitmap.createBitmap(result.getWidth(), result.getHeight(), Bitmap.Config.ARGB_8888);
                bitmap.setPixels(pix, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
                mFullImage.setImageBitmap(bitmap);

                currentBitmap = bitmap;
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(this)
                .load(mImageUri)
                .into(target);
    }

    private void save() {
        if (currentBitmap != null) {
            new Thread(() -> {
                final String newImageUrl = Environment.getExternalStorageDirectory().getPath() + "/" + (new Date().getTime() + ".jpeg");
                File file = new File(newImageUrl);
                try {
                    FileOutputStream ostream = new FileOutputStream(file);
                    currentBitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                    ostream.flush();
                    ostream.close();
                    Toast.makeText(ImageActivity.this, "Image saved as " + newImageUrl, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(ImageActivity.this, "Image not saved", Toast.LENGTH_LONG).show();
                    Log.e("IOException", e.getLocalizedMessage());
                }
            }).start();
        }
    }
}
