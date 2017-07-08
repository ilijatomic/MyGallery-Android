package com.ilija.mygallery.ui.main.di;

import android.content.Context;

import com.ilija.mygallery.ui.main.model.MainModel;
import com.ilija.mygallery.ui.main.presenter.MainPresenter;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by ikac on 7/8/17.
 */
@Module
public class MainModule {

    private final Context mContext;

    public MainModule(Context mContext) {
        this.mContext = mContext;
    }

    @Provides
    Context provideContext() {
        return mContext;
    }

    @Provides
    MainPresenter provideMainPresenter(MainModel mainModel, Scheduler scheduler) {
        return new MainPresenter(mainModel, scheduler);
    }

    @Provides
    MainModel provideMainModel(Context context) {
        return new MainModel(context);
    }

    @Provides
    Scheduler provideScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
