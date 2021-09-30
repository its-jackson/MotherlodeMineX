package scripts.api;

import org.tribot.api.Timing;
import scripts.api.interfaces.Validatable;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.StringJoiner;

/**
 * Purpose of class: Create a Time object for each work instance (Composition methodology).
 * input fields can be left blank, which will be taken as 0 by default.
 */

public class TimeElapse implements Validatable {

    private long startTime = System.currentTimeMillis();

    private String condition;
    private Duration duration;
    private long timeElapsed;

    private long day;
    private long hour;
    private long minute;
    private long second;

    private TimeElapse() {
    }

    public TimeElapse(String condition) {
        this.condition = condition;

        if (this.condition != null
                && !this.condition.isBlank()
                && !this.condition.isEmpty()
                && this.condition.matches("\\d\\d:\\d\\d:\\d\\d:\\d\\d")) {

            final String[] split = this.condition.split(":");

            if (split.length > 0) {
                this.day = Integer.parseInt(split[0], 10);
                this.hour = Integer.parseInt(split[1], 10);
                this.minute = Integer.parseInt(split[2], 10);
                this.second = Integer.parseInt(split[3], 10);
                completeDuration(
                        this.day,
                        this.hour,
                        this.minute,
                        this.second
                );
            }
        } else {
            this.duration = Duration.ZERO;
            System.out.println("Incorrect time elapsed format: DAYS:HOURS:MINUTES:SECONDS - 00:00:00:00");
        }
    }

    public static TimeElapse generateRandomTimer() {
        SecureRandom secureRandom = new SecureRandom();

        long day = Duration.ofDays(secureRandom.nextInt(24)).toMillis();
        long hour = Duration.ofDays(secureRandom.nextInt(24)).toMillis();
        long minute = Duration.ofMinutes(secureRandom.nextInt(24)).toMillis();
        long second = Duration.ofSeconds(secureRandom.nextInt(100)).toMillis();

        Duration duration = Duration.of(day + hour + minute + second, ChronoUnit.MILLIS);

        TimeElapse timer = new TimeElapse();

        StringJoiner stringJoinerCondition = new StringJoiner(":");

        stringJoinerCondition.add(Long.toString(day, 10))
                .add(Long.toString(hour, 10))
                .add(Long.toString(minute, 10))
                .add(Long.toString(second, 10));

        timer.setDuration(duration);
        timer.setCondition(stringJoinerCondition.toString());

        return timer;
    }

    public long getTimeElapsed() {
        calculateTimeElapsed();
        return timeElapsed;
    }

    private void calculateTimeElapsed() {
        setTimeElapsed(Timing.timeFromMark(getStartTime()));
    }

    // convert all fields (day, hour, minute, second) to milliseconds.
    // create a duration of the amount in ChronoUnit milliseconds.
    // for time elapsed validation
    private void completeDuration(long day, long hour, long minute, long second) {
        final long day_to_millisecond = Duration.of(day, ChronoUnit.DAYS)
                .toMillis();

        final long hour_to_millisecond = Duration.of(hour, ChronoUnit.HOURS)
                .toMillis();

        final long minute_to_millisecond = Duration.of(minute, ChronoUnit.MINUTES)
                .toMillis();

        final long second_to_millisecond = Duration.of(second, ChronoUnit.SECONDS)
                .toMillis();

        final long total_time_millisecond = day_to_millisecond
                + hour_to_millisecond
                + minute_to_millisecond
                + second_to_millisecond;

        this.duration = Duration.of(total_time_millisecond, ChronoUnit.MILLIS)
                .abs();
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public long getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public long getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public long getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public long getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setTimeElapsed(long timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    @Override
    public String toString() {
        return this.condition;
    }

    /**
     * If the duration is surpassed, work is complete.
     *
     * @return True if duration of time is surpassed; false otherwise.
     */
    @Override
    public boolean validate() {
        return getDuration() != null && getDuration() != Duration.ZERO && getTimeElapsed() >= getDuration().toMillis();
    }
}
