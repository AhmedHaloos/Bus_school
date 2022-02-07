package com.eng.ashm.buschool.data.datamodel;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.eng.ashm.buschool.data.IFirestoreDataModel;
@Entity
public class Car implements IFirestoreDataModel {
    @PrimaryKey
    public String carNum;
    public String kind;
    public int noOfSeats;
    public int color;
    public String model;
    public Car(){}

    @Override
    public String document(){
        return null;
    }

    @Override
    public String collection() {
        return null;
    }
}
