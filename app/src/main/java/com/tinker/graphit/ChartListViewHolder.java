package com.tinker.graphit;

import android.accounts.Account;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by SY on 2016/07/06.
 */
public class ChartListViewHolder extends RecyclerView.ViewHolder {
    protected TextView vName;
    protected ImageButton vEditChartInfoButton;
    protected ImageButton vDeleteChartButton;
    protected TargetChartInfo vChartInfo;

    public ChartListViewHolder(View v) {
        super(v);
        vName =  (TextView) v.findViewById(R.id.chart_fragment_name);
        vEditChartInfoButton = (ImageButton)  v.findViewById(R.id.edit_chart_info_button);
        vDeleteChartButton = (ImageButton)  v.findViewById(R.id.delete_chart_button);
    }
}
