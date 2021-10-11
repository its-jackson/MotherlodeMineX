package scripts.api;

import org.tribot.api.General;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.sdk.MyPlayer;
import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.WorldTile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Purpose: Backwards compatibility SDK and Previous API
 */

public class PolymorphicArea {

    private List<WorldTile> worldTiles;
    private Area area;
    private RSArea RSArea;

    public PolymorphicArea(WorldTile... tiles) {
        this.worldTiles = new ArrayList<>();
        this.worldTiles.addAll(Arrays.asList(tiles));
    }

    public PolymorphicArea(Area area) {
        this.area = area;
        this.worldTiles = this.area.getAllTiles();
    }

    public PolymorphicArea(RSArea rsArea) {
        this.RSArea = rsArea;
        this.worldTiles = Arrays.stream(this.RSArea.getAllTiles())
                .map(PolymorphicArea::convertRSTileToWorldTile)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    /**
     * Convert RSTile to WorldTile.
     * @param rsTile The current RSTile desired.
     * @return The converted WorldTile Optional otherwise; an empty Optional.
     */
    public static Optional<WorldTile> convertRSTileToWorldTile(RSTile rsTile) {
        if (rsTile != null) {
            return Optional.of(new WorldTile(rsTile.getX(), rsTile.getY(), rsTile.getPlane()));
        }
        return Optional.empty();
    }

    public boolean containsMyPlayer() {
        return getWorldTiles().contains(MyPlayer.getPosition());
    }

    public WorldTile getRandomTile() {
        int cardinal = getWorldTiles().size();
        int randomNumber = General.random(0, cardinal -1);
        return getWorldTiles().get(randomNumber);
    }

    public List<WorldTile> getWorldTiles() {
        return worldTiles;
    }

    public void setWorldTiles(ArrayList<WorldTile> worldTiles) {
        this.worldTiles = worldTiles;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "PolymorphicArea{" +
                "worldTiles=" + worldTiles +
                ", area=" + area +
                '}';
    }
}
