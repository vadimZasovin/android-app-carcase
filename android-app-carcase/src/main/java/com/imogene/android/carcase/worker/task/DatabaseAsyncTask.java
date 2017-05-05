package com.imogene.android.carcase.worker.task;

import com.imogene.android.carcase.exception.RuntimeSQLException;

import java.sql.SQLException;

/**
 * Created by Admin on 14.04.2017.
 */

public abstract class DatabaseAsyncTask<Param, Progress, Result>
        extends BaseAsyncTask<Param, Progress, Result>{

    public DatabaseAsyncTask(int taskId, Callbacks callbacks, long minDuration) {
        super(taskId, callbacks, minDuration);
    }

    public DatabaseAsyncTask(int taskId, Callbacks callbacks) {
        super(taskId, callbacks);
    }

    public DatabaseAsyncTask(int taskId) {
        super(taskId);
    }

    public DatabaseAsyncTask(int taskId, GenericCallbacks<Progress, Result> callbacks, long minDuration) {
        super(taskId, callbacks, minDuration);
    }

    public DatabaseAsyncTask(int taskId, GenericCallbacks<Progress, Result> callbacks) {
        super(taskId, callbacks);
    }

    @Override
    protected final Result onExecute(Param[] params) {
        try {
            return onExecuteOnDatabase(params);
        }catch (SQLException e){
            throw new RuntimeSQLException(e);
        }
    }

    protected abstract Result onExecuteOnDatabase(Param[] params) throws SQLException;
}