package com.tinker.graphit;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
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
    public TargetTableParameter table_to_reference;
    public static final String DEFAULT_ENTRY = "default_entry";

    public ArrayList<String> initAccountSelector(final Activity activity_reference) {
        gUsernameList = new ArrayList<String>();
        accountManager = AccountManager.get(activity_reference);
        accounts = accountManager.getAccountsByType("com.google");
        gUsernameList.clear();
        initTableParameter();
        for (Account account : accounts)
        {
            gUsernameList.add(account.name);
        }
        return gUsernameList;
    }

    //TODO:create method to return Account Array, to input to GoogleAuthToken

    public boolean checkTableParameter(){
        if(table_to_reference.getUserAccount() == DEFAULT_ENTRY ||
                table_to_reference.getAxisNumber() == DEFAULT_ENTRY ||
                table_to_reference.getDataNumber() == DEFAULT_ENTRY){
            Log.v("row_number", table_to_reference.getAxisNumber());
            Log.v("data_number", table_to_reference.getDataNumber());
            Log.v("user_account", table_to_reference.getUserAccount());
            return false;
        }else {

            return true;
        }
    }
    public void initTableParameter(){
        table_to_reference = new TargetTableParameter();
        table_to_reference.setUrl(DEFAULT_ENTRY);
        table_to_reference.setTableName(DEFAULT_ENTRY);
        table_to_reference.setAxisColumnNumber(DEFAULT_ENTRY);
        table_to_reference.setDataColumnNumber(DEFAULT_ENTRY);
        table_to_reference.setUserAccount(DEFAULT_ENTRY);
    }

}
