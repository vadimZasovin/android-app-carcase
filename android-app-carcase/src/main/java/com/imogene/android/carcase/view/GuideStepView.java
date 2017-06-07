package com.imogene.android.carcase.view;

/**
 * Created by Admin on 06.06.2017.
 */

public interface GuideStepView {

    void setCurrentStep(int currentStep);

    int getCurrentStep();

    void setStepsCount(int stepsCount);

    int getStepsCount();

    boolean isAnimationEnabled();
}
