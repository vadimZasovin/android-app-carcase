package com.imogene.android.carcase.worker.task.compound;

import android.os.Bundle;

/**
 * Created by Admin on 17.05.2017.
 */

public abstract class BaseTask implements Task {

    private volatile boolean requiresCommit;
    private String errorMessage;
    private boolean cancelled;

    public BaseTask(boolean requiresCommit){
        this.requiresCommit = requiresCommit;
    }

    public BaseTask(){
        this(true);
    }

    @Override
    public boolean requiresCommit() {
        return requiresCommit;
    }

    @Override
    public void setRequiresCommit(boolean requiresCommit) {
        this.requiresCommit = requiresCommit;
    }

    @Override
    public void cancel(String errorMessage) {
        this.errorMessage = errorMessage;
        cancelled = true;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void updateExtras(Bundle extras) {}
}
