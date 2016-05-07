package com.tinker.graphit;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sonny.kurniawan on 2016/05/01.
 */
public class TargetTableParameter implements Parcelable {
    private String url;
    private String user_account;
    private String table_name;
    private String axis_column_number;
    private String data_column_number;


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<TargetTableParameter> CREATOR = new Parcelable.Creator<TargetTableParameter>() {
        public TargetTableParameter createFromParcel(Parcel in) {
            return new TargetTableParameter(in);
        }

        public TargetTableParameter[] newArray(int size) {
            return new TargetTableParameter[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private TargetTableParameter(Parcel in) {
        url=in.readString();
        user_account=in.readString();
        axis_column_number = in.readString();
        data_column_number = in.readString();
        table_name = in.readString();
    }
    public TargetTableParameter(){

    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(url);
        out.writeString(user_account);
        out.writeString(axis_column_number);
        out.writeString(data_column_number);
        out.writeString(table_name);
    }


    public String setUrl(String input_url){
        url = input_url;
        return url;
    }
    public String setUserAccount(String input_user_account){
        user_account = input_user_account;
        return user_account;
    }

    public String getUserAccount(){
        return user_account;
    }
    public String getTable_name(){
        return table_name;
    }
    public String getAxisNumber(){
        return axis_column_number;
    }
    public String getDataNumber(){
        return data_column_number;
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
