package com.eng.ashm.buschool.data.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.eng.ashm.buschool.data.IDataModel;

import java.util.ArrayList;

@Entity
public class Trip implements IDataModel {

    public static final String COLLECTION = "TRIP";
    public static final String ACTIVE_TRIP = "not completed";
    public static final String INACTIVE_TRIP = "completed";

    @PrimaryKey
    public long tripNum = 0;
    public String startTime = "";
    public String endTime = "";
    public String tripDate = "";
    public String tripState = "";
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
            Trip trip = new Trip();
            //long
            trip.tripNum = source.readLong();
            //string
            trip.startTime = source.readString();
            trip.endTime = source.readString();
            trip.tripDate = source.readString();
            trip.tripState = source.readString();
            //parcelable
            trip.bus = source.readParcelable(Car.class.getClassLoader());
            trip.tripDriver = source.readParcelable(Driver.class.getClassLoader());
            //parcelable array
            Parcelable students[] = source.readParcelableArray(Student.class.getClassLoader());
            for (Parcelable student :students ){
                trip.tripStudents.add((Student) student);
            }
            return trip;
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
        dest.writeLong(tripNum);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(tripDate);
        dest.writeString(tripState);
        dest.writeParcelable(bus, 0);
        dest.writeParcelable(tripDriver, 0);
        Student[] students = new Student[tripStudents.size()];
        for (int i = 0 ;i< tripStudents.size() ;i++ ) {
            students[i] = tripStudents.get(i);
        }
        dest.writeParcelableArray(students, 0);
    }
}
