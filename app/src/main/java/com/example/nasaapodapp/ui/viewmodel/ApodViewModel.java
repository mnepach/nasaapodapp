package com.example.nasaapodapp.ui.viewmodel;

import android.app.Application;
import android.util.Log;

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
        Log.d(TAG, "Установка selectedApod: " + (apod != null ? apod.getTitle() : "null"));
        selectedApod.setValue(apod);
        if (apod != null) {
            checkIfFavorite(apod.getDate());
            checkIfHasQuiz(apod.getDate());
        }
    }

    public void loadApodList(int count) {
        Log.d(TAG, "Загрузка списка APOD, количество: " + count);
        isLoading.setValue(true);
        Disposable disposable = repository.getApodList(count)
                .subscribe(
                        result -> {
                            Log.d(TAG, "Список APOD загружен, размер: " + result.size());
                            apodList.setValue(result);
                            isLoading.setValue(false);
                        },
                        throwable -> {
                            Log.e(TAG, "Ошибка загрузки списка APOD: " + throwable.getMessage());
                            error.setValue("Ошибка загрузки данных NASA APOD: " + throwable.getMessage());
                            isLoading.setValue(false);
                        }
                );
        disposables.add(disposable);
    }

    public void loadFavorites() {
        Log.d(TAG, "Загрузка избранного");
        isLoading.setValue(true);
        Disposable disposable = repository.getFavorites()
                .subscribe(
                        result -> {
                            Log.d(TAG, "Избранное загружено, размер: " + result.size());
                            favorites.setValue(result);
                            isLoading.setValue(false);
                        },
                        throwable -> {
                            Log.e(TAG, "Ошибка загрузки избранного: " + throwable.getMessage());
                            error.setValue("Ошибка загрузки избранного: " + throwable.getMessage());
                            isLoading.setValue(false);
                        }
                );
        disposables.add(disposable);
    }

    public void checkIfFavorite(String date) {
        Log.d(TAG, "Проверка статуса избранного для даты: " + date);
        Disposable disposable = repository.isFavorite(date)
                .subscribe(
                        result -> {
                            Log.d(TAG, "Статус избранного: " + result);
                            isFavorite.setValue(result);
                        },
                        throwable -> {
                            Log.e(TAG, "Ошибка проверки статуса избранного: " + throwable.getMessage());
                            error.setValue("Ошибка проверки статуса избранного: " + throwable.getMessage());
                        }
                );
        disposables.add(disposable);
    }

    public void toggleFavorite() {
        ApodResponse apod = selectedApod.getValue();
        if (apod == null) {
            Log.e(TAG, "Ошибка: selectedApod равен null при переключении избранного");
            error.setValue("Изображение не выбрано для добавления в избранное");
            return;
        }

        boolean currentIsFavorite = isFavorite.getValue() != null && isFavorite.getValue();
        Log.d(TAG, "Переключение избранного для: " + apod.getTitle() + ", текущее состояние: " + currentIsFavorite);

        Disposable disposable;
        if (currentIsFavorite) {
            disposable = repository.removeFromFavorites(apod.getDate())
                    .subscribe(
                            () -> {
                                Log.d(TAG, "Удалено из избранного: " + apod.getDate());
                                isFavorite.postValue(false);
                            },
                            throwable -> {
                                Log.e(TAG, "Ошибка удаления из избранного: " + throwable.getMessage());
                                error.postValue("Ошибка удаления из избранного: " + throwable.getMessage());
                            }
                    );
        } else {
            disposable = repository.addToFavorites(apod)
                    .subscribe(
                            () -> {
                                Log.d(TAG, "Добавлено в избранное: " + apod.getDate());
                                isFavorite.postValue(true);
                            },
                            throwable -> {
                                Log.e(TAG, "Ошибка добавления в избранное: " + throwable.getMessage());
                                error.postValue("Ошибка добавления в избранное: " + throwable.getMessage());
                            }
                    );
        }
        disposables.add(disposable);
    }

    // Quiz methods
    public void loadQuizForApod(String date) {
        Log.d(TAG, "Загрузка теста для даты: " + date);
        Disposable disposable = repository.getQuizForApod(date)
                .subscribe(
                        quiz -> {
                            Log.d(TAG, "Тест загружен: " + (quiz != null ? quiz.getQuestion() : "null"));
                            quizData.setValue(quiz);
                        },
                        throwable -> {
                            Log.e(TAG, "Ошибка загрузки теста: " + throwable.getMessage());
                            error.setValue("Ошибка загрузки теста: " + throwable.getMessage());
                        }
                );
        disposables.add(disposable);
    }

    public void checkIfHasQuiz(String date) {
        Log.d(TAG, "Проверка наличия теста для даты: " + date);
        Disposable disposable = repository.hasQuiz(date)
                .subscribe(
                        result -> {
                            Log.d(TAG, "Наличие теста: " + result);
                            hasQuiz.setValue(result);
                        },
                        throwable -> {
                            Log.e(TAG, "Ошибка проверки наличия теста: " + throwable.getMessage());
                            error.setValue("Ошибка проверки наличия теста: " + throwable.getMessage());
                        }
                );
        disposables.add(disposable);
    }

    public void createOrUpdateQuiz(String question, String correctAnswer,
                                   String wrongAnswer1, String wrongAnswer2, String wrongAnswer3) {
        ApodResponse apod = selectedApod.getValue();
        if (apod == null) {
            Log.e(TAG, "Ошибка: selectedApod равен null при создании теста");
            error.setValue("Изображение не выбрано для создания теста");
            return;
        }

        Log.d(TAG, "Создание/обновление теста для: " + apod.getDate() + ", вопрос: " + question);
        Disposable disposable = repository.createQuiz(
                        apod.getDate(), question, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3)
                .subscribe(
                        () -> {
                            Log.d(TAG, "Тест успешно создан/обновлён для: " + apod.getDate());
                            hasQuiz.postValue(true);
                            checkIfHasQuiz(apod.getDate()); // Явная проверка наличия теста
                            loadQuizForApod(apod.getDate());
                        },
                        throwable -> {
                            Log.e(TAG, "Ошибка создания теста: " + throwable.getMessage(), throwable);
                            error.setValue("Ошибка создания теста: " + throwable.getMessage());
                        }
                );
        disposables.add(disposable);
    }

    public void deleteQuiz() {
        ApodResponse apod = selectedApod.getValue();
        if (apod == null) {
            Log.e(TAG, "Ошибка: selectedApod равен null при удалении теста");
            error.setValue("Изображение не выбрано для удаления теста");
            return;
        }

        Log.d(TAG, "Удаление теста для: " + apod.getDate());
        Disposable disposable = repository.deleteQuiz(apod.getDate())
                .subscribe(
                        () -> {
                            Log.d(TAG, "Тест удалён для: " + apod.getDate());
                            hasQuiz.postValue(false);
                            quizData.postValue(null);
                        },
                        throwable -> {
                            Log.e(TAG, "Ошибка удаления теста: " + throwable.getMessage());
                            error.setValue("Ошибка удаления теста: " + throwable.getMessage());
                        }
                );
        disposables.add(disposable);
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "Очистка disposables");
        disposables.clear();
        super.onCleared();
    }
}