package com.ilija.mygallery.ui.main.model;

import com.ilija.mygallery.core.model.ImageModel;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ikac on 7/8/17.
 */

public interface IMainModel {

    Observable<List<ImageModel>> getImages();
}
