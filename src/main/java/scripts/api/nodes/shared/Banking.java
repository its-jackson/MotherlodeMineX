package scripts.api.nodes.shared;

import org.tribot.script.sdk.Bank;
import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.Waiting;
import scripts.MotherlodeMineXVariables;
import scripts.api.enums.ResourceOption;
import scripts.api.works.MotherlodeMine;
import scripts.api.works.Work;
import scripts.api.Worker;
import scripts.api.antiban.AntiBan;
import scripts.api.interfaces.Nodeable;
import scripts.api.interfaces.Workable;

public class Banking implements Nodeable, Workable {

    private Work work;

    private final MotherlodeMineXVariables variables = MotherlodeMineXVariables.get();

    public Banking(Work work) {
        this.work = work;
    }

    public Banking() {
    }

    @Override
    public void execute() {
        AntiBan.sleep(getVariables().getWaitTimes());

        String depositing = "Depositing inventory";
        boolean bankingResult = false;

        // open bank
        if (!Bank.isOpen()) {
            {
                Bank.open();
            }
            // check if open
            if (Bank.isOpen()) {
                log(depositing);
                getVariables().setState(depositing);
                if (getWork() instanceof MotherlodeMine) {
                    // deposit all except
                    depositAllMotherlodeMine(Worker.getInstance().getPickaxe());
                } else {
                    depositAllMining(Worker.getInstance().getPickaxe());
                }
                // wait until not full
                bankingResult = Waiting.waitUntil(() -> !Inventory.isFull());
                // upgrade pick axe
                UpgradePickaxeFromBank upgrade = new UpgradePickaxeFromBank(getWork());
                if (upgrade.validate()) {
                    upgrade.execute();
                }
            }
        }

        String successful = "Inventory deposited successful";
        String unsuccessful = "Inventory deposited unsuccessful";

        if (bankingResult) {
            log(successful);
        } else {
            log(unsuccessful);
        }
    }

    @Override
    public boolean validate() {
        if (getWork() instanceof MotherlodeMine) {
            return shouldBankMotherlode(getWork());
        } else {
            return shouldBankMining(getWork());
        }
    }

    @Override
    public String name() {
        return "[Banking Control]";
    }

    // inventory full
    // inventory contains ore
    // is at the bank
    private boolean shouldBankMotherlode(Work work) {
        if (inventoryFullOre()) {
            return isAtBank(work.getBankLocation());
        }
        return false;
    }

    private boolean shouldBankMining(Work work) {
        if (Inventory.isFull()) {
            if (work.getResourceOption().equals(ResourceOption.BANK)) {
                return isAtBank(work.getBankLocation());
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

    public MotherlodeMineXVariables getVariables() {
        return variables;
    }
}
