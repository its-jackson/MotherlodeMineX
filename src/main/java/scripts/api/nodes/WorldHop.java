package scripts.api.nodes;

import org.apache.commons.lang3.time.StopWatch;
import org.tribot.api.General;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.query.Query;

import scripts.MotherlodeMineXSettings;
import scripts.MotherlodeMineXVariables;
import scripts.api.PolymorphicArea;
import scripts.api.Work;
import scripts.api.antiban.AntiBan;
import scripts.api.interfaces.Nodeable;
import scripts.api.interfaces.Workable;

public class WorldHop implements Nodeable, Workable {

    private StopWatch stopWatch = new StopWatch();
    private long worldHopTimeInterval = General.randomLong(300000, 1200000); // 5 - 20 minutes

    private Work work;

    public WorldHop() {}

    public WorldHop(Work work) {
        this.work = work;
    }

    @Override
    public void execute() {
        MotherlodeMineXVariables vars = MotherlodeMineXVariables.get();
        int sleepTime = AntiBan.sleep(vars.getWaitTimes());

        if (MyPlayer.isMember()) {
            Worlds.getRandomMembers()
                    .ifPresent(world -> {
                        int worldNumber = world.getWorldNumber();
                        log("World hopping to members world " + worldNumber);
                        boolean result = WorldHopper.hop(world.getWorldNumber());
                        if (result) {
                            log("World hopped successful");
                        } else {
                            log("World hopped unsuccessful");
                        }
                    });
        } else {
            Worlds.getRandomNonMembers()
                    .ifPresent(world -> {
                        int worldNumber = world.getWorldNumber();
                        log("World hopping to non members world " + worldNumber);
                        boolean result = WorldHopper.hop(worldNumber);
                        if (result) {
                            log("World hopped successful");
                        } else {
                            log("World hopped unsuccessful");
                        }
                    });
        }
    }

    @Override
    public boolean validate() {
        MotherlodeMineXSettings settings = MotherlodeMineXVariables.get().getSettings();

        // start stop watch if not started
        if (getStopWatch().isStopped()) {
            getStopWatch().start();
        }
        // validate world hopping
        if (Login.isLoggedIn()) {
            PolymorphicArea polymorphicArea = getWork().getResourceLocation().getArea();
            if (polymorphicArea.containsMyPlayer()) {
                // world hop player count
                if (settings.isWorldHop()) {
                    int playerCount = Query.players()
                            .filter(player -> polymorphicArea.getWorldTiles().contains(player.getTile()))
                            .count();
                    if (playerCount >= settings.getWorldHopFactor()) {
                        return true;
                    }
                }
                // world hop random time
                if (settings.isWorldHopRandom()) {
                    if (getStopWatch().getTime() >= getWorldHopTimeInterval()) {
                        setWorldHopTimeInterval(General.randomLong(300000, 1200000));
                        getStopWatch().reset();
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public String name() {
        return "[World Hop Control]";
    }

    public StopWatch getStopWatch() {
        return stopWatch;
    }

    public void setStopWatch(StopWatch stopWatch) {
        this.stopWatch = stopWatch;
    }

    public long getWorldHopTimeInterval() {
        return worldHopTimeInterval;
    }

    public void setWorldHopTimeInterval(long worldHopTimeInterval) {
        this.worldHopTimeInterval = worldHopTimeInterval;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }
}
