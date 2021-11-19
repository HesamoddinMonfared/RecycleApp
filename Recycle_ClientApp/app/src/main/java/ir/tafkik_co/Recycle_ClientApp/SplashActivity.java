package ir.tafkik_co.Recycle_ClientApp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.util.HashMap;

import ir.tafkik_co.Recycle_ClientApp.LoginApp.LoginAppActivity;

public class SplashActivity extends AppCompatActivity implements HMS_AbstractInterface {

    HMS_HelperClass hms_helperClass = new HMS_HelperClass();
    Context context;
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.splash_layout_activity_xmlfile);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                SendCommandToServer("AppVersionCheck");
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    //////////////////////////////////////START: JSON Manager///////////////////////////////////////
    private void SendCommandToServer(String inputCommand){
        try {
            if (!hms_helperClass.isNetworkConnected(context)) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                dialogBuilder.setCancelable(false);
                dialogBuilder.setMessage("لطفا اتصال به اینترنت را بررسی کنید")
                        .setNegativeButton("خروج", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                ActivityCompat.finishAffinity(SplashActivity.this);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                            }
                        })
                        .setPositiveButton("تلاش مجدد", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SendCommandToServer("Login_CheckStatus");
                            }
                        });
                Dialog dialog = dialogBuilder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
            else {
                //Check for Login info. if correct goto main activity, else goto login activity
                SharedPreferences settings = getApplicationContext().getSharedPreferences(hms_helperClass.MY_PREFS_NAME, MODE_PRIVATE);
                String tmpPhone = settings.getString("userInfo_phone", "0");
                tmpPhone = URLEncoder.encode(tmpPhone, "UTF-8");
                String tmpSecret = settings.getString("userInfo_secretcode", "0");
                tmpSecret = URLEncoder.encode(tmpSecret, "UTF-8");

                String mainSiteAddress = hms_helperClass.getMainSiteAddress(getApplicationContext());

                //START: USE GET METHOD
                /*if(inputCommand.equals("AppVersionCheck")) {
                    hms_helperClass.downloadJSON_GET(this, "AppVersionCheck", mainSiteAddress + "App_API/AppInfo_getData.php");
                }
                else if(inputCommand.equals("Login_CheckStatus")) {
                    hms_helperClass.downloadJSON_GET(this, "Login_CheckStatus", mainSiteAddress + "App_API/Login_CheckStatus.php?p1=" + tmpPhone + "&p2=" + tmpSecret);
                }*/
                //END: USE GET METHOD

                //START: USE POST METHOD
                final String finalTmpPhone = tmpPhone;
                final String finalTmpSecret = tmpSecret;
                HashMap<String, String> myMap = new HashMap<String, String>() {{
                    put("p1", finalTmpPhone);
                    put("p2", finalTmpSecret);
                }};
                if(inputCommand.equals("AppVersionCheck")) {
                    hms_helperClass.downloadJSON_POST(this, "AppVersionCheck", mainSiteAddress + "App_API/AppInfo_getData.php", myMap);
                }
                else if(inputCommand.equals("Login_CheckStatus")) {
                    hms_helperClass.downloadJSON_POST(this, "Login_CheckStatus", mainSiteAddress + "App_API/Login_CheckStatus.php", myMap);
                }
                //END: USE POST METHOD

                hms_helperClass.showLoadingDialog(context);
            }
        }
        catch (Exception ex){
            hms_helperClass.showException(context, ex.getMessage(), false, false);
            ActivityCompat.finishAffinity(SplashActivity.this);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    }

    //////////////////////////////////////START: JSON Manager///////////////////////////////////////
    @Override
    public void actionJSON(String inputCommand, String json) throws Exception {
        //JSONObject jsonObj = new JSONObject(json);
        JSONArray jsonArray = new JSONArray(json);
        JSONObject jsonObj = jsonArray.getJSONObject(0);

        if(inputCommand.equals("AppVersionCheck")) {
            int currentVersionCode = hms_helperClass.getVersionCode(context);
            if(currentVersionCode == -1){
                Toast.makeText(context,"خطا در دریافت کد ورژن!",Toast.LENGTH_SHORT).show();
                ActivityCompat.finishAffinity(SplashActivity.this);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                return;
            }

            if(currentVersionCode < jsonObj.getInt("minVersionCode")){
                try {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setMessage("نسخه فعلی، نیاز به بروزرسانی دارد:")
                            .setPositiveButton("دریافت بروزرسانی", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://tafkik-co.ir/index.html#intro"));
                                    startActivity(browserIntent);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                                    ActivityCompat.finishAffinity((Activity) context);
                                }
                            });
                    Dialog dialog = dialogBuilder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
                catch (Exception ex){
                    hms_helperClass.showException(context, ex.getMessage(), false, false);
                }
            }
            else if(currentVersionCode < jsonObj.getInt("lastVersionCode")){
                try {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setMessage("بروزرسانی جدید برای برنامه منتشر شده است:")
                            .setNegativeButton("فعلا نه!", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    //Check for Login info. if correct goto main activity, else goto login activity
                                    SendCommandToServer("Login_CheckStatus");
                                }
                            })
                            .setPositiveButton("بروزرسانی", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://tafkik-co.ir/index.html#intro"));
                                    startActivity(browserIntent);
                                    ActivityCompat.finishAffinity((Activity) context);
                                }
                            });
                    Dialog dialog = dialogBuilder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
                catch (Exception ex){
                    hms_helperClass.showException(context, ex.getMessage(), false, false);
                }
            }
            else if(currentVersionCode >= jsonObj.getInt("lastVersionCode")){
                SendCommandToServer("Login_CheckStatus");
            }
        }
        else if(inputCommand.equals("Login_CheckStatus")) {
            try {
                if (jsonObj.getString("CanLogin").equals("True")) {
                    hms_helperClass.openActivity(this, MainActivity.class, true, true);
                }
                else if(jsonObj.getString("CanLogin").equals("False")) {
                    hms_helperClass.openActivity(this, LoginAppActivity.class, true, true);
                }
            }
            catch (Exception ex){
                hms_helperClass.showException(context, ex.getMessage(), false, false);
            }
        }
    }

    @Override
    public void failedJSON(){
        ActivityCompat.finishAffinity(SplashActivity.this);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    public Activity HMS_getActivity(){
        return this;
    }
    ////////////////////////////////////////END: JSON Manager///////////////////////////////////////
}
