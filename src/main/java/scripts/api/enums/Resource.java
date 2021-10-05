package scripts.api.enums;

public enum Resource {

    ORE_VEIN("Ore vein");

    private final String resource;

    Resource(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }
}
