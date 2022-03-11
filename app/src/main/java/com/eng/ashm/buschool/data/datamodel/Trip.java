package com.eng.ashm.buschool.data.datamodel;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.eng.ashm.buschool.data.IDataModel;

import java.util.ArrayList;

@Entity
public class Trip implements IDataModel {

    public static final String COLLECTION = "TRIP";
    @PrimaryKey
    public long tripNum = 0;
    public String startTime = "";
    public String endTime = "";
    public String tripDate = "";
    public String tripState = "";
    @Ignore
    public ArrayList<String> stopPoints = new ArrayList<>();
    @Ignore
    public Car bus = new Car();
    @Ignore
    public Driver tripDriver = new Driver();
    @Ignore
    public ArrayList<Student> tripStudents = new ArrayList<>();

    @Override
    public String document(){
        if (tripNum <= 0)
            return null;
        return "/" + tripNum;
    }
    @Override
    public String collection() {
        return COLLECTION ;
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[0];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
