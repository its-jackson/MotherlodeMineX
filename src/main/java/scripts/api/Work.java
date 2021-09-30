package scripts.api;

import dax.api_lib.models.RunescapeBank;
import org.tribot.script.sdk.Skill;
import scripts.api.interfaces.Executable;
import scripts.api.interfaces.Nodeable;
import scripts.api.interfaces.Validatable;

import java.util.List;

public abstract class Work implements Validatable, Executable {

    private List<Nodeable> nodes;

    private String resourceName;
    private String resourceLocation;
    private String resourceOption;

    private int untilLevel;
    private TimeElapse timer;
    private RunescapeBank bankLocation;
    private ResourceLocation actualResourceLocation;

    public Work(String resourceName, String resourceLocation, String resourceOption, int untilLevel, TimeElapse timer, RunescapeBank bankLocation, ResourceLocation actualResourceLocation) {
        this.resourceName = resourceName;
        this.resourceLocation = resourceLocation;
        this.resourceOption = resourceOption;
        this.untilLevel = untilLevel;
        this.timer = timer;
        this.bankLocation = bankLocation;
        this.actualResourceLocation = actualResourceLocation;
        initializeNodes();
    }

    public Work(String resourceName, String resourceLocation, String resourceOption, TimeElapse timer, RunescapeBank bankLocation, ResourceLocation actualResourceLocation) {
        this.resourceName = resourceName;
        this.resourceLocation = resourceLocation;
        this.resourceOption = resourceOption;
        this.timer = timer;
        this.bankLocation = bankLocation;
        this.actualResourceLocation = actualResourceLocation;
        initializeNodes();
    }

    public Work(String resourceName, String resourceLocation, String resourceOption, int untilLevel, RunescapeBank bankLocation, ResourceLocation actualResourceLocation) {
        this.resourceName = resourceName;
        this.resourceLocation = resourceLocation;
        this.resourceOption = resourceOption;
        this.untilLevel = untilLevel;
        this.bankLocation = bankLocation;
        this.actualResourceLocation = actualResourceLocation;
        initializeNodes();
    }

    public Work() {
        initializeNodes();
    }

    /**
     * Critical method for setting up the nodes for the given work (constructor)
     */
    public abstract void initializeNodes();

    /**
     * Validate the work given - if the worker's level hasn't been reached
     * Or if the worker's timer hasn't elapsed then the method will return false; otherwise
     * the method will return true if not reached;
     *
     * @return True when the work has been reached or surpassed.
     */
    @Override
    public boolean validate() {
        // reached specific level
        // or reached the time elapsed
        return reachedLevel() || reachedTime();
    }

    /**
     * Execute the node for the given work specified
     */
    @Override
    public void execute() {
        if (getNodes() != null) {
            getNodes().stream()
                    .filter(Nodeable::validate)
                    .findFirst()
                    .ifPresent(Nodeable::execute);
        }
    }

    public boolean reachedLevel() {
        // return if worker mining level is greater than the desired level
        if (getUntilLevel() <= 0 || getUntilLevel() >= 99) {
            return false;
        }
        return Skill.MINING.getActualLevel() >= getUntilLevel();
    }

    public boolean reachedTime() {
        // return true if worker timer has surpassed desired time
        return getTimer() != null && getTimer().validate();
    }

    public List<Nodeable> getNodes() {
        return nodes;
    }

    public void setNodes(List<Nodeable> nodes) {
        this.nodes = nodes;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceLocation() {
        return resourceLocation;
    }

    public void setResourceLocation(String resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public String getResourceOption() {
        return resourceOption;
    }

    public void setResourceOption(String resourceOption) {
        this.resourceOption = resourceOption;
    }

    public int getUntilLevel() {
        return untilLevel;
    }

    public void setUntilLevel(int untilLevel) {
        this.untilLevel = untilLevel;
    }

    public TimeElapse getTimer() {
        return timer;
    }

    public void setTimer(TimeElapse timer) {
        this.timer = timer;
    }

    public RunescapeBank getBankLocation() {
        return bankLocation;
    }

    public void setBankLocation(RunescapeBank bankLocation) {
        this.bankLocation = bankLocation;
    }

    public ResourceLocation getActualResourceLocation() {
        return actualResourceLocation;
    }

    public void setActualResourceLocation(ResourceLocation actualResourceLocation) {
        this.actualResourceLocation = actualResourceLocation;
    }

    @Override
    public String toString() {
        return "Work{" +
                "resourceName='" + resourceName + '\'' +
                ", resourceLocation='" + resourceLocation + '\'' +
                ", resourceOption='" + resourceOption + '\'' +
                ", untilLevel=" + untilLevel +
                ", timer=" + timer +
                ", bankLocation=" + bankLocation +
                ", actualResourceLocation=" + actualResourceLocation +
                '}';
    }
}
