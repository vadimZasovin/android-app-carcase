package com.imogene.android.carcase.exception;

import java.io.IOException;

/**
 * Created by Admin on 13.04.2017.
 */

public class RequestException extends IOException {

    private final int errorCode;

    public RequestException(Throwable cause) {
        super(cause);
        if(!(cause instanceof IOException)){
            throw new IllegalArgumentException("The cause must be an instance of IOException");
        }
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