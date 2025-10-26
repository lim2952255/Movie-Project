package com.limhs.movie_project.service.scheduled;

public enum MovieType {
    PLAYING("isPlaying"), POPULAR("ispopular");

    String value;
    MovieType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
