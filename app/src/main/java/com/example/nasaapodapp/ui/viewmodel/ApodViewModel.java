package com.example.nasaapodapp.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nasaapodapp.data.local.ApodEntity;
import com.example.nasaapodapp.data.local.QuizEntity;
import com.example.nasaapodapp.data.model.ApodResponse;
import com.example.nasaapodapp.data.repository.ApodRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
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

    // Quiz related LiveData
    private final MutableLiveData<QuizEntity> quizData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> hasQuiz = new MutableLiveData<>();

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

    // Quiz accessors
    public LiveData<QuizEntity> getQuizData() {
        return quizData;
    }

    public LiveData<Boolean> getHasQuiz() {
        return hasQuiz;
    }

    public void setSelectedApod(ApodResponse apod) {
        selectedApod.setValue(apod);
        checkIfFavorite(apod.getDate());
        checkIfHasQuiz(apod.getDate());
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

    // Quiz methods
    public void loadQuizForApod(String date) {
        Disposable disposable = repository.getQuizForApod(date)
                .subscribe(
                        quizData::setValue,
                        throwable -> error.postValue("Error loading quiz: " + throwable.getMessage())
                );
        disposables.add(disposable);
    }

    public void checkIfHasQuiz(String date) {
        Disposable disposable = repository.hasQuiz(date)
                .subscribe(
                        hasQuiz::setValue,
                        throwable -> error.postValue("Error checking quiz status: " + throwable.getMessage())
                );
        disposables.add(disposable);
    }

    public void createOrUpdateQuiz(String question, String correctAnswer,
                                   String wrongAnswer1, String wrongAnswer2, String wrongAnswer3) {
        ApodResponse apod = selectedApod.getValue();
        if (apod != null) {
            Disposable disposable = repository.createQuiz(
                            apod.getDate(), question, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3)
                    .subscribe(
                            () -> {
                                hasQuiz.postValue(true);
                                loadQuizForApod(apod.getDate());
                            },
                            throwable -> error.postValue("Error creating quiz: " + throwable.getMessage())
                    );
            disposables.add(disposable);
        }
    }

    public void deleteQuiz() {
        ApodResponse apod = selectedApod.getValue();
        if (apod != null) {
            Disposable disposable = repository.deleteQuiz(apod.getDate())
                    .subscribe(
                            () -> {
                                hasQuiz.postValue(false);
                                quizData.postValue(null);
                            },
                            throwable -> error.postValue("Error deleting quiz: " + throwable.getMessage())
                    );
            disposables.add(disposable);
        }
    }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }
}