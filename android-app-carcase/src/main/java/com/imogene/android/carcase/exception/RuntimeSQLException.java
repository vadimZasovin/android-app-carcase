package com.imogene.android.carcase.exception;

import java.sql.SQLException;

/**
 * Created by Admin on 13.04.2017.
 */

public class RuntimeSQLException extends RuntimeException {

    public RuntimeSQLException(Throwable cause) {
        super(cause);
        if(!(cause instanceof SQLException)){
            throw new IllegalArgumentException("The cause must be an instance of SQLException");
        }
    }
}