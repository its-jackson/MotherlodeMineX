package scripts.api.nodes;

import scripts.MotherlodeMineXVariables;
import scripts.api.antiban.AntiBan;
import scripts.api.interfaces.Nodeable;
import scripts.api.interfaces.Workable;

public class WorldHop implements Nodeable, Workable {

    @Override
    public void execute() {
        MotherlodeMineXVariables vars = MotherlodeMineXVariables.get();
        int sleepTime = AntiBan.sleep(vars.getWaitTimes());

    }

    @Override
    public boolean validate() {
        return false;
    }

    @Override
    public String name() {
        return "[World Hop Control]";
    }
}
