package com.ilija.mygallery.ui.main.view;

import com.ilija.mygallery.core.model.ImageModel;

import java.util.List;

/**
 * Created by ikac on 7/8/17.
 */

public interface MainListener {

    void showImages(List<ImageModel> images);
}
