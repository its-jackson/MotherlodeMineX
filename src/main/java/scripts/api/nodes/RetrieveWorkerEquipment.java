package scripts.api.nodes;

import org.tribot.script.sdk.Bank;
import org.tribot.script.sdk.Equipment;
import org.tribot.script.sdk.tasks.BankTask;
import scripts.MotherlodeMineXVariables;
import scripts.api.Work;
import scripts.api.Worker;
import scripts.api.antiban.AntiBan;
import scripts.api.interfaces.Nodeable;
import scripts.api.interfaces.Workable;

public class RetrieveWorkerEquipment implements Nodeable, Workable {

    private Work work;
    private BankTask task;

    private boolean retrieveGemBag;
    private boolean retrieveCoalBag;
    private boolean retrieveFullProspector;

    public RetrieveWorkerEquipment(Work work) {
        this.work = work;
    }

    @Override
    public void execute() {
        MotherlodeMineXVariables vars = MotherlodeMineXVariables.get();
        int sleepTime = AntiBan.sleep(vars.getWaitTimes());

        log("Retrieving equipment");

        if (!Bank.isOpen()) {
            if (!Bank.open()) {
                walkToBank(getWork().getBankLocation());
            }
        }
        if (Bank.isOpen()) {
            depositAllMotherlodeMine(Worker.getInstance().getPickaxe());

            Bank.withdraw(GEM_BAG, 1);
            Bank.withdraw(GEM_BAG_OPEN, 1);
            Bank.withdraw(COAL_BAG,1);
            Bank.withdraw(COAL_BAG_OPEN, 1);
            Bank.withdraw(PROSPECTOR_HELMET, 1);
            Bank.withdraw(PROSPECTOR_JACKET, 1);
            Bank.withdraw(PROSPECTOR_LEGS, 1);
            Bank.withdraw(PROSPECTOR_BOOTS, 1);
            Bank.withdraw(MINING_CAPE_UNTRIMMED, 1);
            Bank.withdraw(MINING_CAPE_TRIMMED, 1);

            Equipment.equip(PROSPECTOR_HELMET);
            Equipment.equip(PROSPECTOR_JACKET);
            Equipment.equip(PROSPECTOR_LEGS);
            Equipment.equip(PROSPECTOR_BOOTS);
            Equipment.equip(MINING_CAPE_UNTRIMMED);
            Equipment.equip(MINING_CAPE_TRIMMED);

            depositAllMotherlodeMine(Worker.getInstance().getPickaxe());
        }

    }

    @Override
    public boolean validate() {
        // use gem bag or coal bag or mining gear
        // has optimal pickaxe
        // is at bank
        if (isAtBank(getWork().getBankLocation())) {
            if (workerHasOptimalPickaxe(Worker.getInstance().getPickaxe())) {
                // worker doesnt have the required items on their player
                // and worker bank
                return !workerHasMotherlodeEquipment(MotherlodeMineXVariables.get().getSettings());
            }
        }
        return false;
    }

    @Override
    public String name() {
        return "[Retrieve Equipment Control]";
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public BankTask getTask() {
        return task;
    }

    public void setTask(BankTask task) {
        this.task = task;
    }

    public boolean isRetrieveGemBag() {
        return retrieveGemBag;
    }

    public void setRetrieveGemBag(boolean retrieveGemBag) {
        this.retrieveGemBag = retrieveGemBag;
    }

    public boolean isRetrieveCoalBag() {
        return retrieveCoalBag;
    }

    public void setRetrieveCoalBag(boolean retrieveCoalBag) {
        this.retrieveCoalBag = retrieveCoalBag;
    }

    public boolean isRetrieveFullProspector() {
        return retrieveFullProspector;
    }

    public void setRetrieveFullProspector(boolean retrieveFullProspector) {
        this.retrieveFullProspector = retrieveFullProspector;
    }
}
