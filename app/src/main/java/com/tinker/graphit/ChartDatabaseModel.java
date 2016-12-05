package com.tinker.graphit;

/**
 * Created by sonny on 2016/07/24.
 */

/*
* Database object model for chart list item
* */

public class ChartDatabaseModel {

    private int data_id;
    private String account_name;
    private String table_name;
    private String sheet_name;
    private String data_row_number;
    private String data_col_number;
    private String axis_col_number;


    public ChartDatabaseModel(){

    }

    public ChartDatabaseModel(int data_id, String account_name, String table_name, String sheet_name, String data_row_number, String data_col_number, String axis_col_number){
        this.data_id = data_id;
        this.account_name = account_name;
        this.table_name = table_name;
        this.sheet_name = sheet_name;
        this.data_row_number = data_row_number;
        this.data_col_number = data_col_number;
        this.axis_col_number = axis_col_number;
    }

    public void setDataId(int data_id){this.data_id = data_id;}
    public void setAccountName(String account_name){this.account_name = account_name;}
    public void setTableName(String table_name){this.table_name = table_name;}
    public void setSheetName(String sheet_name){this.sheet_name = sheet_name;}
    public void setDataRowNumber(String data_row_number){this.data_row_number = data_row_number;}
    public void setDataColNumber(String data_col_number){this.data_col_number = data_col_number;}
    public void setAxisColNumber(String axis_col_number){this.axis_col_number = axis_col_number;}


    public int getDataId(){return this.data_id;}
    public String getAccountName(){return this.account_name;}
    public String getTable_name(){return this.table_name;}
    public String getSheet_name(){return this.sheet_name;}
    public String getDataRowNumber(){return this.data_row_number;}
    public String getDataColNumber(){return this.data_col_number;}
    public String getAxisColNumber(){return this.axis_col_number;}

}
