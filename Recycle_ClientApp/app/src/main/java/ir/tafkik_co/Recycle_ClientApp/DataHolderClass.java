package ir.tafkik_co.Recycle_ClientApp;

import android.app.Application;

import java.util.ArrayList;

import ir.tafkik_co.Recycle_ClientApp.GarbageSelection.AddressSelection.AddressSelection_DataType;
import ir.tafkik_co.Recycle_ClientApp.GarbageSelection.GarbageSelection_ListView_DataType;

public class DataHolderClass extends Application {
    private ArrayList<GarbageSelection_ListView_DataType> GarbageSelection_ArrayList = new ArrayList<GarbageSelection_ListView_DataType>();
    private AddressSelection_DataType SelectedAddress;
    private String SelectedDate;
    private String SelectedDay;
    private  String SelectedHour;

    public ArrayList<GarbageSelection_ListView_DataType> get_GarbageSelection_ArrayList() {
        return GarbageSelection_ArrayList;
    }
    public void set_GarbageSelection_ArrayList(ArrayList<GarbageSelection_ListView_DataType> garbageSelection_ArrayList) {
        this.GarbageSelection_ArrayList = garbageSelection_ArrayList;
    }

    public void set_SelectedAddress(AddressSelection_DataType selectedAddress) {
        this.SelectedAddress = selectedAddress;
    }
    public AddressSelection_DataType get_SelectedAddress(){
        return  this.SelectedAddress;
    }
    public void setSelectedDate(String selectedDate){
        this.SelectedDate = selectedDate;
    }
    public String getSelectedDate(){
        return this.SelectedDate;
    }
    public void setSelectedDay(String selectedDay){
        this.SelectedDay = selectedDay;
    }
    public String getSelectedDay(){
        return this.SelectedDay;
    }
    public void setSelectedHour(String selectedHour){
        this.SelectedHour = selectedHour;
    }
    public String getSelectedHour(){
        return this.SelectedHour;
    }
}
