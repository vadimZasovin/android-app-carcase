package com.imogene.android.carcase.sample;

import com.imogene.android.carcase.controller.BaseGuideActivity;
import com.imogene.android.carcase.controller.BaseGuideFragment;

/**
 * Created by Admin on 06.06.2017.
 */

public class SampleGuideActivity extends BaseGuideActivity {

    @Override
    protected BaseGuideFragment createGuideFragment() {
        return SampleGuideFragment.newInstance();
    }
}
