package com.tinker.graphit;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by sonny on 2016/07/25.
 */
public class ChartDatabaseHandler extends SQLiteOpenHelper{

    private static final String TAG = "SQLiteForChartItem";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Chart_Database.db";

    // Table name: Charts.
    public static final String TABLE_NAME = "Charts";

    public static final String COLUMN_CHART_ID ="Data_Id";
    public static final String COLUMN_ACCOUNT_NAME="Account_Name";
    public static final String COLUMN_CHART_NAME ="Chart_Name";
    public static final String COLUMN_SHEET_NAME = "Sheet_Name";
    public static final String COLUMN_STARTING_ROW_NUMBER = "Starting_Row_Number";
    public static final String COLUMN_DATA_COL_NUMBER = "Data_Col_Number";
    public static final String COLUMN_AXIS_COL_NUMBER = "Axis_Col_Number";

    private static final String CREATE_CHART_LIST_TABLE = "CREATE TABLE " + TABLE_NAME +
            "(" +
            COLUMN_CHART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_ACCOUNT_NAME + " TEXT," +
            COLUMN_CHART_NAME + " TEXT," +
            COLUMN_SHEET_NAME + " TEXT," +
            COLUMN_STARTING_ROW_NUMBER + " TEXT," +
            COLUMN_DATA_COL_NUMBER + " TEXT," +
            COLUMN_AXIS_COL_NUMBER + " TEXT" +
            ")";

    public ChartDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.print(CREATE_CHART_LIST_TABLE);
        db.execSQL(CREATE_CHART_LIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
