package com.eng.ashm.buschool.data.datamodel;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Car.class, Driver.class, Parent.class, Student.class, Trip.class}, version = 1)
public abstract class CacheDataSource extends RoomDatabase {
    public abstract CarDao carDao();
    public abstract DriverDao driverDao();
    public abstract ParentDao parentDao();
    public abstract StudentDao studentDao();
    public abstract TripDao tripDao();
}
