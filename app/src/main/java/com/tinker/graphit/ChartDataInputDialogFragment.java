package com.tinker.graphit;

import android.accounts.Account;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by sonny on 2016/07/10.
 */
public class ChartDataInputDialogFragment extends BaseDialogFragment<ChartDataInputDialogFragment.OnDialogFragmentClickListener> {

    private ArrayList<String> account_names_strings;
    private ArrayAdapter<String> adapter;
    private Activity reference_to_calling_activity;
    private String caller;
    private int position;

    // interface to handle the dialog click back to the Activity which called the dialog
    public interface OnDialogFragmentClickListener {
        public void showChartClicked(ChartDataInputDialogFragment dialog, View view, String caller, int position);

    }
    public void setReferenceToCallingActivity(Activity calling_activity){
        reference_to_calling_activity = calling_activity;
    }
    public void setListOfAccounts(ArrayList<String> list_for_spinner){
        account_names_strings = list_for_spinner;
    }
    public void setCallerUiName(String caller_input){
        caller = caller_input;
    }
    public void setPositionNumber(int position_input){
        position = position_input;
    }
    // Create an instance of the Dialog with the input
    public static ChartDataInputDialogFragment newInstance(String title, String message, ArrayList<Account> list_of_accounts, Activity calling_activity, String caller, int position) {
        ChartDataInputDialogFragment frag = new ChartDataInputDialogFragment();
        Bundle args = new Bundle();
        ArrayList<String> account_names_strings = new ArrayList<String>();
        args.putString("title", title);
        args.putString("msg", message);
        frag.setArguments(args);
        frag.setReferenceToCallingActivity(calling_activity);
        frag.setListOfAccounts(account_names_strings);
        frag.setCallerUiName(caller);
        frag.setPositionNumber(position);
        if(list_of_accounts.size() >0){

            for(Account one_account : list_of_accounts){
                account_names_strings.add(one_account.name);
            }
        }

        return frag;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.input_chart_data, container, false);
        final Button show_chart = (Button) view.findViewById(R.id.show_chart_button);
        final Spinner account_selector = (Spinner) view.findViewById(R.id.account_selector);

        show_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivityInstance().showChartClicked(ChartDataInputDialogFragment.this, view, ChartDataInputDialogFragment.this.caller, position);
            }
        });

        adapter = new ArrayAdapter<String>(reference_to_calling_activity, R.layout.custom_spinner_item, android.R.id.text1, account_names_strings);

        account_selector.setAdapter(adapter);
        return view;
    }

}

