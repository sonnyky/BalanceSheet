package com.tinker.balancesheet;

import android.accounts.AccountManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import android.support.v4.view.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;

import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.gbase.client.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;



import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends FragmentActivity {
    SectionsPagerManager mSectionsPagerAdapter;
    ViewPager mViewPager;
    SpreadSheetIntegration spread_sheet;
    private static final int REQ_SIGN_IN_REQUIRED = 55664;


    private ImageButton btnSpeak;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerManager(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
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
                                MainActivity.this,
                                "sonny.kurniawan.yap@gmail.com",
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
                    // Print the title of this spreadsheet to the screen
                    System.err.println(spreadsheet.getTitle().getPlainText());
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
        }
    }


}
