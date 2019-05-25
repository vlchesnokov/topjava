package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> userMealWithExceedList = new ArrayList<>();
        if (mealList.size() != 0) {
            Map<LocalDate, Integer> map = new HashMap<>();
            mealList.forEach(a -> map.merge(a.getDateTime().toLocalDate(), a.getCalories(), Integer::sum));
            mealList.forEach(a -> {
                LocalTime userMealLocalTime = a.getDateTime().toLocalTime();
                if (TimeUtil.isBetween(userMealLocalTime, startTime, endTime)) {
                    boolean exceed = map.get(a.getDateTime().toLocalDate()) > caloriesPerDay;
                    UserMealWithExceed userMealWithExceed = new UserMealWithExceed(a.getDateTime(), a.getDescription(), a.getCalories(), exceed);
                    userMealWithExceedList.add(userMealWithExceed);
                }
            });
        }
        return userMealWithExceedList;
    }

    public static List<UserMealWithExceed> getFilteredWithExceededStreams(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return mealList.stream()
                .collect(Collectors.groupingBy(a -> a.getDateTime().toLocalDate()))
                .values().stream()
                .flatMap(a -> {
                    int sum = a.stream().mapToInt(UserMeal::getCalories).sum();
                    return a.stream()
                            .filter(x -> (TimeUtil.isBetween(x.getDateTime().toLocalTime(), startTime, endTime)))
                            .map(p -> new UserMealWithExceed(p.getDateTime(), p.getDescription(), p.getCalories(), sum > caloriesPerDay))
                            .collect(Collectors.toList()).stream();
                }).collect(Collectors.toList());
    }
}