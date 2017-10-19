package com.imogene.android.carcase.sample;

import com.imogene.android.carcase.controller.guide.BaseGuideFragment;

/**
 * Created by Admin on 06.06.2017.
 */

public class SampleGuideFragment extends BaseGuideFragment<SampleGuideStepFragment> {

    public static SampleGuideFragment newInstance() {
        return new SampleGuideFragment();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_sample_guide;
    }

    @Override
    protected int getGuideStepsCount() {
        return 5;
    }

    @Override
    protected SampleGuideStepFragment createGuideStepFragment(int position) {
        return SampleGuideStepFragment.newInstance(position);
    }
}
