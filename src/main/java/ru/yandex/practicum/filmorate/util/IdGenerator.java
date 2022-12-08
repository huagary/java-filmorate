package ru.yandex.practicum.filmorate.util;

public class IdGenerator {
    private static int uId = 1;
    private static int filmId = 1;

    public static int generateUid() {
        return uId++;
    }

    public static int generateFilmId() {
        return filmId++;
    }

    public static void resetIds() {
        uId = 1;
        filmId = 1;
    }
}
