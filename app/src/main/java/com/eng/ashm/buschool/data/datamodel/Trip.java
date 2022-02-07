package com.eng.ashm.buschool.data.datamodel;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.eng.ashm.buschool.data.IFirestoreDataModel;

@Entity
public class Trip implements IFirestoreDataModel {
    @PrimaryKey
    public long tripNum;
    public String startTime;
    public String endTime;
    public String tripDate;
    public String[] stopPoints;
    public Car bus;
    public Driver tripDriver;
    public Student[] tripStudents;

    @Override
    public String document(){
        return null;
    }
    @Override
    public String collection() {
        return null;
    }
}
