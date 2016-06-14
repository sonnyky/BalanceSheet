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
    ArrayList<Account> accountsInDevice;
    AccountManager accountManager;
    Account[] accounts;
    String account_selected;
    public TargetTableParameter table_to_reference;
    public static final String DEFAULT_ENTRY = "default_entry";

    public ArrayList<Account> initAccountSelector(final Activity activity_reference) {
        accountsInDevice = new ArrayList<Account>();
        accountManager = AccountManager.get(activity_reference);
        accounts = accountManager.getAccountsByType("com.google");
        accountsInDevice.clear();
        initTableParameter();
        for (Account account : accounts)
        {
            accountsInDevice.add(account);
        }
        return accountsInDevice;
    }

    public boolean checkTableParameter(){
        if(table_to_reference.getUserAccount()==null || table_to_reference.getAxisColumnNumber() == DEFAULT_ENTRY || table_to_reference.getDataColumnNumber() == DEFAULT_ENTRY){

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
    }

}
