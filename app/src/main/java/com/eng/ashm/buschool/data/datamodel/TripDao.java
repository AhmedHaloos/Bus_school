package com.eng.ashm.buschool.data.datamodel;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TripDao {

    @Insert
    long addTrip(Trip trip);
    @Delete
    int deleteTrip(Trip trip);
    @Update
    int updateTrip(Trip trip);
    @Query("SELECT * FROM trip")
    List<Trip> getAllTrips();
    @Query("SELECT * FROM trip WHERE tripNum LIKE :num")
    List<Trip> searchTrip(Integer num);
    @Query("SELECT * FROM trip WHERE tripNum = :tripNum")
    Trip getTrip(long tripNum);
}
