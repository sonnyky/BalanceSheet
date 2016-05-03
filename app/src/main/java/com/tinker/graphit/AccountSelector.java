package com.tinker.graphit;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * Created by sonny.kurniawan on 2016/05/03.
 */
public class AccountSelector {
    ArrayList<String> gUsernameList;
    AccountManager accountManager;
    Account[] accounts;
    String account_selected;

    public void initAccountSelector(final Activity activity_reference) {
        gUsernameList = new ArrayList<String>();
        accountManager = AccountManager.get(activity_reference);
        accounts = accountManager.getAccountsByType("com.google");
        gUsernameList.clear();
        for (Account account : accounts)
        {
            gUsernameList.add(account.name);
        }
    }

    public void buildAccountSelectorDialog(final Activity activity_reference){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity_reference);
        builder.setTitle("Choose your Google Account");
        ListView lv = new ListView(activity_reference);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (activity_reference, android.R.layout.simple_list_item_1, android.R.id.text1,
                        gUsernameList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
                                  {
                                      public void onItemClick(AdapterView<?> parent, View view, int position, long
                                              id) {
                                          Toast.makeText(activity_reference, "You selected :" + gUsernameList.get(position), Toast.LENGTH_LONG).show();
                                          account_selected = gUsernameList.get(position);
                                      }
                                  }
        );

        builder.setView(lv);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (account_selected == null) {
                            return;
                        }
                        dialog.dismiss();
                        Intent myIntent = new Intent(activity_reference, GraphActivity.class);
                        myIntent.putExtra("account_selected", account_selected); //Optional parameters
                        activity_reference.startActivity(myIntent);
                    }
                }
        );
        final Dialog dialog = builder.create();
        dialog.show();
    }

    public boolean checkTableParameter(){
        //TODO : Check that table parameter has all the necessary input to pass to Graph activity
        return false;
    }
    public void GetChartName(){
        //TODO : Get chart name from user input
    }

}
