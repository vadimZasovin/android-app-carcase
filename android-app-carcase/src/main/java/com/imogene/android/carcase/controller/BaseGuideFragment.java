package com.imogene.android.carcase.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.imogene.android.carcase.R;
import com.imogene.android.carcase.view.GuideStepView;

/**
 * Created by Admin on 06.06.2017.
 */

public abstract class BaseGuideFragment<T extends Fragment> extends BaseFragment
        implements ViewPager.OnPageChangeListener{

    protected ViewPager viewPager;
    @Nullable
    protected GuideStepView guideStepView;

    private int stepsCount;
    private GuideAdapter adapter;
    private int currentStep;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stepsCount = getGuideStepsCount();
    }

    protected abstract int getGuideStepsCount();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        if(viewPager == null){
            throw new IllegalStateException("The specified layout does not contains a ViewPager");
        }
        guideStepView = (GuideStepView) view.findViewById(R.id.guide_step_view);

        FragmentManager fragmentManager = getChildFragmentManager();
        adapter = new GuideAdapter(fragmentManager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);

        if(guideStepView != null){
            guideStepView.setStepsCount(stepsCount);
        }
    }

    private class GuideAdapter extends FragmentPagerAdapter{

        private final SparseArray<T> fragments;

        GuideAdapter(FragmentManager fm) {
            super(fm);
            fragments = new SparseArray<>(getCount());
        }

        @Override
        public int getCount() {
            return stepsCount;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object item = super.instantiateItem(container, position);
            @SuppressWarnings("unchecked")
            T fragment = (T) item;
            fragments.put(position, fragment);
            return item;
        }

        @Override
        public Fragment getItem(int position) {
            return createGuideStepFragment(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            fragments.delete(position);
        }
    }

    protected abstract T createGuideStepFragment(int position);

    protected final T getGuideStepFragment(int position){
        if(adapter != null){
            return adapter.fragments.get(position);
        }
        return null;
    }

    @Override
    public final void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        T first = getGuideStepFragment(position);
        T second = getGuideStepFragment(position + 1);
        onPageScrolled(first, second, positionOffset, positionOffsetPixels);
    }

    protected void onPageScrolled(T first, T second, float positionOffset, int positionOffsetPixels){}

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    public final void onPageSelected(int position) {
        int lastPosition = stepsCount - 1;
        if(position == lastPosition){
            onLastStepSelected();
        }else if(currentStep == lastPosition){
            onReturnFromLastStep();
        }
        currentStep = position;

        if(guideStepView != null){
            guideStepView.setCurrentStep(position);
        }
    }

    protected void onLastStepSelected(){}

    protected void onReturnFromLastStep(){}
}
