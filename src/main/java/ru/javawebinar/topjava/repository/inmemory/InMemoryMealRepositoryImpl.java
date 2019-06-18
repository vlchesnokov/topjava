package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(a -> save(a, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            if (mealMap == null) {
                mealMap = new ConcurrentHashMap<>();
                repository.put(userId, mealMap);
            }
            mealMap.put(meal.getId(), meal);
            return meal;
        }

        // treat case: update, but absent in storage
        return mealMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        if ((mealMap != null) && (mealMap.remove(id) != null)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap != null) {
            return mealMap.get(id);
        } else {
            return null;
        }
    }

    @Override
    public List<Meal> getAll(int userId) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap != null) {
            return MealsUtil.getSorted(mealMap.values(), meal -> true);
        } else {
            return Collections.<Meal>emptyList();
        }
    }

    @Override
    public List<Meal> getAll(int userId, LocalDate startDay, LocalDate endDay) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap != null) {
            return MealsUtil.getSorted(mealMap.values(), meal -> DateTimeUtil.isBetweenDay(meal.getDate(), startDay, endDay));
        } else {
            return Collections.<Meal>emptyList();
        }
    }
}