package com.tinker.graphit;

/**
 * Created by sonny on 2016/07/25.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
public class ChartDatabaseOperator {

    private ChartDatabaseHandler chartDbHelper;

    private Context context;

    private SQLiteDatabase database;

    public ChartDatabaseOperator(Context c) {
        context = c;
    }

    public ChartDatabaseOperator openChartDatabase() throws SQLException{
        chartDbHelper = new ChartDatabaseHandler(context);
        database = chartDbHelper.getWritableDatabase();
        return this;
    }

    public void closeChartDatabase(){
        database.close();
    }

    public void insertNewChartToDatabase(String account_name, String table_name, String sheet_name, String data_row_number, String data_col_number, String axis_col_number){
        ContentValues content_values = new ContentValues();
        content_values.put(chartDbHelper.COLUMN_ACCOUNT_NAME, account_name);
        content_values.put(chartDbHelper.COLUMN_CHART_NAME, table_name);
        content_values.put(chartDbHelper.COLUMN_SHEET_NAME, sheet_name);
        content_values.put(chartDbHelper.COLUMN_STARTING_ROW_NUMBER, data_row_number);
        content_values.put(chartDbHelper.COLUMN_DATA_COL_NUMBER, data_col_number);
        content_values.put(chartDbHelper.COLUMN_AXIS_COL_NUMBER, axis_col_number);
        database.insert(chartDbHelper.TABLE_NAME, null, content_values);
    }

    public Cursor fetchChartData(){
        String[] columns_of_chart_data = new String[] { chartDbHelper.COLUMN_CHART_ID, chartDbHelper.COLUMN_ACCOUNT_NAME, chartDbHelper.COLUMN_CHART_NAME, chartDbHelper.COLUMN_SHEET_NAME, chartDbHelper.COLUMN_STARTING_ROW_NUMBER, chartDbHelper.COLUMN_DATA_COL_NUMBER, chartDbHelper.COLUMN_AXIS_COL_NUMBER };
        Cursor cursor = database.query(chartDbHelper.TABLE_NAME, columns_of_chart_data, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int updateChartData(int _id, String account_name, String table_name, String sheet_name, String data_row_number, String data_col_number, String axis_col_number) {
        ContentValues content_values = new ContentValues();
        content_values.put(chartDbHelper.COLUMN_ACCOUNT_NAME, account_name);
        content_values.put(chartDbHelper.COLUMN_CHART_NAME, table_name);
        content_values.put(chartDbHelper.COLUMN_SHEET_NAME, sheet_name);
        content_values.put(chartDbHelper.COLUMN_STARTING_ROW_NUMBER, data_row_number);
        content_values.put(chartDbHelper.COLUMN_DATA_COL_NUMBER, data_col_number);
        content_values.put(chartDbHelper.COLUMN_AXIS_COL_NUMBER, axis_col_number);
        int i = database.update(chartDbHelper.TABLE_NAME, content_values, chartDbHelper.COLUMN_CHART_ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(chartDbHelper.TABLE_NAME, chartDbHelper.COLUMN_CHART_ID + "=" + _id, null);
    }

}
