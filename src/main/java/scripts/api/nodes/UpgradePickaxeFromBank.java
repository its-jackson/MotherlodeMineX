package scripts.api.nodes;

import org.tribot.script.sdk.Bank;
import org.tribot.script.sdk.Equipment;
import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.cache.BankCache;
import scripts.MotherlodeMineX;
import scripts.api.Work;
import scripts.api.Worker;
import scripts.api.interfaces.Nodeable;
import scripts.api.interfaces.Workable;

import java.util.Optional;

public class UpgradePickaxeFromBank implements Nodeable, Workable {

    private Work work;

    public UpgradePickaxeFromBank(Work work) {
        this.work = work;
    }

    @Override
    public void execute() {
        log("Upgrading pickaxe");
        // calculate the better pickaxe
        Optional<PickAxe> betterPickAxe = workerHasBetterPickAxe(Worker.getInstance().getActualMiningLevel(), Worker.getInstance().getPickaxe());
        if (betterPickAxe.isPresent()) {
            PickAxe bankBetterPickAxe = betterPickAxe.get();
            int bankBetterPickAxeId = bankBetterPickAxe.getPickAxeId();
            int bankBetterPickAxeLevel = bankBetterPickAxe.getPickAxeLevel();
            // withdraw the better pickaxe
            if (Bank.contains(bankBetterPickAxeId)) {
                if (Bank.withdraw(bankBetterPickAxeId, 1)) {
                    if (Waiting.waitUntil(() -> Inventory.contains(bankBetterPickAxeId))) {
                        if (Worker.getInstance().getActualAttackLevel() >= bankBetterPickAxeLevel) {
                            log("Equipping " + bankBetterPickAxe);
                            Equipment.equip(bankBetterPickAxeId);
                        } else {
                            log("Adding inventory " + bankBetterPickAxe);
                        }
                        depositAllMotherlodeMine(bankBetterPickAxe);
                        // set the new pickaxe on the worker
                        Worker.getInstance().setPickaxe(bankBetterPickAxe);
                    }
                }
            }
        }
    }

    @Override
    public boolean validate() {
        // current worker axe mining level less than actual worker pickaxe
        // bank cache has the next optimal axe
        // is at the bank
        if (BankCache.isInitialized()) {
           log("Bank initialized");
           if (!MotherlodeMineX.getSettings().isDoNotUpgrade()) {
               if (workerHasBetterPickAxe(Worker.getInstance().getActualMiningLevel(), Worker.getInstance().getPickaxe())
                       .isPresent()) {
                   return Bank.isNearby() && Bank.isOpen();
               }
           }
        } else {
            log("Bank not initialized");
        }
        return false;
    }

    @Override
    public String name() {
        return "[Upgrade Pickaxe Control]";
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }
}
