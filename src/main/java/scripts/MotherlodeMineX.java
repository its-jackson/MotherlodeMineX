package scripts;

import org.apache.commons.lang3.time.StopWatch;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.painting.Painting;
import org.tribot.script.sdk.script.ScriptConfig;
import org.tribot.script.sdk.script.TribotScript;
import org.tribot.script.sdk.script.TribotScriptManifest;
import org.tribot.script.sdk.util.ScriptSettings;
import org.tribot.script.sdk.walking.GlobalWalking;
import org.tribot.script.sdk.walking.adapter.DaxWalkerAdapter;
import scripts.api.*;
import scripts.api.antiban.AntiBan;
import scripts.api.interfaces.Nodeable;
import scripts.api.interfaces.Workable;
import scripts.api.nodes.motherlodemine.*;
import scripts.api.nodes.shared.*;
import scripts.api.utilities.PolymorphicScriptSettings;
import scripts.api.works.MotherlodeMine;
import scripts.api.works.Work;
import scripts.gui.GUIFX;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


/**
 * TODO:
 * 1) Auto buy prospector equipment, gem bag, and coal bag
 * <p>
 * 2) Client args - COMPLETE
 * <p>
 * 3) Different type of work (generic mining) - COMPLETE
 */

/*
 Version 1.01
 */

@TribotScriptManifest(
        name = "Motherlode Mine X",
        author = "Polymorphic",
        category = "Mining",
        description = "Motherlode Mine X - Written By Polymorphic")

public class MotherlodeMineX implements TribotScript {

    private final static StopWatch stop_watch = new StopWatch();
    private final MotherlodeMineXVariables variables = MotherlodeMineXVariables.get();

    /**
     * Optional configuration for a script. It's expected that the implementer of the interface modifies the given
     * config object to what settings it wants. These settings are read BEFORE the script starts.
     *
     * @param config The configuration object to modify.
     */
    @Override
    public void configure(ScriptConfig config) {
        config.setRandomsAndLoginHandlerEnabled(true);
        config.setBreakHandlerEnabled(true);
    }

    @Override
    public void execute(String s) {
        if (parseArgument(s)) {
            getVariables().setStart(true);
        } else {
            handleGraphicalUserInterface();
        }

        // set script engine, anti ban, listeners
        setup();

        // main paint
        handlePaint();

        // main script loop
        handleWork();
    }

    private void setup() {
        // start timer
        if (stop_watch.isStopped()) {
            stop_watch.start();
        }

        // set global walking engine
        GlobalWalking.setEngine(
                new DaxWalkerAdapter(
                        "sub_JK3knXqxVGZtGR",
                        "74aa47de-1cb1-4ee1-a8c9-5bae53c70b22")
        );

        // anti ban settings
        General.useAntiBanCompliance(true);
        AntiBan.setPrintDebug(true);
        AntiBan.setMicroSleep(getVariables().getSettings().isMicroSleep());
        AntiBan.setHumanFatigue(getVariables().getSettings().isFatigue());

        // listeners
        MessageListening.addServerMessageListener(message -> {
            message = message.toLowerCase(Locale.ROOT);
            if (message.contains("you manage to mine some")) {
                Worker.getInstance().incrementOreCount();
            }
            if (message.contains("congratulations")) {
                Log.log("You just leveled up, congratulations!");
                Worker.getInstance().incrementLevelCount();
                Workable.takeScreenShot("Mining", Worker.getInstance().user_name, Skill.MINING.getActualLevel());
            }
        });

        ScriptListening.addEndingListener(this::end);
    }

    private void handleGraphicalUserInterface() {
        // run gui
        try {
            getVariables().setFxml(new URL("https://jacksonjohnson.ca/motherlodeminex/motherlodeminefxml.fxml"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // set gui
        MotherlodeMineXVariables.get().setGui(new GUIFX(getVariables().getFxml()));
        MotherlodeMineXVariables.get().getGui().show();

        while (getVariables().getGui().isOpen()) {
            Waiting.wait(500);
        }

        getVariables().getGui().close();
    }

    private void handlePaint() {
        // mouse paint
        Painting.setMousePaint(new PolymorphicMousePaint(Color.CYAN, Color.WHITE, 24));

        Painting.setMouseSplinePaint((graphics, list) -> {
            // TODO
        });

        // script paint
        Painting.addPaint(graphics2D -> {
            // anti aliasing
            graphics2D.setRenderingHints(MotherlodeMineXVariables.get().getAntiAliasing());

            // vars
            int factor = 3600000;
            long oresPerHour = (long) (Worker.getInstance().getOreCount() * (factor / (double) stop_watch.getTime()));
            int gainedEXP = Worker.getInstance().calculateExperienceGained();
            long EXPPerHour = (long) (gainedEXP * (factor / (double) stop_watch.getTime()));
            long gpPerHour = (long) (Worker.getInstance().getGoldGained() * (factor / (double) stop_watch.getTime()));
            int percentToNextLevel = Skill.MINING.getXpPercentToNextLevel();

            // main img
            graphics2D.drawImage(MotherlodeMineXVariables.get().getImg(), -1, 313, null);

            // statistics primary
            graphics2D.setFont(getVariables().getSecondaryFont());
            graphics2D.drawString("Version: 1.01", 10, 100); // profit
            graphics2D.drawString("Time Running:", 15, 370); // runtime
            graphics2D.drawString("Status:", 15, 385); // state

            graphics2D.drawString("Ores Mined:", 75, 410); // ores mined
            graphics2D.drawString("Ores/HR:", 75, 425); // ores mined per hour
            graphics2D.drawString("XP Gained:", 75, 440); // gained xp
            graphics2D.drawString("XP/HR:", 75, 455); // gained xp/hour
            graphics2D.drawString("GP Gained:", 250, 440); // ores mined
            graphics2D.drawString("GP/HR:", 250, 455); // ores mined
            graphics2D.drawString("Levels Gained:", 250, 410); // ores mined
            graphics2D.drawString("Current Level:", 250, 425); // ores mined

            // statistics secondary
            graphics2D.drawString(Timing.msToString(stop_watch.getTime()), 105, 371); // runtime
            graphics2D.drawString(getVariables().getState(), 105, 385);

            graphics2D.drawString(String.format("%,d", Worker.getInstance().getGoldGained()) + " GP", 340, 440); // gp gained
            graphics2D.drawString(String.format("%,d", gpPerHour) + " GP/HR", 340, 455); // gp hour

            graphics2D.drawString(Worker.getInstance().getLevelCount() + " LVLS", 340, 410);
            graphics2D.drawString(String.valueOf(Skill.MINING.getActualLevel()), 340, 425);

            graphics2D.drawString(String.format("%,d", Worker.getInstance().getOreCount()) + " Ores", 150, 410); // ores mined
            graphics2D.drawString(String.format("%,d", oresPerHour) + " Ores/HR", 150, 425); // logs hr
            graphics2D.drawString(String.format("%,d", gainedEXP) + " XP", 150, 440); // gained xp
            graphics2D.drawString(String.format("%,d", EXPPerHour) + " XP/HR", 150, 455); // gained xp hour

            // percentage to level bar
            graphics2D.setColor(getVariables().getProgressColourBackground());
            graphics2D.fillRect(-1, 479, 519, 25);
            graphics2D.setColor(new Color(0, 148, 153));
            graphics2D.fillRect(-1, 479, percentToNextLevel * 519 / 100, 25);
            graphics2D.setColor(Color.WHITE);
            graphics2D.drawString(String.format("%d%% TO NEXT LEVEL", percentToNextLevel), 200, 495);
        });
    }

    private void handleWork() {
        while (true) {
            // script will only function while logged in and script start = true
            if (Login.isLoggedIn() && MotherlodeMineXVariables.get().isStart()) {
                // Optimize the script when logged in every time new work occurs
                if (!getVariables().isOptimization()) {
                    Log.log("Optimizing Motherlode Mine X");
                    // optimize the game before executing the script
                    // set worker starting mining level
                    // set worker attack level
                    Worker.getInstance().setActualAttackLevel(Skill.ATTACK.getActualLevel());
                    // set start xp
                    Worker.getInstance().setStartExperience(Skill.MINING.getXp());
                    // set the optimal pickaxe currently on the worker
                    if (MotherlodeMineXVariables.get().getSettings().isDoNotUpgrade()) {
                        Worker.getInstance().setPickaxe(MotherlodeMineXVariables.get().getSettings().getDesiredPickaxe());
                    } else {
                        Worker.getInstance().setPickaxe(Workable.calculateOptimalPickAxeOnWorker(Skill.MINING.getActualLevel()).orElse(null));
                    }
                    // set random zoom percent
                    Camera.setZoomPercent(General.random(0, 50));
                    // set random angle
                    Camera.setAngle(General.random(85, 100));
                    // reset the inventory once complete
                    GameTab.INVENTORY.open();
                    // display all fatigue multiples
                    if (getVariables().getSettings().isFatigue()) {
                        Log.log(AntiBan.getFatigue());
                    }
                    // set optimization true
                    getVariables().setOptimization(true);
                }
                do {
                    // perform the work
                    for (Work work : getVariables().getSettings().getWork()) {
                        Log.log(work);
                        // get nodes and clear
                        List<Nodeable> nodes = MotherlodeMineXVariables.get().getNodes();
                        nodes.clear();
                        // get timer and reset start time
                        TimeElapse timer = work.getTime();
                        if (timer != null) {
                            timer.setStartTime(System.currentTimeMillis());
                        }
                        // initialize the nodes for the type of work polymorphic
                        if (work instanceof MotherlodeMine) {
                            // is motherlode mine work
                            nodes.add(new MineResource(work));
                            nodes.add(new DepositPayDirt(work));
                            nodes.add(new RepairBrokenStrut(work));
                            nodes.add(new Walking(work));
                            nodes.add(new Banking(work));
                            nodes.add(new SearchCrateForHammer(work));
                            nodes.add(new DepletePayDirtSack(work));
                            nodes.add(new RetrievePickAxeFromBank(work));
                            nodes.add(new RetrieveMotherlodeEquipment(work));
                            nodes.add(new WorldHop(work));
                        } else {
                            // is mining work
                            nodes.add(new MineResource(work));
                            nodes.add(new RetrievePickAxeFromBank(work));
                            nodes.add(new Walking(work));
                            nodes.add(new Banking(work));
                            nodes.add(new Dropping(work));
                            nodes.add(new WorldHop(work));
                        }
                        // loop while the work is validated (reached time or level)
                        while (!work.validate()) {
                            // execute the valid node within the given work
                            nodes.stream()
                                    .filter(Nodeable::validate)
                                    .findFirst()
                                    .ifPresent(Nodeable::execute);

                            Waiting.waitUniform(100, 300);
                        }
                        Log.log("Work complete. Please be patient.");
                    }
                    if (getVariables().getSettings().isRepeatShuffle()) {
                        Collections.shuffle(getVariables().getSettings().getWork());
                    }
                }
                // keep repeating the script if necessary
                while (getVariables().getSettings().isRepeat() || getVariables().getSettings().isRepeatShuffle());
                // end script once all work is completed
                end();
            }
        }
    }

    private void end() {
        MotherlodeMineXVariables.get().setStart(false);
        throw new RuntimeException("Script is over! Thanks for playing " + Worker.getInstance().user_name);
    }

    private boolean parseArgument(String arg) {
        if (arg.isBlank() || arg.isEmpty()) {
            return false;
        }

        ScriptSettings settingsHandler = new PolymorphicScriptSettings()
                .getSettings();

        Optional<MotherlodeMineXSettings> settingsOptional = settingsHandler.load(arg, MotherlodeMineXSettings.class);

        if (settingsOptional.isPresent()) {
            Log.log(String.format("Loaded file: %s", arg));
            getVariables().setSettings(settingsOptional.get());
            Log.log(String.format("Loaded settings successfully: %s", getVariables().getSettings()));
            return true;
        } else {
            Log.log("Incorrect argument parsed. You must create settings via GUI first.");
            return false;
        }
    }

    public MotherlodeMineXVariables getVariables() {
        return variables;
    }
}
