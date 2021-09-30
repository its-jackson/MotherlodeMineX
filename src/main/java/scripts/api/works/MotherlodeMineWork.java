package scripts.api.works;

import dax.api_lib.models.RunescapeBank;
import scripts.api.ResourceLocation;
import scripts.api.TimeElapse;
import scripts.api.Work;
import scripts.api.interfaces.Nodeable;
import scripts.api.nodes.*;

import java.util.ArrayList;
import java.util.List;

public class MotherlodeMineWork extends Work {

    // state/fields to be added soon

    public MotherlodeMineWork(String resourceName, String resourceLocation, String resourceOption, int untilLevel, TimeElapse timer, RunescapeBank bankLocation, ResourceLocation actualResourceLocation) {
        super(resourceName, resourceLocation, resourceOption, untilLevel, timer, bankLocation, actualResourceLocation);
    }

    public MotherlodeMineWork(String resourceName, String resourceLocation, String resourceOption, TimeElapse timer, RunescapeBank bankLocation, ResourceLocation actualResourceLocation) {
        super(resourceName, resourceLocation, resourceOption, timer, bankLocation, actualResourceLocation);
    }

    public MotherlodeMineWork(String resourceName, String resourceLocation, String resourceOption, int untilLevel, RunescapeBank bankLocation, ResourceLocation actualResourceLocation) {
        super(resourceName, resourceLocation, resourceOption, untilLevel, bankLocation, actualResourceLocation);
    }

    public MotherlodeMineWork() {
        super();
    }

    /**
     * Validate the work given - if the worker's level hasn't been reached
     * Or if the worker's timer hasn't elapsed then the method will return false; otherwise
     * the method will return true if not reached;
     *
     * @return True when the work has been reached or surpassed.
     */
    @Override
    public boolean validate() {
        return super.validate();
    }

    /**
     * Execute the node for the given work specified
     */
    @Override
    public void execute() {
        super.execute();
    }

    /**
     * Critical method for setting up the nodes for the given work
     */
    @Override
    public void initializeNodes() {
        List<Nodeable> nodes = new ArrayList<>();
        nodes.add(new MineOreVein(this));
        nodes.add(new DepositPayDirt(this));
        nodes.add(new RepairBrokenStrut(this));
        nodes.add(new Walking(this));
        nodes.add(new Banking(this));
        nodes.add(new SearchCrateForHammer(this));
        nodes.add(new DepletePayDirtSack(this));
        nodes.add(new RetrievePickAxeFromBank(this));
        nodes.add(new RetrieveWorkerEquipment(this));
        setNodes(nodes);
    }
}
