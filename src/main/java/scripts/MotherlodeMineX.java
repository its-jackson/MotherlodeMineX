package scripts;

import org.apache.commons.lang3.time.StopWatch;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.painting.Painting;
import org.tribot.script.sdk.script.ScriptConfig;
import org.tribot.script.sdk.script.TribotScript;
import org.tribot.script.sdk.script.TribotScriptManifest;
import org.tribot.script.sdk.walking.GlobalWalking;
import org.tribot.script.sdk.walking.adapter.DaxWalkerAdapter;
import scripts.api.*;
import scripts.api.antiban.AntiBan;
import scripts.api.interfaces.Nodeable;
import scripts.api.interfaces.Workable;
import scripts.api.nodes.*;
import scripts.api.works.MotherlodeMine;
import scripts.gui.GuiFx;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;


/**
 * TODO:
 * 1) Auto buy prospector equipment, gem bag, and coal bag
 * <p>
 * 2) Client args
 * <p>
 * 3) Different type of work (generic mining)
 */

/*
 Version 1.00
 */

@TribotScriptManifest(
        name = "Motherlode Mine X",
        author = "Polymorphic",
        category = "Mining",
        description = "Motherlode Mine X - Written By Polymorphic")

public class MotherlodeMineX implements TribotScript {

    private final static StopWatch stop_watch = new StopWatch();

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
        // run gui
        try {
            MotherlodeMineXVariables.get().setFxml(new URL("https://jacksonjohnson.ca/motherlodeminex/motherlodeminefxml.fxml"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // set gui
        MotherlodeMineXVariables.get().setGui(new GuiFx(MotherlodeMineXVariables.get().getFxml()));
        MotherlodeMineXVariables.get().getGui().show();
        while (MotherlodeMineXVariables.get().getGui().isOpen()) {
            Waiting.wait(500);
        }
        // settings
//        getSettings().setRepeat(true);
//        getSettings().setDoNotUpgrade(true);
//        getSettings().setUseGemBag(true);
//        getSettings().setWearProspectorEquipment(true);
//        getSettings().setDesiredPickaxe(Workable.PickAxe.RUNE_PICKAXE);
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
            graphics.setFont(MotherlodeMineXVariables.get().getSecondaryFont());
            graphics.drawString("Version: 1.00", 10, 100); // runtime
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
            graphics.drawString(String.format("%,d", Worker.getInstance().getGoldGained()) + " GP", 120, 385);
            graphics.drawString(String.format("%,d", Worker.getInstance().getOreCount()) + " Ores", 170, 410); // ores mined
            graphics.drawString(String.format("%,d", oresPerHour) + " Ores/Hour", 170, 425); // logs hr
            graphics.drawString(String.format("%,d", gainedEXP) + " XP", 170, 440); // gained xp
            graphics.drawString(String.format("%,d", EXPPerHour) + " XP/Hour", 170, 455); // gained xp hour
            // percentage to level bar
            graphics.setColor(Color.CYAN);
            graphics.fillRect(-1, 479, percentToNextLevel * 519 / 100, 25);
            graphics.setColor(MotherlodeMineXVariables.get().getProgressColourBackground());
            graphics.fillRect(-1, 479, 519, 25);
            // ore vein outline
        });

//        Work motherLodeMineWork = new MotherlodeMine(
//                Resource.ORE_VEIN,
//                ResourceLocation.MOTHERLODE_MINE_UPPER_LEVEL,
//                null,
//                new TimeElapse("00:02:00:00"),
//                RunescapeBank.MOTHERLOAD
//                );

        //MotherlodeMineXVariables.get().getWork().add(motherLodeMineWork);

        General.useAntiBanCompliance(true);
        Log.log("ABC2 - " + General.useAntiBanCompliance());

        AntiBan.setPrintDebug(true);
        Log.log("Print Debug - " + AntiBan.getPrintDebug());

        AntiBan.setMicroSleep(MotherlodeMineXVariables.get().getSettings().isMicroSleep());
        Log.log("Micro Sleep - " + AntiBan.getMicroSleep());

        AntiBan.setHumanFatigue(MotherlodeMineXVariables.get().getSettings().isFatigue());
        Log.log("Fatigue - " + AntiBan.getHumanFatigue());

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
                    if (MotherlodeMineXVariables.get().getSettings().isDoNotUpgrade()) {
                        Worker.getInstance().setPickaxe(MotherlodeMineXVariables.get().getSettings().getDesiredPickaxe());
                    } else {
                        Worker.getInstance().setPickaxe(Workable.calculateOptimalPickAxeOnWorker(Worker.getInstance().getActualMiningLevel()).orElse(null));
                    }
                    // set random angle
                    Camera.setAngle(General.random(85, 100));
                    // set random zoom percent
                    //Camera.setZoomPercent(General.random(0, 50));
                    // reset the inventory once complete
                    GameTab.INVENTORY.open();
                    // display all fatigue multiples
                    if (MotherlodeMineXVariables.get().getSettings().isFatigue()) {
                        Log.log(AntiBan.getFatigue());
                    }
                    // set optimization true
                    MotherlodeMineXVariables.get().setOptimization(true);
                }
                do {
                    // perform the work
                    for (Work work : MotherlodeMineXVariables.get().getSettings().getMotherlodeWork()) {
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
                            nodes.add(new MineOreVein(work));
                            nodes.add(new DepositPayDirt(work));
                            nodes.add(new RepairBrokenStrut(work));
                            nodes.add(new Walking(work));
                            nodes.add(new Banking(work));
                            nodes.add(new SearchCrateForHammer(work));
                            nodes.add(new DepletePayDirtSack(work));
                            nodes.add(new RetrievePickAxeFromBank(work));
                            nodes.add(new RetrieveWorkerEquipment(work));
                            nodes.add(new WorldHop(work));
                        }
                        // loop while the work is validated (reached time or level)
                        while (!work.validate()) {
                            // execute the valid node within the given work
                            nodes.stream()
                                    .filter(Nodeable::validate)
                                    .findFirst()
                                    .ifPresent(Nodeable::execute);
                            //work.execute();
                            Waiting.waitUniform(100, 300);
                        }
                        Log.log("Work complete. Please be patient.");
                    }
                }
                // keep repeating the script if necessary
                while (MotherlodeMineXVariables.get().getSettings().isRepeat() || MotherlodeMineXVariables.get().getSettings().isRepeatShuffle());
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
}
