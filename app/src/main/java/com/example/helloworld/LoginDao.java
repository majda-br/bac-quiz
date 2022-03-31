package com.example.helloworld;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LoginDao {

    @Insert
    void insertDetails(LoginTable data);

    @Query("UPDATE LoginDetails SET Score=:score WHERE Name =:name")
    void updateScore(int score, String name);

    @Query("SELECT id, Name, Score FROM LoginDetails ORDER BY Score DESC limit 1")
    LiveData<LoginTable> getMaxScore();

    @Query("SELECT * FROM LoginDetails")
    LiveData<List<LoginTable>> getDetails();

    @Query("SELECT COUNT(id) FROM LoginDetails")
    int getSizeTable();

    @Query("DELETE FROM LoginDetails")
    void deleteAllData();

}

