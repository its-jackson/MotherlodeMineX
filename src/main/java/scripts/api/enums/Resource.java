package scripts.api.enums;

public enum Resource {

    ORE_VEIN("Ore vein", new int[0]),
    CLAY_ROCK("Clay Rock", new int[] {11362, 11363}),
    TIN_ROCK("Tin Rock", new int[] {11361, 11360}),
    COPPER_ROCK("Copper Rock", new int[]{10943, 11161}),
    IRON_ROCK("Iron Rock", new int[] {11364, 11365,}),
    SILVER_ROCK("Silver Rock", new int[] {11361, 11368, 11369}),
    COAL_ROCK("Coal Rock", new int[] {11366, 11367}),
    GOLD_ROCK("Gold Rock", new int[] {11366, 11367}),
    MITHRIL_ROCK("Mithril Rock", new int[] {11372, 11373}),
    ADAMANTITE_ROCK("Adamantite Rock", new int[] {11374, 11375})
    ;

    private final String resourceName;
    private final int[] resourceIDS;

    Resource(String resourceName, int[] resourceIDS) {
        this.resourceName = resourceName;
        this.resourceIDS = resourceIDS;
    }

    public String getResourceName() {
        return resourceName;
    }

    public int[] getResourceIDS() {
        return resourceIDS;
    }
}
