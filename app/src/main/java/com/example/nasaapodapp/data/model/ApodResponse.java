package com.example.nasaapodapp.data.model;

import java.util.Arrays;
import java.util.List;

public class ApodResponse {
    private String date;
    private String explanation;
    private String title;
    private String url;
    private String copyright;
    private boolean isPreloaded;

    // Fields for pre-loaded quizzes
    private List<Quiz> quizzes;

    public ApodResponse() {
        // Default constructor for API responses
    }

    // Constructor for pre-loaded APODs with quizzes
    public ApodResponse(String date, String title, String explanation, String url,
                        String copyright, List<Quiz> quizzes) {
        this.date = date;
        this.title = title;
        this.explanation = explanation;
        this.url = url;
        this.copyright = copyright;
        this.quizzes = quizzes;
        this.isPreloaded = true;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public boolean isPreloaded() {
        return isPreloaded;
    }

    public void setPreloaded(boolean preloaded) {
        isPreloaded = preloaded;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    // Inner class for Quiz questions
    public static class Quiz {
        private String question;
        private String correctAnswer;
        private List<String> wrongAnswers;

        public Quiz(String question, String correctAnswer, String... wrongAnswers) {
            this.question = question;
            this.correctAnswer = correctAnswer;
            this.wrongAnswers = Arrays.asList(wrongAnswers);
        }

        public String getQuestion() {
            return question;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }

        public List<String> getWrongAnswers() {
            return wrongAnswers;
        }

        public List<String> getAllAnswers() {
            return Arrays.asList(correctAnswer, wrongAnswers.get(0), wrongAnswers.get(1), wrongAnswers.get(2));
        }
    }
}