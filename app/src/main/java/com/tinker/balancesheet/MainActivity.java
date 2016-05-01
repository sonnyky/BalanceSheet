package com.tinker.balancesheet;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by sonny.kurniawan on 2015/10/11.
 */
public class MainActivity extends Activity {

    ArrayList<String> gUsernameList;
    AccountManager accountManager;
    Account[] accounts;
    String account_selected;
    private TargetTableParameter table_to_reference;
    //TODO : create method to get table parameters from user;

    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_login);

        final Button button = (Button) findViewById(R.id.select_account_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountSelector();
            }
        });
    }

    private void accountSelector(){

        gUsernameList = new ArrayList<String>();
        accountManager = AccountManager.get(this);
        accounts = accountManager.getAccountsByType("com.google");

        gUsernameList.clear();
        for (Account account : accounts) {
            gUsernameList.add(account.name);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your Google Account");
        ListView lv = new ListView(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1, android.R.id.text1,
                        gUsernameList);

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent,View view,int position,long
                    id)
            {
                Toast.makeText(getApplicationContext(), "You selected :" +  gUsernameList.get(position), Toast.LENGTH_LONG).show();
                //Remember the selected account
                account_selected = gUsernameList.get(position);
            }
        });

        builder.setView(lv);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(account_selected==null){
                    return;
                }
                //Dismiss dialog and pass selected account to the Main activity
                dialog.dismiss();
                Intent myIntent = new Intent(MainActivity.this, GraphActivity.class);
                myIntent.putExtra("account_selected", account_selected); //Optional parameters
                MainActivity.this.startActivity(myIntent);
            }
        });
        final Dialog dialog = builder.create();
        dialog.show();
    }

    private void GetChartName(){
        //TODO : Get chart name from user input
    }

}
