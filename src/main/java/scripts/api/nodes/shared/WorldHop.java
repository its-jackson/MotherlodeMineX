package scripts.api.nodes.shared;

import org.apache.commons.lang3.time.StopWatch;
import org.tribot.api.General;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.query.Query;

import scripts.MotherlodeMineXVariables;
import scripts.api.PolymorphicArea;
import scripts.api.Worker;
import scripts.api.works.Mining;
import scripts.api.works.Work;
import scripts.api.antiban.AntiBan;
import scripts.api.interfaces.Nodeable;
import scripts.api.interfaces.Workable;

public class WorldHop implements Nodeable, Workable {

    private StopWatch stopWatch = new StopWatch();
    private long worldHopTimeInterval = General.randomLong(300000, 1200000); // 5 - 20 minutes
    private Work work;

    private final MotherlodeMineXVariables variables = MotherlodeMineXVariables.get();

    public WorldHop() {
    }

    public WorldHop(Work work) {
        this.work = work;
    }

    @Override
    public void execute() {
        int sleepTime = AntiBan.sleep(getVariables().getWaitTimes());
        String successful = "World hopped successful";
        String unsuccessful = "World hopped unsuccessful";

        if (MyPlayer.isMember()) {
            Worlds.getRandomMembers()
                    .ifPresent(world -> {
                        int worldNumber = world.getWorldNumber();
                        String worldHopMember = "World hopping to members world " + worldNumber;
                        log(worldHopMember);
                        getVariables().setState(worldHopMember);
                        boolean result = WorldHopper.hop(world.getWorldNumber());
                        if (result) {
                            log(successful);
                            getVariables().setState(successful);
                        } else {
                            log(unsuccessful);
                            getVariables().setState(unsuccessful);
                        }
                    });
        } else {
            Worlds.getRandomNonMembers()
                    .ifPresent(world -> {
                        int worldNumber = world.getWorldNumber();
                        String worldHopNonMember = "World hopping to non members world " + worldNumber;
                        log(worldHopNonMember);
                        getVariables().setState(worldHopNonMember);
                        boolean result = WorldHopper.hop(worldNumber);
                        if (result) {
                            log(successful);
                            getVariables().setState(successful);
                        } else {
                            log(unsuccessful);
                            getVariables().setState(unsuccessful);
                        }
                    });
        }
    }

    @Override
    public boolean validate() {
        // start stop watch if not started
        if (getStopWatch().isStopped()) {
            getStopWatch().start();
        }
        // validate world hopping
        if (Login.isLoggedIn()) {
            PolymorphicArea polymorphicArea = getWork().getResourceLocation().getPolymorphicArea();
            if (workerIsInLocation(getWork())) {
                // world hop player count
                if (getVariables().getSettings().isWorldHop()) {
                    int playerCount = Query.players()
                            .filter(player -> polymorphicArea.getWorldTiles().contains(player.getTile()))
                            .count();
                    if (playerCount >= getVariables().getSettings().getWorldHopFactor()) {
                        return true;
                    }
                }
                // world hop random time
                if (getVariables().getSettings().isWorldHopRandom()) {
                    if (getStopWatch().getTime() >= getWorldHopTimeInterval()) {
                        setWorldHopTimeInterval(General.randomLong(300000, 1200000));
                        getStopWatch().reset();
                        return true;
                    }
                }
                // world hop no resources - mining only right now
                if (getVariables().getSettings().isWorldHopNoResources() && getWork() instanceof Mining) {
                    if (!Inventory.isFull()) {
                        if (workerHasOptimalPickaxe(Worker.getInstance().getPickaxe())) {
                            return locateGameObject(getWork()).isEmpty();
                        }
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

    public MotherlodeMineXVariables getVariables() {
        return variables;
    }
}
