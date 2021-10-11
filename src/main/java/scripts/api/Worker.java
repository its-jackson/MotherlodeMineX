package scripts.api;

import org.tribot.script.sdk.Skill;
import org.tribot.script.sdk.Tribot;
import scripts.api.interfaces.Workable;

/**
 * Purpose of class: Cache all important information relating to the worker
 * (used for online database/leaderboard)
 */

public class Worker {

    private static Worker instance = new Worker();

    public final String user_name = Tribot.getUsername();

    private Workable.PickAxe pickaxe;

    private int actualMiningLevel;
    private int actualAttackLevel;
    private int oreCount;
    private int levelCount;
    private int startExperience;
    private int experienceGained;
    private int goldGained;

    private Worker() {}

    public int calculateExperienceGained() {
        return Skill.MINING.getXp() - getStartExperience();
    }

    public void incrementOreCount() {
        this.oreCount++;
    }

    public void incrementLevelCount() {
        this.levelCount++;
        this.actualMiningLevel += this.levelCount;
    }

    public static Worker getInstance() {
        return instance;
    }

    public static void setInstance(Worker instance) {
        Worker.instance = instance;
    }

    public Workable.PickAxe getPickaxe() {
        return pickaxe;
    }

    public void setPickaxe(Workable.PickAxe pickaxe) {
        this.pickaxe = pickaxe;
    }

    public int getActualMiningLevel() {
        return actualMiningLevel;
    }

    public void setActualMiningLevel(int actualMiningLevel) {
        this.actualMiningLevel = actualMiningLevel;
    }

    public int getActualAttackLevel() {
        return actualAttackLevel;
    }

    public void setActualAttackLevel(int actualAttackLevel) {
        this.actualAttackLevel = actualAttackLevel;
    }

    public int getOreCount() {
        return oreCount;
    }

    public void setOreCount(int oreCount) {
        this.oreCount = oreCount;
    }

    public int getLevelCount() {
        return levelCount;
    }

    public void setLevelCount(int levelCount) {
        this.levelCount = levelCount;
    }

    public int getStartExperience() {
        return startExperience;
    }

    public void setStartExperience(int startExperience) {
        this.startExperience = startExperience;
    }

    public int getExperienceGained() {
        return experienceGained;
    }

    public void setExperienceGained(int experienceGained) {
        this.experienceGained = experienceGained;
    }

    public int getGoldGained() {
        return goldGained;
    }

    public void setGoldGained(int goldGained) {
        this.goldGained += goldGained;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "user_name='" + user_name + '\'' +
                ", pickaxe=" + pickaxe +
                ", actualMiningLevel=" + actualMiningLevel +
                ", actualAttackLevel=" + actualAttackLevel +
                ", oreCount=" + oreCount +
                ", levelCount=" + levelCount +
                ", startExperience=" + startExperience +
                ", experienceGained=" + experienceGained +
                ", goldGained=" + goldGained +
                '}';
    }
}
