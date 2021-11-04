package scripts.api.antiban;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.util.abc.ABCProperties;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api.util.abc.preferences.OpenBankPreference;
import org.tribot.api.util.abc.preferences.WalkingPreference;
import org.tribot.api2007.*;
import org.tribot.script.sdk.Log;
import org.tribot.script.sdk.types.GameObject;

import java.text.NumberFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The AntiBan class provides an easy way to implement Anti-ban Compliance 2.0
 * into any script.
 *
 * @author Adapted from Starfox
 * https://github.com/frankkeuning/chopper/blob/master/AntiBan.java
 * @Version 4.0 (1/24/2016 3:37 PM GTM+0)
 * @Version 4.1 (12/13/2020 9:10 PM EST) - Polymorphic
 * --Replaced all deprecated code with updated code.
 * @Version 4.2 (01/01/2021 4:20 PM EST) - Polymorphic
 * -- Updated checkXP method
 * -- If checkXP is called then game tab will reset to inventory once checkXP is finished executing.
 * @Version 4.3 (01/14/2021 3:25 PM EST) - Polymorphic
 * -- Added ABCCount for every ant-iban task performed
 */

public final class AntiBan {

    /**
     * The object that stores the seeds
     */
    private static ABCUtil abc;

    /**
     * The boolean flag that determines whether or not to print debug information
     */
    private static boolean printDebug;

    /**
     * The boolean flag that determines whether or not to afk micro sleep
     */
    private static boolean microSleep;

    /**
     * The boolean flag that determines whether or not to afk micro sleep
     */
    private static boolean humanFatigue;

    /**
     * The amount of resources you have won
     */
    private static int resourcesWon;

    /**
     * The amount of resources you have lost
     */
    private static int resourcesLost;

    /**
     * The % run energy to activate run at
     */
    private static int runAt;

    /**
     * The % hp to eat food at
     */
    private static int eatAt;

    /**
     * The bool that determines whether or not we should be hovering.
     */
    private static boolean shouldHover;

    /**
     * The bool that determines whether or not we should be opening the menu.
     */
    private static boolean shouldOpenMenu;

    /**
     * The time stamp at which we were last under attack.
     */
    private static long lastUnderAttackTime;

    /**
     * The bool that determines whether or not we should reaction sleep
     */
    private static boolean enableReactionSleep;

    /**
     * The amount of times abc has performed a check
     */
    private static int abcCount;

    /**
     * The start time
     */
    private static long abcStartTime;

    /**
     * The afk timer
     */
    private static long abcAfkTimer;

    private static Fatigue fatigue;

    private static double fatigueMultiple;

    /**
     * Static initialization block. By default, the use of general anti-ban
     * compliance is set to be true.
     */
    static {
        abc = new ABCUtil();
        abcCount = 0;
        abcStartTime = System.currentTimeMillis();
        abcAfkTimer = General.randomLong(300000, 1200000); // 5-20 minutes
        printDebug = false;
        microSleep = false;
        humanFatigue = false;
        fatigue = new Fatigue();
        getFatigue().setCurrentFatigueMultiple(getFatigue().getFatigueMultipleSubset().iterator().next());
        setFatigueMultiple(getFatigue().getCurrentFatigueMultiple());
        resourcesWon = 0;
        resourcesLost = 0;
        runAt = abc.generateRunActivation();
        eatAt = abc.generateEatAtHP();
        shouldHover = abc.shouldHover();
        shouldOpenMenu = abc.shouldOpenMenu() && abc.shouldHover();
        lastUnderAttackTime = 0;
        enableReactionSleep = true;
        General.useAntiBanCompliance(true);
    }

    /**
     * Prevent instantiation of this class
     */
    private AntiBan() {
    }

    /**
     * Destroys the current instance of ABCUtil and stops all anti-ban threads.
     * Call this at the end of your script
     */
    public static void destroy() {
        abc.close();
        abc = null;
    }

    /**
     * Gets the ABCCount int
     *
     * @return The ABCCount int
     */
    public static int getABCCount() {
        return abcCount;
    }

    /**
     * Set the ABCCount int
     *
     * @param abc_count The int to set
     */
    public static void setABCCount(int abc_count) {
        AntiBan.abcCount = abc_count;
    }

    public static void incrementABCCount() {
        abcCount++;
    }

    /**
     * Creates a new instance of ABCUtil and sets the instance to be equal to
     * the current ABCUtil.
     */
    public static void create() {
        abc = new ABCUtil();
    }

    /**
     * Gets the ABCUtil object.
     *
     * @Return The ABCUtil object.
     */
    public static ABCUtil getABCUtil() {
        return abc;
    }

    /**
     * Gets the energy % to run at
     *
     * @return The energy % to run at
     */
    public static int getRunAt() {
        return runAt;
    }

    /**
     * Gets the % hp to eat at
     *
     * @return The hitpoints % to eat at
     */
    public static int getEatAt() {
        return eatAt;
    }

    /**
     * Gets the bool to hover or not a certain entity
     *
     * @return True if should hover, false otherwise
     */
    public static boolean getShouldHover() {
        return shouldHover;
    }

    /**
     * Gets the bool should open menu
     *
     * @return True if should open menu, false otherwise
     */
    public static boolean getShouldOpenMenu() {
        return shouldOpenMenu;
    }

    /**
     * Gets the last time the character was under attack, in milliseconds
     *
     * @return The last time under attack
     */
    public static long getLastUnderAttackTime() {
        return lastUnderAttackTime;
    }

    /**
     * Gets the properties for ABCUtil.
     *
     * @Return The properties.
     */
    public static ABCProperties getProperties() {
        return getABCUtil().getProperties();
    }

    /**
     * Gets the waiting time for the next action we want to perform.
     *
     * @Return The waiting time.
     */
    public static int getWaitingTime() {
        return getProperties().getWaitingTime();
    }

    /**
     * Gets the reaction time that we should sleep for before performing our
     * next action. Examples:
     * <ul>
     * <li>Reacting to when our character stops fishing. The response time will
     * be used before we move on to the next fishing spot, or before we walk to
     * the bank.</li>
     * <li>Reacting to when our character stops mining. The response time will
     * be used before we move on to the next rock, or before we walk to the
     * bank.</li>
     * <li>Reacting to when our character kills our target NPC. The response
     * time will be used before we attack our next target, or before we walk to
     * the bank.</li>
     * </ul>
     *
     * @Return The reaction time.
     */
    public static int getReactionTime() {
        return getABCUtil().generateReactionTime();
    }

    /**
     * Sets the print_debug bool to be equal to the specified bool. By calling
     * this method and providing a true value, other methods in this class will
     * start printing debug information into the system print stream when they
     * are executed.
     *
     * @param state The bool to set.
     */
    public static void setPrintDebug(boolean state) {
        printDebug = state;
    }

    public static boolean getPrintDebug() {
        return printDebug;
    }

    /**
     * Sets the micro_sleep bool to be equal to the specified bool. By calling
     * this method and providing a true value, other methods in this class will
     * start afk micro sleeping when they are executed.
     *
     * @param state The bool to set.
     */
    public static void setMicroSleep(boolean state) {
        microSleep = state;
    }

    public static boolean getMicroSleep() {
        return microSleep;
    }

    public static boolean getHumanFatigue() {
        return humanFatigue;
    }

    public static void setHumanFatigue(boolean state) {
        humanFatigue = state;
    }

    /**
     * Gets the amount of resources won.
     *
     * @Return The amount of resources won.
     */
    public static int getResourcesWon() {
        return resourcesWon;
    }

    /**
     * Gets the amount of resources lost.
     *
     * @Return The amount of recourses lost.
     */
    public static int getResourcesLost() {
        return resourcesLost;
    }

    /**
     * Sets the amount of resources won to the specified amount.
     *
     * @param amount The amount to set.
     */
    public static void setResourcesWon(int amount) {
        resourcesWon = amount;
    }

    /**
     * Sets the amount of resources lost to the specified amount.
     *
     * @param amount The amount to set.
     */
    public static void setResourcesLost(int amount) {
        resourcesLost = amount;
    }

    /**
     * Increments the amount of resources won by 1.
     */
    public static void incrementResourcesWon() {
        resourcesWon++;
    }

    /**
     * Increments the amount of resources lost by 1.
     */
    public static void incrementResourcesLost() {
        resourcesLost++;
    }

    /**
     * Sets the last_under_attack_time to be equal to the specified time stamp.
     *
     * @param time_stamp The time stamp.
     */
    public static void setLastUnderAttackTime(long time_stamp) {
        lastUnderAttackTime = time_stamp;
    }

    public static long getAbcStartTime() {
        return abcStartTime;
    }

    public static void setAbcStartTime(long abc_start_time) {
        AntiBan.abcStartTime = abc_start_time;
    }

    public static long getAbcAfkTimer() {
        return abcAfkTimer;
    }

    public static void setAbcAfkTimer(long abc_afk_timer) {
        AntiBan.abcAfkTimer = abc_afk_timer;
    }

    public static double getFatigueMultiple() {
        return fatigueMultiple;
    }

    public static void setFatigueMultiple(double fatigueMultiple) {
        AntiBan.fatigueMultiple = fatigueMultiple;
    }

    public static Fatigue getFatigue() {
        return fatigue;
    }

    public static void setFatigue(Fatigue fatigue) {
        AntiBan.fatigue = fatigue;
    }

    /**
     * Sleeps for the reaction time generated by ABCUtil. Note that this method
     * uses a special sleeping method from ABCUtil that allows the ABC2
     * background thread to interrupt the sleep when needed.
     */
    public static void sleepReactionTime(final int time) {
        if (!enableReactionSleep) {
            return;
        }
        try {
            getABCUtil().sleep(time);
            incrementABCCount();
        } catch (InterruptedException e) {
            debug("Sleep has been skipped");
        }
    }

    /**
     * Generates the trackers for ABCUtil. Call this only after successfully
     * completing an action that has a dynamic wait time for the next action.
     *
     * @param estimated_wait The estimated wait time (in milliseconds) before the next
     *                       action occurs.
     * @param fixed_wait     True if estimated wait is fixed, false otherwise
     */
    public static void generateTrackers(int estimated_wait, boolean fixed_wait) {
        final ABCProperties properties = getProperties();

        properties.setHovering(shouldHover);
        properties.setMenuOpen(shouldOpenMenu);
        properties.setWaitingFixed(fixed_wait);
        properties.setWaitingTime(estimated_wait);

        properties.setUnderAttack(Combat.isUnderAttack() || (Timing.currentTimeMillis() - lastUnderAttackTime < 2000));

        getABCUtil().generateTrackers();
    }

    /**
     * Resets the should_hover bool to match the ABCUtil value. This method
     * should be called after successfully clicking an entity.
     */
    public static void resetShouldHover() {
        shouldHover = getABCUtil().shouldHover();
    }

    /**
     * Resets the should_open_menu bool to match the ABCUtil value. This method
     * should be called after successfully clicking an entity.
     */
    public static void resetShouldOpenMenu() {
        shouldOpenMenu = getABCUtil().shouldOpenMenu() && getABCUtil().shouldHover();
    }

    /**
     * Randomly moves the camera. Happens only if the time tracker for camera
     * movement is ready.
     *
     * @Return True if the action was performed, false otherwise.
     */
    public static boolean moveCamera() {
        if (getABCUtil().shouldRotateCamera()) {
            incrementABCCount();
            if (printDebug) {
                debug("Rotated camera");
            }
            getABCUtil().rotateCamera();
            return true;
        }
        return false;
    }

    /**
     * Checks the exp of the skill being trained. Happens only if the time
     * tracker for checking exp is ready.
     *
     * @Return True if the exp was checked, false otherwise.
     */
    public static boolean checkXp() {
        if (getABCUtil().shouldCheckXP()) {
            incrementABCCount();
            if (printDebug) {
                debug("Checked xp");
            }
            getABCUtil().checkXP();
            General.sleep(1000, 7000);
            // reset tab to inventory once xp is checked.
            if (!GameTab.getOpen().equals(GameTab.TABS.INVENTORY)) {
                debug("Resetting game tab");
                GameTab.open(GameTab.TABS.INVENTORY);
            }
            return true;
        }
        return false;
    }

    /**
     * Picks up the mouse. Happens only if the time tracker for picking up the
     * mouse is ready.
     *
     * @Return True if the mouse was picked up, false otherwise.
     */
    public static boolean pickUpMouse() {
        if (getABCUtil().shouldPickupMouse()) {
            incrementABCCount();
            if (printDebug) {
                debug("Picked up mouse");
            }
            getABCUtil().pickupMouse();
            return true;
        }
        return false;
    }

    /**
     * Navigates the mouse off game window and mimics de-focusing the window.
     * Happens only if the time tracker for leaving the game is ready.
     *
     * @Return True if the mouse left the game window, false otherwise.
     */
    public static boolean leaveGame() {
        if (getABCUtil().shouldLeaveGame()) {
            incrementABCCount();
            if (printDebug) {
                debug("Left game");
            }
            getABCUtil().leaveGame();
            return true;
        }
        return false;
    }

    /**
     * Examines an entity near your player. Happens only if the time tracker for
     * examining an entity is ready.
     *
     * @Return True if an entity was examined, false otherwise.
     */
    public static boolean examineEntity() {
        if (getABCUtil().shouldExamineEntity()) {
            incrementABCCount();
            if (printDebug) {
                debug("Examined entity");
            }
            getABCUtil().examineEntity();
            return true;
        }
        return false;
    }

    /**
     * Right clicks the mouse. Happens only if the time tracker for right
     * clicking the mouse is ready.
     *
     * @Return True if a random spot was right clicked, false otherwise.
     */
    public static boolean rightClick() {
        if (getABCUtil().shouldRightClick()) {
            incrementABCCount();
            if (printDebug) {
                debug("Right clicked");
            }
            getABCUtil().rightClick();
            return true;
        }
        return false;
    }

    /**
     * Moves the mouse. Happens only if the time tracker for moving the mouse is
     * ready.
     *
     * @Return True if the mouse was moved to a random point, false otherwise.
     */
    public static boolean mouseMovement() {
        if (getABCUtil().shouldMoveMouse()) {
            incrementABCCount();
            if (printDebug) {
                debug("Mouse moved");
            }
            getABCUtil().moveMouse();
            return true;
        }
        return false;
    }

    /**
     * Opens up a game tab. Happens only if the time tracker for tab checking is
     * ready.
     *
     * @Return True if the combat tab was checked, false otherwise.
     */
    public static boolean checkTabs() {
        if (getABCUtil().shouldCheckTabs()) {
            incrementABCCount();
            if (printDebug) {
                debug("Tab checked");
            }
            getABCUtil().checkTabs();
            General.sleep(1000, 7000);
            // reset tab to inventory once xp is checked.
            if (!GameTab.getOpen().equals(GameTab.TABS.INVENTORY)) {
                debug("Resetting game tab");
                GameTab.open(GameTab.TABS.INVENTORY);
            }
            return true;
        }
        return false;
    }

    public static boolean moveToAnticipated() {
        if (getABCUtil().shouldMoveToAnticipated()) {
            incrementABCCount();
            if (printDebug) {
                debug("Moved to anticipated");
            }
            getABCUtil().moveMouse();
            return true;
        }
        return false;
    }

    public static boolean afkMicroSleep(Long afk_timer) {
        if (microSleep) {
            incrementABCCount();
            final long timer_run = Timing.timeFromMark(getAbcStartTime());
            final long eight_minutes_cut = 450000;
            final int sleep_magic = 40 * getReactionTime();
            if (timer_run >= afk_timer) {
                if (!(sleep_magic >= eight_minutes_cut)) {
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(sleep_magic);
                    debug("AFK " + sleep_magic + "ms");
                    debug("AFK " + minutes + " minutes");
                    Mouse.leaveGame();
                    sleepReactionTime(sleep_magic);
                } else {
                    debug("AFK has been skipped");
                }
                setAbcStartTime(System.currentTimeMillis());
                setAbcAfkTimer(General.randomLong(300000, 1200000));
                return true;
            }
        }
        return false;
    }

    public static boolean checkFatigue() {
        if (getFatigue().shouldIncrementFatigue(getABCCount())) {
            setFatigueMultiple(getFatigue().getCurrentFatigueMultiple());
            //debug(String.format("Fatigue level incremented: %d%%", (int) getFatigueMultiple() * 100));
            return true;
        }
        return false;
    }

    public static void sleep(List<Integer> waitTimes) {
        int reactionTime;

        if (waitTimes.isEmpty()) {
            AntiBan.generateTrackers(General.random(800, 1400), false);
        } else {
            AntiBan.generateTrackers(average(waitTimes), false);
        }

        if (getHumanFatigue()) {
            checkFatigue();
            NumberFormat percentInstance = NumberFormat.getPercentInstance();
            percentInstance.setMinimumFractionDigits(0);
            debug("Fatigue percentage: " + percentInstance.format(getFatigueMultiple()));
            reactionTime = (int) (AntiBan.getReactionTime() * getFatigueMultiple());
        } else {
            reactionTime = (int) (AntiBan.getReactionTime() * 0.3);
        }

        waitTimes.add(reactionTime);
        debug(String.format("Sleeping reaction time: %sms", reactionTime));
        AntiBan.sleepReactionTime(reactionTime);
    }

    public static int average(List<Integer> times) {
        return (int) times.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
    }

    /**
     * Checks all of the actions that are perform with the time tracker; if any
     * are ready, they will be performed.
     */
    public static void timedActions() {
        moveCamera();
        checkXp();
        checkTabs();
        pickUpMouse();
        leaveGame();
        examineEntity();
        rightClick();
        mouseMovement();
        afkMicroSleep(AntiBan.getAbcAfkTimer());
    }

    public static void checkAntiBanTask(GameObject object) {
        if (object != null) {
            shouldHoverObject(object);
            shouldExamineObject(object);
        }

        timedActions();
    }

    public static void shouldHoverObject(GameObject object) {
        if (getShouldHover()) {
            hoverEntity(object);
            resetShouldHover();
        }
    }

    public static void shouldExamineObject(GameObject object) {
        if (getShouldOpenMenu()) {
            if (object.interact("Examine")) {
                Timing.waitCondition(() -> {
                    General.sleep(20, 60);
                    return ChooseOption.isOpen();
                }, General.random(1200, 2000));
            }
            resetShouldOpenMenu();
        }
    }

    /**
     * Gets the next target that should be interacted with from the specified
     * list of targets.
     *
     * @param targets The targets to choose from.
     * @param <T>     The generic type.
     * @Return The target to interact with.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Positionable> T selectNextTarget(Positionable[] targets) {
        return (T) getABCUtil().selectNextTarget(targets);
    }

    /**
     * Activates run. No action is taken if run is already enabled or the
     * current run energy is less than the value returned by ABCUtil.
     *
     * @Return True if run was enabled, false otherwise.
     */
    public static boolean activateRun() {
        if (Game.getRunEnergy() >= getRunAt() && !Game.isRunOn()) {
            Options.setRunEnabled(true);
            if (Options.setRunEnabled(true)) {
                incrementABCCount();
                if (printDebug) {
                    debug("Turned run on at " + Game.getRunEnergy() + "%");
                }
                runAt = getABCUtil().generateRunActivation();
                return true;
            }
        }
        return false;
    }

    public static boolean doEat(String option, String name) {
        if (Player.getRSPlayer().getHealthPercent() <= getEatAt()) {
            return eat(option, name);
        }
        return false;
    }

    /**
     * Eats/drinks an item in your inventory with the specified name if your
     * current HP percent is less than or equal to the value generated by
     * ABCUtil. Note that if there is any delay/lag that is longer than 3000
     * milliseconds between the time the food/drink was clicked and when your
     * players HP is changed the tracker will not be reset and you will have to
     * reset it manually.
     *
     * @param option The option to click the food/drink with (this is normally
     *               "Eat" or "Drink"). Input an empty string to have the method
     *               attempt to find the correct option automatically. Note that
     *               this is not guaranteed to execute properly if an empty string
     *               is inputted.
     * @param name   The name of the food or drink.
     * @Return True if the food/drink was successfully eaten/drank, false
     * otherwise.
     * @see(java.lang.String, org.tribot.api2007.types.RSItem)
     */
    public static boolean eat(String option, final String name) {
        return eat(option, Inventory.getCount(name));
    }

    /**
     * Eats/drinks an item in your inventory with the specified ID if your
     * current HP percent is less than or equal to the value generated by
     * ABCUtil. Note that if there is any delay/lag that is longer than 3000
     * milliseconds between the time the food/drink was clicked and when your
     * players HP is changed the tracker will not be reset and you will have to
     * reset it manually.
     *
     * @param option The option to click the food/drink with (this is normally
     *               "Eat" or "Drink"). Input an empty string to have the method
     *               attempt to find the correct option automatically. Note that
     *               this is not guaranteed to execute properly if an empty string
     *               is inputted.
     * @param id     The ID of the food or drink.
     * @Return True if the food/drink was successfully eaten/drank, false
     * otherwise.
     * @seet(java.lang.String, org.tribot.api2007.types.RSItem)
     */
    public static boolean eat(String option, final int id) {
        return eat(option, Inventory.getCount(id));
    }

    /**
     * Checks to see if the player should switch resources. Note that this
     * method will only return correctly if you have been tracking the resources
     * you have won and lost. Note also that you must create the check time in
     * your script and reset it accordingly. e.g. to check if you should switch
     * resources, you should check the following condition:
     * <code>Timing.currentTimeMillis() >= check_time && AntiBan.shouldSwitchResources()</code>
     *
     * @param player_count The amount of players gathering resources near you.
     * @Return True if your player should switch resources, false otherwise.
     */
    public static boolean shouldSwitchResources(int player_count) {
        double win_percent = ((double) (resourcesWon + resourcesLost) / (double) resourcesWon);
        return win_percent < 50.0 && getABCUtil().shouldSwitchResources(player_count);
    }

    /**
     * Sleeps the current thread for the item interaction delay time. This
     * method should be called directly after interacting with an item in your
     * players inventory.
     */
    public static void waitItemInteractionDelay() {
        General.sleep(25, 75);
    }

    /**
     * Sleeps the current thread for the item interaction delay time multiplied
     * by the specified number of iterations. This method can be used to sleep
     * between certain actions that do not have a designated method already
     * assigned to them such as casting spells or clicking interfaces.
     * <p/>
     * This method does not guarantee a static sleep time each iteration.
     *
     * @param iterations How many times to sleep the item interaction delay time.
     * @see #waitItemInteractionDelay()
     */
    public static final void waitItemInteractionDelay(int iterations) {
        for (int i = 0; i < iterations; i++) {
            waitItemInteractionDelay();
        }
    }

    /**
     * Hovers the entity if applicable.
     * <p>
     * Note that you <i>must</i> reset the tracker yourself after the current
     * Object interaction is finished.
     */
    public static boolean hoverEntity(GameObject b) {
        if (shouldHover) {
            incrementABCCount();
            if (printDebug) {
                debug("Hovering entity");
            }
            b.hover();
            return true;
        }
        return false;
    }

    /**
     * Enable or disable reaction sleeps
     *
     * @param state The new state
     */
    public static void setEnableReactionSleep(boolean state) {
        enableReactionSleep = state;
    }

    /**
     *
     */
    public static Inventory.DROPPING_METHOD generateDroppingPreference() {
        int code = General.random(1, 2);
        return code == 1 ? Inventory.DROPPING_METHOD.RIGHT_CLICK : Inventory.DROPPING_METHOD.SHIFT;
    }

    /**
     *
     */
    public static Inventory.DROPPING_PATTERN generateDroppingPattern() {
        int code = General.random(1, 4);
        switch (code) {
            case 2: {
                return Inventory.DROPPING_PATTERN.TOP_TO_BOTTOM;
            }
            case 3: {
                return Inventory.DROPPING_PATTERN.TOP_TO_BOTTOM_ZIGZAG;
            }
            case 4: {
                return Inventory.DROPPING_PATTERN.ZIGZAG;
            }
            default: {
                return Inventory.DROPPING_PATTERN.LEFT_TO_RIGHT;
            }
        }
    }

    /**
     * Returns the special attack preference of the player's profile. MAIN_HUD or COMBAT_TAB
     * or both
     *
     * @return SpecialAttackPreference Special attack preference
     */
//    public static SpecialAttackPreference generateSpecialAttackPreference() {
//        int code = General.random(1, 2);
//        return code == 1 ? SpecialAttackPreference.MAIN_HUD : SpecialAttackPreference.COMBAT_TAB;
//    }

    /**
     * Returns the walking preference of the player's profile. SCREEN or MINIMAP
     * or both
     *
     * @return WalkingPreference Walking preference
     */
    public static WalkingPreference generateWalkingPreference(int distance) {
        return getABCUtil().generateWalkingPreference(distance);
    }

    /**
     * Returns the bank preference of the player's profile. BANKER or BOOTH
     *
     * @return OpenBankPreference Banking preference
     */
    public static OpenBankPreference generateOpenBankPreference() {
        return getABCUtil().generateOpenBankPreference();
    }

    /**
     * Sends the specified message to the system print stream with the [ABC2]
     * tag.
     *
     * @param message The message to print.
     */
    private static void debug(Object message) {
        Log.log("[ABC2] " + message);
    }
}
