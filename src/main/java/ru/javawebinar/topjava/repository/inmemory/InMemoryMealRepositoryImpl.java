package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        if (mealMap == null) {
            mealMap = new ConcurrentHashMap<>();
            repository.put(userId, mealMap);
        }
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);

            mealMap.put(meal.getId(), meal);
            return meal;
        }

        // treat case: update, but absent in storage
        return mealMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        return ((mealMap != null) && (mealMap.remove(id) != null));
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        return mealMap != null ? mealMap.get(id) : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        return (mealMap != null) ?
                getSorted(mealMap.values(), meal -> true) : Collections.<Meal>emptyList();

    }

    @Override
    public List<Meal> getAllBetweenDay(int userId, LocalDate startDay, LocalDate endDay) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        return (mealMap != null) ?
                getSorted(mealMap.values(), meal -> DateTimeUtil.isBetween(meal.getDate(), startDay, endDay)) : Collections.<Meal>emptyList();
    }

    private static List<Meal> getSorted(Collection<Meal> meals, Predicate<Meal> filter) {
        return meals.stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDate).reversed())
                .collect(Collectors.toList());
    }
}