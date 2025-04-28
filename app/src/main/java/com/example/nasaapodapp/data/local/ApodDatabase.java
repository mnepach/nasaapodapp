package com.example.nasaapodapp.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ApodEntity.class}, version = 1, exportSchema = false)
public abstract class ApodDatabase extends RoomDatabase {

    private static ApodDatabase instance;

    public abstract ApodDao apodDao();

    public static synchronized ApodDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            ApodDatabase.class,
                            "apod_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}