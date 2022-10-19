package com.fiordor.dynamic2drangetree.utils;

public class ImageNode {
    
    public String data;
    public boolean isBlack;

    public int x;
    public int y;
    public int width;
    public int height;

    public ImageNode parent;

    public ImageNode() {
        this(null, false);
    }

    public ImageNode(String data, boolean isBlack) {
        this.data = data;
        this.isBlack = isBlack;
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.parent = null;
    }
}
