package ir.tafkik_co.Recycle_ClientApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class HMS_HelperClass {

    public ProgressDialog loadingDialog;

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static final int connectionTimeOut = 30000;
    public static final int readTimeOut = 30000;
    public static final int HourExpireInterval = 5;// use in DateSelection_ListView_Adapter
    public static int Minimum_Request_Point = 10;// use in ShebaPaymentActivity
    public static int Maximum_NumberOfAddress = 10;//use in AddressSelectionActivity
    public static int Maximum_NumberOfCollectionRequest = 5;//use in GarbageSelection_Fragment
    public static int Minimum_GarbageQuantityToContinue = 5;//use in GarbageSelection_Fragment

    public void showLoadingDialog(Context context){
        /*loadingDialog = new ProgressDialog(context);
        //loadingDialog.setMessage("در حال  اتصال به سرور");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(false);
        loadingDialog.show();*/

        loadingDialog = new ProgressDialog(context,R.style.MyThemeForDialogStyle);
        loadingDialog.setCancelable(false);
        loadingDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        loadingDialog.show();
    }

    //////////////////////////////////START: Web Database Manager///////////////////////////////////
    //send getApplicationContext() {or getActivity() in Fragment} as param to this function
    public String getMainSiteAddress(Context baseContext){
        BufferedReader reader;
        String main_site_address = "";
        try{
            final InputStream file = baseContext.getAssets().open("siteConf/main_site_address.txt");
            reader = new BufferedReader(new InputStreamReader(file));
            main_site_address = reader.readLine();
        } catch(IOException ioe){
            Log.d("MYTAG", "getMainSiteAddress(): " + ioe.getMessage());
            ioe.printStackTrace();
        }
        return main_site_address;
    }
    ////////////////////////////////////END: Web Database Manager///////////////////////////////////


    public String getVersionName(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("MYTAG", "getVersionName(): " + e.getMessage());
            return "";
        }
    }

    public int getVersionCode(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            int version = pInfo.versionCode;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("MYTAG", "getVersionCode(): " + e.getMessage());
            return -1;
        }
    }

    public boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean response = cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        if(!response)
            showException(context,HMS_Constant_Strings.There_IS_No_Internet_Connection, false, false);

        return response;
    }

    /////////////////////////////////START: Convert EN number to FA/////////////////////////////////
    /*public String convertFaNum(String faNumbers) {
        String[][] mChars = new String[][]{
                {"0", "۰"},
                {"1", "۱"},
                {"2", "۲"},
                {"3", "۳"},
                {"4", "۴"},
                {"5", "۵"},
                {"6", "۶"},
                {"7", "۷"},
                {"8", "۸"},
                {"9", "۹"}
        };
        for (String[] num : mChars) {
            faNumbers = faNumbers.replace(num[0], num[1]);
        }
        return faNumbers;
    }*/

    //private String[] faNumbers = new String[]{"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹"};

    /*public String convertFaNum2(String text) {
        if (text.length() == 0) {
            return "";
        }
        String out = "";
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if ('0' <= c && c <= '9') {
                int number = Integer.parseInt(String.valueOf(c));
                out += faNumbers[number];
            } else if (c == '٫' || c == ',') {
                out += '،';
            } else {
                out += c;
            }
        }
        return out;
    }*/

    public void setFontType_FarsiNumber(Context context, TextView v) {
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/yekan.ttf");
        v.setTypeface(face);
    }
    /////////////////////////////////END: Convert EN number to FA///////////////////////////////////
    public ArrayList<View> getViewsByTag(ViewGroup root, String tag) {
        ArrayList<View> views = new ArrayList<View>();
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                views.addAll(getViewsByTag((ViewGroup) child, tag));
            }

            final Object tagObj = child.getTag();
            if (tagObj != null && tagObj.equals(tag)) {
                views.add(child);
            }
        }
        return views;
    }
    ///////////////////////////////////START:Align Title of Activity////////////////////////////////
    public void  ActionBarTitleGravity(final AppCompatActivity context, String inputTitle){
        // Inflate your custom layout
        final ViewGroup actionBarLayout = (ViewGroup) context.getLayoutInflater().inflate(
                R.layout.custom_titlebar,
                null);

        // Set up your ActionBar
        final ActionBar actionBar = context.getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER );

        context.getSupportActionBar().setCustomView(actionBarLayout, params);

        final TextView actionBarTitle = context.findViewById(R.id.custom_titlebar_textView);
        actionBarTitle.setText(inputTitle);
        final ImageView actionBarBackButton = context.findViewById(R.id.custom_titlebar_imageView_back);
        actionBarBackButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.onBackPressed();
            }
        });
    }
    ////////////////////////////////////END:Align Title of Activity/////////////////////////////////
    public void openActivity(AppCompatActivity context, Class nextActivityClass, boolean clearFlag, boolean leftToRightFlag){
        Intent intent = new Intent(context, nextActivityClass);
        if(clearFlag == true)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
        if(leftToRightFlag == true)
            context.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        else
            context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

        context.finish();
    }
    /////////////////////////////////////////START: JSON Downloader/////////////////////////////////
    public void downloadJSON_GET(final HMS_AbstractInterface hms_abstractInterface, final String inputCommand, final String urlWebService) {
        class DownloadJSON extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String jsonResult) {
                try {
                    loadingDialog.dismiss();
                }catch (Exception ex) {
                }
                if(TextUtils.isEmpty(jsonResult)) {
                    hms_abstractInterface.failedJSON();
                    showExceptionOnUiThread(hms_abstractInterface, "پیغامی از سرور دریافت نشد!", true, false);
                    return;
                }

                super.onPostExecute(jsonResult);
                try {
                    hms_abstractInterface.actionJSON(inputCommand, jsonResult);
                } catch (Exception e) {
                    hms_abstractInterface.failedJSON();
                    showExceptionOnUiThread(hms_abstractInterface,  "خطا در تحلیل جواب سرور!" + e.getMessage(), true, false);
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setConnectTimeout(connectionTimeOut);
                    con.setReadTimeout(readTimeOut);
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    hms_abstractInterface.failedJSON();
                    showExceptionOnUiThread(hms_abstractInterface,"خطا در ارتباط با سرور." + e.getMessage(), true, false);
                    return null;
                }
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }

    public void downloadJSON_POST(final HMS_AbstractInterface hms_abstractInterface, final String inputCommand, final String urlWebService, final HashMap<String, String> postDataParams) {
        class DownloadJSON extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String jsonResult) {
                loadingDialog.dismiss();
                if(TextUtils.isEmpty(jsonResult)) {
                    hms_abstractInterface.failedJSON();
                    showExceptionOnUiThread(hms_abstractInterface, "پیغامی از سرور دریافت نشد!", true, false);
                    return;
                }

                super.onPostExecute(jsonResult);
                try {
                    hms_abstractInterface.actionJSON(inputCommand, jsonResult);
                } catch (Exception e) {
                    hms_abstractInterface.failedJSON();
                    showExceptionOnUiThread(hms_abstractInterface,  "خطا در تحلیل جواب سرور!" + e.getMessage(), true, false);
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    String response = "";
                    URL url = new URL(urlWebService);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(connectionTimeOut);
                    conn.setConnectTimeout(connectionTimeOut);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));
                    writer.flush();
                    writer.close();
                    os.close();
                    int responseCode=conn.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while ((line=br.readLine()) != null) {
                            response+=line;
                        }
                    }
                    else {
                        response="";
                    }
                    return response;
                } catch (Exception e) {
                    hms_abstractInterface.failedJSON();
                    showExceptionOnUiThread(hms_abstractInterface,"خطا در ارتباط با سرور." + e.getMessage(), true, false);
                    return null;
                }
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }

    /*public String  performPostCall(String requestURL, HashMap<String, String> postDataParams) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }*/

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            //result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append(entry.getKey());
            result.append("=");
            //result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append(entry.getValue());
        }
        Log.d("ZZZZZ", result.toString());
        return result.toString();
    }
    //////////////////////////////////////////END: JSON Downloader//////////////////////////////////

    ///////////////////////////////////START: Exception Manager/////////////////////////////////////
    public void showException(Context context, String msg, boolean isLongFlag, boolean needToReport) {
        try {
            if (isLongFlag == true)
                Toast.makeText(context, "Exception: "+ msg, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context, "Exception: "+ msg, Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            Log.d("MYTAG", "showException(): " + ex.getMessage());
        }
    }

    private void showExceptionOnUiThread(HMS_AbstractInterface hms_abstractInterface, final String msg, final boolean isLongFlag, boolean needToReport){
        try {
            final Activity tmpActivity = hms_abstractInterface.HMS_getActivity();
            tmpActivity.runOnUiThread(new Runnable() {
                public void run() {
                    if (isLongFlag == true)
                        Toast.makeText(tmpActivity, "Exception: " + msg, Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(tmpActivity, "Exception: "+ msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception ex){
                Log.d("MYTAG", "showExceptionOnUiThread(): " + ex.getMessage());
            }
    }
    ////////////////////////////////////END: Exception Manager//////////////////////////////////////
}
