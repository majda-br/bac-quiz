package com.example.helloworld;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class LoginViewModel extends AndroidViewModel {

    private LoginRepository repository;
    private LiveData<List<LoginTable>> getAllData;
    private LiveData<LoginTable> getMaxScore;
    private int getSizeTable;

    public LoginViewModel(Application application) {
        super(application);

        repository = new LoginRepository(application);
        getAllData = repository.getAllData();
        /*getMaxScore = repository.getMaxScore();
        getSizeTable = repository.getSizeTable();*/


    }

    public void insert(LoginTable data) {
        repository.insertData(data);
    }

    public void updateScore(int score, String name) { repository.updateScore(score, name); }

    public LiveData<List<LoginTable>> getGetAllData() {
        return getAllData;
    }
    public LiveData<LoginTable> getMaxScore() {
        return getMaxScore;
    }
    public int getSizeTable() {
        return getSizeTable;
    }

}