package com.example.helloworld;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {LoginTable.class}, version = 3, exportSchema = false)
public abstract class LoginDatabase extends RoomDatabase {

    public abstract LoginDao loginDoa();

    private static LoginDatabase INSTANCE;

    public static LoginDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {

            synchronized (LoginDatabase.class) {

                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(
                            context, LoginDatabase.class, "LOGIN_DATABASE")
                            .fallbackToDestructiveMigration()
                            .build();

                }

            }

        }

        return INSTANCE;

    }

}

