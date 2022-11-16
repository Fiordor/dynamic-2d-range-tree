
package DynamicTreeStructures.imagegenerator;

import java.awt.Color;

/**
 *
 * @author Fiordor
 */
public class ImageLabel {

    private String label;
    private int x;
    private int y;
    private int w;
    private int h;

    private Color c;

    public ImageLabel(String label, int x, int y, int w, int h) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.c = Color.BLACK;
    }

    public ImageLabel(String label, int x, int y, int w, int h, Color c) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.c = c;
    }

    public String getLabel() {
        return label;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public Color getC() {
        return c;
    }

}
