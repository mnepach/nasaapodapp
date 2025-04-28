package com.example.nasaapodapp.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface ApodDao {
    @Query("SELECT * FROM favorites")
    Single<List<ApodEntity>> getAllFavorites();

    @Query("SELECT * FROM favorites WHERE date = :date")
    Single<ApodEntity> getFavoriteByDate(String date);

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE date = :date)")
    Single<Boolean> isFavorite(String date);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertFavorite(ApodEntity apodEntity);

    @Query("DELETE FROM favorites WHERE date = :date")
    Completable deleteFavorite(String date);
}