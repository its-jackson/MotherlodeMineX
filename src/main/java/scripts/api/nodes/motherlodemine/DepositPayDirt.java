package scripts.api.nodes.motherlodemine;

import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.GameObject;
import scripts.MotherlodeMineXVariables;
import scripts.api.PayDirt;
import scripts.api.works.Work;
import scripts.api.antiban.AntiBan;
import scripts.api.interfaces.Nodeable;
import scripts.api.interfaces.Workable;

public class DepositPayDirt implements Nodeable, Workable {

    private Work work;
    private GameObject hopper;

    private final MotherlodeMineXVariables variables = MotherlodeMineXVariables.get();

    public DepositPayDirt(Work work) {
        this.work = work;
    }

    public DepositPayDirt() {}

    @Override
    public void execute() {
        AntiBan.sleep(getVariables().getWaitTimes());

        String depositing = "Depositing pay-dirt";
        log(depositing);
        getVariables().setState(depositing);

        // get the inventory pay-dirt count
        int inventoryPayDirtCount = Inventory.getCount("Pay-dirt");
        // get the current sack pay-dirt count
        int currentPayDirtSackCount = payDirtSackCount();
        // set the future payDirtSackCount
        PayDirt.setFuturePayDirtSackCount(inventoryPayDirtCount + currentPayDirtSackCount);

        String futurePayDirt = "Future pay-dirt sack count = " + PayDirt.getFuturePayDirtSackCount();
        log(futurePayDirt);
        getVariables().setState(futurePayDirt);

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
                    String success = "Pay-dirt successfully deposited";
                    log(success);
                    getVariables().setState(success);
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

    public MotherlodeMineXVariables getVariables() {
        return variables;
    }
}
