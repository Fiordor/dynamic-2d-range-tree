
package DynamicTreeStructures.imagegenerator;

import java.awt.Color;

/**
 *
 * @author Fiordor
 */
public class ImageLine {
    
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    private Color c;
    
        public ImageLine(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.c = Color.BLACK;
    }

    public ImageLine(int x1, int y1, int x2, int y2, Color c) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.c = c;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public Color getC() {
        return c;
    }
    
    
    
}
