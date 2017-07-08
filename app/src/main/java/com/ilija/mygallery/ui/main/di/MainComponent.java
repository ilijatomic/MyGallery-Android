package com.ilija.mygallery.ui.main.di;

import android.content.Context;

import com.ilija.mygallery.ui.main.view.MainActivity;

import dagger.Component;

/**
 * Created by ikac on 7/8/17.
 */

@Component(modules = MainModule.class)
public interface MainComponent {

    void inject(MainActivity mainActivity);
}
