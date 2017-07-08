package com.ilija.mygallery.ui.main.presenter;

import com.ilija.mygallery.core.model.ImageModel;
import com.ilija.mygallery.ui.main.model.MainModel;
import com.ilija.mygallery.ui.main.view.MainListener;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ikac on 7/8/17.
 */

public class MainPresenter implements IMainPresenter {

    private MainModel mMainModel;
    private Scheduler mScheduler;

    private MainListener mMainListener;
    private List<ImageModel> mImages;

    public MainPresenter(MainModel mMainModel, Scheduler mScheduler) {
        this.mMainModel = mMainModel;
        this.mScheduler = mScheduler;
    }

    public void init(MainListener mainListener) {
        this.mMainListener = mainListener;
    }

    @Override
    public void loadImages() {
        mMainModel.getImages()
                .subscribeOn(Schedulers.io())
                .observeOn(mScheduler)
                .subscribe(images -> {
                    this.mImages = images;
                    if (mMainListener != null) {
                        mMainListener.showImages(mImages);
                    }
                });
    }
}
