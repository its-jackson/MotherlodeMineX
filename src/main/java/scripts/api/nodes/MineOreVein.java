package scripts.api.nodes;

import org.tribot.api2007.PathFinding;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.GameObject;
import org.tribot.script.sdk.types.WorldTile;
import scripts.MotherlodeMineXVariables;
import scripts.api.ResourceLocation;
import scripts.api.Worker;
import scripts.api.antiban.AntiBan;
import scripts.api.interfaces.Nodeable;
import scripts.api.Work;
import scripts.api.interfaces.Workable;

import java.util.ArrayList;

public class MineOreVein implements Nodeable, Workable {

    private final long start_time = System.currentTimeMillis();

    private Work work;
    private GameObject currentOreVein;
    private UpgradePickaxeFromBank upgradePickAxe;
    private WorldHop worldHop;

    public MineOreVein(Work work, UpgradePickaxeFromBank upgradePickAxe) {
        this.work = work;
        this.upgradePickAxe = upgradePickAxe;
    }

    public MineOreVein(Work work) {
        this.work = work;
        this.upgradePickAxe = new UpgradePickaxeFromBank(work);
    }

    public MineOreVein() {}

    @Override
    public void execute() {
        // sleep before executing
        MotherlodeMineXVariables vars = MotherlodeMineXVariables.get();
        int sleepTime = AntiBan.sleep(vars.getWaitTimes());

        ResourceLocation location = getWork().getResourceLocation();

        // equip optimal pickaxe before mining

        // open up the gem bag before mining
        boolean openGemBagResult = openGemBag();

        if (openGemBagResult) {
            log("Opened gem bag successful");
        }

        // open up coal bag before mining
        boolean openCoalBagResult = openCoalBag();

        if (openCoalBagResult) {
            log("Opened coal bag successful");
        }

        log(State.LOCATING_ORE_VEIN + ": " + location);

        // set the ore vein to be mined based on actual work location
        if (location.equals(ResourceLocation.MOTHERLODE_MINE_UPPER_LEVEL)) {
            Query.gameObjects()
                    .nameEquals(getWork().getResource().getResource())
                    .filter(gameObject -> {
                        WorldTile worldTile = gameObject.getTile();
                        int x = worldTile.getX();
                        int y = worldTile.getY();
                        int plane = worldTile.getPlane();
                        // validate tiles inside the upper area
                        if (y != 5687) {
                            if (y != 5686) {
                                if (y != 5685) {
                                    if (y != 5682) {
                                        if (y != 5681) {
                                            if (y != 5680) {
                                                if (y != 5668) {
                                                    if (y != 5667) {
                                                        if (y != 5660) {
                                                            if (y != 5657) {
                                                                return PathFinding.canReach(new RSTile(x, y, plane), true);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        return false;
                    })
                    .findClosest()
                    .ifPresent(this::setCurrentOreVein);
        } else {
            Query.gameObjects()
                    .nameEquals(getWork().getResource().getResource())
                    .filter(gameObject -> {
                        WorldTile worldTile = gameObject.getTile();
                        int x = worldTile.getX();
                        int y = worldTile.getY();
                        int plane = worldTile.getPlane();
                        ArrayList<WorldTile> workTiles = getWork()
                                .getResourceLocation()
                                .getArea()
                                .getWorldTiles();
                        // validate lower level tiles
                        for (WorldTile tile : workTiles) {
                            if (worldTile.distanceTo(tile) < 2) {
                                return PathFinding.canReach(new RSTile(x, y, plane), true);
                            }
                        }
                        return false;
                    })
                    .findClosest()
                    .ifPresent(this::setCurrentOreVein);
        }

        if (getCurrentOreVein() != null) {
            log(State.LOCATING_ORE_VEIN_SUCCESS.getState());
            // clear rockfall if in the way of mining ore vein
            // perform special attack if able before mining
            boolean specialAttackResult = performSpecialAttack();
            // interact with ore vein
            boolean clickResult = interactGameObject(getCurrentOreVein());
            // if click true, wait until animating and complete the task
            if (clickResult) {
                log(State.CLICK_ORE_VEIN_SUCCESS.getState());
                // wait until not animating
                boolean timeResult = Waiting.waitUntil(1200, () -> !MyPlayer.isAnimating());
                // wait until animating
                if (Waiting.waitUntilAnimating(7000)) {
                    completeMiningTask();
                }
            } else {
                // failed to interact with ore vein
            }
        } else {
            // failed to locate game object
        }
        // destroy the game object
        // Generate the trackers
        AntiBan.generateTrackers((int) (System.currentTimeMillis() - this.start_time), false);
    }

    @Override
    public boolean validate() {
        // inside the mining area
        // ore vein exists inside the mining area
        // inventory has pickaxe
        // inventory is not full
        // pay-dirt sack is not full
        return shouldMineOreVein();
    }

    @Override
    public String name() {
        return "[Ore Vein Control]";
    }

    // special attack, antiban, game tab, world hop
    private void completeMiningTask() {
        int timeOut = 0;

         // set world hop node
        MotherlodeMineXVariables.get()
                .getNodes()
                .stream()
                .filter(node -> node instanceof WorldHop)
                .findFirst()
                .ifPresent(node -> setWorldHop((WorldHop) node));

        while (timeOut < 15) {
            Waiting.waitUniform(1500, 2500);
            log(State.MINING_ORE_VEIN.getState());
            while (!MyPlayer.isAnimating()) {
                Waiting.waitUniform(100, 300);
                timeOut++;
                if (timeOut > 15) {
                    break;
                }
            }
            // world hop
            if (getWorldHop() != null && getWorldHop().validate()) {
                getWorldHop().execute();
            }
            Waiting.waitUniform(100, 300);
            // do special attack if able
            boolean specialAttackResult = performSpecialAttack();
            Waiting.waitUniform(100, 300);
            // reset game tab if not open
            boolean gameTabResult = resetGameTab();
            Waiting.waitUniform(100, 300);
            // perform AntiBan
            AntiBan.checkAntiBanTask(getCurrentOreVein());
        }
    }

    private boolean shouldMineOreVein() {
        if (!inventoryFullPayDirt()) {
            if (workerHasMotherlodeEquipment(MotherlodeMineXVariables.get().getSettings())) {
                if (workerHasOptimalPickaxe(Worker.getInstance().getPickaxe())) {
                    if (inventoryContainsHammer()) {
                        if (workerIsInLocation(getWork())) {
                            return !payDirtSackIsFull();
                        }
                    }
                }
            }
        }
        return false;
    }

    public GameObject getCurrentOreVein() {
        return currentOreVein;
    }

    public void setCurrentOreVein(GameObject currentOreVein) {
        this.currentOreVein = currentOreVein;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public UpgradePickaxeFromBank getUpgradePickAxe() {
        return upgradePickAxe;
    }

    public void setUpgradePickAxe(UpgradePickaxeFromBank upgradePickAxe) {
        this.upgradePickAxe = upgradePickAxe;
    }

    public WorldHop getWorldHop() {
        return worldHop;
    }

    public void setWorldHop(WorldHop worldHop) {
        this.worldHop = worldHop;
    }
}
