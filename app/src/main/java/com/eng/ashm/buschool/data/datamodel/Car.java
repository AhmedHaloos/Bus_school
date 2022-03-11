package com.eng.ashm.buschool.data.datamodel;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.eng.ashm.buschool.data.IDataModel;

@Entity
public class Car implements IDataModel {

    public static final String COLLECTION = "Car";
    @PrimaryKey
    @NonNull
    public String carNum;
    public String kind;
    public int noOfSeats;
    public String color;
    public String model;
    public Car(){}

    private Car(Parcel in) {
        carNum = in.readString();
        kind = in.readString();
        noOfSeats = in.readInt();
        color = in.readString();
        model = in.readString();
    }

    @Override
    public String document(){
        return  "/" + carNum;
    }

    @Override
    public String collection() {
        return COLLECTION ;
    }

    public static final Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(carNum);
        dest.writeString(kind);
        dest.writeInt(noOfSeats);
        dest.writeString(color);
        dest.writeString(model);
    }
}
