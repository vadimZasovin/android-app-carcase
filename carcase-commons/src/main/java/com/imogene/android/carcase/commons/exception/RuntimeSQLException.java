package com.imogene.android.carcase.commons.exception;

import java.sql.SQLException;

/**
 * Created by Admin on 13.04.2017.
 */

public class RuntimeSQLException extends RuntimeException {

    public RuntimeSQLException(SQLException cause) {
        super(cause);
    }
}