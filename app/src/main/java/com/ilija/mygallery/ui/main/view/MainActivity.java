package com.ilija.mygallery.ui.main.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ilija.mygallery.R;
import com.ilija.mygallery.core.model.ImageModel;
import com.ilija.mygallery.ui.image.view.ImageActivity;
import com.ilija.mygallery.ui.main.di.DaggerMainComponent;
import com.ilija.mygallery.ui.main.di.MainModule;
import com.ilija.mygallery.ui.main.presenter.MainPresenter;
import com.ilija.mygallery.ui.main.view.adapter.MainAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by ikac on 7/8/17.
 */

public class MainActivity extends AppCompatActivity implements MainListener, EasyPermissions.PermissionCallbacks {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int READ_WRITE_EXTERNAL_STORAGE = 1;

    @BindView(R.id.main_images_list)
    RecyclerView mMainImages;
    @BindView(R.id.main_progress_bar)
    ProgressBar mMainProgressBar;

    @Inject
    MainPresenter mMainPresenter;

    private MainAdapter mMainAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        DaggerMainComponent
                .builder()
                .mainModule(new MainModule(this))
                .build()
                .inject(this);

        mMainAdapter = new MainAdapter();
        mMainAdapter.getViewClickSubject().subscribe(this::startImageActivity);

        mMainImages.setLayoutManager(new GridLayoutManager(this, 3));
        mMainImages.setHasFixedSize(true);
        mMainImages.setItemViewCacheSize(20);
        mMainImages.setDrawingCacheEnabled(true);
        mMainImages.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mMainImages.setAdapter(mMainAdapter);

        requirePermissions();
    }

    private void loadImages() {
        mMainPresenter.init(this);
        mMainPresenter.loadImages();
        mMainProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(READ_WRITE_EXTERNAL_STORAGE)
    private void requirePermissions() {
        String[] perms = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            loadImages();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.read_external_storage),
                    READ_WRITE_EXTERNAL_STORAGE, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsGranted: granted");
        loadImages();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(this, "Cannot load images due to lack of permission", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showImages(List<ImageModel> images) {
        mMainProgressBar.setVisibility(View.GONE);
        mMainAdapter.setmImageModels(images);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_download:
                showDowloadDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDowloadDialog() {

        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setHint(R.string.alert_dialog_hint);

        new AlertDialog.Builder(this)
                .setTitle(R.string.alert_dialog_title)
                .setPositiveButton(R.string.download, (dialog, which) -> {
                    if (input.getText().length() > 0) {
                        startDownload(input.getText().toString());
                    }
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                })
                .setView(input)
                .show();
    }

    private void startDownload(final String url) {
        Target target = new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(() -> {
                    final String newImageUrl = Environment.getExternalStorageDirectory().getPath() + "/" + (new Date().getTime() + ".jpeg");
                    File file = new File(newImageUrl);
                    try {
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                        ostream.flush();
                        ostream.close();
                    } catch (IOException e) {
                        Log.e("IOException", e.getLocalizedMessage());
                    }

                    new Handler(Looper.getMainLooper()).post(() -> startImageActivity(newImageUrl));
                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Toast.makeText(MainActivity.this, "Download bitmap failed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        Picasso.with(this)
                .load(url)
                .into(target);
    }


    void startImageActivity(String imageUri) {
        Intent intent = new Intent(this, ImageActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra(ImageActivity.IMAGE_URI_EXTRA, imageUri);
        startActivity(intent);
    }
}
