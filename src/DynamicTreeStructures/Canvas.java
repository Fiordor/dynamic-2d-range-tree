package DynamicTreeStructures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author Fiordor
 */
public class Canvas extends JPanel {

    private BufferedImage bufferedImage;

    private int x;
    private int y;
    private double zoom;

    public Canvas() {
        this(null);
    }

    public Canvas(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        this.x = 0;
        this.y = 0;
        this.zoom = 1.0;
    }

    public void clear() {
        bufferedImage = null;
        this.x = 0;
        this.y = 0;
        this.zoom = 1.0;
    }

    public void setImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        this.x = 0;
        this.y = 0;
        this.zoom = 1.0;
    }

    public void setPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int[] getPoint() {
        return new int[]{this.x, this.y};
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (bufferedImage != null) {
            Graphics2D g2d = (Graphics2D) g;

            paintBackground(g2d);

            g2d.scale(zoom, zoom);
            g2d.drawImage(bufferedImage, x, y, this);
        }
    }

    private void paintBackground(Graphics2D g) {

        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(new Color(230, 230, 230));
        for (int i = 0; i < getWidth(); i += 5) {
            for (int j = i % 2 == 0 ? 0 : 5; j < getHeight(); j += 10) {
                g.fillRect(i, j, 5, 5);
            }
        }
    }
}
