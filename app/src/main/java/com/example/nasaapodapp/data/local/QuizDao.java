package com.example.nasaapodapp.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface QuizDao {
    @Query("SELECT * FROM quizzes WHERE apodDate = :apodDate LIMIT 1")
    Single<QuizEntity> getQuizForApod(String apodDate);

    @Query("SELECT EXISTS(SELECT 1 FROM quizzes WHERE apodDate = :apodDate)")
    Single<Boolean> hasQuiz(String apodDate);

    @Insert
    Completable insertQuiz(QuizEntity quizEntity);

    @Query("DELETE FROM quizzes WHERE apodDate = :apodDate")
    Completable deleteQuiz(String apodDate);
}