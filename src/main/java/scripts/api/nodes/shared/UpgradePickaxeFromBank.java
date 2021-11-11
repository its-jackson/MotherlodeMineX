package scripts.api.nodes.shared;

import org.tribot.script.sdk.*;
import org.tribot.script.sdk.cache.BankCache;
import scripts.MotherlodeMineXVariables;
import scripts.api.works.Work;
import scripts.api.Worker;
import scripts.api.antiban.AntiBan;
import scripts.api.interfaces.Nodeable;
import scripts.api.interfaces.Workable;

import java.util.Optional;

public class UpgradePickaxeFromBank implements Nodeable, Workable {

    private Work work;

    private final MotherlodeMineXVariables variables = MotherlodeMineXVariables.get();

    public UpgradePickaxeFromBank(Work work) {
        this.work = work;
    }

    @Override
    public void execute() {
        AntiBan.sleep(getVariables().getWaitTimes());
        String upgrading = "Upgrading pickaxe";

        log(upgrading);
        getVariables().setState(upgrading);
        // calculate the better pickaxe
        Optional<PickAxe> betterPickAxe = workerHasBetterPickAxe(Skill.MINING.getActualLevel(), Worker.getInstance().getPickaxe());
        if (betterPickAxe.isPresent()) {
            PickAxe bankBetterPickAxe = betterPickAxe.get();
            int bankBetterPickAxeId = bankBetterPickAxe.getPickAxeId();
            int bankBetterPickAxeLevel = bankBetterPickAxe.getPickAxeLevel();
            // withdraw the better pickaxe
            if (Bank.contains(bankBetterPickAxeId)) {
                if (Bank.withdraw(bankBetterPickAxeId, 1)) {
                    if (Waiting.waitUntil(() -> Inventory.contains(bankBetterPickAxeId))) {
                        if (Skill.ATTACK.getActualLevel() >= bankBetterPickAxeLevel) {
                            String equip = "Equipping " + bankBetterPickAxe;
                            log(equip);
                            getVariables().setState(equip);
                            Equipment.equip(bankBetterPickAxeId);
                        } else {
                            String addInv = "Adding inventory " + bankBetterPickAxe;
                            log(addInv);
                            getVariables().setState(addInv);
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
           if (!getVariables().getSettings().isDoNotUpgrade()) {
               if (workerHasBetterPickAxe(Skill.MINING.getActualLevel(), Worker.getInstance().getPickaxe())
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

    public MotherlodeMineXVariables getVariables() {
        return variables;
    }
}
