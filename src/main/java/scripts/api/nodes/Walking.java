package scripts.api.nodes;

import org.tribot.script.sdk.Bank;
import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.MyPlayer;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.cache.BankCache;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.GameObject;
import scripts.MotherlodeMineXVariables;
import scripts.api.*;
import scripts.api.antiban.AntiBan;
import scripts.api.interfaces.Nodeable;
import scripts.api.interfaces.Workable;

import java.util.Optional;

public class Walking implements Nodeable, Workable {

    private Work work;

    private boolean walkToHopper;
    private boolean walkToOreVein;
    private boolean walkToBrokenStrut;
    private boolean walkToBank;
    private boolean walkToPayDirtSack;
    private boolean walkToHammerCrate;
    private boolean walkToBankRetrievePickAxe;
    private boolean walkToBankUpgradeAxe;
    private boolean walkToBankRetrieveEquipment;

    public Walking(Work work) {
        this.work = work;
    }

    public Walking() {}

    @Override
    public void execute() {
        MotherlodeMineXVariables vars = MotherlodeMineXVariables.get();
        int sleepTime = AntiBan.sleep(vars.getWaitTimes());

        boolean walkResult = false;

        if (isWalkToHopper()) {
            // walk to hopper
            log("Walking to hopper");
            if (ResourceLocation.MOTHERLODE_MINE_UPPER_LEVEL.getArea().containsMyPlayer()) {
                if (!climbMotherlodeMineLadderInside()) {
                    walkResult = walkToTile(LADDER_UPPER_WALK_TILE);
                }
            } else {
                walkResult = walkToTile(HOPPER_TILE);
            }
            setWalkToHopper(false);
        } else if (isWalkToOreVein()) {
            // walk to mining area
            log("Walking to ore vein");
            if (getWork().getResourceLocation().equals(ResourceLocation.MOTHERLODE_MINE_UPPER_LEVEL)) {
                walkResult = walkToMotherlodeMineUpperLevelOutside();
                boolean inLocationResult = Waiting.waitUntil(4000, () -> getWork().getResourceLocation().getArea().containsMyPlayer());
                if (inLocationResult) {
                    walkResult = walkToTile(getWork().getResourceLocation().getArea().getRandomTile());
                }
            } else {
                walkResult = walkToTile(getWork().getResourceLocation().getArea().getRandomTile());
            }
            setWalkToOreVein(false);
        } else if (isWalkToBrokenStrut()) {
            // walk to the broken strut
            log("Walking to broken strut");
            walkResult = walkToTile(STRUT_TILE_NORTH);
            setWalkToBrokenStrut(false);
        } else if (isWalkToBank()) {
            // walk to the bank
            log("Walking to bank");
            if (ResourceLocation.MOTHERLODE_MINE_UPPER_LEVEL.getArea().containsMyPlayer()) {
                if (!climbMotherlodeMineLadderInside()) {
                    walkResult = walkToTile(LADDER_UPPER_WALK_TILE);
                }
            } else {
                walkResult = walkToBank(getWork().getBankLocation());
            }
            setWalkToBank(false);
        } else if (isWalkToPayDirtSack()) {
            // walk to payDirtSack
            log("Walking to pay-dirt sack");
            walkResult = walkToTile(PAY_DIRT_SACK_TILE);
            setWalkToPayDirtSack(false);
        } else if (isWalkToHammerCrate()) {
            // walk to hammer crate
            log("Walking to hammer crate");
            if (ResourceLocation.MOTHERLODE_MINE_UPPER_LEVEL.getArea().containsMyPlayer()) {
                if (!climbMotherlodeMineLadderInside()) {
                    walkResult = walkToTile(LADDER_UPPER_WALK_TILE);
                }
            } else {
                walkResult = walkToTile(CRATE_TILE_NORTH);
            }
            setWalkToHammerCrate(false);
        } else if (isWalkToBankRetrievePickAxe()) {
            // walk to bank retrieve pickaxe
            log("Walking to bank - retrieving pickaxe");
            if (ResourceLocation.MOTHERLODE_MINE_UPPER_LEVEL.getArea().containsMyPlayer()) {
                if (!climbMotherlodeMineLadderInside()) {
                    walkResult = walkToTile(LADDER_UPPER_WALK_TILE);
                }
            } else {
                walkResult = walkToBank();
            }
            setWalkToBankRetrievePickAxe(false);
        } else if (isWalkToBankRetrieveEquipment()) {
            // walk to bank upgrade pickaxe
            log("Walking to bank - retrieving equipment");
            if (ResourceLocation.MOTHERLODE_MINE_UPPER_LEVEL.getArea().containsMyPlayer()) {
                if (!climbMotherlodeMineLadderInside()) {
                    walkResult = walkToTile(LADDER_UPPER_WALK_TILE);
                }
            } else {
                walkResult = walkToBank(getWork().getBankLocation());
            }
            setWalkToBankRetrieveEquipment(false);
        }

        if (walkResult) {
            log("Walking successful");
        } else {
            log("Walking unsuccessful");
        }
    }

    @Override
    public boolean validate() {
        if (!MyPlayer.isMoving()) {
            return shouldWalkToHopper()
                    || shouldWalkToOreVeins()
                    || shouldWalkToBrokenStrut()
                    || shouldWalkToBank()
                    || shouldWalkToPayDirtSack()
                    || shouldWalkToHammerCrate()
                    || shouldRetrievePickAxeFromBank()
                    || shouldRetrieveEquipmentFromBank()
                    ;
        }
        return false;
    }

    @Override
    public String name() {
        return "[Walking Control]";
    }

    // inventory is full of pay-dirt
    // not at the hopper
    // pay-dirt sack is not full
    private boolean shouldWalkToHopper() {
        if (inventoryFullPayDirt()) {
            if (!isAtMotherlodeMineHopper()) {
                if (!payDirtSackIsFull() && !payDirtSackAlmostFull(PayDirt.getFuturePayDirtSackCount())) {
                    setWalkToHopper(true);
                    return isWalkToHopper();
                }
            }
        }
        return false;
    }

    // not inside the mining area
    // inventory has pickaxe
    // inventory is not full
    // pay-dirt sack is not full
    private boolean shouldWalkToOreVeins() {
        if (!inventoryFullPayDirt()) {
            if (workerHasOptimalPickaxe(Worker.getInstance().getPickaxe())) {
                if (workerHasMotherlodeEquipment(MotherlodeMineXVariables.get().getSettings())) {
                    if (!workerIsInLocation(getWork())) {
                        if (inventoryContainsHammer()) {
                            if (!payDirtSackIsFull() && !payDirtSackAlmostFull(PayDirt.getFuturePayDirtSackCount())) {
                                setWalkToOreVein(true);
                                return isWalkToOreVein();
                            }
                        }
                    }
                }

            }
        }
        return false;
    }

    // inventory is full of pay-dirt
    // both struts are broken
    // not at broken struts
    // pay-dirt sack is not full
    private boolean shouldWalkToBrokenStrut() {
        if (inventoryFullPayDirt()) {
            if (isAllStrutsBroken()) {
                if (!payDirtSackIsFull()) {
                    if (!isAtBrokenStruts()) {
                        if (inventoryContainsHammer()) {
                            setWalkToBrokenStrut(true);
                            return isWalkToBrokenStrut();
                        }
                    }
                }
            }
        }
        return false;
    }

    // inventory full
    // inventory does not contain pay-dirt
    // not at the bank
    private boolean shouldWalkToBank() {
        if (inventoryFullContainsNoPayDirt()) {
            if (!isAtBank(getWork().getBankLocation())) {
                setWalkToBank(true);
                return isWalkToBank();
            }
        }
        return false;
    }

    // inventory not full
    // pay-dirt sack full or pay-dirt sack will be full
    // not at the pay-dirt sack
    private boolean shouldWalkToPayDirtSack() {
        if (!Inventory.isFull() && inventoryContainsHammer()) {
            if (payDirtSackIsFull() || payDirtSackAlmostFull(PayDirt.getFuturePayDirtSackCount())) {
                if (!isAtPayDirtSack()) {
                    setWalkToPayDirtSack(true);
                    return isWalkToPayDirtSack();
                }
            }
        }
        return false;
    }

    private boolean shouldWalkToHammerCrate() {
        // is not at crate
        // both struts are broken
        // pay-dirt sack is not full
        // inventory is full of pay-dirt
        // inventory does not contain hammer
        if (workerHasOptimalPickaxe(Worker.getInstance().getPickaxe())) {
            if (workerHasMotherlodeEquipment(MotherlodeMineXVariables.get().getSettings())) {
                if (!isAtHammerCrate()) {
                    if (!payDirtSackIsFull() && !payDirtSackAlmostFull(PayDirt.getFuturePayDirtSackCount())) {
                        if (!inventoryContainsHammer()) {
                            setWalkToHammerCrate(true);
                            return isWalkToHammerCrate();
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean shouldRetrievePickAxeFromBank() {
        if (!isAtBank(getWork().getBankLocation()) && Bank.isNearby()) {
            if (!workerHasOptimalPickaxe(Worker.getInstance().getPickaxe())) {
                setWalkToBankRetrievePickAxe(true);
                return isWalkToBankRetrievePickAxe();
            }
        }
        return false;
    }

    private boolean shouldUpgradePickAxeFromBank() {
        if (BankCache.isInitialized()) {
            log("Bank initialized");
            if (!MotherlodeMineXVariables.get().getSettings().isDoNotUpgrade()) {
                if (workerHasBetterPickAxe(Worker.getInstance().getActualMiningLevel(), Worker.getInstance().getPickaxe())
                        .isPresent()) {
                    if (!isAtBank(getWork().getBankLocation())) {
                        setWalkToBankUpgradeAxe(true);
                        return isWalkToBankUpgradeAxe();
                    }
                }
            }
        } else {
            log("Bank not initialized");
        }
        return false;
    }

    private boolean shouldRetrieveEquipmentFromBank() {
        // use gem bag or coal bag or mining gear
        // has optimal pickaxe
        // is at bank
        if (!isAtBank(getWork().getBankLocation())) {
            if (workerHasOptimalPickaxe(Worker.getInstance().getPickaxe())) {
                if (!workerHasMotherlodeEquipment(MotherlodeMineXVariables.get().getSettings())) {
                    setWalkToBankRetrieveEquipment(true);
                    return isWalkToBankRetrieveEquipment();
                }
            }
        }
        return false;
    }

    private boolean walkToMotherlodeMineUpperLevelOutside() {
        if (!climbMotherlodeMineLadderOutside()) {
            return walkToTile(LADDER_LOWER_WALK_TILE);
        }
        return false;
    }

    private boolean climbMotherlodeMineLadderOutside() {
        Optional<GameObject> ladderGameObject = Query.gameObjects()
                .idEquals(LADDER_LOWER_ID)
                .inArea(Area.fromRadius(LADDER_LOWER_TILE, 5))
                .isReachable()
                .findFirst();

        if (ladderGameObject.filter(this::interactGameObject).isPresent()) {
            return Waiting.waitUntil(() -> MyPlayer.getPosition().equals(LADDER_UPPER_WALK_TILE));
        }

        return false;
    }

    private boolean climbMotherlodeMineLadderInside() {
        Optional<GameObject> ladderGameObject = Query.gameObjects()
                .idEquals(LADDER_UPPER_ID)
                .inArea(Area.fromRadius(LADDER_UPPER_TILE, 5))
                .isReachable()
                .findFirst();

        if (ladderGameObject.filter(this::interactGameObject).isPresent()) {
            return Waiting.waitUntil(() -> MyPlayer.getPosition().equals(LADDER_LOWER_WALK_TILE));
        }

        return false;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public boolean isWalkToHopper() {
        return walkToHopper;
    }

    public void setWalkToHopper(boolean walkToHopper) {
        this.walkToHopper = walkToHopper;
    }

    public boolean isWalkToOreVein() {
        return walkToOreVein;
    }

    public void setWalkToOreVein(boolean walkToOreVein) {
        this.walkToOreVein = walkToOreVein;
    }

    public boolean isWalkToBrokenStrut() {
        return walkToBrokenStrut;
    }

    public void setWalkToBrokenStrut(boolean walkToBrokenStrut) {
        this.walkToBrokenStrut = walkToBrokenStrut;
    }

    public boolean isWalkToBank() {
        return walkToBank;
    }

    public void setWalkToBank(boolean walkToBank) {
        this.walkToBank = walkToBank;
    }

    public boolean isWalkToPayDirtSack() {
        return walkToPayDirtSack;
    }

    public void setWalkToPayDirtSack(boolean walkToPayDirtSack) {
        this.walkToPayDirtSack = walkToPayDirtSack;
    }

    public boolean isWalkToHammerCrate() {
        return walkToHammerCrate;
    }

    public void setWalkToHammerCrate(boolean walkToHammerCrate) {
        this.walkToHammerCrate = walkToHammerCrate;
    }

    public boolean isWalkToBankRetrievePickAxe() {
        return walkToBankRetrievePickAxe;
    }

    public void setWalkToBankRetrievePickAxe(boolean walkToBankRetrievePickAxe) {
        this.walkToBankRetrievePickAxe = walkToBankRetrievePickAxe;
    }

    public boolean isWalkToBankUpgradeAxe() {
        return walkToBankUpgradeAxe;
    }

    public void setWalkToBankUpgradeAxe(boolean walkToBankUpgradeAxe) {
        this.walkToBankUpgradeAxe = walkToBankUpgradeAxe;
    }

    public boolean isWalkToBankRetrieveEquipment() {
        return walkToBankRetrieveEquipment;
    }

    public void setWalkToBankRetrieveEquipment(boolean walkToBankRetrieveEquipment) {
        this.walkToBankRetrieveEquipment = walkToBankRetrieveEquipment;
    }
}
