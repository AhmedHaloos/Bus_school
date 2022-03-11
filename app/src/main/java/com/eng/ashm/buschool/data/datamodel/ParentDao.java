package com.eng.ashm.buschool.data.datamodel;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ParentDao {
    @Insert
    long addParent(Parent parent);
    @Delete
    int deleteParent(Parent parent);
    @Update
    int updateParent(Parent parents);

    @Query("SELECT * FROM parent")
    List<Parent> getAllParents();
    @Query("SELECT * FROM parent WHERE name LIKE :parentName")
    List<Parent> searchParents(String parentName);
    @Query("SELECT * FROM parent WHERE phone  = :phone AND email = :email")
    Parent getParent(String phone, String email);
}
