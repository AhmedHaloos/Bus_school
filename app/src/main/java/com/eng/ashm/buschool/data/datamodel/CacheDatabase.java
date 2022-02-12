package com.eng.ashm.buschool.data.datamodel;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Car.class,
        Driver.class, Parent.class, Student.class}, version = 1)
public abstract class CacheDatabase extends RoomDatabase {
    public abstract CarDao carDao();
}
