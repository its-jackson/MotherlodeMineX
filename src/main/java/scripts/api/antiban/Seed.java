package scripts.api.antiban;

import org.tribot.script.sdk.antiban.PlayerPreferences;

public class Seed {

    int preference;
    String seed;

    public Seed(int preference, String seed) {
        this.preference = preference;
        this.seed = seed;
    }

    public Seed(String seed) {
        this.seed = seed;
    }

    public Seed() {

    }

    public int generateRandomPreference(String seed) {

        int preference = PlayerPreferences.preference(seed, generator -> generator.uniform(1, 100));

        if (preference > 30) {
            //
        } else {
            //
        }

        return -1;
    }

    public int getPreference() {
        return preference;
    }

    public void setPreference(int preference) {
        this.preference = preference;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    @Override
    public String toString() {
        return "Seed{" +
                "preference=" + preference +
                ", seed='" + seed + '\'' +
                '}';
    }
}
