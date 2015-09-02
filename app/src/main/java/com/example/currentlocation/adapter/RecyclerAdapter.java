package com.example.currentlocation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.currentlocation.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.currentlocation.view.ProgressBarActivity.TIME;
import static com.example.currentlocation.view.ProgressBarActivity.WATER;

/**
 * Created by Serhiy Petrosyuk on 30.07.15.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<Map<Integer, String>> mCupList;

    public RecyclerAdapter() {
        mCupList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.water.setText(mCupList.get(position).get(WATER));
        viewHolder.time.setText(mCupList.get(position).get(TIME));
    }

    @Override
    public int getItemCount() {
        return mCupList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView water;
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            water = (TextView) itemView.findViewById(R.id.tv_water);
            time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }

    public void addItem(Map<Integer, String> item) {
        mCupList.add(0, item);
        notifyItemInserted(0);
    }

}
