package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.util.MealsUtil.getWithExcess;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;

class MealRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = MealRestController.REST_URL + '/';
    @Autowired
    protected MealService mealService;

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL1));
    }

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson((getWithExcess(MEALS, authUserCaloriesPerDay()))));
    }

    @Test
    void testCreateWithLocation() throws Exception {
        Meal expected = getCreated();
        ResultActions action;
        action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        Meal returned = readFromJson(action, Meal.class);
        expected.setId(returned.getId());

        MealTestData.assertMatch(returned, expected);
        MealTestData.assertMatch(mealService.getAll(USER_ID), expected, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        MealTestData.assertMatch(mealService.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    void testUpdate() throws Exception {
        Meal updated = getUpdated();
        mealService.update(updated, USER_ID);
        mockMvc.perform(put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        MealTestData.assertMatch(mealService.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    void testGetBetween() throws Exception {
        mockMvc.perform(post(REST_URL + "filter")
                .param("startDateStr", "2011-12-03")
                .param("startTimeStr", "00:00:30")
                .param("endDateStr", "2020-12-03")
                .param("endTimeStr", "23:30:30")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(contentJson((getWithExcess(MEALS, authUserCaloriesPerDay()))));
    }
}