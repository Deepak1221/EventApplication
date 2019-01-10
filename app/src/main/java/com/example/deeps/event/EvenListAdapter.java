package com.example.deeps.event;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class EvenListAdapter extends RecyclerView.Adapter<EvenListAdapter.MyViewHolder> {

    private List<EventModel> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, number, address,date,time;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            number = (TextView) view.findViewById(R.id.number);
            address = (TextView) view.findViewById(R.id.address);
            date = (TextView) view.findViewById(R.id.date);
            time = (TextView) view.findViewById(R.id.time);
        }
    }


    public EvenListAdapter(List<EventModel> moviesList, EventList eventList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        EventModel item = moviesList.get(position);
        holder.name.setText(item.getName());
        holder.number.setText(item.getMobile_no());
        holder.address.setText(item.getAddress());
        holder.date.setText(item.getDate()+":"+item.getTime());
//        holder.time.setText(item.getTime());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}