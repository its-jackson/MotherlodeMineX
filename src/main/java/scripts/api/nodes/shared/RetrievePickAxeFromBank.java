package scripts.api.nodes.shared;


import org.tribot.script.sdk.Bank;
import org.tribot.script.sdk.Equipment;
import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.Waiting;

import org.tribot.script.sdk.tasks.BankTask;

import scripts.MotherlodeMineXVariables;
import scripts.api.works.Work;
import scripts.api.Worker;
import scripts.api.antiban.AntiBan;
import scripts.api.interfaces.Nodeable;
import scripts.api.interfaces.Workable;


/**
 * Purpose of class: Will retrieve the optimal pickaxe from the workers bank
 * Only if the worker has no pickaxe.
 */
public class RetrievePickAxeFromBank implements Nodeable, Workable {

    private Work work;
    private PickAxe workerOptimalPickaxe;
    private BankTask task;

    private final MotherlodeMineXVariables variables = MotherlodeMineXVariables.get();

    public RetrievePickAxeFromBank(Work work) {
        this.work = work;
    }

    public RetrievePickAxeFromBank() {
    }

    @Override
    public void execute() {
        int sleepTime = AntiBan.sleep(getVariables().getWaitTimes());
        String retrievingPickaxe = "Retrieving pickaxe";

        log(retrievingPickaxe);
        getVariables().setState(retrievingPickaxe);
        // open the bank
        if (!Bank.isOpen()) {
            if (!Bank.open()) {
                walkToBank();
            }
        }
        // bank is open
        if (Bank.isOpen()) {
            // deposit all items
            depositAllMotherlodeMine(Worker.getInstance().getPickaxe());
            // calculate optimal pickaxe
            if (!MotherlodeMineXVariables.get().getSettings().isDoNotUpgrade()) {
                Workable.calculateOptimalPickAxeInBank(
                                Worker.getInstance()
                                        .getActualMiningLevel())
                        .ifPresent(this::setWorkerOptimalPickaxe);
            } else {
                setWorkerOptimalPickaxe(Worker.getInstance().getPickaxe());
            }
            // check if pickaxe id found
            if (getWorkerOptimalPickaxe() != null) {
                // construct bank task
                if (Bank.contains(getWorkerOptimalPickaxe().getPickAxeId())) {
                    if (Bank.withdraw(getWorkerOptimalPickaxe().getPickAxeId(), 1)) {
                        boolean result = Waiting.waitUntil(() -> Inventory.contains(getWorkerOptimalPickaxe().getPickAxeId()));
                        if (result) {
                            if (Worker.getInstance().getActualAttackLevel() >= getWorkerOptimalPickaxe().getPickAxeLevel()) {
                                String equipping = "Equipping " + getWorkerOptimalPickaxe();
                                log(equipping);
                                getVariables().setState(equipping);
                                Equipment.equip(getWorkerOptimalPickaxe().getPickAxeId());
                            } else {
                                String addingInv = "Adding inventory " + getWorkerOptimalPickaxe();
                                log(addingInv);
                                getVariables().setState(addingInv);
                            }
                            // deposit all except the required pickaxe and mining tools
                            depositAllMotherlodeMine(getWorkerOptimalPickaxe());
                            // set the pickaxe to the worker instance
                            Worker.getInstance().setPickaxe(getWorkerOptimalPickaxe());
                        }
                    }
                }
            } else {
                // No pickaxe inside the bank
                log("No pickaxe available - please have a pickaxe inside your bank");
            }
        }

    }

    @Override
    public boolean validate() {
        // inventory does not contain pickaxe
        // worker does not have pickaxe equipped
        // is at bank
        return shouldRetrievePickAxe();
    }

    @Override
    public String name() {
        return "[Pickaxe Control]";
    }

    private boolean shouldRetrievePickAxe() {
        if (!workerHasOptimalPickaxe(Worker.getInstance().getPickaxe())) {
            return isAtBank(getWork().getBankLocation()) || Bank.isNearby();
        }
        return false;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public PickAxe getWorkerOptimalPickaxe() {
        return workerOptimalPickaxe;
    }

    public void setWorkerOptimalPickaxe(PickAxe workerOptimalPickaxe) {
        this.workerOptimalPickaxe = workerOptimalPickaxe;
    }

    public BankTask getTask() {
        return task;
    }

    public void setTask(BankTask task) {
        this.task = task;
    }

    public MotherlodeMineXVariables getVariables() {
        return variables;
    }
}
