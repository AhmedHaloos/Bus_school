package com.eng.ashm.buschool.data.datamodel;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StudentDao {

    @Insert
    long addStudent(Student student);
    @Delete
    int deleteStudent(Student student);
    @Update
    int updateStudents(Student... students);
    @Query("SELECT * FROM student")
    List<Student> getAllStudents();
    @Query("SELECT * FROM student WHERE name LIKE '%'||:studentName||'%'")
    List<Student> searchStudent(String studentName);
    @Query("SELECT * FROM student WHERE phone = :phone AND email = :email")
    Student getStudent(String phone, String email);
}
