package com.tinker.graphit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sonny.kurniawan on 2015/10/11.
 */
public class MainActivity extends Activity {
    private AccountSelector account_selector_instance;
    private TextView selected_account_text;
    private EditText row_number, data_column_number;
    private String row_number_string, data_column_number_string;
    private ArrayList<String> accounts_in_device;
    //TODO : create method to get table parameters from user

    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_login);
        account_selector_instance = new AccountSelector();
        accounts_in_device = account_selector_instance.initAccountSelector(MainActivity.this);
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
        account_selector_instance.initTableParameter();
    }

    public void buildAccountSelectorDialog(){

        final Dialog account_selector_dialog = new Dialog(this);
        account_selector_dialog.setContentView(R.layout.custom_list);
        account_selector_dialog.setTitle(R.string.main_dialog_title);

        ListView lv = (ListView ) account_selector_dialog.findViewById(R.id.custom_generic_list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, android.R.id.text1,
                        accounts_in_device);
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

    public void updateDisplay(){
        selected_account_text = (TextView)findViewById(R.id.selected_account);
        selected_account_text.setText(account_selector_instance.table_to_reference.getUserAccount());
    }

    private void getUserInputOfTableInformation(){
        row_number = (EditText) findViewById(R.id.row_number_input_field);
        data_column_number = (EditText) findViewById(R.id.col_number_input_field);

        if(row_number.getText().toString()==""){
            row_number_string = account_selector_instance.DEFAULT_ENTRY;
        }else{
            row_number_string =row_number.getText().toString();
        }
        if(TextUtils.isEmpty(data_column_number.getText())){
            data_column_number_string = account_selector_instance.DEFAULT_ENTRY;
        }else{
            data_column_number_string = data_column_number.getText().toString();
        }

        Log.v("data_column_number",data_column_number_string);
        account_selector_instance.table_to_reference.setAxisColumnNumber(row_number_string);
        account_selector_instance.table_to_reference.setDataColumnNumber(data_column_number_string);
    }

    private void showChart(){

        getUserInputOfTableInformation();
        if(account_selector_instance.checkTableParameter()){
            Intent myIntent = new Intent(MainActivity.this, GraphActivity.class);
            myIntent.putExtra("account_selected", account_selector_instance.table_to_reference);
            this.startActivity(myIntent);
        }
    }

}