package com.imogene.android.carcase.controller;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by Admin on 06.06.2017.
 */

public abstract class BaseGuideActivity extends BaseActivity {

    @Override
    protected final Fragment createFragment(Intent intent) {
        return createGuideFragment();
    }

    protected abstract BaseGuideFragment createGuideFragment();
}
