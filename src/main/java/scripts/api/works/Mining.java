package scripts.api.works;

import dax.api_lib.models.RunescapeBank;
import org.tribot.script.sdk.types.WorldTile;
import scripts.api.*;
import scripts.api.enums.Resource;
import scripts.api.enums.ResourceLocation;
import scripts.api.enums.ResourceOption;

public class Mining extends Work {

    private final WorldTile PORT_SARIM_BANK_TILE = new WorldTile(0, 0, 0);

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

    public Mining() {
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
     * Critical method for setting complete state for constructor
     *
     * @param resourceLocation
     */
    @Override
    public void completeState(ResourceLocation resourceLocation) {
        switch (resourceLocation) {
            case MINING_LUMBRIDGE_SWAMP:
                setBankLocation(RunescapeBank.LUMBRIDGE_TOP);
                break;
            case MINING_LUMBRIDGE_SOUTH_WEST:
                setBankLocation(RunescapeBank.DRAYNOR);
                break;
            case MINING_VARROCK_SOUTH_WEST:
                setBankLocation(RunescapeBank.VARROCK_WEST);
                break;
            case MINING_VARROCK_SOUTH_EAST:
                setBankLocation(RunescapeBank.VARROCK_EAST);
                break;
            case MINING_BARBARIAN_VILLAGE:
                setBankLocation(RunescapeBank.EDGEVILLE);
                break;
            case MINING_RIMMINGTON:
                setBankLocation(RunescapeBank.FALADOR_EAST);
                setAlternateBankLocation(PORT_SARIM_BANK_TILE);
        }
    }
}
