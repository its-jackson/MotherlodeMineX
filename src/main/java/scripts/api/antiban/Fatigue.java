package scripts.api.antiban;

import org.tribot.api.General;
import org.tribot.api.Timing;

import java.util.*;

/**
 * Purpose: Used for controlling the fatigue levels on the player.
 * Date: June 28th, 2021
 * Time: 3:16 PM TORONTO
 */

public class Fatigue {
    private long startTime = System.currentTimeMillis();
    private Set<Double> fatigueMultipleSet;
    private Set<Double> fatigueMultipleSubset;
    private double currentFatigueMultiple;
    private long abcInterval;
    private long fatigueTimeInterval;

    public Fatigue() {
        this.fatigueMultipleSet = initializeFatigueSet();
        this.fatigueMultipleSubset = generateFatigueSubset();
        this.abcInterval = General.randomLong(100, General.randomLong(200, 1000)); // 300 - 1000 actions
        this.fatigueTimeInterval = General.randomLong(600000, 1200000); // 10-20 minutes
    }

    private Set<Double> initializeFatigueSet() {
        List<Double> fatigueList = new ArrayList<>();

        // create fatigue list from 0.01 - 1.00 inclusive
        for (double d = 0.10; d < 1.00; d = d + 0.01) {
            fatigueList.add(d);
        }

        return Set.copyOf(fatigueList);
    }

    private void generateTrackers(int abcCount) {
        long abcInterval = General.randomLong(abcCount, General.randomLong(abcCount + 200, abcCount + 1000));
        long fatigueTimeInterval = General.randomLong(600000, 1200000);
        setAbcInterval(abcInterval);
        setFatigueTimeInterval(fatigueTimeInterval);
        setStartTime(System.currentTimeMillis());
    }

    // if k is small, keep generating random numbers
    // if k is large shuffle array first then create random subset ( k is large in my thoughts )
    private Set<Double> generateFatigueSubset() {
        // temp list for caching random multiples
        List<Double> subListFatigueMultiples = new ArrayList<>();

        if (getFatigueMultipleSet() != null) {
            List<Double> list = new ArrayList<>(getFatigueMultipleSet());

            for (int j = 0; j < General.random(1, 6); j++) {
                // randomize the list
                Collections.shuffle(list);
            }

            // fatigue multiple set cardinal
            int n = list.size();

            // k elements of random fatigue proper subset based
            // n - 1 is a proper subset
            int k = General.random(1, n - 1);

            // create random k elements
            for (int i = 0; i < k; i++) {
                subListFatigueMultiples.add(list.get(i));
            }
        }

        // return subset containing random k elements in ascending order (tree set is useful here)
        return new TreeSet<>(subListFatigueMultiples);
    }

    /**
     * Based on 10 - 20 minute intervals at random OR abc count every 200 - * intervals.
     *
     * @param abcCount
     * @return
     */
    public boolean shouldIncrementFatigue(int abcCount) {
        if (!getFatigueMultipleSubset().isEmpty()) {
            final long time_ran = Timing.timeFromMark(getStartTime());
            if (time_ran >= getFatigueTimeInterval() || abcCount >= getAbcInterval()) {
                if (incrementFatigue()) {
                    generateTrackers(abcCount);
                    return true;
                }
            }
        }

        return false;
    }

    private boolean incrementFatigue() {
        if (!getFatigueMultipleSubset().isEmpty()) {
            getFatigueMultipleSubset().remove(getCurrentFatigueMultiple());
            for (double multiple : getFatigueMultipleSubset()) {
                if (getCurrentFatigueMultiple() < multiple) {
                    setCurrentFatigueMultiple(multiple);
                    return true;
                }
            }
        }

        return false;
    }


    public Set<Double> getFatigueMultipleSet() {
        return fatigueMultipleSet;
    }

    public void setFatigueMultipleSet(Set<Double> fatigueMultipleSet) {
        this.fatigueMultipleSet = fatigueMultipleSet;
    }

    @Override
    public String toString() {
        return "Fatigue{" +
                "startTime=" + startTime +
                ", fatigueMultipleSet=" + fatigueMultipleSet +
                ", fatigueMultipleSubset=" + fatigueMultipleSubset +
                ", currentFatigueMultiple=" + currentFatigueMultiple +
                ", abcInterval=" + abcInterval +
                ", fatigueTimeInterval=" + fatigueTimeInterval +
                '}';
    }

    public Set<Double> getFatigueMultipleSubset() {
        return fatigueMultipleSubset;
    }

    public void setFatigueMultipleSubset(Set<Double> fatigueMultipleSubset) {
        this.fatigueMultipleSubset = fatigueMultipleSubset;
    }

    public double getCurrentFatigueMultiple() {
        return currentFatigueMultiple;
    }

    public void setCurrentFatigueMultiple(double currentFatigueMultiple) {
        this.currentFatigueMultiple = currentFatigueMultiple;
    }

    public long getAbcInterval() {
        return abcInterval;
    }

    public void setAbcInterval(long abcInterval) {
        this.abcInterval = abcInterval;
    }

    public long getFatigueTimeInterval() {
        return fatigueTimeInterval;
    }

    public void setFatigueTimeInterval(long fatigueTimeInterval) {
        this.fatigueTimeInterval = fatigueTimeInterval;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}