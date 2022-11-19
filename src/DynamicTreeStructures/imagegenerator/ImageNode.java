package DynamicTreeStructures.imagegenerator;

import java.awt.Color;

/**
 *
 * @author Fiordor
 */
public class ImageNode {

    public String value;
    public int x;
    public int y;
    public int w;
    public int h;

    public Color c;

    public ImageNode parent;

    public ImageNode() {
        this(null, Color.BLACK);
    }

    public ImageNode(String value) {
        this(value, Color.BLACK);
    }

    public ImageNode(String value, Color c) {
        this.value = value;
        this.x = 0;
        this.y = 0;
        this.w = 0;
        this.h = 0;
        this.c = c;
        this.parent = null;
    }
}
