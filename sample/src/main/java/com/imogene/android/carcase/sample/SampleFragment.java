package com.imogene.android.carcase.sample;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imogene.android.carcase.controller.BaseFragment;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Admin on 14.04.2017.
 */

public class SampleFragment extends BaseFragment {

    public static SampleFragment newInstance() {
        return new SampleFragment();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_sample;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        Context context = getActivity();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        SampleAdapter adapter = new SampleAdapter();
        recyclerView.setAdapter(adapter);
    }

    private class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.SampleViewHolder>{

        @Override
        public SampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_layout, parent, false);
            return new SampleViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(SampleViewHolder holder, int position) {
            TextView textView = (TextView) holder.itemView;
            String text = "Item" + (position + 1);
            textView.setText(text);
        }

        @Override
        public int getItemCount() {
            return 1000;
        }

        class SampleViewHolder extends RecyclerView.ViewHolder{

            SampleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}