package scripts.api.works;

import dax.api_lib.models.RunescapeBank;
import org.tribot.script.sdk.Skill;
import org.tribot.script.sdk.types.WorldTile;
import scripts.api.TimeElapse;
import scripts.api.enums.Resource;
import scripts.api.enums.ResourceLocation;
import scripts.api.enums.ResourceOption;
import scripts.api.interfaces.Validatable;

import java.io.Serializable;

public abstract class Work implements Validatable, Serializable {

    private Resource resource;
    private ResourceLocation resourceLocation;
    private ResourceOption resourceOption;

    private int level;
    private TimeElapse time;
    private RunescapeBank bankLocation;
    private WorldTile alternateBankLocation;

    public Work(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption, int level, TimeElapse time) {
        this.resource = resource;
        this.resourceLocation = resourceLocation;
        this.resourceOption = resourceOption;
        this.level = level;
        this.time = time;
        completeState(this.resourceLocation);
    }

    public Work(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption, int level) {
        this.resource = resource;
        this.resourceLocation = resourceLocation;
        this.resourceOption = resourceOption;
        this.level = level;
        completeState(this.resourceLocation);
    }

    public Work(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption, TimeElapse time) {
        this.resource = resource;
        this.resourceLocation = resourceLocation;
        this.resourceOption = resourceOption;
        this.time = time;
        completeState(this.resourceLocation);
    }

    public Work(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption) {
        this.resource = resource;
        this.resourceLocation = resourceLocation;
        this.resourceOption = resourceOption;
        completeState(this.resourceLocation);
    }

    public Work() {}

    /**
     * Critical method for setting complete state for constructor
     */
    public abstract void completeState(ResourceLocation resourceLocation);

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

    public boolean reachedLevel() {
        // return if worker mining level is greater than the desired level
        if (getLevel() <= 0 || getLevel() > 99) {
            return false;
        }
        return Skill.MINING.getActualLevel() >= getLevel();
    }

    public boolean reachedTime() {
        // return true if worker timer has surpassed desired time
        return getTime() != null && getTime().validate();
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    public void setResourceLocation(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public ResourceOption getResourceOption() {
        return resourceOption;
    }

    public void setResourceOption(ResourceOption resourceOption) {
        this.resourceOption = resourceOption;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public TimeElapse getTime() {
        return time;
    }

    public void setTime(TimeElapse time) {
        this.time = time;
    }

    public RunescapeBank getBankLocation() {
        return bankLocation;
    }

    public void setBankLocation(RunescapeBank bankLocation) {
        this.bankLocation = bankLocation;
    }

    public WorldTile getAlternateBankLocation() {
        return alternateBankLocation;
    }

    public void setAlternateBankLocation(WorldTile alternateBankLocation) {
        this.alternateBankLocation = alternateBankLocation;
    }

    @Override
    public String toString() {
        return "Work{" +
                "resource=" + resource +
                ", resourceLocation=" + resourceLocation +
                ", resourceOption=" + resourceOption +
                ", level=" + level +
                ", time=" + time +
                ", bankLocation=" + bankLocation +
                ", alternateBankLocation=" + alternateBankLocation +
                '}';
    }
}
