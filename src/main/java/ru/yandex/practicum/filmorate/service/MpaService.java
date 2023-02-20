package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDAO;

import java.util.List;

@Service
public class MpaService {
    private final MpaDAO mpaDAO;

    @Autowired
    public MpaService(MpaDAO mpaDAO) {
        this.mpaDAO = mpaDAO;
    }

    public List<Mpa> getAllMpa() {
        return mpaDAO.getAllMpa();
    }

    public Mpa getMpa(int mpaId) {
        if (mpaDAO.getMpa(mpaId) == null) throw new NotExistException("MPA not found");
        return mpaDAO.getMpa(mpaId);
    }
}
