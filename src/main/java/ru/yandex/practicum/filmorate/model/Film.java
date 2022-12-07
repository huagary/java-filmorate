package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    private int id;
    protected String name;
    protected String description;
    protected LocalDate releaseDate;
    protected int duration;
}
