package com.tinker.graphit;

import android.Manifest;
import android.accounts.Account;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;

/**
 * Created by sonny.kurniawan on 2015/10/11.
 */
public class MainActivity extends Activity implements ChartDataInputDialogFragment.OnDialogFragmentClickListener{
    private AccountSelector account_selector_instance;
    private TextView selected_account_text;
    private EditText data_row_number, data_column_number, axis_column_number;
    private String tableName, sheetName, data_row_number_string, data_column_number_string, axis_column_number_string;
    private Spinner account_selector_spinner;
    private ArrayList<Account> accounts_in_device;

    private static final int REQUEST_GET_ACCOUNT = 112;
    private TargetChartInfo dummyData, anotherdummy;
    public static final String DEFAULT_ENTRY = "default_entry";

    public ChartListRecyclerAdapter recyclerAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_main);

        if(android.os.Build.VERSION.SDK_INT > 22){
            if(isGETACCOUNTSAllowed()){
                //initialize();
                //return;
            }else{
                requestGET_ACCOUNTSPermission();
            }
        }else{
            //initialize();
        }

        initialize();
        ArrayList<TargetChartInfo> list = new ArrayList<>();
        dummyData = new TargetChartInfo();
        anotherdummy = new TargetChartInfo();
        dummyData.setUrl(DEFAULT_ENTRY);
        dummyData.setTableName(DEFAULT_ENTRY);
        dummyData.setAxisColumnNumber(DEFAULT_ENTRY);
        dummyData.setDataColumnNumber(DEFAULT_ENTRY);
        list.add(dummyData);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new ChartListRecyclerAdapter(list);
        Log.v("ChartListRecycler", recyclerAdapter.listOfChartsToShow.get(0).getTableName());

        recyclerView.setAdapter(recyclerAdapter);

        ImageButton fabButton = (ImageButton) findViewById(R.id.add_chart_button);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dummyData.setTableName("List Item" + recyclerAdapter.getItemCount());
                //recyclerAdapter.addItem(dummyData, recyclerAdapter.getItemCount());
                //recyclerAdapter.notifyItemChanged(recyclerAdapter.getItemCount());
                fabButtonOnClick();
            }
        });


    }


    public void fabButtonOnClick(){
        ChartDataInputDialogFragment generalDialogFragment =
                ChartDataInputDialogFragment.newInstance("title", "message", accounts_in_device, this);
        generalDialogFragment.show(getFragmentManager(),"dialog");

    }

    @Override
    public void showChartClicked(ChartDataInputDialogFragment dialog, View view){
        showChart(view);
    }

    private void initialize(){
        account_selector_instance = new AccountSelector();
        accounts_in_device = account_selector_instance.initAccountSelector(this);

    }

    protected void onResume(){
        super.onResume();
        ((ChartListRecyclerAdapter) recyclerAdapter).setOnItemClickListener(new ChartListRecyclerAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i("onResume", " Clicked on Item " + position);
            }
        });
        if(account_selector_instance != null) {
            account_selector_instance.initTableParameter();
        }
    }


    public void updateDisplay(){
        selected_account_text = (TextView)findViewById(R.id.selected_account);
        selected_account_text.setText(account_selector_instance.table_to_reference.getUserAccount().name);
    }

    private void getUserInputOfTableInformation(View view){

        account_selector_spinner = (Spinner) view.findViewById(R.id.account_selector);
        data_row_number = (EditText) view.findViewById(R.id.data_row_number_input_field);
        axis_column_number = (EditText) view.findViewById(R.id.axis_col_number_input_field);
        data_column_number = (EditText) view.findViewById(R.id.data_col_number_input_field);
        tableName = ((EditText) view.findViewById(R.id.table_name_input_field)).getText().toString();
        sheetName = ((EditText) view.findViewById(R.id.sheet_name_input_field)).getText().toString();
        if(TextUtils.isEmpty(data_row_number.getText())){
            data_row_number_string = account_selector_instance.DEFAULT_ENTRY;
        }else{
            data_row_number_string =data_row_number.getText().toString();
        }
        if(TextUtils.isEmpty(data_column_number.getText())){
            data_column_number_string = account_selector_instance.DEFAULT_ENTRY;
        }else{
            data_column_number_string = data_column_number.getText().toString();
        }
        if(TextUtils.isEmpty(data_column_number.getText())){
            axis_column_number_string = account_selector_instance.DEFAULT_ENTRY;
        }else{
            axis_column_number_string = axis_column_number.getText().toString();
        }
        account_selector_instance.table_to_reference.setUserAccount(accounts_in_device.get(account_selector_spinner.getSelectedItemPosition()));
        account_selector_instance.table_to_reference.setTableName(tableName);
        account_selector_instance.table_to_reference.setSheetName(sheetName);
        account_selector_instance.table_to_reference.setAxisColumnNumber(axis_column_number_string);
        account_selector_instance.table_to_reference.setDataColumnNumber(data_column_number_string);
        account_selector_instance.table_to_reference.setDataRowNumber(data_row_number_string);
    }

    private void showManual(){
        Intent manualIntent = new Intent(MainActivity.this, WebViewActivity.class);
        this.startActivity(manualIntent);
    }
    private void showChart(View view){

        getUserInputOfTableInformation(view);
        if(account_selector_instance.checkTableParameter()){
            Intent myIntent = new Intent(MainActivity.this, GraphActivity.class);
            myIntent.putExtra("account_selected", account_selector_instance.table_to_reference);
            this.startActivity(myIntent);
        }
    }

    private boolean isGETACCOUNTSAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }


    //if you don't have the permission then Requesting for permission
    private void requestGET_ACCOUNTSPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.GET_ACCOUNTS)){


        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.GET_ACCOUNTS},REQUEST_GET_ACCOUNT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == REQUEST_GET_ACCOUNT){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Thanks You For Permission Granted ",Toast.LENGTH_LONG).show();
                initialize();
            }else{
                Toast.makeText(this,"Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }

    }

}