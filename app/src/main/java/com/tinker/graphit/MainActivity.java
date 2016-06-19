package com.tinker.graphit;

import android.Manifest;
import android.accounts.Account;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;

/**
 * Created by sonny.kurniawan on 2015/10/11.
 */
public class MainActivity extends Activity {
    private AccountSelector account_selector_instance;
    private TextView selected_account_text;
    private EditText data_row_number, data_column_number, axis_column_number;
    private String tableName, sheetName, data_row_number_string, data_column_number_string, axis_column_number_string;
    private ArrayList<Account> accounts_in_device;
    private ArrayList<String> account_names_strings;
    private static final int REQUEST_GET_ACCOUNT = 112;


    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_login);

        if(android.os.Build.VERSION.SDK_INT > 22){
            if(isGETACCOUNTSAllowed()){
                // do your task

                initialize();
                return;
            }else{
                requestGET_ACCOUNTSPermission();
            }

        }else{
            initialize();
        }


    }

    private void initialize(){
        account_selector_instance = new AccountSelector();
        account_names_strings = new ArrayList<String>();
        accounts_in_device = account_selector_instance.initAccountSelector(MainActivity.this);
        Log.v("account how many", Integer.toString(accounts_in_device.size()));
        for(Account one_account : accounts_in_device){
            Log.v("account 0", one_account.name);
            account_names_strings.add(one_account.name);
        }
        final Button account_select_button = (Button) findViewById(R.id.select_account_btn);
        account_select_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildAccountSelectorDialog();
            }
        });
        final Button show_char_button = (Button) findViewById(R.id.show_chart_button);
        show_char_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChart();
            }
        });
    }

    protected void onResume(){
        super.onResume();;
        if(account_selector_instance != null) {
            account_selector_instance.initTableParameter();
            resetDisplay();
        }
    }

    public void buildAccountSelectorDialog(){

        final Dialog account_selector_dialog = new Dialog(this);
        account_selector_dialog.setContentView(R.layout.custom_list);
        account_selector_dialog.setTitle(R.string.main_dialog_title);

        ListView lv = (ListView ) account_selector_dialog.findViewById(R.id.custom_generic_list_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, android.R.id.text1,
                        account_names_strings);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                          account_selector_instance.table_to_reference.setUserAccount(accounts_in_device.get(position));
                                          account_selector_dialog.dismiss();
                                          updateDisplay();
                                      }
                                  }
        );
        account_selector_dialog.show();
    }

    public void resetDisplay(){
        selected_account_text = (TextView)findViewById(R.id.selected_account);
        selected_account_text.setText("None");
    }

    public void updateDisplay(){
        selected_account_text = (TextView)findViewById(R.id.selected_account);
        selected_account_text.setText(account_selector_instance.table_to_reference.getUserAccount().name);
    }

    private void getUserInputOfTableInformation(){
        data_row_number = (EditText) findViewById(R.id.data_row_number_input_field);
        axis_column_number = (EditText) findViewById(R.id.axis_col_number_input_field);
        data_column_number = (EditText) findViewById(R.id.data_col_number_input_field);
        tableName = ((EditText) findViewById(R.id.table_name_input_field)).getText().toString();
        sheetName = ((EditText) findViewById(R.id.sheet_name_input_field)).getText().toString();
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

        account_selector_instance.table_to_reference.setTableName(tableName);
        account_selector_instance.table_to_reference.setSheetName(sheetName);
        account_selector_instance.table_to_reference.setAxisColumnNumber(axis_column_number_string);
        account_selector_instance.table_to_reference.setDataColumnNumber(data_column_number_string);
        account_selector_instance.table_to_reference.setDataRowNumber(data_row_number_string);
    }

    private void showChart(){

        getUserInputOfTableInformation();
        if(account_selector_instance.checkTableParameter()){
            Intent myIntent = new Intent(MainActivity.this, GraphActivity.class);
            myIntent.putExtra("account_selected", account_selector_instance.table_to_reference);
            Log.v("account", account_selector_instance.table_to_reference.getUserAccount().name);
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

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){


                Toast.makeText(this,"Thanks You For Permission Granted ",Toast.LENGTH_LONG).show();

                initialize();

            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }

    }

}