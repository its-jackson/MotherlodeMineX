package scripts.api.nodes;

import scripts.api.interfaces.Nodeable;
import scripts.api.interfaces.Workable;

public class WorldHop implements Nodeable, Workable {

    @Override
    public void execute() {

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
