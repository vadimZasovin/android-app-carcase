package com.imogene.android.carcase.worker.task;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.StringRes;

import com.imogene.android.carcase.controller.BaseApplication;

import java.lang.ref.WeakReference;

/**
 * Created by Admin on 14.04.2017.
 */

public abstract class BaseAsyncTask<Param, Progress, Result>
        extends AsyncTask<Param, Progress, Result> {

    private final int taskId;
    private WeakReference<Callbacks> callbacksRef;
    private WeakReference<GenericCallbacks<Progress, Result>> genericCallbacksRef;
    private final long minDuration;
    private volatile boolean minDurationEnabled;
    private String errorMessage;

    public BaseAsyncTask(int taskId, Callbacks callbacks, long minDuration){
        this.taskId = taskId;
        if(callbacks != null){
            callbacksRef = new WeakReference<>(callbacks);
        }
        this.minDuration = minDuration;
        minDurationEnabled = minDuration > 0;
    }

    public BaseAsyncTask(int taskId, Callbacks callbacks){
        this(taskId, callbacks, 0);
    }

    public BaseAsyncTask(int taskId){
        this.taskId = taskId;
        minDuration = 0;
        minDurationEnabled = false;
    }

    public BaseAsyncTask(int taskId, GenericCallbacks<Progress, Result> callbacks, long minDuration){
        this.taskId = taskId;
        if(callbacks != null){
            genericCallbacksRef = new WeakReference<>(callbacks);
        }
        this.minDuration = minDuration;
        minDurationEnabled = minDuration > 0;
    }

    public BaseAsyncTask(int taskId, GenericCallbacks<Progress, Result> callbacks){
        this(taskId, callbacks, 0);
    }

    public final void setMinDurationEnabled(boolean minDurationEnabled){
        this.minDurationEnabled = minDurationEnabled;
    }

    @Override
    protected final void onPreExecute() {
        super.onPreExecute();
        Callbacks callbacks = getCallbacks();
        if(callbacks != null){
            callbacks.onTaskStarted(taskId);
        }else {
            GenericCallbacks<Progress, Result> genericCallbacks = getGenericCallbacks();
            if(genericCallbacks != null){
                genericCallbacks.onTaskStarted(taskId);
            }
        }
    }

    private Callbacks getCallbacks(){
        if(callbacksRef != null){
            return callbacksRef.get();
        }
        return null;
    }

    private GenericCallbacks<Progress, Result> getGenericCallbacks(){
        if(genericCallbacksRef != null){
            return genericCallbacksRef.get();
        }
        return null;
    }

    @SafeVarargs
    @Override
    protected final Result doInBackground(Param... params) {
        long startTime = 0;
        final long finishTime;
        final boolean minDurationEnabled = this.minDurationEnabled;
        if(minDurationEnabled){
            startTime = System.currentTimeMillis();
        }

        Result result = onExecute(params);

        if(minDurationEnabled){
            finishTime = System.currentTimeMillis();
            long duration = finishTime - startTime;
            long remainingDuration = minDuration - duration;
            if(remainingDuration > 0){
                try {
                    Thread.sleep(remainingDuration);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    protected abstract Result onExecute(Param[] params);

    protected final void reportError(String errorMessage){
        this.errorMessage = errorMessage;
        cancel(false);
    }

    protected final void reportError(@StringRes int errorMessageRes){
        Context context = getContext();
        String errorMessage = context.getString(errorMessageRes);
        reportError(errorMessage);
    }

    protected final Context getContext(){
        return BaseApplication.getAppContext();
    }

    @SafeVarargs
    @Override
    protected final void onProgressUpdate(Progress... values) {
        super.onProgressUpdate(values);
        Callbacks callbacks = getCallbacks();
        if(callbacks != null){
            callbacks.onTaskProgress(taskId, (Object[]) values);
        }else {
            GenericCallbacks<Progress, Result> genericCallbacks = getGenericCallbacks();
            if(genericCallbacks != null){
                genericCallbacks.onTaskProgress(taskId, values);
            }
        }
    }

    @Override
    protected final void onPostExecute(Result result) {
        super.onPostExecute(result);
        Callbacks callbacks = getCallbacks();
        if(callbacks != null){
            callbacks.onTaskFinished(taskId, result);
        }else {
            GenericCallbacks<Progress, Result> genericCallbacks = getGenericCallbacks();
            if(genericCallbacks != null){
                genericCallbacks.onTaskFinished(taskId, result);
            }
        }
    }

    @Override
    protected final void onCancelled(Result result) {
        super.onCancelled(result);
        Callbacks callbacks = getCallbacks();
        if(callbacks != null){
            callbacks.onTaskError(taskId, errorMessage);
        }else {
            GenericCallbacks<Progress, Result> genericCallbacks = getGenericCallbacks();
            if(genericCallbacks != null){
                genericCallbacks.onTaskError(taskId, errorMessage);
            }
        }
    }

    public interface Callbacks{

        void onTaskStarted(int taskId);

        void onTaskProgress(int taskId, Object... progress);

        void onTaskFinished(int taskId, Object result);

        void onTaskError(int taskId, String errorMessage);
    }

    public static abstract class CallbacksAdapter implements Callbacks{

        @Override
        public void onTaskStarted(int taskId) {}

        @Override
        public void onTaskProgress(int taskId, Object... progress) {}

        @Override
        public void onTaskFinished(int taskId, Object result) {}

        @Override
        public void onTaskError(int taskId, String errorMessage) {}
    }

    public interface GenericCallbacks<Progress, Result>{

        void onTaskStarted(int taskId);

        void onTaskProgress(int taskId, Progress[] progress);

        void onTaskFinished(int taskId, Result result);

        void onTaskError(int taskId, String errorMessage);
    }

    public static abstract class GenericCallbacksAdapter<Progress, Result>
            implements GenericCallbacks<Progress, Result>{

        @Override
        public void onTaskStarted(int taskId) {}

        @Override
        public void onTaskProgress(int taskId, Progress[] progress) {}

        @Override
        public void onTaskFinished(int taskId, Result result) {}

        @Override
        public void onTaskError(int taskId, String errorMessage) {}
    }
}