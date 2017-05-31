package com.imogene.android.carcase.worker.loader;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Admin on 31.05.2017.
 */

public class FilesLoader extends BaseLoader<FilesLoader.Result> {

    public static final String EXTRA_URI = "com.imogene.android.carcase.EXTRA_URI";

    private static final int SOURCE_CUSTOM = 4;

    private final Uri uri;
    private final File file;
    private final File dir;
    private final long maxSize;

    private FilesLoader(Builder builder) {
        super(builder.context, SOURCE_CUSTOM, builder.minDuration);
        uri = builder.uri;
        file = builder.file;
        dir = builder.dir;
        maxSize = builder.maxSize;
    }

    @Override
    protected Result loadFromCustomSource(int source) {
        String scheme = uri.getScheme();
        if(ContentResolver.SCHEME_FILE.equals(scheme)){
            return handleFileUri();
        }else if(ContentResolver.SCHEME_CONTENT.equals(scheme)){
            return handleContentUri();
        }else {
            throw new IllegalStateException("Unsupported URI scheme: " + scheme);
        }
    }

    private Result handleFileUri(){
        String path = uri.getPath();
        File file = new File(path);

        long maxSize = this.maxSize;
        if(maxSize == 0){
            maxSize = Long.MAX_VALUE;
        }

        long size = file.length();
        if(size > maxSize){
            return error(Result.ERROR_TOO_LARGE_FILE);
        }
        return success(file);
    }

    private Result handleContentUri(){
        Context context = getContext();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, null, null, null, null, null);

        try {
            if(cursor != null && cursor.moveToFirst()){
                final long size;
                final String name;

                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                size = !cursor.isNull(sizeIndex) ?
                        cursor.getLong(sizeIndex) :
                        0;

                long maxSize = this.maxSize;
                if(maxSize == 0){
                    maxSize = Long.MAX_VALUE;
                }

                if(size > maxSize){
                    return error(Result.ERROR_TOO_LARGE_FILE);
                }

                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                name = cursor.getString(nameIndex);

                InputStream is = null;
                OutputStream os = null;

                try {
                    try {
                        is = resolver.openInputStream(uri);
                        if(is == null){
                            throw new FileNotFoundException("The InputStream opened by ContentResolver is a null reference");
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return error(Result.ERROR_SOURCE_NOT_FOUND);
                    }

                    File dir = this.dir;
                    File file = this.file;

                    if(file == null){
                        if(dir == null){
                            dir = context.getCacheDir();
                        }else {
                            if(!dir.mkdirs() && !dir.isDirectory()){
                                return error(Result.ERROR_DESTINATION_NOT_FOUND);
                            }
                        }

                        file = new File(dir, name);
                    }

                    try {
                        os = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return error(Result.ERROR_DESTINATION_NOT_FOUND);
                    }

                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = is.read(buffer)) != -1){
                        os.write(buffer, 0, read);
                    }
                    os.flush();

                    return success(file);
                }catch (IOException e){
                    e.printStackTrace();
                    return error(Result.ERROR_COPYING_ERROR);
                }finally {
                    if(is != null){
                        try {
                            is.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    if(os != null){
                        try {
                            os.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }else {
                return error(Result.ERROR_SOURCE_NOT_FOUND);
            }
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }

    private Result error(int errorCode){
        reportError(String.valueOf(errorCode));
        return new Result(errorCode);
    }

    private Result success(File file){
        return new Result(file);
    }

    public static final class Builder{

        private final Context context;
        private final Uri uri;
        private long minDuration;
        private File file;
        private File dir;
        private long maxSize;

        public Builder(Context context, Uri uri){
            this.context = context;
            this.uri = uri;
        }

        public Builder minDuration(long minDuration){
            this.minDuration = minDuration;
            return this;
        }

        public Builder file(File file){
            this.file = file;
            return this;
        }

        public Builder dir(File dir){
            this.dir = dir;
            return this;
        }

        public Builder maxSize(long maxSize){
            this.maxSize = maxSize;
            return this;
        }

        public FilesLoader build(){
            return new FilesLoader(this);
        }
    }

    public static final class Result{

        public static final int ERROR_TOO_LARGE_FILE = 1;
        public static final int ERROR_SOURCE_NOT_FOUND = 2;
        public static final int ERROR_DESTINATION_NOT_FOUND = 3;
        public static final int ERROR_COPYING_ERROR = 4;

        private int errorCode;
        private File file;

        private Result(File file){
            this.file = file;
        }

        private Result(int errorCode){
            this.errorCode = errorCode;
        }

        public boolean isSuccessful(){
            return errorCode == 0 && file != null;
        }

        public int getErrorCode(){
            return errorCode;
        }

        public File getFile(){
            return file;
        }
    }
}
