package com.ilija.mygallery.ui.main.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilija.mygallery.R;
import com.ilija.mygallery.core.model.ImageModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by ikac on 7/8/17.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ImageViewHolder> {

    private PublishSubject<String> mViewClickSubject = PublishSubject.create();

    private List<ImageModel> mImageModels;

    public void setmImageModels(List<ImageModel> mImageModels) {
        this.mImageModels = mImageModels;
        notifyDataSetChanged();
    }

    public PublishSubject<String> getViewClickSubject() {
        return mViewClickSubject;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_images_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        ImageModel imageModel = mImageModels.get(position);

        holder.mImageTitle.setText(imageModel.getTitle());
        holder.mImageThumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.mImageThumbnail.setImageURI(imageModel.getThumbnailUri());
    }

    @Override
    public int getItemCount() {
        return mImageModels != null ? mImageModels.size() : 0;
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_thumbnail)
        ImageView mImageThumbnail;
        @BindView(R.id.image_title)
        TextView mImageTitle;

        ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mImageThumbnail.setOnClickListener(v -> {
                String mediaUrl = mImageModels.get(getLayoutPosition()).getImageUri().toString();
                mViewClickSubject.onNext(mediaUrl);

            });
        }
    }
}
