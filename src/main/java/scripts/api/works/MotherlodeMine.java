package scripts.api.works;

import dax.api_lib.models.RunescapeBank;
import scripts.api.*;
import scripts.api.enums.Resource;
import scripts.api.enums.ResourceLocation;
import scripts.api.enums.ResourceOption;

public class MotherlodeMine extends Work {

    // state/fields to be added soon

    public MotherlodeMine(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption, int level, TimeElapse time) {
        super(resource, resourceLocation, resourceOption, level, time);
    }

    public MotherlodeMine(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption, int level) {
        super(resource, resourceLocation, resourceOption, level);
    }

    public MotherlodeMine(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption, TimeElapse time) {
        super(resource, resourceLocation, resourceOption, time);
    }

    public MotherlodeMine(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption) {
        super(resource, resourceLocation, resourceOption);
    }

    public MotherlodeMine() {}

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
     * Critical method for setting complete state for constructor
     */
    @Override
    public void completeState(ResourceLocation resourceLocation) {
        switch (resourceLocation) {
            case MOTHERLODE_MINE_LOWER_LEVEL_SOUTH_WEST:
            case MOTHERLODE_MINE_LOWER_LEVEL_FAR_NORTH_EAST:
            case MOTHERLODE_MINE_LOWER_LEVEL_MIDDLE_NORTH_EAST:
            case MOTHERLODE_MINE_LOWER_LEVEL_NORTH:
            case MOTHERLODE_MINE_UPPER_LEVEL:
                setBankLocation(RunescapeBank.MOTHERLOAD);
        }
    }
}
