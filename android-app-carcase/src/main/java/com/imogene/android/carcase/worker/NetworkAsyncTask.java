package com.imogene.android.carcase.worker;

import com.imogene.android.carcase.exception.RequestException;

import java.io.IOException;

/**
 * Created by Admin on 14.04.2017.
 */

public abstract class NetworkAsyncTask<Param, Progress, Result>
        extends BaseAsyncTask<Param, Progress, Result> {

    public NetworkAsyncTask(int taskId, Callbacks callbacks, long minDuration) {
        super(taskId, callbacks, minDuration);
    }

    public NetworkAsyncTask(int taskId, Callbacks callbacks) {
        super(taskId, callbacks);
    }

    public NetworkAsyncTask(int taskId) {
        super(taskId);
    }

    public NetworkAsyncTask(int taskId, GenericCallbacks<Progress, Result> callbacks, long minDuration) {
        super(taskId, callbacks, minDuration);
    }

    public NetworkAsyncTask(int taskId, GenericCallbacks<Progress, Result> callbacks) {
        super(taskId, callbacks);
    }

    @Override
    protected final Result onExecute(Param[] params) {
        try {
            return onExecuteOnNetwork(params);
        }catch (RequestException e){
            return handleRequestException(e);
        }
    }

    protected abstract Result onExecuteOnNetwork(Param[] params) throws RequestException;

    protected Result handleRequestException(RequestException e){
        String errorMessage;
        Throwable cause = e.getCause();
        if(cause != null){
            IOException ioException = (IOException) cause;
            errorMessage = createErrorMessage(ioException);
        }else {
            int errorCode = e.getErrorCode();
            errorMessage = createErrorMessage(errorCode);
        }
        reportError(errorMessage);
        return null;
    }

    protected abstract String createErrorMessage(IOException cause);

    protected abstract String createErrorMessage(int errorCode);
}