package com.imogene.android.carcase.async.compound;

import android.os.Bundle;
import android.support.v4.util.SimpleArrayMap;
import android.text.TextUtils;

import com.imogene.android.carcase.async.NetworkAsyncTask;
import com.imogene.android.carcase.commons.exception.RequestException;
import com.imogene.android.carcase.commons.exception.RuntimeSQLException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 05.05.2017.
 */

public class CompoundAsyncTask
        extends NetworkAsyncTask<Bundle, String, SimpleArrayMap<String, Object>> {

    private final List<Task> tasks;
    private final boolean cancelAllOnCancelOne;
    private final String commitMessage;
    private final RequestExceptionHandler errorHandler;

    private CompoundAsyncTask(Builder builder) {
        super(builder.taskId, builder.callbacks, builder.minDuration);
        tasks = builder.tasks;
        cancelAllOnCancelOne = builder.cancelAllOnCancelOne;
        commitMessage = builder.commitMessage;
        errorHandler = builder.errorHandler;
    }

    @Override
    protected SimpleArrayMap<String, Object> onExecuteOnNetwork(Bundle[] params) throws RequestException {
        int tasksCount = tasks.size();
        if(tasksCount == 0){
            return null;
        }

        final Bundle extras;
        if(params != null && params.length > 0){
            extras = params[0];
        }else {
            extras = null;
        }

        SimpleArrayMap<String, Object> results = new SimpleArrayMap<>(tasksCount);

        for(Task task : tasks){
            String description = task.getDescription();
            publishProgress(description);
            String name = task.getName();
            Object result = task.execute(extras);
            if(task.isCancelled() && cancelAllOnCancelOne){
                String errorMessage = task.getErrorMessage();
                reportError(errorMessage);
                return results;
            }
            task.updateExtras(extras);
            if(result != null){
                results.put(name, result);
            }
        }

        try {
            if(!TextUtils.isEmpty(commitMessage)){
                publishProgress(commitMessage);
            }
            for(Task task : tasks){
                if(!task.isCancelled() && task.requiresCommit()){
                    task.commit();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeSQLException(e);
        }

        return results;
    }

    @Override
    protected String createErrorMessage(IOException cause) {
        if(errorHandler != null){
            return errorHandler.createErrorMessage(cause);
        }
        return null;
    }

    @Override
    protected String createErrorMessage(int errorCode) {
        if(errorHandler != null){
            return errorHandler.createErrorMessage(errorCode);
        }
        return null;
    }

    public static final class Builder{

        private GenericCallbacks<String, SimpleArrayMap<String, Object>> callbacks;
        private int taskId;
        private long minDuration;
        private final List<Task> tasks;
        private boolean cancelAllOnCancelOne;
        private String commitMessage;
        private RequestExceptionHandler errorHandler;

        public Builder(){
            tasks = new ArrayList<>();
        }

        public Builder callbacks(GenericCallbacks<String, SimpleArrayMap<String, Object>> callbacks){
            this.callbacks = callbacks;
            return this;
        }

        public Builder taskId(int taskId){
            this.taskId = taskId;
            return this;
        }

        public Builder minDuration(long minDuration){
            this.minDuration = minDuration;
            return this;
        }

        public Builder addTask(Task task){
            tasks.add(task);
            return this;
        }

        public Builder cancelAllOnCancelOne(){
            cancelAllOnCancelOne = true;
            return this;
        }

        public Builder commitMessage(String commitMessage){
            this.commitMessage = commitMessage;
            return this;
        }

        public Builder errorHandler(RequestExceptionHandler handler){
            errorHandler = handler;
            return this;
        }

        public CompoundAsyncTask build(){
            return new CompoundAsyncTask(this);
        }
    }

    public interface RequestExceptionHandler{

        String createErrorMessage(IOException cause);

        String createErrorMessage(int errorCode);
    }
}
