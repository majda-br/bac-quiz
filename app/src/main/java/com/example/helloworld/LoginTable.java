package com.example.helloworld;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "LoginDetails")
public class LoginTable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    private int id;

    @ColumnInfo(name = "Name")
    private String Name;

    @ColumnInfo(name = "Password")
    private String Password;

    @ColumnInfo(name = "Score")
    private int Score;

    public LoginTable(int score, String name){
        this.Score = score;
        this.Name = name;
    }

    public LoginTable(){

    }

    // Creating Setter and Getter

    public int getId() {
        return id;
    }

    public void setId(int id) {        //addId
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {         //addName
        this.Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {      ///addPassword
        this.Password = password;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {         //addScore and updateScore
        this.Score = score;
    }
}

