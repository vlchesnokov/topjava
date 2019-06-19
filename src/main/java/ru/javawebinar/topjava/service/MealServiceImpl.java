package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealServiceImpl implements MealService {

    @Autowired
    private MealRepository repository;

    @Override
    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    @Override
    public void delete(int id, int userId) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    @Override
    public Meal get(int id, int userId) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    @Override
    public void update(Meal meal, int id, int userId) {
        checkNotFoundWithId(repository.save(meal, userId), id);
    }

    @Override
    public List<MealTo> getAll(int userId, int caloriesPerDay) {
        return MealsUtil.getWithExcess(repository.getAll(userId), caloriesPerDay);
    }

    @Override
    public List<MealTo> getAll(int userId, int caloriesPerDay, LocalDate startDate, LocalTime startTime,
                               LocalDate endDate, LocalTime endTime) {
        return MealsUtil.getFilteredWithExcess(repository.getAllBetweenDay(userId, startDate, endDate), caloriesPerDay, startTime, endTime);
    }
}