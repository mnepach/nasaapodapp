package com.example.nasaapodapp.ui.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nasaapodapp.data.local.ApodEntity;
import com.example.nasaapodapp.data.model.ApodResponse;
import com.example.nasaapodapp.data.model.ApodResponse.Quiz;
import com.example.nasaapodapp.data.repository.ApodRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ApodViewModel extends AndroidViewModel {

    private static final String TAG = "ApodViewModel";
    private final ApodRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<List<ApodResponse>> apodList = new MutableLiveData<>();
    private final MutableLiveData<List<ApodEntity>> favorites = new MutableLiveData<>();
    private final MutableLiveData<ApodResponse> selectedApod = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isFavorite = new MutableLiveData<>();

    // Quiz related LiveData
    private final MutableLiveData<Quiz> currentQuiz = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentQuizIndex = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> correctAnswers = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> totalQuestions = new MutableLiveData<>(3);
    private final MutableLiveData<List<String>> shuffledAnswers = new MutableLiveData<>();
    private final MutableLiveData<Boolean> quizCompleted = new MutableLiveData<>(false);

    public ApodViewModel(@NonNull Application application) {
        super(application);
        repository = new ApodRepository(application);
        currentQuizIndex.setValue(0);
        correctAnswers.setValue(0);
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
    public LiveData<Quiz> getCurrentQuiz() {
        return currentQuiz;
    }

    public LiveData<Integer> getCurrentQuizIndex() {
        return currentQuizIndex;
    }

    public LiveData<Integer> getCorrectAnswers() {
        return correctAnswers;
    }

    public LiveData<Integer> getTotalQuestions() {
        return totalQuestions;
    }

    public LiveData<List<String>> getShuffledAnswers() {
        return shuffledAnswers;
    }

    public LiveData<Boolean> getQuizCompleted() {
        return quizCompleted;
    }

    public void setSelectedApod(ApodResponse apod) {
        Log.d(TAG, "Setting selectedApod: " + (apod != null ? apod.getTitle() : "null"));
        selectedApod.setValue(apod);
        if (apod != null) {
            checkIfFavorite(apod.getDate());
            resetQuiz();
        }
    }

    public void loadApodList(int count) {
        Log.d(TAG, "Loading preloaded APOD list");
        isLoading.setValue(true);
        Disposable disposable = repository.getApodList(count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            Log.d(TAG, "APOD list loaded, size: " + result.size());
                            apodList.setValue(result);
                            isLoading.setValue(false);
                        },
                        throwable -> {
                            Log.e(TAG, "Error loading APOD list: " + throwable.getMessage());
                            error.setValue("Error loading NASA APOD data: " + throwable.getMessage());
                            isLoading.setValue(false);
                        }
                );
        disposables.add(disposable);
    }

    public void loadFavorites() {
        Log.d(TAG, "Loading favorites");
        isLoading.setValue(true);
        Disposable disposable = repository.getFavorites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            Log.d(TAG, "Favorites loaded, size: " + result.size());
                            favorites.setValue(result);
                            isLoading.setValue(false);
                        },
                        throwable -> {
                            Log.e(TAG, "Error loading favorites: " + throwable.getMessage());
                            error.setValue("Error loading favorites: " + throwable.getMessage());
                            isLoading.setValue(false);
                        }
                );
        disposables.add(disposable);
    }

    public void checkIfFavorite(String date) {
        Log.d(TAG, "Checking favorite status for date: " + date);
        Disposable disposable = repository.isFavorite(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            Log.d(TAG, "Favorite status: " + result);
                            isFavorite.setValue(result);
                        },
                        throwable -> {
                            Log.e(TAG, "Error checking favorite status: " + throwable.getMessage());
                            error.setValue("Error checking favorite status: " + throwable.getMessage());
                        }
                );
        disposables.add(disposable);
    }

    public void toggleFavorite() {
        ApodResponse apod = selectedApod.getValue();
        if (apod == null) {
            Log.e(TAG, "Error: selectedApod is null when toggling favorite");
            error.setValue("Image not selected for adding to favorites");
            return;
        }

        boolean currentIsFavorite = isFavorite.getValue() != null && isFavorite.getValue();
        Log.d(TAG, "Toggling favorite for: " + apod.getTitle() + ", current state: " + currentIsFavorite);

        Disposable disposable;
        if (currentIsFavorite) {
            disposable = repository.removeFromFavorites(apod.getDate())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> {
                                Log.d(TAG, "Removed from favorites: " + apod.getDate());
                                isFavorite.postValue(false);
                            },
                            throwable -> {
                                Log.e(TAG, "Error removing from favorites: " + throwable.getMessage());
                                error.postValue("Error removing from favorites: " + throwable.getMessage());
                            }
                    );
        } else {
            disposable = repository.addToFavorites(apod)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> {
                                Log.d(TAG, "Added to favorites: " + apod.getDate());
                                isFavorite.postValue(true);
                            },
                            throwable -> {
                                Log.e(TAG, "Error adding to favorites: " + throwable.getMessage());
                                error.postValue("Error adding to favorites: " + throwable.getMessage());
                            }
                    );
        }
        disposables.add(disposable);
    }

    // Quiz methods
    public void loadQuiz() {
        ApodResponse apod = selectedApod.getValue();
        if (apod == null || apod.getQuizzes() == null || apod.getQuizzes().isEmpty()) {
            Log.e(TAG, "Error: No quiz available for this APOD");
            error.setValue("No quiz available for this image");
            return;
        }

        int index = currentQuizIndex.getValue() != null ? currentQuizIndex.getValue() : 0;
        if (index < apod.getQuizzes().size()) {
            Quiz quiz = apod.getQuizzes().get(index);
            currentQuiz.setValue(quiz);

            // Shuffle answers
            List<String> allAnswers = new ArrayList<>();
            allAnswers.add(quiz.getCorrectAnswer());
            allAnswers.addAll(quiz.getWrongAnswers());
            Collections.shuffle(allAnswers);
            shuffledAnswers.setValue(allAnswers);

            Log.d(TAG, "Loaded quiz question " + (index + 1) + ": " + quiz.getQuestion());
        } else {
            Log.d(TAG, "Quiz completed");
            quizCompleted.setValue(true);
        }
    }

    public void checkAnswer(String selectedAnswer) {
        Quiz quiz = currentQuiz.getValue();
        if (quiz == null) {
            return;
        }

        if (selectedAnswer.equals(quiz.getCorrectAnswer())) {
            int correct = correctAnswers.getValue() != null ? correctAnswers.getValue() : 0;
            correctAnswers.setValue(correct + 1);
            Log.d(TAG, "Correct answer! Total correct: " + (correct + 1));
        } else {
            Log.d(TAG, "Incorrect answer selected: " + selectedAnswer);
        }

        // Move to next question
        int currentIndex = currentQuizIndex.getValue() != null ? currentQuizIndex.getValue() : 0;
        currentQuizIndex.setValue(currentIndex + 1);

        // Load next question or complete quiz
        loadQuiz();
    }

    public void resetQuiz() {
        currentQuizIndex.setValue(0);
        correctAnswers.setValue(0);
        quizCompleted.setValue(false);
        loadQuiz();
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "Clearing disposables");
        disposables.clear();
        super.onCleared();
    }
}