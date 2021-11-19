package ir.tafkik_co.Recycle_ClientApp.AboutUs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.util.HashMap;

import ir.tafkik_co.Recycle_ClientApp.HMS_AbstractInterface;
import ir.tafkik_co.Recycle_ClientApp.HMS_HelperClass;
import ir.tafkik_co.Recycle_ClientApp.MainActivity;
import ir.tafkik_co.Recycle_ClientApp.R;
import static android.content.Context.MODE_PRIVATE;

public class AboutUs_Fragment extends Fragment implements HMS_AbstractInterface {

    HMS_HelperClass hms_helperClass = new HMS_HelperClass();
    View root;
    Context context;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();
        root = inflater.inflate(R.layout.aboutus_layout_fragment_xmlfile, container, false);
        MainActivity.changeTitle("درباره ما");
        MainActivity.toolbar.setNavigationIcon(null);
        SetupButtons();
        getDataFromServer();
        return root;
    }

    private void getDataFromServer(){
        try {
            if (!hms_helperClass.isNetworkConnected(context)) {
                return;
            } else {
                SharedPreferences settings = context.getApplicationContext().getSharedPreferences(hms_helperClass.MY_PREFS_NAME, MODE_PRIVATE);
                String tmpPhone = settings.getString("userInfo_phone", "0");
                tmpPhone = URLEncoder.encode(tmpPhone, "UTF-8");

                String tmpSecret = settings.getString("userInfo_secretcode", "0");
                tmpSecret = URLEncoder.encode(tmpSecret, "UTF-8");

                String mainSiteAddress = hms_helperClass.getMainSiteAddress(context.getApplicationContext());
                //START: USE GET METHOD
                //hms_helperClass.downloadJSON_GET(this, "", mainSiteAddress + "App_API/CompanyAndAppInfo_getData.php?p1=" + tmpPhone + "&p2=" + tmpSecret);
                //END: USE GET METHOD

                //START: USE POST METHOD
                final String finalTmpPhone = tmpPhone;
                final String finalTmpSecret = tmpSecret;
                HashMap<String, String> myMap = new HashMap<String, String>() {{
                    put("p1", finalTmpPhone);
                    put("p2", finalTmpSecret);
                }};
                hms_helperClass.downloadJSON_POST(this, "", mainSiteAddress + "App_API/CompanyAndAppInfo_getData.php", myMap);
                hms_helperClass.showLoadingDialog(context);
            }
        }
        catch (Exception ex){
            hms_helperClass.showException(context, ex.getMessage(), false, false);
        }
    }

    //////////////////////////////////////START: JSON Manager///////////////////////////////////////
    @Override
    public void actionJSON(String inputCommand, String json) throws Exception {
        JSONArray jsonArray = new JSONArray(json);
        JSONObject jsonObj = jsonArray.getJSONObject(0);

        Button btn = root.findViewById(R.id.aboutUs_layout_button_companyWebsite);
        btn.setText(jsonObj.getString("companyWebsite"));
        btn = root.findViewById(R.id.aboutUs_layout_button_companyPhone);
        btn.setText(jsonObj.getString("companyPhone"));
        TextView tv = root.findViewById(R.id.aboutUs_layout_textView_companyName);
        tv.setText("کلیه حقوق متعلق به شرکت " + jsonObj.getString("companyName") + " می باشد");

        Button updateBtn = root.findViewById(R.id.aboutUs_layout_button_updateApp);
        jsonObj = jsonArray.getJSONObject(1);//get App info
        final int currentVersionCode = hms_helperClass.getVersionCode(context);
        if(currentVersionCode == -1){
            btn.setEnabled(false);
            Toast.makeText(context,"خطا در دریافت کد ورژن!",Toast.LENGTH_SHORT).show();
            return;
        }

        final JSONObject finalObj = jsonObj;
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    if(currentVersionCode < finalObj.getInt("lastVersionCode")) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://tafkik-co.ir/#intro"));
                        startActivity(browserIntent);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    }
                    else {
                        Toast.makeText(context,"اپلیکیشن شما بروز می باشد.",Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception ex){
                    hms_helperClass.showException(context, ex.getMessage(), false, false);
                }
            }
        });

        String currentVersionName= hms_helperClass.getVersionName(context);
        tv = root.findViewById(R.id.aboutUs_layout_textView_versionName);
        tv.setText("نسخه: " + currentVersionName);
    }

    @Override
    public void failedJSON(){
    }

    @Override
    public Activity HMS_getActivity(){
        return this.getActivity();
    }
    ////////////////////////////////////////END: JSON Manager///////////////////////////////////////

    ///////////////////////////////////////START: UI Elements///////////////////////////////////////
    private void SetupButtons(){
        Button phoneButton = root.findViewById(R.id.aboutUs_layout_button_companyPhone);
        phoneButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Button btn = root.findViewById(R.id.aboutUs_layout_button_companyPhone);
                    intent.setData(Uri.parse("tel:" + btn.getText()));
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                }
                catch (Exception ex){
                    hms_helperClass.showException(context, ex.getMessage(), false, false);
                }
            }
        });

        Button siteButton = root.findViewById(R.id.aboutUs_layout_button_companyWebsite);
        siteButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://tafkik-co.ir/"));
                    startActivity(browserIntent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                }
                catch (Exception ex){
                    hms_helperClass.showException(context, ex.getMessage(), false, false);
                }
            }
        });

        TextView termOfUseTV= root.findViewById(R.id.aboutUs_layout_textView_termOfUse);
        termOfUseTV.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://tafkik-co.ir/#termOfUse"));
                    startActivity(browserIntent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                }
                catch (Exception ex){
                    hms_helperClass.showException(context, ex.getMessage(), false, false);
                }
            }
        });

        TextView privacyTV= root.findViewById(R.id.aboutUs_layout_textView_privacy);
        privacyTV.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://tafkik-co.ir/#privacy"));
                    startActivity(browserIntent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                }
                catch (Exception ex){
                    hms_helperClass.showException(context, ex.getMessage(), false, false);
                }
            }
        });

        TextView frequentQuestionsTV= root.findViewById(R.id.aboutUs_layout_textView_frequentQuestions);
        frequentQuestionsTV.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://tafkik-co.ir/#questions"));
                    startActivity(browserIntent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                }
                catch (Exception ex){
                    hms_helperClass.showException(context, ex.getMessage(), false, false);
                }
            }
        });
    }
    ////////////////////////////////////////END: UI Elements////////////////////////////////////////

}