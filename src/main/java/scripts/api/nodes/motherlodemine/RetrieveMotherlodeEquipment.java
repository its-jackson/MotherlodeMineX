package scripts.api.nodes.motherlodemine;

import org.tribot.api.General;
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

public class RetrieveMotherlodeEquipment implements Nodeable, Workable {

    private Work work;
    private BankTask task;

    private final MotherlodeMineXVariables variables = MotherlodeMineXVariables.get();

    public RetrieveMotherlodeEquipment(Work work) {
        this.work = work;
    }

    @Override
    public void execute() {
        AntiBan.sleep(getVariables().getWaitTimes());

        String retrievingEquipment = "Retrieving equipment";
        log(retrievingEquipment);
        getVariables().setState(retrievingEquipment);

        if (!Bank.isOpen()) {
            if (!Bank.open()) {
                walkToBank(getWork().getBankLocation());
            }
        }

        // need to use bank task in future
        if (Bank.isOpen()) {
            final int firstDeposit = depositAllMotherlodeMine(Worker.getInstance().getPickaxe());
            if (firstDeposit > 0) {
                log("Deposited inventory");
            }

            if (Bank.withdraw(GEM_BAG, 1)) {
                log("Withdrew gem bag");
            }
            if (Bank.withdraw(GEM_BAG_OPEN, 1)) {
                log("Withdrew open gem bag");
            }
            if (Bank.withdraw(COAL_BAG,1)) {
                log("Withdrew coal bag");
            }
            if (Bank.withdraw(COAL_BAG_OPEN, 1)) {
                log("Withdrew open coal bag");
            }

            if (Bank.withdraw(PROSPECTOR_HELMET, 1)) {
                log("Withdrew prospector helmet");
                if (Waiting.waitUntil(() -> Inventory.contains(PROSPECTOR_HELMET))) {
                    if (Equipment.equip(PROSPECTOR_HELMET)) {
                        log("Equipped prospector helmet");
                    }
                }
            }
            if (Bank.withdraw(PROSPECTOR_JACKET, 1)) {
                log("Withdrew prospector jacket");
                if (Waiting.waitUntil(() -> Inventory.contains(PROSPECTOR_JACKET))) {
                    if (Equipment.equip(PROSPECTOR_JACKET)) {
                        log("Equipped prospector jacket");
                    }
                }
            }
            if (Bank.withdraw(PROSPECTOR_LEGS, 1)) {
                log("Withdrew prospector legs");
                if (Waiting.waitUntil(() -> Inventory.contains(PROSPECTOR_LEGS))) {
                    if (Equipment.equip(PROSPECTOR_LEGS)) {
                        log("Equipped prospector legs");
                    }
                }
            }
            if (Bank.withdraw(PROSPECTOR_BOOTS, 1)) {
                log("Withdrew prospector boots");
                if (Waiting.waitUntil(() -> Inventory.contains(PROSPECTOR_BOOTS))) {
                    if (Equipment.equip(PROSPECTOR_BOOTS)) {
                        log("Equipped prospector boots");
                    }
                }
            }
            if (Bank.withdraw(MINING_CAPE_UNTRIMMED, 1)) {
                log("Withdrew mining cape untrimmed");
                if (Waiting.waitUntil(() -> Inventory.contains(MINING_CAPE_UNTRIMMED))) {
                    if (Equipment.equip(MINING_CAPE_UNTRIMMED)) {
                        log("Equipped mining cape untrimmed");
                    }
                }
            }
            if (Bank.withdraw(MINING_CAPE_TRIMMED, 1)) {
                log("Withdrew mining cape trimmed");
                if (Waiting.waitUntil(() -> Inventory.contains(MINING_CAPE_TRIMMED))) {
                    if (Equipment.equip(MINING_CAPE_TRIMMED)) {
                        log("Equipped mining cape trimmed");
                    }
                }
            }

            final int lastDeposit = depositAllMotherlodeMine(Worker.getInstance().getPickaxe());
            if (lastDeposit > 0) {
                log("Deposited inventory");
            }
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

    public MotherlodeMineXVariables getVariables() {
        return variables;
    }
}
