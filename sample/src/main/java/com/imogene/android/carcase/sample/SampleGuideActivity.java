package com.imogene.android.carcase.sample;

import android.content.Intent;

import com.imogene.android.carcase.controller.BaseActivity;
import com.imogene.android.carcase.controller.guide.BaseGuideFragment;


/**
 * Created by Admin on 06.06.2017.
 */

public class SampleGuideActivity extends BaseActivity {

    @Override
    protected BaseGuideFragment createFragment(Intent intent) {
        return SampleGuideFragment.newInstance();
    }
}
