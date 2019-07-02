package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class StopWatchImpl extends Stopwatch {
    private static final Logger log = getLogger(StopWatchImpl.class);

    private static final List<String> messages = new ArrayList();

    private static void logInfo(Description description, long nanos) {
        if (description.getMethodName() == null) {
            log.debug("result of performance of the test " + description.getDisplayName() + ":");
            messages.forEach(a -> log.debug(a));
        } else {
            String str = "Time of performance of the test method " + description.getMethodName() + " is " + TimeUnit.NANOSECONDS.toMillis(nanos) + " milliseconds";
            messages.add(str);
            log.debug(str);
        }
    }

    @Override
    protected void finished(long nanos, Description description) {
        logInfo(description, nanos);
    }
}