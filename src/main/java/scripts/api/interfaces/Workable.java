package scripts.api.interfaces;

import dax.api_lib.models.RunescapeBank;
import org.tribot.api2007.Banking;
import org.tribot.api2007.types.RSTile;
import org.tribot.api2007.types.RSVarBit;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.cache.BankCache;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.*;
import org.tribot.script.sdk.walking.GlobalWalking;
import org.tribot.script.sdk.walking.WalkState;
import scripts.MotherlodeMineXSettings;
import scripts.api.ResourceLocation;
import scripts.api.Work;
import scripts.api.antiban.AntiBan;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Workable {

    enum State {
        MINING_ORE_VEIN("Mining ore vein"),
        LOCATING_ORE_VEIN("Locating ore vein"),
        LOCATING_ORE_VEIN_SUCCESS("Located ore vein successful"),
        CLICK_ORE_VEIN_SUCCESS("Clicked ore vein successful");

        private final String state;

        State(String state) {
            this.state = state;
        }

        public String getState() {
            return state;
        }
    }

    enum PickAxe {
        BRONZE_PICKAXE(1265, 1),
        IRON_PICKAXE(1267, 1),
        STEEL_PICKAXE(1269, 5),
        BLACK_PICKAXE(12297, 10),
        MITHRIL_PICKAXE(1273, 21),
        ADAMANT_PICKAXE(1271, 31),
        RUNE_PICKAXE(1275, 41),
        DRAGON_PICKAXE(11920, 61),
        DRAGON_PICKAXE_UPGRADED(12797, 61),
        DRAGON_PICKAXE_OR(23677, 61),
        INFERNAL_PICKAXE(13243, 61),
        INFERNAL_PICKAXE_UNCHARGED(13244, 61),
        INFERNAL_PICKAXE_OR(25063, 61),
        THIRD_AGE_PICKAXE(20014, 61),
        GILDED_PICKAXE(23276, 41),
        TRAILBLAZER_PICKAXE(25112, 1),
        CRYSTAL_PICKAXE(23680, 71),
        CRYSTAL_PICKAXE_UNCHARGED(23682, 71);

        private final int pick_axe_id;
        private final int pick_axe_level;

        PickAxe(int pickAxeId, int pickAxeLevel) {
            this.pick_axe_id = pickAxeId;
            this.pick_axe_level = pickAxeLevel;
        }

        public int getPickAxeId() {
            return pick_axe_id;
        }

        public int getPickAxeLevel() {
            return pick_axe_level;
        }
    }

    // critical game object ids
    int HOPPER_ID = 26674;
    int BROKEN_STRUT_ID = 26670;
    int PAY_DIRT_SACK_ID = 26688;
    int CRATE_ID = 357;
    int LADDER_LOWER_ID = 19044;
    int LADDER_UPPER_ID = 19045;

    // critical game item ids
    int HAMMER = 2347;
    int GEM_BAG = 12020;
    int GEM_BAG_OPEN = 24481;
    int COAL_BAG = 12019;
    int COAL_BAG_OPEN = 24480;
    int PROSPECTOR_HELMET = 12013;
    int PROSPECTOR_JACKET = 12014;
    int PROSPECTOR_LEGS = 12015;
    int PROSPECTOR_BOOTS = 12016;
    int MINING_CAPE_UNTRIMMED = 9792;
    int MINING_CAPE_TRIMMED = 9793;

    // critical game object tiles
    WorldTile STRUT_TILE_NORTH = new WorldTile(3742, 5669, 0);
    WorldTile STRUT_TILE_SOUTH = new WorldTile(3742, 5663, 0);
    WorldTile HOPPER_TILE = new WorldTile(3748, 5672, 0);
    WorldTile PAY_DIRT_SACK_TILE = new WorldTile(3748, 5659, 0);
    WorldTile CRATE_TILE_NORTH = new WorldTile(3752, 5674, 0);
    WorldTile CRATE_TILE_SOUTH = new WorldTile(3752, 5664, 0);
    WorldTile LADDER_LOWER_TILE = new WorldTile(3755, 5673, 0);
    WorldTile LADDER_UPPER_TILE = new WorldTile(3755, 5674, 0);
    WorldTile LADDER_UPPER_WALK_TILE = new WorldTile(3755, 5675, 0);
    WorldTile LADDER_LOWER_WALK_TILE = new WorldTile(3755, 5672, 0);

    // critical maximum amount of pay-dirt that can be collected (soft)
    int MAX_PAY_DIRT_COUNT_NORMAL = 81;
    int MAX_PAY_DIRT_COUNT_UPGRADE = 162;

    // critical motherlode sack varbits
    // thanks breaker for giving me these varbits
    int SACK_NORMAL = 5558;
    int SACK_UPGRADED = 5556;

    // pay-dirt in the water
    int PAY_DIRT_WATER_NPC = 6564;

    /**
     * Walks to the desired WorldTile using DaxWalker
     *
     * @param tile
     * @return
     */
    default boolean walkToTile(WorldTile tile) {
        AntiBan.activateRun();
        return GlobalWalking.walkTo(tile, () -> {
            AntiBan.activateRun();
            if (MyPlayer.getPosition().distanceTo(tile) < 7) {
                return WalkState.SUCCESS;
            } else {
                return WalkState.CONTINUE;
            }
        });
    }

    /**
     * Walks to the desired RunescapeBank using DaxWalker
     *
     * @param bank
     * @return
     */
    default boolean walkToBank(RunescapeBank bank) {
        // old RSTile
        RSTile oldRSTile = bank.getPosition();
        int x = oldRSTile.getX();
        int y = oldRSTile.getY();
        int plane = oldRSTile.getPlane();
        // construct the WorldTile
        WorldTile bankTile = new WorldTile(x, y, plane);
        // walk to bank tile
        AntiBan.activateRun();
        return GlobalWalking.walkTo(bankTile, () -> {
            AntiBan.activateRun();
            if (MyPlayer.getPosition().distanceTo(bankTile) < 7) {
                return WalkState.SUCCESS;
            } else {
                return WalkState.CONTINUE;
            }
        });
    }

    default boolean walkToBank() {
        AntiBan.activateRun();
        return GlobalWalking.walkToBank(() -> {
            AntiBan.activateRun();
            if (Bank.isNearby()) {
                return WalkState.SUCCESS;
            } else {
                return WalkState.CONTINUE;
            }
        });
    }

    /**
     * Determines if the bankcache contains a better axe in comparison to pickaxe level and
     * mining level to the worker.
     *
     * @return The optional and optimal pickaxe; or empty optional if no better pickaxe.
     */
    default Optional<PickAxe> workerHasBetterPickAxe(int miningLevel, PickAxe workerPickAxe) {
        if (workerPickAxe != null) {
            int currentPickAxeId = workerPickAxe.getPickAxeId();
            int currentPickAxeLevel = workerPickAxe.getPickAxeLevel();

            for (PickAxe pickaxe : PickAxe.values()) {
                Optional<Integer> bankPickAxe = BankCache.entries()
                        .stream()
                        .map(Map.Entry::getKey)
                        .filter(integerIntegerEntry -> integerIntegerEntry == pickaxe.getPickAxeId())
                        .findFirst();
                if (bankPickAxe.isPresent()) {
                    int bankPickAxeLevel = pickaxe.getPickAxeLevel();
                    if (miningLevel > bankPickAxeLevel) {
                        if (bankPickAxeLevel > currentPickAxeLevel) {
                            return Optional.of(pickaxe);
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }

    /**
     * @return True if the worker contains the optimal pickaxe; false otherwise
     */
    default boolean workerHasOptimalPickaxe(PickAxe pickaxe) {
        if (pickaxe == null) {
            return false;
        }
        int pickaxeId = pickaxe.getPickAxeId();
        return Inventory.contains(pickaxeId) || Equipment.contains(pickaxeId);
    }

    default boolean workerHasMotherlodeEquipment(MotherlodeMineXSettings settings) {
        if (settings.isUseGemBag()) {
            if (!inventoryContainsGemBag()) {
                return false;
            }
        }
        if (settings.isUseCoalBag()) {
            if (!inventoryContainsCoalBag()) {
                return false;
            }
        }
        if (settings.isWearProspectorEquipment()) {
            if (!workerHasFullProspectorEquipped()) {
                return false;
            }
        }
        return true;
    }

    default boolean workerHasFullProspectorEquipped() {
        return Equipment.contains(PROSPECTOR_HELMET, PROSPECTOR_JACKET, PROSPECTOR_LEGS, PROSPECTOR_BOOTS);
    }

    /**
     * @param work
     * @return True if the worker is in the resource location; false otherwise
     */
    default boolean workerIsInLocation(Work work) {
        return work.getResourceLocation()
                .getArea()
                .containsMyPlayer();
    }

    /**
     * @param resource
     * @return True if the resource was clicked successfully; false otherwise
     */
    default boolean interactGameObject(GameObject resource) {
        // focus camera
        boolean focusGameObjectResult = resource.adjustCameraTo();
        // walk result if not visible
        boolean walkResult;
        // not visible, walk to gameObject
        if (!resource.isVisible()) {
            walkResult = walkToTile(resource.getTile());
            if (walkResult) {
                Waiting.waitUntil(resource::isVisible);
            }
        }
        // find all viable resource actions such as "Mine"
        List<String> actions = resource.getActions();
        // no actions, return false
        if (actions.size() == 0) {
            return false;
        }
        // interact using the first action available such as "Mine" or "Chop" etc
        return resource.interact(actions.get(0));
    }

    default boolean inventoryIsReadyMotherlode(PickAxe pickAxe) {
       return Query.inventory()
                .idNotEquals(GEM_BAG, COAL_BAG, GEM_BAG_OPEN, COAL_BAG, HAMMER)
                .count() == 0
                ;
    }

    default boolean inventoryFullOre() {
        return Inventory.isFull() && Inventory.contains(inventoryItem -> inventoryItem.getName().contains("ore")
                || inventoryItem.getName().equals("Coal"));
    }

    /**
     * @return True if the inventory is full and contains pay-dirt; false otherwise
     */
    default boolean inventoryFullPayDirt() {
        return Inventory.isFull() && Inventory.contains("Pay-dirt");
    }

    /**
     * @return True if the inventory is full and does not contain pay-dirt; false otherwise
     */
    default boolean inventoryFullContainsNoPayDirt() {
        return Inventory.isFull() && !Inventory.contains("Pay-dirt");
    }

    /**
     * @return True if inventory contains a hammer
     */
    default boolean inventoryContainsHammer() {
        return Inventory.contains(HAMMER);
    }

    default boolean inventoryContainsOre() {
        return Inventory.contains(inventoryItem -> inventoryItem.getName().contains("ore") ||
                inventoryItem.getName().equals("Coal"));
    }

    default boolean inventoryContainsPayDirt() {
        return Inventory.contains("Pay-dirt");
    }

    default boolean inventoryContainsGemBag() {
        return Inventory.contains(GEM_BAG, GEM_BAG_OPEN);
    }

    default boolean inventoryContainsCoalBag() {
        return Inventory.contains(COAL_BAG, COAL_BAG_OPEN);
    }

    /**
     * If the pay-dirt sack colour is red, then it is considered full
     *
     * @return True if the pay-dirt sack is actually full; false otherwise
     */
    default boolean payDirtSackIsFull() {
        // the interface for the payDirtSack
        Optional<Widget> payDirtInterface = Query.widgets()
                .inIndexPath(382, 3, 2)
                .findFirst();

        return payDirtInterface.isPresent() && payDirtInterface.get().getTextColor() != 13158600;
    }

    /**
     * @return The pay-dirt sack count; -1 if not satisfied
     */
    default int payDirtSackCount() {
        int count = -1;

        // the interface for the payDirtSack
        Optional<Widget> payDirtInterface = Query.widgets()
                .inIndexPath(382, 3, 2)
                .findFirst();

        if (payDirtInterface.isPresent()) {
            if (payDirtInterface.get().getText().isPresent()) {
                try {
                    count = Integer.parseInt(payDirtInterface.get().getText().get());
                } catch (NumberFormatException numberFormatException) {
                    numberFormatException.printStackTrace();
                }

            }
        }

        return count;
    }

    /**
     * Takes in account the inventory count of pay-dirt and the actual pay-dirt sack count
     *
     * @return True if the sack will become full before depositing; false if the sack count is 0
     * or if not greater than or equal to the max sack cap
     */
    default boolean payDirtSackAlmostFull(int futurePayDirtCount) {
        if (payDirtSackCount() == 0 || futurePayDirtCount == -1) {
            return false;
        }

        if (isSackNormal()) {
            return futurePayDirtCount >= MAX_PAY_DIRT_COUNT_NORMAL;
        } else if (isSackUpgraded()) {
            return futurePayDirtCount >= MAX_PAY_DIRT_COUNT_UPGRADE;
        }

        return false;
    }

    /**
     * @return True if the sack is normal; false otherwise
     */
    default boolean isSackNormal() {
        RSVarBit normalSack = RSVarBit.get(SACK_NORMAL);

        if (normalSack == null) {
            return false;
        } else {
            return normalSack.getValue() > 0;
        }
    }

    /**
     * @return True if the sack is upgraded; false otherwise
     */
    default boolean isSackUpgraded() {
        RSVarBit upgradedSack = RSVarBit.get(SACK_UPGRADED);

        if (upgradedSack == null) {
            return false;
        } else {
            return upgradedSack.getValue() > 0;
        }
    }

    default boolean isAtMotherlodeMine() {
        Area area = Area.fromRectangle(
                new WorldTile(3711, 5694, 0),
                new WorldTile(3774, 5632, 0));
        return area.containsMyPlayer();
    }

    default boolean isMotherlodeMineStrutBrokenNorth() {
        return Query.gameObjects()
                .tileEquals(STRUT_TILE_NORTH)
                .idEquals(BROKEN_STRUT_ID)
                .count() > 0;
    }

    default boolean isMotherlodeMineStrutBrokenSouth() {
        return Query.gameObjects()
                .tileEquals(STRUT_TILE_SOUTH)
                .idEquals(BROKEN_STRUT_ID)
                .count() > 0;
    }

    default boolean isAllStrutsBroken() {
        return Query.gameObjects()
                .idEquals(BROKEN_STRUT_ID)
                .count() == 2;
    }

    default boolean isAtMotherlodeMineHopper() {
        return Query.gameObjects()
                .maxDistance(7.00)
                .idEquals(HOPPER_ID)
                .isAny() && !ResourceLocation.MOTHERLODE_MINE_UPPER_LEVEL.getArea().containsMyPlayer();
    }

    default boolean isAtBrokenStruts() {
        return Query.gameObjects()
                .maxDistance(7.00)
                .idEquals(BROKEN_STRUT_ID)
                .isAny();
    }

    default boolean isAtPayDirtSack() {
        return Query.gameObjects()
                .maxDistance(7.00)
                .idEquals(PAY_DIRT_SACK_ID)
                .isAny();
    }

    default boolean isAtHammerCrate() {
        return Query.gameObjects()
                .maxDistance(7.00)
                .idEquals(CRATE_ID)
                .inArea(Area.fromRadius(CRATE_TILE_NORTH, 5))
                .isAny();
    }

    /**
     * @param bank
     * @return True if the worker distance to bank tile is less than 7; false otherwise
     */
    default boolean isAtBank(RunescapeBank bank) {
        // get old RSTile
        RSTile oldRSTile = bank.getPosition();
        int x = oldRSTile.getX();
        int y = oldRSTile.getY();
        int plane = oldRSTile.getPlane();
        // construct new WorldTile
        WorldTile bankTile = new WorldTile(x, y, plane);
        // worker distance to bank less than 7
        return MyPlayer.getPosition().distanceTo(bankTile) < 7;
    }

    /**
     * Deposit all except Gem bag, Coal bag, Hammer and desired Pickaxe
     *
     * @return The count of deposited items
     */
    default int depositAllMotherlodeMine(PickAxe pickaxe) {
        if (pickaxe != null) {
            return Banking.depositAllExcept(GEM_BAG, GEM_BAG_OPEN, COAL_BAG, COAL_BAG_OPEN, HAMMER, pickaxe.getPickAxeId());
        } else {
            return Banking.depositAllExcept(GEM_BAG, GEM_BAG_OPEN, COAL_BAG, COAL_BAG_OPEN, HAMMER);
        }
    }

    /**
     * Will perform the special attack if fully charged
     *
     * @return True if able to perform special attack; false otherwise
     */
    default boolean performSpecialAttack() {
        if (Combat.getSpecialAttackPercent() == 100 && !Combat.isSpecOrbDisabled() && Combat.isSpecialAttackEnabled()) {
            return Combat.activateSpecialAttack();
        }
        return false;
    }

    /**
     * Reset the game tab to the inventory
     *
     * @return True if opened inventory game tab; false otherwise
     */
    default boolean resetGameTab() {
        if (GameTab.INVENTORY.isOpen()) {
            return false;
        }
        return GameTab.INVENTORY.open();
    }

    default int[] pickAxeIds() {
        return Arrays.stream(PickAxe.values())
                .mapToInt(PickAxe::getPickAxeId)
                .toArray();
    }

    // static methods below

    /**
     * Calculate the best/optimal pickaxe currently inside the bank
     *
     * @return The best pickaxe ID pertaining to the workers bank and mining level; otherwise -1
     */
    static Optional<PickAxe> calculateOptimalPickAxeInBank(int miningLevel) {
        // order always matters
        if (Bank.contains(PickAxe.CRYSTAL_PICKAXE.getPickAxeId())) {
            if (miningLevel >= PickAxe.CRYSTAL_PICKAXE.getPickAxeLevel()) {
                return Optional.of(PickAxe.CRYSTAL_PICKAXE);
            }
        } else if (Bank.contains(PickAxe.CRYSTAL_PICKAXE_UNCHARGED.getPickAxeId())) {
            if (miningLevel >= PickAxe.CRYSTAL_PICKAXE_UNCHARGED.getPickAxeLevel()) {
                return Optional.of(PickAxe.CRYSTAL_PICKAXE_UNCHARGED);
            }
        } else if (Bank.contains(PickAxe.INFERNAL_PICKAXE_OR.getPickAxeId())) {
            if (miningLevel >= PickAxe.INFERNAL_PICKAXE_OR.getPickAxeLevel()) {
                return Optional.of(PickAxe.INFERNAL_PICKAXE_OR);
            }
        } else if (Bank.contains(PickAxe.INFERNAL_PICKAXE.getPickAxeId())) {
            if (miningLevel >= PickAxe.INFERNAL_PICKAXE.getPickAxeLevel()) {
                return Optional.of(PickAxe.INFERNAL_PICKAXE);
            }
        } else if (Bank.contains(PickAxe.INFERNAL_PICKAXE_UNCHARGED.getPickAxeId())) {
            if (miningLevel >= PickAxe.INFERNAL_PICKAXE_UNCHARGED.getPickAxeLevel()) {
                return Optional.of(PickAxe.INFERNAL_PICKAXE_UNCHARGED);
            }
        } else if (Bank.contains(PickAxe.DRAGON_PICKAXE_UPGRADED.getPickAxeId())) {
            if (miningLevel >= PickAxe.DRAGON_PICKAXE_UPGRADED.getPickAxeLevel()) {
                return Optional.of(PickAxe.DRAGON_PICKAXE_UPGRADED);
            }
        } else if (Bank.contains(PickAxe.DRAGON_PICKAXE_OR.getPickAxeId())) {
            if (miningLevel >= PickAxe.DRAGON_PICKAXE_OR.getPickAxeLevel()) {
                return Optional.of(PickAxe.DRAGON_PICKAXE_OR);
            }
        } else if (Bank.contains(PickAxe.DRAGON_PICKAXE.getPickAxeId())) {
            if (miningLevel >= PickAxe.DRAGON_PICKAXE.getPickAxeLevel()) {
                return Optional.of(PickAxe.DRAGON_PICKAXE);
            }
        } else if (Bank.contains(PickAxe.RUNE_PICKAXE.getPickAxeId())) {
            if (miningLevel >= PickAxe.RUNE_PICKAXE.getPickAxeLevel()) {
                return Optional.of(PickAxe.RUNE_PICKAXE);
            }
        } else if (Bank.contains(PickAxe.ADAMANT_PICKAXE.getPickAxeId())) {
            if (miningLevel >= PickAxe.ADAMANT_PICKAXE.getPickAxeLevel()) {
                return Optional.of(PickAxe.ADAMANT_PICKAXE);
            }
        } else if (Bank.contains(PickAxe.MITHRIL_PICKAXE.getPickAxeId())) {
            if (miningLevel >= PickAxe.MITHRIL_PICKAXE.getPickAxeLevel()) {
                return Optional.of(PickAxe.MITHRIL_PICKAXE);
            }
        } else if (Bank.contains(PickAxe.BLACK_PICKAXE.getPickAxeId())) {
            if (miningLevel >= PickAxe.BLACK_PICKAXE.getPickAxeLevel()) {
                return Optional.of(PickAxe.BLACK_PICKAXE);
            }
        } else if (Bank.contains(PickAxe.STEEL_PICKAXE.getPickAxeId())) {
            if (miningLevel >= PickAxe.STEEL_PICKAXE.getPickAxeLevel()) {
                return Optional.of(PickAxe.STEEL_PICKAXE);
            }
        } else if (Bank.contains(PickAxe.IRON_PICKAXE.getPickAxeId())) {
            if (miningLevel >= PickAxe.IRON_PICKAXE.getPickAxeLevel()) {
                return Optional.of(PickAxe.IRON_PICKAXE);
            }
        } else if (Bank.contains(PickAxe.BRONZE_PICKAXE.getPickAxeId())) {
            if (miningLevel >= PickAxe.BRONZE_PICKAXE.getPickAxeLevel()) {
                return Optional.of(PickAxe.BRONZE_PICKAXE);
            }
        } else {
        }
        return Optional.empty();
    }

    /**
     * Calculate the best/optimal pickaxe currently inside the inventory
     *
     * @return The best pickaxe ID pertaining to the workers inventory or equipment; otherwise -1
     */
    static Optional<PickAxe> calculateOptimalPickAxeOnWorker(int miningLevel) {
        PickAxe equippedPickAxe = null;
        PickAxe inventoryPickAxe = null;

        // check if any pickaxe is equipped
        for (PickAxe pickAxe : PickAxe.values()) {
            int pickAxeId = pickAxe.getPickAxeId();
            if (Equipment.contains(pickAxeId)) {
                equippedPickAxe = pickAxe;
                break;
            }
        }

        // check the inventory
        // order matters
        if (Inventory.contains(PickAxe.CRYSTAL_PICKAXE.getPickAxeId())) {
            if (miningLevel >= PickAxe.CRYSTAL_PICKAXE.getPickAxeLevel()) {
                inventoryPickAxe = PickAxe.CRYSTAL_PICKAXE;
            }
        } else if (Inventory.contains(PickAxe.CRYSTAL_PICKAXE_UNCHARGED.getPickAxeId())) {
            if (miningLevel >= PickAxe.CRYSTAL_PICKAXE_UNCHARGED.getPickAxeLevel()) {
                inventoryPickAxe = PickAxe.CRYSTAL_PICKAXE_UNCHARGED;
            }
        } else if (Inventory.contains(PickAxe.INFERNAL_PICKAXE_OR.getPickAxeId())) {
            if (miningLevel >= PickAxe.INFERNAL_PICKAXE_OR.getPickAxeLevel()) {
                inventoryPickAxe = PickAxe.INFERNAL_PICKAXE_OR;
            }
        } else if (Inventory.contains(PickAxe.INFERNAL_PICKAXE.getPickAxeId())) {
            if (miningLevel >= PickAxe.INFERNAL_PICKAXE.getPickAxeLevel()) {
                inventoryPickAxe = PickAxe.INFERNAL_PICKAXE;
            }
        } else if (Inventory.contains(PickAxe.INFERNAL_PICKAXE_UNCHARGED.getPickAxeId())) {
            if (miningLevel >= PickAxe.INFERNAL_PICKAXE_UNCHARGED.getPickAxeLevel()) {
                inventoryPickAxe = PickAxe.INFERNAL_PICKAXE_UNCHARGED;
            }
        } else if (Inventory.contains(PickAxe.DRAGON_PICKAXE_UPGRADED.getPickAxeId())) {
            if (miningLevel >= PickAxe.DRAGON_PICKAXE_UPGRADED.getPickAxeLevel()) {
                inventoryPickAxe = PickAxe.DRAGON_PICKAXE_UPGRADED;
            }
        } else if (Inventory.contains(PickAxe.DRAGON_PICKAXE_OR.getPickAxeId())) {
            if (miningLevel >= PickAxe.DRAGON_PICKAXE_OR.getPickAxeLevel()) {
                inventoryPickAxe = PickAxe.DRAGON_PICKAXE_OR;
            }
        } else if (Inventory.contains(PickAxe.DRAGON_PICKAXE.getPickAxeId())) {
            if (miningLevel >= PickAxe.DRAGON_PICKAXE.getPickAxeLevel()) {
                inventoryPickAxe = PickAxe.DRAGON_PICKAXE;
            }
        } else if (Inventory.contains(PickAxe.RUNE_PICKAXE.getPickAxeId())) {
            if (miningLevel >= PickAxe.RUNE_PICKAXE.getPickAxeLevel()) {
                inventoryPickAxe = PickAxe.RUNE_PICKAXE;
            }
        } else if (Inventory.contains(PickAxe.ADAMANT_PICKAXE.getPickAxeId())) {
            if (miningLevel >= PickAxe.ADAMANT_PICKAXE.getPickAxeLevel()) {
                inventoryPickAxe = PickAxe.ADAMANT_PICKAXE;
            }
        } else if (Inventory.contains(PickAxe.MITHRIL_PICKAXE.getPickAxeId())) {
            if (miningLevel >= PickAxe.MITHRIL_PICKAXE.getPickAxeLevel()) {
                inventoryPickAxe = PickAxe.MITHRIL_PICKAXE;
            }
        } else if (Inventory.contains(PickAxe.BLACK_PICKAXE.getPickAxeId())) {
            if (miningLevel >= PickAxe.BLACK_PICKAXE.getPickAxeLevel()) {
                inventoryPickAxe = PickAxe.BLACK_PICKAXE;
            }
        } else if (Inventory.contains(PickAxe.STEEL_PICKAXE.getPickAxeId())) {
            if (miningLevel >= PickAxe.STEEL_PICKAXE.getPickAxeLevel()) {
                inventoryPickAxe = PickAxe.STEEL_PICKAXE;
            }
        } else if (Inventory.contains(PickAxe.IRON_PICKAXE.getPickAxeId())) {
            if (miningLevel >= PickAxe.IRON_PICKAXE.getPickAxeLevel()) {
                inventoryPickAxe = PickAxe.IRON_PICKAXE;
            }
        } else if (Inventory.contains(PickAxe.BRONZE_PICKAXE.getPickAxeId())) {
            if (miningLevel >= PickAxe.BRONZE_PICKAXE.getPickAxeLevel()) {
                inventoryPickAxe = PickAxe.BRONZE_PICKAXE;
            }
        }

        // check if we have pickaxes on worker
        // return best pickaxe id pertaining to mining level
        if (equippedPickAxe == null) {
            if (inventoryPickAxe == null) {
                return Optional.empty();
            } else {
                return Optional.of(inventoryPickAxe);
            }
        } else {
            if (inventoryPickAxe == null) {
                return Optional.of(equippedPickAxe);
            } else {
                if (equippedPickAxe.getPickAxeLevel() >= inventoryPickAxe.getPickAxeLevel()) {
                    return Optional.of(equippedPickAxe);
                } else {
                    return Optional.of(inventoryPickAxe);
                }
            }
        }
    }

    default boolean openGemBag() {
        if (Inventory.contains(GEM_BAG_OPEN)) {
            return false;
        }

        Query.inventory()
                .idEquals(GEM_BAG)
                .findFirst()
                .ifPresent(inventoryItem -> inventoryItem.click("Open"));

        return Waiting.waitUntil(() -> Inventory.contains(GEM_BAG_OPEN));
    }

    default boolean openCoalBag() {
        if (Inventory.contains(COAL_BAG_OPEN)) {
            return false;
        }

        Query.inventory()
                .idEquals(COAL_BAG)
                .findFirst()
                .ifPresent(inventoryItem -> inventoryItem.click("Open"));

        return Waiting.waitUntil(() -> Inventory.contains(COAL_BAG_OPEN));
    }

    /**
     * Take a screenshot and save to Tribot directory
     *
     * @return True if successfully taken screenshot; false otherwise
     */
    static boolean takeScreenShot(String skillName, String userName, int skillLevel) {
        String directory = (Tribot.getDirectory().getAbsolutePath() + File.separator + "screenshots" + File.separator + userName + "_" + skillName + "_" + skillLevel + ".png");
        try {
            BufferedImage img = Screenshot.capture();
            Log.log("Screenshot taken");
            Log.log(directory);
            File outPutFile = new File(directory);
            return ImageIO.write(img, "png", outPutFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
