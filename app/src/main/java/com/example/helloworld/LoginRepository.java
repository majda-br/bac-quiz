package com.example.helloworld;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class LoginRepository {
    private LoginDao loginDao;
    private LiveData<List<LoginTable>> allData;
    private LiveData<LoginTable> maxScore;
    private int sizeTable;
    //private LiveData<List<LoginTable>> nameMaxScore;

    public LoginRepository(Application application) {

        LoginDatabase db = LoginDatabase.getDatabase(application);
        loginDao = db.loginDoa();
        allData = loginDao.getDetails();
        maxScore = loginDao.getMaxScore();
        sizeTable = loginDao.getSizeTable();
        //nameMaxScore = loginDao.getNameMaxScore();

    }

    public void updateScore(int score, String name) {
        LoginTable loginTable = new LoginTable(score, name);
        new ScoreUpdate(loginDao).execute(loginTable);
    }

    public static class ScoreUpdate extends AsyncTask<LoginTable, Void, Void> {

        private LoginDao loginDao;

        private ScoreUpdate(LoginDao loginDao) {

            this.loginDao = loginDao;
        }

        @Override
        protected Void doInBackground(LoginTable... data) {
            loginDao.updateScore(data[0].getScore(), data[0].getName());
            return null;
        }
    }
    public LiveData<List<LoginTable>> getAllData() {
        return allData;
    }

    public LiveData<LoginTable> getMaxScore() { return maxScore; }

    //public LiveData<List<LoginTable>> getNameMaxScore() { return nameMaxScore; }

    public int getSizeTable() { return sizeTable; }

    public void insertData(LoginTable data) {
        new LoginInsertion(loginDao).execute(data);
    }

    private static class LoginInsertion extends AsyncTask<LoginTable, Void, Void> {

        private LoginDao loginDao;

        private LoginInsertion(LoginDao loginDao) {

            this.loginDao = loginDao;

        }

        @Override
        protected Void doInBackground(LoginTable... data) {

            //loginDao.deleteAllData();

            loginDao.insertDetails(data[0]);
            return null;

        }

    }

}
