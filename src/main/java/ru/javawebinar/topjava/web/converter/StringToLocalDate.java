package ru.javawebinar.topjava.web.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;

final class StringToLocalDate implements Converter<String, LocalDate> {

    public LocalDate convert(String source) {
        return parseLocalDate(source);
    }
}