package com.imogene.android.carcase.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.imogene.android.carcase.controller.BaseFragment;

/**
 * Created by Admin on 06.06.2017.
 */

public class SampleGuideStepFragment extends BaseFragment {

    private static final String EXTRA_POSITION =
            "com.imogene.android.carcase.sample.SampleGuideStepFragment.EXTRA_POSITION";

    private int position;

    public static SampleGuideStepFragment newInstance(int position) {
        SampleGuideStepFragment fragment = new SampleGuideStepFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_sample_guide_step;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        position = args.getInt(EXTRA_POSITION);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(String.valueOf(position + 1));
    }
}
