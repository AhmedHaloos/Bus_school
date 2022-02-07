package com.eng.ashm.buschool.data.datamodel;

import androidx.room.Database;

@Database(entities = {CarEntity.class, TripEntity.class,
        DriverEntity.class, ParentEntity.class, StudentEntity.class}, version = 1)

public abstract class CacheDatabase {
    public abstract CarDao carDao();
}
