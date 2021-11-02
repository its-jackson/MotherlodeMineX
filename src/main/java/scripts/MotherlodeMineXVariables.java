package scripts;

import scripts.api.interfaces.Nodeable;
import scripts.gui.GUIFX;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MotherlodeMineXVariables {

    private static final MotherlodeMineXVariables instance = new MotherlodeMineXVariables();

    private GUIFX gui;
    private URL fxml;

    private MotherlodeMineXSettings settings = new MotherlodeMineXSettings();
    private List<Nodeable> nodes = new ArrayList<>();
    private List<Integer> waitTimes = new ArrayList<>();

    private String state = "";
    private boolean start;
    private boolean optimization;

    private final Image img = Objects.requireNonNull(getImage("https://jacksonjohnson.ca/motherlodeminex/Full_paint_motherlode_mine_x.png"))
            .getScaledInstance(520, 167, Image.SCALE_SMOOTH);

    private final RenderingHints anti_aliasing = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    private final Font main_font = new Font("Verdana", Font.BOLD, 12);
    private final Font secondary_font = new Font("Verdana", Font.PLAIN, 11);
    private final Color progress_colour_background = new Color(0, 0, 0, 0.8F);
    private final Color main_font_colour = new Color(22, 196, 219);

    public MotherlodeMineXVariables() {}

    /**
     * Return an image from the internet.
     *
     * @param url The address belonging to the image.
     * @return The image; otherwise null.
     */
    private static Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            return null;
        }
    }

    public static MotherlodeMineXVariables get() {
        return instance;
    }

    public GUIFX getGui() {
        return gui;
    }

    public void setGui(GUIFX gui) {
        this.gui = gui;
    }

    public URL getFxml() {
        return fxml;
    }

    public void setFxml(URL fxml) {
        this.fxml = fxml;
    }

    public MotherlodeMineXSettings getSettings() {
        return settings;
    }

    public void setSettings(MotherlodeMineXSettings settings) {
        this.settings = settings;
    }

    public List<Nodeable> getNodes() {
        return nodes;
    }

    public void setNodes(List<Nodeable> nodes) {
        this.nodes = nodes;
    }

    public List<Integer> getWaitTimes() {
        return waitTimes;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public boolean isOptimization() {
        return optimization;
    }

    public void setOptimization(boolean optimization) {
        this.optimization = optimization;
    }

    public void setWaitTimes(List<Integer> waitTimes) {
        this.waitTimes = waitTimes;
    }

    public Image getImg() {
        return img;
    }

    public RenderingHints getAntiAliasing() {
        return anti_aliasing;
    }

    public Font getMainFont() {
        return main_font;
    }

    public Font getSecondaryFont() {
        return secondary_font;
    }

    public Color getProgressColourBackground() {
        return progress_colour_background;
    }

    public Color getMainFontColour() {
        return main_font_colour;
    }
}
