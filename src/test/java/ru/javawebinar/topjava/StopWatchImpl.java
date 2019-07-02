package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class StopWatchImpl extends Stopwatch {
    private static final Logger log = getLogger(StopWatchImpl.class);

    private static void logInfo(Description description, long nanos) {
        if (description.getMethodName() == null) {
            log.debug("General time of performance of the test {} is {} milliseconds", description.getDisplayName(), TimeUnit.NANOSECONDS.toMillis(nanos));
        } else {
            log.debug("Time of performance of the test method {} is {} milliseconds", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
        }
    }

    @Override
    protected void finished(long nanos, Description description) {
        logInfo(description, nanos);
    }
}