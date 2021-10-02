package scripts.api;

import org.tribot.script.sdk.painting.MousePaint;

import java.awt.*;
import java.awt.geom.Arc2D;

public class PolymorphicMousePaint implements MousePaint {

    private Color primary;
    private Color secondary;
    private int size;
    private long angle;

    public PolymorphicMousePaint(Color primary, Color secondary, int size) {
        this.primary = primary;
        this.secondary = secondary;
        this.size = size;
    }

    @Override
    public void paintMouse(Graphics graphics, Point mousePos, Point dragPos) {
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(getPrimary());
        g2d.drawLine(mousePos.x - 6, mousePos.y - 6, mousePos.x + 6, mousePos.y + 6);
        g2d.drawLine(mousePos.x - 6, mousePos.y + 6, mousePos.x + 6, mousePos.y - 6);
        g2d.setColor(getSecondary());
        g2d.rotate(Math.toRadians(this.angle += 12), mousePos.x, mousePos.y);
        g2d.draw(new Arc2D.Double(mousePos.x - 12, mousePos.y - 12, getSize(), getSize(), 330, 180, Arc2D.OPEN));
        g2d.draw(new Arc2D.Double(mousePos.x - 12, mousePos.y - 12, getSize(), getSize(), 150, 60, Arc2D.OPEN));
    }

    public Color getPrimary() {
        return primary;
    }

    public void setPrimary(Color primary) {
        this.primary = primary;
    }

    public Color getSecondary() {
        return secondary;
    }

    public void setSecondary(Color secondary) {
        this.secondary = secondary;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
