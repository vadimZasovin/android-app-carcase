package com.imogene.android.carcase.worker.loader;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import com.imogene.android.carcase.exception.RequestException;
import com.imogene.android.carcase.exception.RuntimeSQLException;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Admin on 13.04.2017.
 */

public abstract class BaseLoader<D> extends AsyncTaskLoader<D> {

    public static final int SOURCE_DATABASE = 1;
    public static final int SOURCE_SERVER = 2;
    public static final int SOURCE_SERVER_DATABASE = 3;

    public static final String EXTRA_SOURCE = "com.imogene.android.carcase.EXTRA_SOURCE";

    private final int source;
    private final long minDuration;
    private volatile boolean minDurationEnabled;
    private String errorMessage;
    private D data;

    public BaseLoader(Context context, int source, long minDuration) {
        super(context);
        this.source = source;
        this.minDuration = minDuration;
        minDurationEnabled = minDuration > 0;
    }

    public BaseLoader(Context context, int source){
        this(context, source, 0);
    }

    public final void setMinDurationEnabled(boolean minDurationEnabled){
        this.minDurationEnabled = minDurationEnabled;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(data == null || takeContentChanged()){
            forceLoad();
        }else {
            deliverResult(data);
        }
    }

    @Override
    public final D loadInBackground() {
        long startTime = 0;
        final long finishTime;
        final boolean minDurationEnabled = this.minDurationEnabled;
        if(minDurationEnabled){
            startTime = System.currentTimeMillis();
        }

        D data;

        try {
            switch (source){
                case SOURCE_SERVER:
                    data = loadFromServer();
                    break;
                case SOURCE_DATABASE:
                    data = loadFromDatabase();
                    break;
                case SOURCE_SERVER_DATABASE:
                    data = loadFromServer();
                    saveData(data);
                    data = loadFromDatabase();
                    break;
                default:
                    throw new IllegalStateException("Unsupported source: " + source);
            }
        }catch (SQLException e){
            throw new RuntimeSQLException(e);
        }catch (RequestException e){
            return handleRequestException(e);
        }

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

        return data;
    }

    protected D loadFromServer() throws RequestException{
        throw new UnsupportedOperationException(
                "This method must be overridden without calling through super class implementation");
    }

    protected void saveData(D data) throws SQLException {
        throw new UnsupportedOperationException(
                "This method must be overridden without calling through super class implementation");
    }

    protected D loadFromDatabase() throws SQLException{
        throw new UnsupportedOperationException(
                "This method must be overridden without calling through super class implementation");
    }

    protected D handleRequestException(RequestException e){
        String errorMessage;
        Throwable cause = e.getCause();
        if(cause != null){
            IOException ioException = (IOException) cause;
            errorMessage = createErrorMessage(ioException);
        }else {
            int errorCode = e.getErrorCode();
            errorMessage = createErrorMessage(errorCode);
        }
        if(!TextUtils.isEmpty(errorMessage)){
            reportError(errorMessage);
        }
        return null;
    }

    protected String createErrorMessage(IOException cause){
        return null;
    }

    protected String createErrorMessage(int errorCode){
        return null;
    }

    protected final void reportError(@StringRes int messageRes){
        Context context = getContext();
        String errorMessage = context.getString(messageRes);
        reportError(errorMessage);
    }

    protected final void reportError(String errorMessage){
        this.errorMessage = errorMessage;
    }

    @Override
    public void deliverResult(D data) {
        super.deliverResult(data);
        if(shouldStoreData()){
            this.data = data;
        }
    }

    protected boolean shouldStoreData(){
        return true;
    }

    public final int getSource(){
        return source;
    }

    public final String getErrorMessage(){
        return errorMessage;
    }
}