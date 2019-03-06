package com.example.aldrian.musicin2firebase.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aldrian.musicin2firebase.R;
import com.example.aldrian.musicin2firebase.model.Job;

import java.util.List;

/**
 * Created by Tommy on 12/12/17.
 */

public class CurrentRecyclerAdapter extends RecyclerView.Adapter<CurrentRecyclerAdapter.JobViewHolder> {

    public interface OnItemClickListener{
        void onItemClick(Job job, int position);
    }
    private List<Job> listJobs;
    private OnItemClickListener listener;
    public CurrentRecyclerAdapter(List<Job> listJobs, OnItemClickListener listener){
        this.listJobs = listJobs;
        this.listener = listener;
    }


    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_current, parent, false);
        return new JobViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CurrentRecyclerAdapter.JobViewHolder holder, int position) {
        holder.bind(listJobs.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return listJobs.size();
    }

    public class JobViewHolder  extends RecyclerView.ViewHolder {

        public TextView tv_job_address;
        public TextView tv_job_time;
        public TextView tv_job_date;

        public JobViewHolder(View itemView) {
            super(itemView);
            tv_job_address = itemView.findViewById(R.id.tv_job_address);
            tv_job_time = itemView.findViewById(R.id.tv_job_time);
            tv_job_date = itemView.findViewById(R.id.tv_job_date);
        }

        public void bind(final Job job,final CurrentRecyclerAdapter.OnItemClickListener listener){
            tv_job_address.setText(job.getAddress());
            tv_job_time.setText(job.getTime());
            tv_job_date.setText(job.getDate());

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    listener.onItemClick(job, getLayoutPosition());
                }
            });
        }
    }



}
