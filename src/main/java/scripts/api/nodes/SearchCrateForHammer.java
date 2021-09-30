package scripts.api.nodes;

import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.Log;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.GameObject;
import org.tribot.script.sdk.types.InventoryItem;
import scripts.MotherlodeMineX;
import scripts.api.MotherlodeMineXSettings;
import scripts.api.PayDirt;
import scripts.api.Work;
import scripts.api.Worker;
import scripts.api.interfaces.Nodeable;
import scripts.api.interfaces.Workable;

import java.util.List;
import java.util.function.BooleanSupplier;

public class SearchCrateForHammer implements Nodeable, Workable {

    private Work work;
    private GameObject hammerCrate;

    public SearchCrateForHammer(Work work) {
        this.work = work;
    }

    public SearchCrateForHammer() {
    }

    @Override
    public void execute() {
        log("Searching crate for a hammer");

        boolean clickResult = false;
        boolean waitResult = false;
        boolean dropResult = false;

        // find a random crate
        Query.gameObjects()
                .idEquals(CRATE_ID)
                .inArea(Area.fromRadius(CRATE_TILE_NORTH, 5))
                .findFirst()
                .ifPresent(this::setHammerCrate);

        // drop a pay-dirt
        if (inventoryFullPayDirt()) {
            dropResult = dropOnePayDirt();
            waitResult = Waiting.waitUntil(() -> !Inventory.isFull());
            // interact with crate
            if (dropResult || waitResult) {
                if (getHammerCrate() != null) {
                    clickResult = interactGameObject(getHammerCrate());
                }
            }
        } else {
            if (getHammerCrate() != null) {
                clickResult = interactGameObject(getHammerCrate());
            }
        }

        // wait until inventory contains hammer
        if (clickResult) {
            waitResult = Waiting.waitUntil(this::inventoryContainsHammer);
        }
        // crate search successful
        if (waitResult) {
            log("Crate searched successful");
        } else {
            log("Crate searched unsuccessful");
        }
    }

    @Override
    public boolean validate() {
        // is at crate
        // both struts are broken
        // pay-dirt sack is not full
        // inventory is full of pay-dirt
        // inventory does not contain hammer
        return shouldFetchHammer();
    }

    @Override
    public String name() {
        return "[Hammer Control]";
    }

    private boolean dropOnePayDirt() {
        List<InventoryItem> inventoryItemList = Inventory.getAll();

        if (inventoryItemList.size() > 0) {
            for (InventoryItem item : inventoryItemList) {
                if (item.getName().equals("Pay-dirt")) {
                    if (item.click("Drop")) {
                        break;
                    }
                }
            }
        }

        return false;
    }

    private boolean shouldFetchHammer() {
        if (isAtHammerCrate()) {
            if (workerHasMotherlodeEquipment(MotherlodeMineX.getSettings())) {
                if (workerHasOptimalPickaxe(Worker.getInstance().getPickaxe())) {
                    if (!payDirtSackIsFull() && !payDirtSackAlmostFull(PayDirt.getFuturePayDirtSackCount())) {
                        return !inventoryContainsHammer();
                    }
                }
            }
        }
        return false;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public GameObject getHammerCrate() {
        return hammerCrate;
    }

    public void setHammerCrate(GameObject hammerCrate) {
        this.hammerCrate = hammerCrate;
    }
}