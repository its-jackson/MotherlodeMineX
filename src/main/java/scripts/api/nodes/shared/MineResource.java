package scripts.api.nodes.shared;

import org.tribot.api2007.PathFinding;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.painting.Painting;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.GameObject;
import org.tribot.script.sdk.types.Model;
import org.tribot.script.sdk.types.WorldTile;
import scripts.MotherlodeMineXVariables;
import scripts.api.enums.ResourceLocation;
import scripts.api.Worker;
import scripts.api.antiban.AntiBan;
import scripts.api.interfaces.Nodeable;
import scripts.api.works.MotherlodeMine;
import scripts.api.works.Work;
import scripts.api.interfaces.Workable;

import java.util.List;

public class MineResource implements Nodeable, Workable {

    private final long start_time = System.currentTimeMillis();
    private final MotherlodeMineXVariables variables = MotherlodeMineXVariables.get();

    private Work work;
    private GameObject currentResource;
    private UpgradePickaxeFromBank upgradePickAxe;
    private WorldHop worldHop;

    public MineResource(Work work) {
        this.work = work;
        this.upgradePickAxe = new UpgradePickaxeFromBank(work);
    }

    public MineResource() {}

    @Override
    public void execute() {
        // sleep before executing
        int sleepTime = AntiBan.sleep(getVariables().getWaitTimes());
        // location
        ResourceLocation location = getWork().getResourceLocation();
        // log
        String locatingState = "Locating " + getWork().getResource() + " " + location;
        log(locatingState);
        getVariables().setState(locatingState);
        // equip optimal pickaxe before mining
        //
        if (getWork() instanceof MotherlodeMine) {
            // do motherlode work
            // open up the gem bag before mining
            boolean openGemBagResult = openGemBag();
            if (openGemBagResult) {
                String open = "Opened gem bag successful";
                log(open);
                getVariables().setState(open);
            }
            // open up coal bag before mining
            boolean openCoalBagResult = openCoalBag();
            if (openCoalBagResult) {
                String open = "Opened coal bag successful";
                log(open);
                getVariables().setState(open);
            }
            // set the ore vein to be mined based on actual work location
            if (location.equals(ResourceLocation.MOTHERLODE_MINE_UPPER_LEVEL)) {
                Query.gameObjects()
                        .nameEquals(getWork().getResource().getResourceName())
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
                                                                    //return LocalWalking.createMap().canReach(worldTile);
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
                        .ifPresent(this::setCurrentResource);
            } else {
                Query.gameObjects()
                        .nameEquals(getWork().getResource().getResourceName())
                        .filter(gameObject -> {
                            WorldTile worldTile = gameObject.getTile();
                            int x = worldTile.getX();
                            int y = worldTile.getY();
                            int plane = worldTile.getPlane();
                            List<WorldTile> workTiles = getWork()
                                    .getResourceLocation()
                                    .getPolymorphicArea()
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
                        .ifPresent(this::setCurrentResource);
            }
        } else {
            // do mining work
            // set the rock
            locateGameObject(getWork())
                    .ifPresent(this::setCurrentResource);
        }

        if (getCurrentResource() != null) {
            String locatedSuccessful = "Located " + getWork().getResource() + " successful";
            log(locatedSuccessful);
            getVariables().setState(locatedSuccessful);
            // perform special attack if able before mining
            boolean specialAttackResult = performSpecialAttack();
            if (specialAttackResult) {
                String activatedSpecialAttack = "Activated special attack successful";
                log(activatedSpecialAttack);
                getVariables().setState(activatedSpecialAttack);
            }
            // interact with resource
            boolean clickResult = interactGameObject(getCurrentResource());
            // if click true, wait until animating and complete the task
            if (clickResult) {
                String clickSuccess = "Clicked " +  getWork().getResource() + " successful";
                log(clickSuccess);
                getVariables().setState(clickSuccess);
                // wait until not animating
                boolean timeResult = Waiting.waitUntil(1200, () -> !MyPlayer.isAnimating());
                // wait until animating
                if (Waiting.waitUntilAnimating(7000)) {
                    completeMiningTask(getWork());
                }
            } else {
                // failed to interact with resource
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
        if (getWork() instanceof MotherlodeMine) {
            return shouldMineOreVein(getWork());
        } else {
            return shouldMineRock(getWork());
        }
    }

    @Override
    public String name() {
        return "[Resource Control]";
    }

    // special attack, antiban, game tab, world hop
    private void completeMiningTask(Work work) {
        // state
        String miningResource = "Mining " + getWork().getResource();

         // set world hop node
        getVariables()
                .getNodes()
                .stream()
                .filter(node -> node instanceof WorldHop)
                .findFirst()
                .ifPresent(node -> setWorldHop((WorldHop) node));

        // set profit before
        int goldGainedBefore = calculateGoldGained();

        if (work instanceof MotherlodeMine) {
            int timeOut = 0;
            int cap = 15;

            while (timeOut < cap) {
                log(miningResource);
                getVariables().setState(miningResource);
                timeOut = 0;
                while (!MyPlayer.isAnimating()) {
                    Waiting.waitUniform(60, 100);
                    ++timeOut;
                    if (timeOut > cap) {
                        break;
                    }
                }
                performLoopingChecks();
            }
        } else {
            while (MyPlayer.isAnimating()) {
                log(miningResource);
                getVariables().setState(miningResource);
                performLoopingChecks();
            }
            // set profit after
            int goldGainedAfter = calculateGoldGained();
            // set gold gained by subtraction
            Worker.getInstance().setGoldGained(goldGainedAfter - goldGainedBefore);
        }

        // remove paint for game object
        Painting.removePaint(graphics2D -> {
            getCurrentResource()
                    .getModel()
                    .flatMap(Model::getBounds)
                    .ifPresent(graphics2D::drawPolygon);
        });
    }

    private void performLoopingChecks() {
        // world hop
        if (getWorldHop() != null && getWorldHop().validate()) {
            getWorldHop().execute();
            Waiting.waitUniform(100, 300);
        }
        // do special attack if able
        boolean specialAttackResult = performSpecialAttack();
        if (specialAttackResult) {
            log("Activated special attack successful");
            getVariables().setState("Activated special attack successful");
            Waiting.waitUniform(100, 300);
        }
        // reset game tab if not open
        boolean gameTabResult = resetGameTab();
        if (gameTabResult) {
            Waiting.waitUniform(100, 300);
        }
        // perform AntiBan
        AntiBan.checkAntiBanTask(getCurrentResource());
        Waiting.waitUniform(500, 1000);
    }

    private boolean shouldMineOreVein(Work work) {
        if (!Inventory.isFull()) {
            if (workerHasMotherlodeEquipment(getVariables().getSettings())) {
                if (workerHasOptimalPickaxe(Worker.getInstance().getPickaxe())) {
                    if (inventoryContainsHammer()) {
                        if (workerIsInLocation(work)) {
                            return !payDirtSackIsFull();
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean shouldMineRock(Work work) {
        if (!Inventory.isFull()) {
            if (workerHasOptimalPickaxe(Worker.getInstance().getPickaxe())) {
                if (workerIsInLocation(work)) {
                    return locateGameObject(work).isPresent();
                }
            }
        }
        return false;
    }

    public GameObject getCurrentResource() {
        return currentResource;
    }

    public void setCurrentResource(GameObject currentResource) {
        this.currentResource = currentResource;
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
