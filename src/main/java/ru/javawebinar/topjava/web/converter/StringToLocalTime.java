package ru.javawebinar.topjava.web.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

final class StringToLocalTime implements Converter<String, LocalTime> {

    public LocalTime convert(String source) {
        return parseLocalTime(source);
    }
}