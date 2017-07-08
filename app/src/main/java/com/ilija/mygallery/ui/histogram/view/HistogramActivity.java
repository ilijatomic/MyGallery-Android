package com.ilija.mygallery.ui.histogram.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.ilija.mygallery.R;
import com.ilija.mygallery.ui.image.view.ImageActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ikac on 7/8/17.
 */

public class HistogramActivity extends AppCompatActivity {

    @BindView(R.id.layout_histogram )
    LinearLayout mHistogramLayout;

    private Context mContext;
    private String mImageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histogram);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getExtras() != null && intent.hasExtra(ImageActivity.IMAGE_URI_EXTRA)) {
                mImageUri = intent.getStringExtra(ImageActivity.IMAGE_URI_EXTRA);
            } else {
                return;
            }
        }
        mContext = this;

        Target histogramBitmapTarget = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap result, Picasso.LoadedFrom from) {
                mHistogramLayout.addView(new HistogramView(mContext, result));
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
                .into(histogramBitmapTarget);
    }
}
