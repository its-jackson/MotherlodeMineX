package scripts.api.works;

import scripts.api.*;

public class Mining extends Work {

    public Mining(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption, int level, TimeElapse time) {
        super(resource, resourceLocation, resourceOption, level, time);
    }

    public Mining(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption, int level) {
        super(resource, resourceLocation, resourceOption, level);
    }

    public Mining(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption, TimeElapse time) {
        super(resource, resourceLocation, resourceOption, time);
    }

    public Mining(Resource resource, ResourceLocation resourceLocation, ResourceOption resourceOption) {
        super(resource, resourceLocation, resourceOption);
    }

    public Mining() {}

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
     *
     * @param resourceLocation
     */
    @Override
    public void completeState(ResourceLocation resourceLocation) {

    }
}
