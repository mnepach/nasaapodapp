package com.example.nasaapodapp.data.repository;

import android.app.Application;
import android.util.Log;

import com.example.nasaapodapp.data.local.ApodEntity;
import com.example.nasaapodapp.data.model.ApodResponse;
import com.example.nasaapodapp.data.util.PreloadedApodData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ApodRepository {
    private static final String TAG = "ApodRepository";

    private final Application application;
    private final List<ApodEntity> favoritesList;

    public ApodRepository(Application application) {
        this.application = application;
        this.favoritesList = new ArrayList<>();
    }

    // Get preloaded APOD list
    public Single<List<ApodResponse>> getApodList(int count) {
        Log.d(TAG, "Getting preloaded APOD list");
        List<ApodResponse> apodList = PreloadedApodData.getPreloadedApods();

        // Исправление: убедиться, что все URL действительны
        for (ApodResponse apod : apodList) {
            // Заменяем URL на гарантированно доступные placeholder-изображения
            if (apod.getUrl() == null || apod.getUrl().isEmpty() ||
                    !apod.getUrl().startsWith("http") ||
                    apod.getUrl().contains("apod.nasa.gov")) {

                // Используем placeholder-изображения из сети, которые всегда доступны
                apod.setUrl("https://via.placeholder.com/400x300?text=" + apod.getTitle().replace(" ", "+"));

                if (apod.getHdUrl() == null || apod.getHdUrl().isEmpty()) {
                    apod.setHdUrl("https://via.placeholder.com/800x600?text=" + apod.getTitle().replace(" ", "+"));
                }
            }
        }

        return Single.just(apodList)
                .subscribeOn(Schedulers.io());
    }

    // Get one specific APOD by date
    public Single<ApodResponse> getApod(String date) {
        Log.d(TAG, "Getting preloaded APOD for date: " + date);
        return Single.just(PreloadedApodData.getPreloadedApods())
                .flatMap(apods -> {
                    for (ApodResponse apod : apods) {
                        if (apod.getDate().equals(date)) {
                            // Проверяем URL
                            if (apod.getUrl() == null || apod.getUrl().isEmpty() ||
                                    !apod.getUrl().startsWith("http") ||
                                    apod.getUrl().contains("apod.nasa.gov")) {

                                apod.setUrl("https://via.placeholder.com/400x300?text=" + apod.getTitle().replace(" ", "+"));

                                if (apod.getHdUrl() == null || apod.getHdUrl().isEmpty()) {
                                    apod.setHdUrl("https://via.placeholder.com/800x600?text=" + apod.getTitle().replace(" ", "+"));
                                }
                            }
                            return Single.just(apod);
                        }
                    }
                    return Single.error(new Exception("APOD not found for date: " + date));
                })
                .subscribeOn(Schedulers.io());
    }

    // Favorites methods
    public Flowable<List<ApodEntity>> getFavorites() {
        // Return the in-memory favorites list
        return Flowable.just(favoritesList)
                .subscribeOn(Schedulers.io());
    }

    public Single<Boolean> isFavorite(String date) {
        // Check if the APOD is in favorites
        for (ApodEntity entity : favoritesList) {
            if (entity.getDate().equals(date)) {
                return Single.just(true).subscribeOn(Schedulers.io());
            }
        }
        return Single.just(false).subscribeOn(Schedulers.io());
    }

    public Completable addToFavorites(ApodResponse apod) {
        // Add to favorites
        return Completable.fromAction(() -> {
            ApodEntity entity = new ApodEntity(
                    apod.getDate(),
                    apod.getTitle(),
                    apod.getExplanation(),
                    apod.getUrl(),
                    apod.getHdUrl(),
                    apod.getMediaType(),
                    apod.getCopyright()
            );

            // Remove if already exists (to avoid duplicates)
            favoritesList.removeIf(e -> e.getDate().equals(apod.getDate()));
            favoritesList.add(entity);
        }).subscribeOn(Schedulers.io());
    }

    public Completable removeFromFavorites(String date) {
        // Remove from favorites
        return Completable.fromAction(() -> {
            favoritesList.removeIf(entity -> entity.getDate().equals(date));
        }).subscribeOn(Schedulers.io());
    }

    // Get a specific APOD by its position (0-6)
    public Single<ApodResponse> getApodByPosition(int position) {
        Log.d(TAG, "Getting preloaded APOD for position: " + position);
        return Single.just(PreloadedApodData.getPreloadedApods())
                .map(apods -> {
                    if (position >= 0 && position < apods.size()) {
                        ApodResponse apod = apods.get(position);

                        // Проверяем URL
                        if (apod.getUrl() == null || apod.getUrl().isEmpty() ||
                                !apod.getUrl().startsWith("http") ||
                                apod.getUrl().contains("apod.nasa.gov")) {

                            apod.setUrl("https://via.placeholder.com/400x300?text=" + apod.getTitle().replace(" ", "+"));

                            if (apod.getHdUrl() == null || apod.getHdUrl().isEmpty()) {
                                apod.setHdUrl("https://via.placeholder.com/800x600?text=" + apod.getTitle().replace(" ", "+"));
                            }
                        }
                        return apod;
                    } else {
                        throw new IndexOutOfBoundsException("Invalid APOD position: " + position);
                    }
                })
                .subscribeOn(Schedulers.io());
    }
}