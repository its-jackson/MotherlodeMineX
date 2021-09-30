package scripts.api.nodes;

import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.GameObject;
import scripts.api.Work;
import scripts.api.interfaces.Nodeable;
import scripts.api.interfaces.Workable;

public class RepairBrokenStrut implements Nodeable, Workable {

    private Work work;
    private GameObject brokenStrutNorth;
    private GameObject brokenStrutSouth;

    public RepairBrokenStrut(Work work) {
        this.work = work;
    }

    public RepairBrokenStrut() {}

    @Override
    public void execute() {
        boolean waitResult = false;

        // find random broken strut
        Query.gameObjects()
                .idEquals(BROKEN_STRUT_ID)
                .findRandom()
                .ifPresent(this::setBrokenStrutNorth);

        // fix the northern broken strut
        if (getBrokenStrutNorth() != null) {
            log("Repairing broken strut 1");
            boolean clickResult = interactGameObject(getBrokenStrutNorth());
            if (clickResult) {
                waitResult = Waiting.waitUntil(() -> Query.gameObjects()
                        .idEquals(BROKEN_STRUT_ID)
                        .count() == 1);
                if (!isMotherlodeMineStrutBrokenNorth()) {
                    log("Strut repaired successful");
                } else {
                    log("Strut repaired unsuccessful");
                }
            }
        }

        // successfully repaired one strut
        if (waitResult) {
            // find relative broken strut
            Query.gameObjects()
                    .idEquals(BROKEN_STRUT_ID)
                    .findClosest()
                    .ifPresent(this::setBrokenStrutSouth);

            // fix the southern broken strut
            if (getBrokenStrutSouth() != null) {
                log("Repairing broken strut 2");
                boolean clickResult = interactGameObject(getBrokenStrutSouth());
                if (clickResult) {
                    Waiting.waitUntil(() -> Query.gameObjects()
                            .idEquals(BROKEN_STRUT_ID)
                            .count() == 0);
                    if (!isMotherlodeMineStrutBrokenSouth()) {
                        log("Strut repaired successful");
                    } else {
                        log("Strut repaired unsuccessful");
                    }
                }
            }
        }
    }

    @Override
    public boolean validate() {
        // is at struts
        // both struts are broken
        // pay-dirt sack is not full
        // inventory is full of pay-dirt
        // inventory contains hammer
        return shouldFixBrokenStruts();
    }

    @Override
    public String name() {
        return "[Strut Control]";
    }

    private boolean shouldFixBrokenStruts() {
        return isAllStrutsBroken()
                && isAtBrokenStruts()
                && !payDirtSackIsFull()
                && inventoryFullPayDirt()
                && inventoryContainsHammer();
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public GameObject getBrokenStrutNorth() {
        return brokenStrutNorth;
    }

    public void setBrokenStrutNorth(GameObject brokenStrutNorth) {
        this.brokenStrutNorth = brokenStrutNorth;
    }

    public GameObject getBrokenStrutSouth() {
        return brokenStrutSouth;
    }

    public void setBrokenStrutSouth(GameObject brokenStrutSouth) {
        this.brokenStrutSouth = brokenStrutSouth;
    }
}
