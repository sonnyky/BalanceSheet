package com.tinker.graphit;

/**
 * Created by SY on 2016/07/05.
 */
import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class ChartListFragment extends ListFragment {
    public static final String TAG = "PresidentListFragment.TAG";
    public static ChartListFragment newInstance() {
        ChartListFragment frag = new ChartListFragment();
        return frag;
    }

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

    }

    public void CreateChartList(String[] list_of_chart_names){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.chart_list_fragment, list_of_chart_names);
        setListAdapter(adapter);
    }
}