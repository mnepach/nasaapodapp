package com.example.nasaapodapp.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nasaapodapp.data.local.ApodEntity;
import com.example.nasaapodapp.data.model.ApodResponse;
import com.example.nasaapodapp.data.repository.ApodRepository;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ApodViewModel extends AndroidViewModel {

    private final ApodRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<List<ApodResponse>> apodList = new MutableLiveData<>();
    private final MutableLiveData<List<ApodEntity>> favorites = new MutableLiveData<>();
    private final MutableLiveData<ApodResponse> selectedApod = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isFavorite = new MutableLiveData<>();

    public ApodViewModel(@NonNull Application application) {
        super(application);
        repository = new ApodRepository(application);
    }

    public LiveData<List<ApodResponse>> getApodList() {
        return apodList;
    }

    public LiveData<List<ApodEntity>> getFavorites() {
        return favorites;
    }

    public LiveData<ApodResponse> getSelectedApod() {
        return selectedApod;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getIsFavorite() {
        return isFavorite;
    }

    public void setSelectedApod(ApodResponse apod) {
        selectedApod.setValue(apod);
        checkIfFavorite(apod.getDate());
    }

    public void loadApodList(int count) {
        isLoading.setValue(true);
        Disposable disposable = repository.getApodList(count)
                .subscribe(
                        result -> {
                            apodList.setValue(result);
                            isLoading.setValue(false);
                        },
                        throwable -> {
                            error.setValue("Error loading NASA APOD data: " + throwable.getMessage());
                            isLoading.setValue(false);
                        }
                );
        disposables.add(disposable);
    }

    public void loadFavorites() {
        isLoading.setValue(true);
        Disposable disposable = repository.getFavorites()
                .subscribe(
                        result -> {
                            favorites.setValue(result);
                            isLoading.setValue(false);
                        },
                        throwable -> {
                            error.setValue("Error loading favorites: " + throwable.getMessage());
                            isLoading.setValue(false);
                        }
                );
        disposables.add(disposable);
    }

    public void checkIfFavorite(String date) {
        Disposable disposable = repository.isFavorite(date)
                .subscribe(
                        isFavorite::setValue,
                        throwable -> error.setValue("Error checking favorite status: " + throwable.getMessage())
                );
        disposables.add(disposable);
    }

    public void toggleFavorite() {
        ApodResponse apod = selectedApod.getValue();
        if (apod != null) {
            boolean currentIsFavorite = isFavorite.getValue() != null && isFavorite.getValue();

            Disposable disposable;
            if (currentIsFavorite) {
                disposable = repository.removeFromFavorites(apod.getDate())
                        .subscribe(
                                () -> isFavorite.postValue(false),
                                throwable -> error.postValue("Error removing from favorites: " + throwable.getMessage())
                        );
            } else {
                disposable = repository.addToFavorites(apod)
                        .subscribe(
                                () -> isFavorite.postValue(true),
                                throwable -> error.postValue("Error adding to favorites: " + throwable.getMessage())
                        );
            }
            disposables.add(disposable);
        }
    }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }
}