package com.eng.ashm.buschool.data.datamodel;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DriverDao {

    @Insert
    long addDriver(Driver driver);
    @Delete
    int deleteDriver(Driver driver);
    @Update
    int updateDrivers(Driver drivers);
    @Query("SELECT * FROM driver")
    List<Driver> getAllDrivers();
    @Query("SELECT * FROM driver WHERE name LIKE :driverName")
    List<Driver> searchDrivers(String driverName);
    @Query("SELECT * FROM driver WHERE phone = :phone AND email = :email")
    Driver getDriver(String phone, String email);
}
