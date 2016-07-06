package com.tinker.graphit;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SY on 2016/07/06.
 */
public class ChartListRecyclerAdapter extends RecyclerView.Adapter<ChartListRecyclerAdapter.DataObjectHolder>  {

    public ArrayList<TargetChartInfo> listOfChartsToShow;
    private static MyClickListener myClickListener;

    @Override
    public int getItemCount() {
        return listOfChartsToShow.size();
    }

    public ChartListRecyclerAdapter(ArrayList<TargetChartInfo> myDataset) {
        listOfChartsToShow = myDataset;

        Log.v("ChartListRecycler", listOfChartsToShow.get(0).getTableName());
    }

    public void addItem(TargetChartInfo dataObj, int index) {
        listOfChartsToShow.add(index, dataObj);
        this.notifyItemInserted(index);
        Log.v("after adding", listOfChartsToShow.get(index).getTableName());
    }

    public void deleteItem(int index) {
        listOfChartsToShow.remove(index);
        this.notifyItemRemoved(index);
    }


    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.vName.setText(listOfChartsToShow.get(position).getTableName());
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView vName;
        protected ImageButton vEditChartInfoButton;
        protected ImageButton vDeleteChartButton;
        public DataObjectHolder(View itemView) {
            super(itemView);
            vName =  (TextView) itemView.findViewById(R.id.chart_fragment_name);
            vEditChartInfoButton = (ImageButton)  itemView.findViewById(R.id.edit_chart_info_button);
            vDeleteChartButton = (ImageButton)  itemView.findViewById(R.id.delete_chart_button);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }
    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public ChartListRecyclerAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_list_fragment, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

}
