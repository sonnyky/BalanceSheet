package com.tinker.graphit;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import android.support.v4.view.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;

import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
/*
* Using MPAndroidChart. Home page : https://github.com/PhilJay/MPAndroidChart
* */

public class GraphActivity extends FragmentActivity {
    private ProgressBar spinner;
    private static final int USER_PERMISSION_REQUIRED = 55664;

    LineChart chart;
    YAxis yAxis;
    XAxis xAxis;
    CellData[] cell_data;

    TargetChartInfo user_table_information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        Intent intent = getIntent();

        //The table information will be passed here by the MainActivity class
        user_table_information = intent.getParcelableExtra("account_selected");
        screenInit();
        showSpinner();
        ChartSettings();
    }

    private void screenInit(){
        TextView chartScreenTitle;
        chartScreenTitle = (TextView) findViewById(R.id.chart_screen_title);
        chartScreenTitle.setText(user_table_information.getTableName());
    }

    private void showSpinner(){
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new SpreadSheetIntegration().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO : add menu to change chart settings
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == USER_PERMISSION_REQUIRED) {
            if (resultCode == RESULT_OK) {
                showSpinner();
                new SpreadSheetIntegration().execute();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), R.string.need_sign_in, Toast.LENGTH_SHORT).show();
            }
        } else {

        }
    }

    private class SpreadSheetIntegration extends AsyncTask<String, Integer, String> {
        float temp_float_number=0.0f;
        float min_data_value = 1000000.0f, max_data_value = 0.0f;
        int data_counter=0;

        String token;
        WorksheetEntry worksheet;

        @Override
        protected  String doInBackground(String...params){
            String scopes = "oauth2:https://www.googleapis.com/auth/userinfo.profile  https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/userinfo.email https://spreadsheets.google.com/feeds https://spreadsheets.google.com/feeds/spreadsheets/private/full https://docs.google.com/feeds https://www.googleapis.com/auth/drive";
            try {
                token =
                        GoogleAuthUtil.getToken(
                                GraphActivity.this,
                                user_table_information.getUserAccount(),
                                scopes);
                System.out.println("Token: " + token);

                SpreadsheetService service = new SpreadsheetService("balanceSheet");
                service.setProtocolVersion(SpreadsheetService.Versions.V3);
                service.setAuthSubToken(token);

                URL metafeedUrl = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
                SpreadsheetFeed feed = service.getFeed(metafeedUrl, SpreadsheetFeed.class);
                List<SpreadsheetEntry> spreadsheets = feed.getEntries();


                // Iterate through all of the spreadsheets returned
                for (SpreadsheetEntry spreadsheet : spreadsheets) {
                    if(spreadsheet.getTitle().getPlainText().equals(user_table_information.getTableName())) {
                        System.out.println(spreadsheet.getTitle().getPlainText());
                        //Get the first worksheet in the spreadsheet which we found
                        WorksheetFeed worksheetFeed = service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
                        List<WorksheetEntry> worksheets = worksheetFeed.getEntries();

                        for(WorksheetEntry cur_worksheet : worksheets){
                            if(cur_worksheet.getTitle().getPlainText().equals(user_table_information.getSheetName())){
                                worksheet = cur_worksheet;
                                break;
                            }else{
                                return "No such sheet name, try again";
                            }
                        }

                        try {
                            URL cellFeedUrl = new URI(worksheet.getCellFeedUrl().toString() + "?min-row=" + user_table_information.getRowWhereDataStarts() + "&min-col="+user_table_information.getDataColumnNumber()+"&max-col="+user_table_information.getDataColumnNumber()).toURL();
                            URL axisLabelUrl = new URI(worksheet.getCellFeedUrl().toString() + "?min-row=" + user_table_information.getRowWhereDataStarts() + "&min-col=" + user_table_information.getAxisColumnNumber() + "&max-col=" + user_table_information.getAxisColumnNumber()).toURL();
                            CellFeed cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);
                            CellFeed axisLabelFeed = service.getFeed(axisLabelUrl, CellFeed.class);

                            cell_data = new CellData[cellFeed.getTotalResults()];
                            ArrayList<Entry> valsComp1 = new ArrayList<Entry>();
                            ArrayList<String> xVals = new ArrayList<String>();

                            for(CellEntry label_data : axisLabelFeed.getEntries()){
                                xVals.add(String.valueOf(label_data.getCell().getValue()));
                            }

                            for (CellEntry cell : cellFeed.getEntries()) {
                                temp_float_number = (float)(cell.getCell().getDoubleValue());
                                valsComp1.add(new Entry(temp_float_number, data_counter));

                                if(data_counter == 0) {
                                    min_data_value = temp_float_number;
                                    max_data_value = temp_float_number;
                                }
                                if(temp_float_number < min_data_value){
                                    min_data_value = temp_float_number;
                                }
                                if(temp_float_number > max_data_value){
                                    max_data_value = temp_float_number;
                                }
                                // xVals.add(String.valueOf(data_counter));
                                data_counter++;
                                // Print the cell's address in A1 notation
                                //System.out.print(cell.getTitle().getPlainText() + "\t");
                                // Print the cell's address in R1C1 notation
                                //System.out.print(cell.getId().substring(cell.getId().lastIndexOf('/') + 1) + "\t");
                                // Print the cell's formula or text value
                                //System.out.print(cell.getCell().getInputValue() + "\t");
                                // Print the cell's calculated value if the cell's value is numeric
                                // Prints empty string if cell's value is not numeric
                                //System.out.print(cell.getCell().getNumericValue() + "\t");
                                // Print the cell's displayed value (useful if the cell has a formula)
                                //System.out.println(cell.getCell().getValue() + "\t");
                            }

                            LineDataSet setComp1 = new LineDataSet(valsComp1, "Monthly");
                            setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
                            setComp1.setDrawValues(false);

                            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                            dataSets.add(setComp1);

                            final LineData line_data = new LineData(xVals, dataSets);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    yAxis.setAxisMinValue(min_data_value);
                                    chart.setDescription("");
                                    chart.setData(line_data);
                                    chart.invalidate();
                                }
                            });

                        }catch (URISyntaxException us_exc){
                            System.out.println("Exception");
                        }

                    }

                }

            } catch (AuthenticationException e) {
                e.printStackTrace();
            } catch (MalformedURLException me){

            }catch (ServiceException se){

            }catch (IOException ie){
                System.out.println("IOException");
            } catch (final UserRecoverableAuthException ure){
                startActivityForResult(ure.getIntent(), USER_PERMISSION_REQUIRED);
            }catch (GoogleAuthException gae){
                System.out.println(gae.getMessage());
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String aLong) {
            super.onPostExecute(aLong);
            System.out.println("Finished");
            spinner.setVisibility(View.GONE);
        }
    }

    private void ChartSettings(){
        chart = (LineChart) findViewById(R.id.chart);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
        yAxis = chart.getAxisLeft();
        yAxis.resetAxisMaxValue();
        yAxis.resetAxisMinValue();
        yAxis.setValueFormatter(new LargeValueFormatter());
    }
}
