package com.example.nasaapodapp.data.repository;

import android.content.Context;

import com.example.nasaapodapp.data.local.ApodDatabase;
import com.example.nasaapodapp.data.local.ApodEntity;
import com.example.nasaapodapp.data.local.QuizEntity;
import com.example.nasaapodapp.data.model.ApodResponse;
import com.example.nasaapodapp.data.remote.NasaApiService;
import com.example.nasaapodapp.data.remote.RetrofitClient;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ApodRepository {
    private static final String API_KEY = "DEMO_KEY"; // Replace with your NASA API key
    private final NasaApiService apiService;
    private final ApodDatabase database;

    public ApodRepository(Context context) {
        apiService = RetrofitClient.getClient().create(NasaApiService.class);
        database = ApodDatabase.getInstance(context);
    }

    public Single<ApodResponse> getApod() {
        return apiService.getApod(API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<ApodResponse>> getApodList(int count) {
        return apiService.getApodList(API_KEY, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<ApodEntity>> getFavorites() {
        return database.apodDao().getAllFavorites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Boolean> isFavorite(String date) {
        return database.apodDao().isFavorite(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable addToFavorites(ApodResponse apod) {
        ApodEntity entity = new ApodEntity(
                apod.getDate(),
                apod.getTitle(),
                apod.getExplanation(),
                apod.getUrl(),
                apod.getHdUrl(),
                apod.getMediaType(),
                apod.getCopyright()
        );
        return database.apodDao().insertFavorite(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable removeFromFavorites(String date) {
        return database.apodDao().deleteFavorite(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    // Quiz related methods
    public Single<QuizEntity> getQuizForApod(String date) {
        return database.quizDao().getQuizForApod(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Boolean> hasQuiz(String date) {
        return database.quizDao().hasQuiz(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable createQuiz(String apodDate, String question, String correctAnswer,
                                  String wrongAnswer1, String wrongAnswer2, String wrongAnswer3) {
        QuizEntity quizEntity = new QuizEntity(
                apodDate, question, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3
        );
        return database.quizDao().insertQuiz(quizEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deleteQuiz(String apodDate) {
        return database.quizDao().deleteQuiz(apodDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}