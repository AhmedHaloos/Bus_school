package com.eng.ashm.buschool.data.datamodel;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.eng.ashm.buschool.data.datamodel.Car;

import java.util.List;

@Dao
public interface CarDao {
    @Insert
    void addCar(Car car);
    @Delete
    void deleteCar(Car car);

}
