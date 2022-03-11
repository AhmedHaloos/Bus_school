package com.eng.ashm.buschool.data.datamodel;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.eng.ashm.buschool.data.datamodel.Car;

import java.security.PublicKey;
import java.util.List;

@Dao
public interface CarDao {
    @Insert
    long addCar(Car car);
    @Delete
    int deleteCar(Car car);
    @Update
    int updateCars(Car cars);
    @Query("SELECT * FROM car")
    List<Car> getAllCars();
    @Query("SELECT * FROM car WHERE carNum LIKE :num")
    List<Car> searchCars(int num);
    @Query("SELECT * FROM car WHERE carNum = :carNum")
    Car getCar(String carNum);

}
