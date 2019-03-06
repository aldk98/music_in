package com.example.aldrian.musicin2firebase.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aldrian.musicin2firebase.R;
import com.example.aldrian.musicin2firebase.model.Job;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tommy on 8/12/17.
 */

public class MusicianFindJobRecyclerAdapter extends RecyclerView.Adapter<MusicianFindJobRecyclerAdapter.FindJobViewHolder> {

    public interface OnItemClickListener{
        void onItemClick(Job job, int position);
    }
    private List<Job> jobList;

    private OnItemClickListener listener;
    public MusicianFindJobRecyclerAdapter(List<Job> jobList, OnItemClickListener listener){
        this.jobList = jobList;
        this.listener = listener;
    }
    @Override
    public FindJobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_musician_findjob, parent, false);
        return new FindJobViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FindJobViewHolder holder, int position) {
        holder.bind(jobList.get(position),listener);
    }

    @Override
    public int getItemCount() {
        if(jobList!=null){
            return jobList.size();
        }
        return 0;
    }

    public class FindJobViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_businessName;
        public TextView tv_job_time;
        public TextView tv_job_address;

        public FindJobViewHolder(View view) {
            super(view);
            tv_businessName =  view.findViewById(R.id.tv_businessName);
            tv_job_time = view.findViewById(R.id.tv_job_time);
            tv_job_address = view.findViewById(R.id.tv_job_address);
        }
        public void bind(final Job job,final OnItemClickListener listener){
            tv_businessName.setText(job.getBusinessName());
            tv_job_time.setText(job.getTime());
            tv_job_address.setText(job.getAddress());
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    listener.onItemClick(job, getLayoutPosition());
                }
            });
        }

    }
}
