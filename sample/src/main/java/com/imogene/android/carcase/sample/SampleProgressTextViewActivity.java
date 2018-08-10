package com.imogene.android.carcase.sample;

import android.os.Bundle;
import android.view.View;

import com.imogene.android.carcase.view.progress.ProgressTextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Vadim Zasovin on 26.12.17.
 */

public class SampleProgressTextViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_progress_text_view);

        final ProgressTextView progressTextView = findViewById(R.id.progress_view);
        progressTextView.collapse();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressTextView.isStarted()){
                    progressTextView.stop();
                } else {
                    progressTextView.expand();
                    progressTextView.start();
                }
            }
        });
    }
}
