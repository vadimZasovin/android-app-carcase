package com.imogene.android.carcase.commons.exception;

import java.io.IOException;

/**
 * Created by Admin on 13.04.2017.
 */

public class RequestException extends IOException {

    private final int errorCode;

    public RequestException(IOException cause) {
        super(cause);
        errorCode = 0;
    }

    public RequestException(int errorCode){
        this.errorCode = errorCode;
    }

    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }

    public int getErrorCode() {
        return errorCode;
    }
}