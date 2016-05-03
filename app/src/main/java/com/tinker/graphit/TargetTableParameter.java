package com.tinker.graphit;

/**
 * Created by sonny.kurniawan on 2016/05/01.
 */
public class TargetTableParameter {
    private String url;
    private String user_account;
    private String table_name;
    private String axis_column_number;
    private String data_column_number;

    public String setUrl(String input_url){
        url = input_url;
        return url;
    }
    public String setUserAccount(String input_user_account){
        user_account = input_user_account;
        return user_account;
    }
    public String setTableName(String input_table_name){
        table_name = input_table_name;
        return table_name;
    }
    public String setAxisColumnNumber(String input_axis_column_number){
        axis_column_number = input_axis_column_number;
        return axis_column_number;
    }
    public String setDataColumnNumber(String input_data_column_number){
        data_column_number = input_data_column_number;
        return data_column_number;
    }

}
