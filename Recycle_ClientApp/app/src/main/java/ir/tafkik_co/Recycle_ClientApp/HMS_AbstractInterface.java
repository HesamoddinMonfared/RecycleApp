package ir.tafkik_co.Recycle_ClientApp;


import android.app.Activity;

public interface HMS_AbstractInterface {

    void actionJSON(String inputCommand, String json) throws Exception;
    void failedJSON();
    Activity HMS_getActivity();
}
