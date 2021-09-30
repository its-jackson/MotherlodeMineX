package scripts.api.nodes;

import org.tribot.script.sdk.Bank;
import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.interfaces.Item;
import scripts.api.Work;
import scripts.api.Worker;
import scripts.api.interfaces.Nodeable;
import scripts.api.interfaces.Workable;

import java.util.Optional;

public class Banking implements Nodeable, Workable {

    private Work work;

    public Banking(Work work) {
        this.work = work;
    }

    public Banking() {}

    @Override
    public void execute() {
        log("Depositing inventory");

        boolean bankingResult = false;

        // open bank
        if (Bank.open()) {
            // check if open
            if (Bank.isOpen()) {
                // deposit all except
                depositAllMotherlodeMine(Worker.getInstance().getPickaxe());
                // wait until not full
                bankingResult = Waiting.waitUntil(() -> !Inventory.isFull());
            }
        }

        if (bankingResult) {
            log("Inventory deposited successful");
        } else {
            log("Inventory deposited unsuccessful");
        }

        // upgrade pick axe
        UpgradePickaxeFromBank upgrade = new UpgradePickaxeFromBank(getWork());
        if (upgrade.validate()) {
            upgrade.execute();
        }
    }

    @Override
    public boolean validate() {
        return shouldBankOre();
    }

    @Override
    public String name() {
        return "[Banking Control]";
    }

    // inventory full
    // inventory contains ore
    // is at the bank
    private boolean shouldBankOre() {
        if (inventoryFullOre()) {
            return isAtBank(getWork().getBankLocation());
        }
        return false;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }
}
