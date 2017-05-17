package com.imogene.android.carcase.worker.task.compound;

import android.os.Bundle;

import com.imogene.android.carcase.exception.RequestException;

import java.sql.SQLException;

/**
 * Created by Admin on 05.05.2017.
 */

public interface Task {

    String getName();

    String getDescription();

    Object execute(Bundle extras) throws RequestException;

    void cancel(String errorMessage);

    boolean isCancelled();

    String getErrorMessage();

    void updateExtras(Bundle extras);

    boolean requiresCommit();

    void setRequiresCommit(boolean requiresCommit);

    void commit() throws SQLException;
}
