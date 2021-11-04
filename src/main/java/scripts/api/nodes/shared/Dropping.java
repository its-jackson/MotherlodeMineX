package scripts.api.nodes.shared;

import org.tribot.script.sdk.Inventory;
import scripts.MotherlodeMineXVariables;
import scripts.api.Worker;
import scripts.api.antiban.AntiBan;
import scripts.api.enums.ResourceOption;
import scripts.api.interfaces.Nodeable;
import scripts.api.interfaces.Workable;
import scripts.api.works.MotherlodeMine;
import scripts.api.works.Work;

public class Dropping implements Nodeable, Workable {

    private Work work;

    private final MotherlodeMineXVariables variables = MotherlodeMineXVariables.get();

    public Dropping(Work work) {
        this.work = work;
    }

    @Override
    public void execute() {
        // sleep
        AntiBan.sleep(getVariables().getWaitTimes());
        // check work type
        if (getWork() instanceof MotherlodeMine) {
            // todo
            // drop motherlode work
        } else {
            // drop mining work
            int dropCount = dropInventoryMining();
            if (dropCount > 0) {
                log("Dropping successful");
            } else {
                log("Dropping unsuccessful");
            }
        }
    }

    @Override
    public boolean validate() {
        if (getWork() instanceof MotherlodeMine) {

        } else {
            return shouldDropMining(getWork());
        }
        return false;
    }

    @Override
    public String name() {
        return "[Dropping Control]";
    }

    private boolean shouldDropMining(Work work) {
        if (Inventory.isFull()) {
            if (getWork().getResourceOption().equals(ResourceOption.DROP)) {
                return workerHasOptimalPickaxe(Worker.getInstance().getPickaxe());
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
