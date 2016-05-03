package com.tinker.graphit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by sonny.kurniawan on 2015/10/11.
 */
public class MainActivity extends Activity {

    private TargetTableParameter table_to_reference;
    private static final String DEFAULT_ENTRY = "default_entry";
    private AccountSelector account_selector_instance;
    //TODO : create method to get table parameters from user

    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_login);
        account_selector_instance = new AccountSelector();
        account_selector_instance.initAccountSelector(MainActivity.this);
        initTableParameter();
        final Button button = (Button) findViewById(R.id.select_account_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account_selector_instance.buildAccountSelectorDialog(MainActivity.this);
            }
        });
    }

    private void initTableParameter(){
        table_to_reference = new TargetTableParameter();
        table_to_reference.setUrl(DEFAULT_ENTRY);
        table_to_reference.setTableName(DEFAULT_ENTRY);
        table_to_reference.setAxisColumnNumber(DEFAULT_ENTRY);
        table_to_reference.setDataColumnNumber(DEFAULT_ENTRY);
        table_to_reference.setUserAccount(DEFAULT_ENTRY);
    }
}