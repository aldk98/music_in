package com.example.aldrian.musicin2firebase.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aldrian.musicin2firebase.R;
import com.example.aldrian.musicin2firebase.model.Job;

import java.util.List;

/**
 * Created by Tommy on 12/12/17.
 */

public class InboxRecyclerAdapter  extends RecyclerView.Adapter<InboxRecyclerAdapter.ViewHolder> {

    public interface OnItemClickListener{
        void onItemClick(Job job, int position);
    }
    private List<Job> listJobs;
    private OnItemClickListener listener;
    public InboxRecyclerAdapter(List<Job> listJobs, OnItemClickListener listener){
        this.listJobs = listJobs;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inbox, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(listJobs.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return listJobs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
        public void bind(final Job job,final InboxRecyclerAdapter.OnItemClickListener listener){

                itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    listener.onItemClick(job, getLayoutPosition());
                }
            });
        }
    }
}
