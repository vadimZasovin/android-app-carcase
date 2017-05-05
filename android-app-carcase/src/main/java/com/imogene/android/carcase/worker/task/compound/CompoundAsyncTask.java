package com.imogene.android.carcase.worker.task.compound;

import android.os.Bundle;
import android.support.v4.util.SimpleArrayMap;

import com.imogene.android.carcase.worker.task.BaseAsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 05.05.2017.
 */

public class CompoundAsyncTask
        extends BaseAsyncTask<Bundle, String, SimpleArrayMap<String, Object>> {

    private final List<Task> tasks;

    private CompoundAsyncTask(Builder builder) {
        super(builder.taskId, builder.callbacks, builder.minDuration);
        tasks = builder.tasks;
    }

    @Override
    protected SimpleArrayMap<String, Object> onExecute(Bundle[] params) {
        return null;
    }

    public static final class Builder{

        private GenericCallbacks<String, SimpleArrayMap<String, Object>> callbacks;
        private int taskId;
        private long minDuration;
        private final List<Task> tasks;

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

        public CompoundAsyncTask build(){
            return new CompoundAsyncTask(this);
        }
    }
}
