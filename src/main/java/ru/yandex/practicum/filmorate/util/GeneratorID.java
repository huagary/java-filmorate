package ru.yandex.practicum.filmorate.util;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeneratorID {
    int id = 1;

    public int generateId() {
        return id++;
    }
}
