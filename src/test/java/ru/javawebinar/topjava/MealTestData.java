package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDateTime;

public class MealTestData {
    public static final Meal meal1 = new Meal(START_SEQ + 2, parseLocalDateTime("2019-06-24T07:40:00"), "Завтрак", 520);
    public static final Meal meal2 = new Meal(START_SEQ + 3, parseLocalDateTime("2019-06-24T12:00:00"), "Обед", 1200);
    public static final Meal meal3 = new Meal(START_SEQ + 4, parseLocalDateTime("2019-06-24T21:00:00"), "Обед", 1500);
    public static final Meal meal4 = new Meal(START_SEQ + 5, parseLocalDateTime("2019-06-28T07:00:00"), "Завтрак", 700);
    public static final Meal meal5 = new Meal(START_SEQ + 6, parseLocalDateTime("2019-06-28T14:00:00"), "Обед", 1500);
    public static final Meal meal6 = new Meal(START_SEQ + 7, parseLocalDateTime("2019-06-28T23:00:00"), "Ужин", 1200);


    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingDefaultElementComparator().isEqualTo(expected);
    }
}