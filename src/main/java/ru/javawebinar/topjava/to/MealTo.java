package ru.javawebinar.topjava.to;

import java.time.LocalDateTime;

public class MealTo {
    private Integer id;

    private LocalDateTime dateTime;

    private String description;

    private int calories;

    private boolean excess;

    public MealTo() {
    }

    public MealTo(Integer id, LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public boolean isExcess() {
        return excess;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !getClass().equals(o.getClass())) {
            return false;
        }
        MealTo that = (MealTo) o;
        return id.equals(that.id) && dateTime.equals(that.dateTime) && description.equals(that.description)
                && (calories == that.calories) && (excess == that.excess);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id;
    }
}