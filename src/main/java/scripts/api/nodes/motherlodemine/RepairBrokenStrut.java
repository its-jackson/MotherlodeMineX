package scripts.api.nodes.motherlodemine;

import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.GameObject;
import scripts.MotherlodeMineXVariables;
import scripts.api.works.Work;
import scripts.api.antiban.AntiBan;
import scripts.api.interfaces.Nodeable;
import scripts.api.interfaces.Workable;

public class RepairBrokenStrut implements Nodeable, Workable {

    private Work work;
    private GameObject brokenStrutNorth;
    private GameObject brokenStrutSouth;

    private final MotherlodeMineXVariables variables = MotherlodeMineXVariables.get();

    public RepairBrokenStrut(Work work) {
        this.work = work;
    }

    public RepairBrokenStrut() {}

    @Override
    public void execute() {
        AntiBan.sleep(getVariables().getWaitTimes());

        boolean waitResult = false;

        String repairingFirst = "Repairing broken strut 1";
        String repairingSecond = "Repairing broken strut 2";
        String successful = "Strut repaired successful";
        String unsuccessful = "Strut repaired unsuccessful";

        // find random broken strut
        Query.gameObjects()
                .idEquals(BROKEN_STRUT_ID)
                .findRandom()
                .ifPresent(this::setBrokenStrutNorth);

        // fix the northern broken strut
        if (getBrokenStrutNorth() != null) {
            log(repairingFirst);
            getVariables().setState(repairingFirst);
            boolean clickResult = interactGameObject(getBrokenStrutNorth());
            if (clickResult) {
                waitResult = Waiting.waitUntil(() -> Query.gameObjects()
                        .idEquals(BROKEN_STRUT_ID)
                        .count() == 1);
                if (!isMotherlodeMineStrutBrokenNorth()) {
                    log(successful);
                    getVariables().setState(successful);
                } else {
                    log(unsuccessful);
                    getVariables().setState(unsuccessful);
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
                log(repairingSecond);
                getVariables().setState(repairingSecond);
                boolean clickResult = interactGameObject(getBrokenStrutSouth());
                if (clickResult) {
                    Waiting.waitUntil(() -> Query.gameObjects()
                            .idEquals(BROKEN_STRUT_ID)
                            .count() == 0);
                    if (!isMotherlodeMineStrutBrokenSouth()) {
                        log(successful);
                        getVariables().setState(successful);
                    } else {
                        log(unsuccessful);
                        getVariables().setState(unsuccessful);
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

    public MotherlodeMineXVariables getVariables() {
        return variables;
    }
}
