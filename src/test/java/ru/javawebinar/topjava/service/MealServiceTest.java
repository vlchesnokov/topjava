package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;

@ContextConfiguration({"classpath:spring/spring-app.xml", "classpath:spring/spring-db.xml"})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(START_SEQ + 2, USER_ID);
        assertMatch(meal, USER_MEAL_1);
    }

    @Test
    public void delete() {
        service.delete(START_SEQ + 2, USER_ID);
        assertMatch(service.getAll(USER_ID), USER_MEAL_6, USER_MEAL_5, USER_MEAL_4, USER_MEAL_3, USER_MEAL_2);
    }


    @Test
    public void getBetweenDates() {
        List<Meal> meals = service.getBetweenDates(parseLocalDate("2019-06-25"), parseLocalDate("2019-06-28"), USER_ID);
        assertMatch(meals, USER_MEAL_6, USER_MEAL_5, USER_MEAL_4);
    }


    @Test
    public void getBetweenDateTimes() {
        List<Meal> meals = service.getBetweenDateTimes(LocalDateTime.of(2019, Month.JUNE, 24, 12, 00), LocalDateTime.of(2019, Month.JUNE, 28, 14, 00), USER_ID);
        assertMatch(meals, USER_MEAL_5, USER_MEAL_4, USER_MEAL_3, USER_MEAL_2);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, USER_MEAL_6, USER_MEAL_5, USER_MEAL_4, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);
    }

    @Test
    public void update() {
        Meal updated = new Meal(USER_MEAL_1);
        updated.setDescription("UpdatedDescription");
        updated.setCalories(1400);
        service.update(updated, USER_ID);
        assertMatch(service.get(START_SEQ + 2, USER_ID), updated);
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(null, LocalDateTime.now(), "торт Панчо", 1300);
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(service.getAll(USER_ID), USER_MEAL_6, USER_MEAL_5, USER_MEAL_4, newMeal, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);
    }

    @Test(expected = DataAccessException.class)
    public void duplicateDateTimeForOneUserCreate() throws Exception {
        service.create(new Meal(null, LocalDateTime.of(2019, Month.JUNE, 24, 7, 40), "Dublicate", 1300), USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateMealAnotherUser() {
        Meal updated = new Meal(USER_MEAL_1);
        service.update(updated, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteMealAnotherUser() {
        service.delete(START_SEQ + 2, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getMealAnotherUser() {
        service.get(START_SEQ, ADMIN_ID);
    }
}