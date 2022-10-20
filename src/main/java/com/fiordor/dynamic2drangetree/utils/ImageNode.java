package com.fiordor.dynamic2drangetree.utils;

public class ImageNode {
    
    public String data;
    public boolean isRed;

    public int x;
    public int y;
    public int width;
    public int height;

    public ImageNode parent;

    public ImageNode() {
        this(null, true);
    }

    public ImageNode(String data, boolean isRed) {
        this.data = data;
        this.isRed = isRed;
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.parent = null;
    }
}
