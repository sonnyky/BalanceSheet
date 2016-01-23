package com.tinker.balancesheet;

import android.accounts.AccountManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import android.support.v4.view.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.LargeValueFormatter;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;

import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.gbase.client.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.Worksheet;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;



import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/*
* Using MPAndroidChart. Home page : https://github.com/PhilJay/MPAndroidChart
* */

public class GraphActivity extends FragmentActivity {
    SectionsPagerManager mSectionsPagerAdapter;
    ViewPager mViewPager;
    SpreadSheetIntegration spread_sheet;
    private ProgressBar spinner;
    private static final int REQ_SIGN_IN_REQUIRED = 55664;
    LineChart chart;
    YAxis yAxis;
    XAxis xAxis;
    CellData[] cell_data;

    String account_selected_by_user;

    private ImageButton btnSpeak;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        account_selected_by_user = intent.getStringExtra("account_selected");
        Toast.makeText(getApplicationContext(), "We got this account :" + account_selected_by_user, Toast.LENGTH_LONG).show();
        //Button to input by voice
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //btnSpeak.setBackgroundColor(0xFF908F7B);
                Toast.makeText(getApplicationContext(), "Please input number", Toast.LENGTH_LONG).show();
                //btnSpeak.setBackgroundColor(0xFF5f6960);
            }
        });

        //This is the spinner to indicate data loading
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

        //Enable chart layout and setup chart settings
        chart = (LineChart) findViewById(R.id.chart);
        chart.getAxisRight().setEnabled(false);
        yAxis = chart.getAxisLeft();
        yAxis.setValueFormatter(new LargeValueFormatter());
        yAxis.setAxisMinValue(3000000);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        //mSectionsPagerAdapter = new SectionsPagerManager(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter. comment out for now
        // mViewPager = (ViewPager) findViewById(R.id.pager);
        // mViewPager.setAdapter(mSectionsPagerAdapter);

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class SpreadSheetIntegration extends AsyncTask<String, Integer, String> {
        final String accountName = AccountManager.KEY_ACCOUNT_NAME;
        final String sheetName = "Ausgaben";
        float temp_float_number=0.0f;
        int data_counter=0;
        @Override
        protected  String doInBackground(String...params){
            long totalSize=0;
            System.out.println("Starting MySpreadSheetIntegration");
            // Application code here

            File p12 = new File("src/main/balanceSheet-74fe8f58993f.p12");
            //SpreadsheetService spreadsheet= new SpreadsheetService("balanceSheet");
            //spreadsheet.setProtocolVersion(SpreadsheetService.Versions.V3);
            System.out.println("MySpreadSheetIntegration");
            HttpTransport httpTransport = new NetHttpTransport();
            JacksonFactory jsonFactory = new JacksonFactory();
            String[] SCOPESArray = {"https://spreadsheets.google.com/feeds", "https://spreadsheets.google.com/feeds/spreadsheets/private/full", "https://docs.google.com/feeds"};
            final List SCOPES = Arrays.asList(SCOPESArray);
            //String scopes = "oauth2:188599119327-i69elrjun6bsd0huiak3tt0il40l8v7k@developer.gserviceaccount.com  https://www.googleapis.com/auth/plus.login";
            String scopes = "oauth2:https://www.googleapis.com/auth/userinfo.profile  https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/userinfo.email https://spreadsheets.google.com/feeds https://spreadsheets.google.com/feeds/spreadsheets/private/full https://docs.google.com/feeds";
            try {
                String token =
                        GoogleAuthUtil.getToken(
                                GraphActivity.this,
                                account_selected_by_user,
                                scopes);
                System.err.println("Token: " + token);
                System.out.println("Trying to get Ausgaben spreadsheet data");
                /**
                 *
                 * IMPORTANT! Make sure to share the spreadsheet with the email specified in OAuth Service Account in Google Developer Console
                 */
                /*
                GoogleCredential credential = new GoogleCredential.Builder()
                        .setTransport(httpTransport)
                        .setJsonFactory(jsonFactory)
                        .setServiceAccountId("188599119327-i69elrjun6bsd0huiak3tt0il40l8v7k@developer.gserviceaccount.com")
                        .setServiceAccountScopes(SCOPES)
                        .setServiceAccountPrivateKeyFromP12File(p12)
                        .build();
                        */
                SpreadsheetService service = new SpreadsheetService("balanceSheet");
                service.setProtocolVersion(SpreadsheetService.Versions.V3);
                service.setAuthSubToken(token);
                //service.setOAuth2Credentials(credential);

                //spreadsheet.setUserCredentials("sonny.kurniawan.yap@gmail.com", "gmail_pa55");

                URL metafeedUrl = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
                SpreadsheetFeed feed = service.getFeed(metafeedUrl, SpreadsheetFeed.class);
                List<SpreadsheetEntry> spreadsheets = feed.getEntries();


                // Iterate through all of the spreadsheets returned
                for (SpreadsheetEntry spreadsheet : spreadsheets) {

                    //check to find a specific sheet name, if we find it, do stuff
                    if(spreadsheet.getTitle().getPlainText().equals(sheetName)) {

                        // Print the title of this spreadsheet to the screen
                        System.err.println(spreadsheet.getTitle().getPlainText());

                        //Get the first worksheet in the spreadsheet which we found
                        WorksheetFeed worksheetFeed = service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
                        List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
                        WorksheetEntry worksheet = worksheets.get(0);

                        try {
                            URL cellFeedUrl = new URI(worksheet.getCellFeedUrl().toString() + "?min-row=4&min-col=3&max-col=3").toURL();
                            URL axisLabelUrl = new URI(worksheet.getCellFeedUrl().toString() + "?min-row=4&min-col=1&max-col=1").toURL();
                            CellFeed cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);
                            CellFeed axisLabelFeed = service.getFeed(axisLabelUrl, CellFeed.class);

                            cell_data = new CellData[cellFeed.getTotalResults()];
                            ArrayList<Entry> valsComp1 = new ArrayList<Entry>();
                            ArrayList<String> xVals = new ArrayList<String>();

                            for(CellEntry label_data : axisLabelFeed.getEntries()){
                                xVals.add(String.valueOf(label_data.getCell().getValue()));
                            }


                            // Iterate through each cell, printing its value.
                            for (CellEntry cell : cellFeed.getEntries()) {
                                temp_float_number = (float)(cell.getCell().getDoubleValue());
                                valsComp1.add(new Entry(temp_float_number, data_counter));

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

                            ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
                            dataSets.add(setComp1);

                            final LineData line_data = new LineData(xVals, dataSets);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
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
            } catch (UserRecoverableAuthException ure){
                startActivityForResult(ure.getIntent(), REQ_SIGN_IN_REQUIRED);
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


}
