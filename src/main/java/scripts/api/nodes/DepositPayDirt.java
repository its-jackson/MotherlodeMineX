package scripts.api.nodes;

import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.GameObject;
import scripts.api.PayDirt;
import scripts.api.Work;
import scripts.api.interfaces.Nodeable;
import scripts.api.interfaces.Workable;

public class DepositPayDirt implements Nodeable, Workable {

    private Work work;
    private GameObject hopper;

    public DepositPayDirt(Work work) {
        this.work = work;
    }

    public DepositPayDirt() {}

    @Override
    public void execute() {
        log("Depositing pay-dirt");

        // get the inventory pay-dirt count
        int inventoryPayDirtCount = Inventory.getCount("Pay-dirt");
        // get the current sack pay-dirt count
        int currentPayDirtSackCount = payDirtSackCount();
        // set the future payDirtSackCount
        PayDirt.setFuturePayDirtSackCount(inventoryPayDirtCount + currentPayDirtSackCount);

        log("Future pay-dirt sack count = " + PayDirt.getFuturePayDirtSackCount());

        // find the hopper
        Query.gameObjects()
                .idEquals(HOPPER_ID)
                .findFirst()
                .ifPresent(this::setHopper);

        // click the hopper to deposit all the pay-dirt
        if (getHopper() != null) {
            boolean clickResult = interactGameObject(getHopper());
            if (clickResult) {
                if (Waiting.waitUntil(() -> !inventoryContainsPayDirt())) {
                    log("Pay-dirt successfully deposited");
                }
            }
        }
    }

    @Override
    public boolean validate() {
        // inventory is full of pay-dirt
        // pay-dirt sack is not full
        // worker is at hopper
        // strut is not broken
        return shouldDepositPayDirt();
    }

    @Override
    public String name() {
        return "[Deposit Pay Dirt Control]";
    }

    private boolean shouldDepositPayDirt() {
        if (inventoryFullPayDirt()) {
            if (!payDirtSackIsFull()) {
                if (isAtMotherlodeMineHopper()) {
                    if (inventoryContainsHammer()) {
                        return !isAllStrutsBroken();
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

    public GameObject getHopper() {
        return hopper;
    }

    public void setHopper(GameObject hopper) {
        this.hopper = hopper;
    }
}
