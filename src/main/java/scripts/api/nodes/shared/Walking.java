package scripts.api.nodes.shared;

import org.tribot.api2007.PathFinding;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.cache.BankCache;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.GameObject;
import scripts.MotherlodeMineXVariables;
import scripts.api.*;
import scripts.api.antiban.AntiBan;
import scripts.api.enums.ResourceLocation;
import scripts.api.enums.ResourceOption;
import scripts.api.interfaces.Nodeable;
import scripts.api.interfaces.Workable;
import scripts.api.works.MotherlodeMine;
import scripts.api.works.Work;

import java.util.Optional;

public class Walking implements Nodeable, Workable {

    private Work work;

    private final MotherlodeMineXVariables variables = MotherlodeMineXVariables.get();

    // mining
    private boolean walkToRock;
    private boolean walkToBankMining;

    // motherlode mine
    private boolean walkToHopper;
    private boolean walkToOreVein;
    private boolean walkToBrokenStrut;
    private boolean walkToBankMotherlode;
    private boolean walkToPayDirtSack;
    private boolean walkToHammerCrate;
    private boolean walkToBankRetrievePickAxe;
    private boolean walkToBankUpgradeAxe;
    private boolean walkToBankRetrieveEquipment;

    public Walking(Work work) {
        this.work = work;
    }

    public Walking() {
    }

    @Override
    public void execute() {
        AntiBan.sleep(getVariables().getWaitTimes());

        String walkingToBank = "Walking to bank";
        String walkingToBankPickaxe = "Walking to bank - retrieving pickaxe";
        String successful = "Walking successful";
        String unsuccessful = "Walking unsuccessful";

        boolean walkResult = false;

        if (getWork() instanceof MotherlodeMine) {
            String walkingToHopper = "Walking to hopper";
            String walkingToOreVein = "Walking to ore vein";
            String walkingToBrokenStrut = "Walking to broken strut";
            String walkingToPayDirtSack = "Walking to pay-dirt sack";
            String walkingToHammerCrate = "Walking to hammer crate";
            String walkingToBankEquipment = "Walking to bank - retrieving equipment";
            // do motherlode work
            if (isWalkToHopper()) {
                // walk to hopper
                log(walkingToHopper);
                getVariables().setState(walkingToHopper);
                if (ResourceLocation.MOTHERLODE_MINE_UPPER_LEVEL.getPolymorphicArea().containsMyPlayer()) {
                    if (!climbMotherlodeMineLadderInside()) {
                        walkResult = walkToTile(LADDER_UPPER_WALK_TILE);
                    }
                } else {
                    walkResult = walkToTile(HOPPER_TILE);
                }
                setWalkToHopper(false);
            } else if (isWalkToOreVein()) {
                // walk to mining area
                log(walkingToOreVein);
                getVariables().setState(walkingToOreVein);
                if (getWork().getResourceLocation().equals(ResourceLocation.MOTHERLODE_MINE_UPPER_LEVEL)) {
                    walkResult = walkToMotherlodeMineUpperLevelOutside();
                    boolean inLocationResult = Waiting.waitUntil(4000, () -> getWork().getResourceLocation().getPolymorphicArea().containsMyPlayer());
                    if (inLocationResult) {
                        walkResult = walkToTile(getWork().getResourceLocation().getPolymorphicArea().getRandomTile());
                    }
                } else {
                    walkResult = walkToTile(getWork().getResourceLocation().getPolymorphicArea().getRandomTile());
                }
                setWalkToOreVein(false);
            } else if (isWalkToBrokenStrut()) {
                // walk to the broken strut
                log(walkingToBrokenStrut);
                getVariables().setState(walkingToBrokenStrut);
                walkResult = walkToTile(STRUT_TILE_NORTH);
                setWalkToBrokenStrut(false);
            } else if (isWalkToBankMotherlode()) {
                // walk to the bank
                log(walkingToBank);
                if (ResourceLocation.MOTHERLODE_MINE_UPPER_LEVEL.getPolymorphicArea().containsMyPlayer()) {
                    if (!climbMotherlodeMineLadderInside()) {
                        walkResult = walkToTile(LADDER_UPPER_WALK_TILE);
                    }
                } else {
                    walkResult = walkToBank(getWork().getBankLocation());
                }
                setWalkToBankMotherlode(false);
            } else if (isWalkToPayDirtSack()) {
                // walk to payDirtSack
                log(walkingToPayDirtSack);
                getVariables().setState(walkingToPayDirtSack);
                walkResult = walkToTile(PAY_DIRT_SACK_TILE);
                setWalkToPayDirtSack(false);
            } else if (isWalkToHammerCrate()) {
                // walk to hammer crate
                log(walkingToHammerCrate);
                getVariables().setState(walkingToHammerCrate);
                if (ResourceLocation.MOTHERLODE_MINE_UPPER_LEVEL.getPolymorphicArea().containsMyPlayer()) {
                    if (!climbMotherlodeMineLadderInside()) {
                        walkResult = walkToTile(LADDER_UPPER_WALK_TILE);
                    }
                } else {
                    walkResult = walkToTile(CRATE_TILE_NORTH);
                }
                setWalkToHammerCrate(false);
            } else if (isWalkToBankRetrieveEquipment()) {
                // walk to bank upgrade pickaxe
                log(walkingToBankEquipment);
                getVariables().setState(walkingToBankEquipment);
                if (ResourceLocation.MOTHERLODE_MINE_UPPER_LEVEL.getPolymorphicArea().containsMyPlayer()) {
                    if (!climbMotherlodeMineLadderInside()) {
                        walkResult = walkToTile(LADDER_UPPER_WALK_TILE);
                    }
                } else {
                    walkResult = walkToBank(getWork().getBankLocation());
                }
                setWalkToBankRetrieveEquipment(false);
            }

        } else {
            // do mining work
            String walkingToRock = "Walking to rock";

            if (isWalkToRock()) {
                // walk to mining area
                log(walkingToRock);
                getVariables().setState(walkingToRock);
                if (ResourceLocation.MOTHERLODE_MINE_UPPER_LEVEL.getPolymorphicArea().containsMyPlayer()) {
                    if (!climbMotherlodeMineLadderInside()) {
                        walkResult = walkToTile(LADDER_UPPER_WALK_TILE);
                    }
                } else {
                    walkResult = walkToTile(getWork().getResourceLocation().getPolymorphicArea().getRandomTile());
                }
                setWalkToRock(false);
            } else if (isWalkToBankMining()) {
                log(walkingToBank);
                getVariables().setState(walkingToBank);
                if (ResourceLocation.MOTHERLODE_MINE_UPPER_LEVEL.getPolymorphicArea().containsMyPlayer()) {
                    if (!climbMotherlodeMineLadderInside()) {
                        walkResult = walkToTile(LADDER_UPPER_WALK_TILE);
                    }
                } else {
                    walkResult = walkToBank(getWork().getBankLocation());
                }
            }
        }

        // walk to bank get pickaxe
        if (isWalkToBankRetrievePickAxe()) {
            // walk to bank retrieve pickaxe
            log(walkingToBankPickaxe);
            getVariables().setState(walkingToBankPickaxe);
            if (ResourceLocation.MOTHERLODE_MINE_UPPER_LEVEL.getPolymorphicArea().containsMyPlayer()) {
                if (!climbMotherlodeMineLadderInside()) {
                    walkResult = walkToTile(LADDER_UPPER_WALK_TILE);
                }
            } else {
                walkResult = walkToBank();
            }
            setWalkToBankRetrievePickAxe(false);
        }

        if (walkResult) {
            log(successful);
            getVariables().setState(successful);
        } else {
            log(unsuccessful);
            getVariables().setState(unsuccessful);
        }
    }

    @Override
    public boolean validate() {
        if (getWork() instanceof MotherlodeMine) {
            return shouldWalkMotherlodeMine();
        } else {
            return shouldWalkMining();
        }
    }

    @Override
    public String name() {
        return "[Walking Control]";
    }

    private boolean shouldWalkMining() {
        if (!MyPlayer.isMoving()) {
            return shouldWalkToRocks() || shouldWalkToBankMining() || shouldRetrievePickAxeFromBank();
        }
        return false;
    }

    private boolean shouldWalkToRocks() {
        if (!Inventory.isFull()) {
            if (workerHasOptimalPickaxe(Worker.getInstance().getPickaxe())) {
                if (!workerIsInLocation(getWork())) {
                    setWalkToRock(true);
                    return isWalkToRock();
                }
            }
        }
        return false;
    }

    private boolean shouldWalkToBankMining() {
        if (Inventory.isFull()) {
            if (workerHasOptimalPickaxe(Worker.getInstance().getPickaxe())) {
                if (getWork().getResourceOption().equals(ResourceOption.BANK)) {
                    if (!isAtBank(getWork().getBankLocation())) {
                        setWalkToBankMining(true);
                        return isWalkToBankMining();
                    }
                }
            }
        }
        return false;
    }

    private boolean shouldWalkMotherlodeMine() {
        if (!MyPlayer.isMoving()) {
            return shouldWalkToHopper()
                    || shouldWalkToOreVeins()
                    || shouldWalkToBrokenStrut()
                    || shouldWalkToBankMotherlode()
                    || shouldWalkToPayDirtSack()
                    || shouldWalkToHammerCrate()
                    || shouldRetrievePickAxeFromBank()
                    || shouldRetrieveEquipmentFromBank()
                    ;
        }
        return false;
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
        if (!Inventory.isFull()) {
            if (workerHasOptimalPickaxe(Worker.getInstance().getPickaxe())) {
                if (workerHasMotherlodeEquipment(getVariables().getSettings())) {
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
    private boolean shouldWalkToBankMotherlode() {
        if (inventoryFullContainsNoPayDirt()) {
            if (!isAtBank(getWork().getBankLocation())) {
                setWalkToBankMotherlode(true);
                return isWalkToBankMotherlode();
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
            if (workerHasMotherlodeEquipment(getVariables().getSettings())) {
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
        if (!isAtBank(getWork().getBankLocation()) && !Bank.isNearby()) {
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
            if (!getVariables().getSettings().isDoNotUpgrade()) {
                if (workerHasBetterPickAxe(Skill.MINING.getActualLevel(), Worker.getInstance().getPickaxe())
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
                if (!workerHasMotherlodeEquipment(getVariables().getSettings())) {
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
                .filter(gameObject -> PathFinding.canReach(new RSTile(gameObject.getTile().getX(), gameObject.getTile().getY(), gameObject.getTile().getPlane()), true))
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
                .filter(gameObject -> PathFinding.canReach(new RSTile(gameObject.getTile().getX(), gameObject.getTile().getY(), gameObject.getTile().getPlane()), true))
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

    public MotherlodeMineXVariables getVariables() {
        return variables;
    }

    public boolean isWalkToRock() {
        return walkToRock;
    }

    public void setWalkToRock(boolean walkToRock) {
        this.walkToRock = walkToRock;
    }

    public boolean isWalkToBankMining() {
        return walkToBankMining;
    }

    public void setWalkToBankMining(boolean walkToBankMining) {
        this.walkToBankMining = walkToBankMining;
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

    public boolean isWalkToBankMotherlode() {
        return walkToBankMotherlode;
    }

    public void setWalkToBankMotherlode(boolean walkToBankMotherlode) {
        this.walkToBankMotherlode = walkToBankMotherlode;
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
