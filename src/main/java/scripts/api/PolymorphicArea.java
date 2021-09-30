package scripts.api;

import org.tribot.api.General;
import org.tribot.script.sdk.MyPlayer;
import org.tribot.script.sdk.types.WorldTile;
import java.util.ArrayList;
import java.util.Arrays;

public class PolymorphicArea {

    private ArrayList<WorldTile> worldTiles = new ArrayList<>();

    public PolymorphicArea(WorldTile... tiles) {
        this.worldTiles.addAll(Arrays.asList(tiles));
    }

    public boolean containsMyPlayer() {
        return getWorldTiles().contains(MyPlayer.getPosition());
    }

    public WorldTile getRandomTile() {
        int cardinal = getWorldTiles().size();
        int randomNumber = General.random(0, cardinal -1);
        return getWorldTiles().get(randomNumber);
    }

    public ArrayList<WorldTile> getWorldTiles() {
        return worldTiles;
    }

    public void setWorldTiles(ArrayList<WorldTile> worldTiles) {
        this.worldTiles = worldTiles;
    }

    @Override
    public String toString() {
        return "PolymorphicArea{" +
                "worldTiles=" + worldTiles +
                '}';
    }
}
