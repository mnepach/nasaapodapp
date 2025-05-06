package com.example.nasaapodapp.data.repository;

import android.app.Application;
import android.util.Log;

import com.example.nasaapodapp.data.local.ApodEntity;
import com.example.nasaapodapp.data.model.ApodResponse;
import com.example.nasaapodapp.data.util.PreloadedApodData;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ApodRepository {
    private static final String TAG = "ApodRepository";

    private final Application application;

    public ApodRepository(Application application) {
        this.application = application;
    }

    // Get preloaded APOD list
    public Single<List<ApodResponse>> getApodList(int count) {
        Log.d(TAG, "Getting preloaded APOD list");
        return Single.just(PreloadedApodData.getPreloadedApods())
                .subscribeOn(Schedulers.io());
    }

    // Get one specific APOD by date
    public Single<ApodResponse> getApod(String date) {
        Log.d(TAG, "Getting preloaded APOD for date: " + date);
        return Single.just(PreloadedApodData.getPreloadedApods())
                .flatMap(apods -> {
                    for (ApodResponse apod : apods) {
                        if (apod.getDate().equals(date)) {
                            return Single.just(apod);
                        }
                    }
                    return Single.error(new Exception("APOD not found for date: " + date));
                })
                .subscribeOn(Schedulers.io());
    }

    // Favorites methods
    public Flowable<List<ApodEntity>> getFavorites() {
        // This would normally come from a Room database
        // For this implementation, we'll return an empty list
        return Flowable.just(List.of())
                .subscribeOn(Schedulers.io());
    }

    public Single<Boolean> isFavorite(String date) {
        // Simple stub
        return Single.just(false)
                .subscribeOn(Schedulers.io());
    }

    public Completable addToFavorites(ApodResponse apod) {
        // Stub for adding to favorites
        return Completable.complete()
                .subscribeOn(Schedulers.io());
    }

    public Completable removeFromFavorites(String date) {
        // Stub for removing from favorites
        return Completable.complete()
                .subscribeOn(Schedulers.io());
    }

    // Get a specific APOD by its position (0-6)
    public Single<ApodResponse> getApodByPosition(int position) {
        Log.d(TAG, "Getting preloaded APOD for position: " + position);
        return Single.just(PreloadedApodData.getPreloadedApods())
                .map(apods -> {
                    if (position >= 0 && position < apods.size()) {
                        return apods.get(position);
                    } else {
                        throw new IndexOutOfBoundsException("Invalid APOD position: " + position);
                    }
                })
                .subscribeOn(Schedulers.io());
    }
}