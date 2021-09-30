package scripts;

import dax.api_lib.models.RunescapeBank;

import org.apache.commons.lang3.time.StopWatch;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.painting.Painting;
import org.tribot.script.sdk.script.ScriptConfig;
import org.tribot.script.sdk.script.TribotScript;
import org.tribot.script.sdk.script.TribotScriptManifest;
import org.tribot.script.sdk.types.GameObject;
import org.tribot.script.sdk.walking.GlobalWalking;
import org.tribot.script.sdk.walking.adapter.DaxWalkerAdapter;
import scripts.api.*;
import scripts.api.antiban.AntiBan;
import scripts.api.interfaces.Workable;
import scripts.api.works.MotherlodeMineWork;

import java.awt.*;
import java.util.Locale;


/**
 * TODO:
 * 1) Script settings saving ability
 *
 * 2) Client args
 *
 * 3) Increased anti-ban
 */

@TribotScriptManifest(
        name = "Motherlode Mine X",
        author = "Polymorphic",
        category = "Mining",
        description = "Motherlode Mine X - Written By Polymorphic")

public class MotherlodeMineX implements TribotScript {

    private final static StopWatch stop_watch = new StopWatch();

    private static MotherlodeMineXSettings settings;

    private static GameObject currentOreVein;

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
        // settings
        settings = new MotherlodeMineXSettings();
        getSettings().setRepeat(true);
        getSettings().setDoNotUpgrade(true);
        getSettings().setGemBag(true);
        getSettings().setWearProspectorEquipment(true);
        getSettings().setDesiredPickaxe(Workable.PickAxe.RUNE_PICKAXE);
        // start script timer
        getStopWatch().start();
        // listeners
        MessageListening.addServerMessageListener(message -> {
            message = message.toLowerCase(Locale.ROOT);
            if (message.contains("you manage to mine some pay-dirt")) {
                Worker.getInstance().incrementOreCount();
            }
            if (message.contains("congratulations")) {
                Log.log("You just leveled up, congratulations!");
                Worker.getInstance().incrementLevelCount();
                Workable.takeScreenShot("Mining", Worker.getInstance().user_name, Worker.getInstance().getActualMiningLevel());
            }
        });
        // set global walking engine
        GlobalWalking.setEngine(new DaxWalkerAdapter("sub_JK3knXqxVGZtGR", "74aa47de-1cb1-4ee1-a8c9-5bae53c70b22"));
        // mouse paint
        Painting.setMousePaint(new PolymorphicMousePaint(Color.CYAN, Color.WHITE, 24));
        Painting.setMouseSplinePaint((graphics, list) -> {
            // TODO
        });
        // script paint
        Painting.setPaint(graphics -> {
            // anti aliasing
            graphics.setRenderingHints(MotherlodeMineXVariables.get().getAntiAliasing());
            // vars
            long oresPerHour = (long) (Worker.getInstance().getOreCount() * (3600000 / (double) getStopWatch().getTime()));
            int gainedEXP = Worker.getInstance().calculateExperienceGained();
            long EXPPerHour = (long) (gainedEXP * (3600000 / (double) getStopWatch().getTime()));
            int percentToNextLevel = Skill.MINING.getXpPercentToNextLevel();
            // main img
            graphics.drawImage(MotherlodeMineXVariables.get().getImg(), -1, 318, null);
            // statistics primary
            //g2d.setFont(getVariables().getMainFont());
            graphics.setFont( MotherlodeMineXVariables.get().getSecondaryFont());
            graphics.drawString("Time Running:", 15, 370); // runtime
            graphics.drawString("Profit:", 15, 385); // state
            //
            graphics.drawString("Ores Mined:", 80, 410); // ores mined
            graphics.drawString("Ores/Hour:", 80, 425); // ores mined per hour
            graphics.drawString("XP Gained:", 80, 440); // gained xp
            graphics.drawString("XP/Hour:", 80, 455); // gained xp/hour
            // statistics secondary
            graphics.drawString(Timing.msToString(getStopWatch().getTime()), 120, 371); // runtime
            //
            graphics.drawString(Worker.getInstance().getGoldGained() + " GP", 120, 385);
            graphics.drawString(Worker.getInstance().getOreCount() + " Ores", 170, 410); // ores mined
            graphics.drawString(oresPerHour + " Ores/Hour", 170, 425); // logs hr
            graphics.drawString(gainedEXP + " XP", 170, 440); // gained xp
            graphics.drawString(EXPPerHour + " XP/Hour", 170, 455); // gained xp hour
            // percentage to level bar
            graphics.setColor(Color.CYAN);
            graphics.fillRect(-1, 479, percentToNextLevel * 519 / 100, 25);
            graphics.setColor( MotherlodeMineXVariables.get().getProgressColourBackground());
            graphics.fillRect(-1, 479, 519, 25);
            // ore vein outline
        });

        Work motherLodeMineWork = new MotherlodeMineWork(
                "Ore vein",
                "Motherlode Mine Upper Level",
                "Bank",
                new TimeElapse("00:02:00:00"),
                RunescapeBank.MOTHERLOAD,
                ResourceLocation.MOTHERLODE_MINE_UPPER_LEVEL);

        MotherlodeMineXVariables.get().getWork().add(motherLodeMineWork);

        General.useAntiBanCompliance(true);
        Log.log("ABC2 - " + General.useAntiBanCompliance());

        AntiBan.setMicroSleep(true);
        Log.log("AFK Micro Sleep - " + AntiBan.getMicroSleep());

        AntiBan.setHumanFatigue(true);
        Log.log("Human Fatigue - " + AntiBan.getHumanFatigue());

        AntiBan.setPrintDebug(true);
        Log.log("Print Debug - " + AntiBan.getPrintDebug());

        MotherlodeMineXVariables.get().setStart(true);

        // main script loop
        while (true) {
            // script will only function while logged in and script start = true
            if (Login.isLoggedIn() && MotherlodeMineXVariables.get().isStart()) {
                // Optimize the script when logged in every time new work occurs
                if (!MotherlodeMineXVariables.get().isOptimization()) {
                    Log.log("Optimizing Motherlode Mine X");
                    // optimize the game before executing the script
                    // set worker starting mining level
                    Worker.getInstance().setActualMiningLevel(Skill.MINING.getActualLevel());
                    // set worker attack level
                    Worker.getInstance().setActualAttackLevel(Skill.ATTACK.getActualLevel());
                    // set start xp
                    Worker.getInstance().setStartExperience(Skill.MINING.getXp());
                    // set the optimal pickaxe currently on the worker
                    if (getSettings().isDoNotUpgrade()) {
                        Worker.getInstance().setPickaxe(getSettings().getDesiredPickaxe());
                    } else {
                        Worker.getInstance().setPickaxe(Workable.calculateOptimalPickAxeOnWorker(Worker.getInstance().getActualMiningLevel()).orElse(null));
                    }
                    // set random angle
                    Camera.setAngle(General.random(85, 100));
                    // set random zoom percent
                    //Camera.setZoomPercent(General.random(0, 50));
                    // reset the inventory once complete
                    GameTab.INVENTORY.open();
                    // set optimization true
                    MotherlodeMineXVariables.get().setOptimization(true);
                }
                do {
                    // perform the work
                    for (Work work : MotherlodeMineXVariables.get().getWork()) {
                        Log.log(work);
                        // loop while the work is validated (reached time or level)
                        while (!work.validate()) {
                            // execute the valid node within the given work
                            work.execute();
                            // sleep/wait reduce cpu and amount of calculations
                            Waiting.waitUniform(100, 300);
                        }
                        Log.log("Work complete. Please be patient.");
                    }
                }
                // keep repeating the script if necessary
                while (getSettings().isRepeat() || getSettings().isRepeatShuffle());
                // end script once all work is completed
                end();
            }
        }
    }

    private boolean end() {
        MotherlodeMineXVariables.get().setStart(false);
        throw new RuntimeException("Script is over! Thanks for playing " + Worker.getInstance().user_name);
    }

    public static StopWatch getStopWatch() {
        return stop_watch;
    }

    public static MotherlodeMineXSettings getSettings() {
        return settings;
    }

    public static void setSettings(MotherlodeMineXSettings settings) {
        MotherlodeMineX.settings = settings;
    }

    public static GameObject getCurrentOreVein() {
        return currentOreVein;
    }

    public static void setCurrentOreVein(GameObject oreVein) {
        currentOreVein = oreVein;
    }
}
