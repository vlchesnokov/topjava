package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class StopWatchImpl extends Stopwatch {
    private static final Logger log = getLogger(StopWatchImpl.class);

    private static final StringBuilder messages = new StringBuilder();

    private static void logInfo(Description description, long nanos) {
        if (description.getMethodName() == null) {
            log.debug(messages.toString());
        } else {
            String str = String.format("%-20s%-20s ", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos) + " milliseconds");
            messages.append("\n" + str);
            log.debug(description.getMethodName() + " " + TimeUnit.NANOSECONDS.toMillis(nanos) + " milliseconds");
        }
    }

    @Override
    protected void finished(long nanos, Description description) {
        logInfo(description, nanos);
    }
}