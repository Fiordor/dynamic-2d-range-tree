package com.fiordor.dynamic2drangetree.utils;

public class ImageLabel {

    private String label;
    private int x;
    private int y;
    private int w;
    private int h;
    private boolean b;

    public ImageLabel(String label, int x, int y, int w, int h, boolean b) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.b = b;
    }

    public String label() { return label; }
    public int x() { return x; }
    public int y() { return y; }
    public int w() { return w; }
    public int h() { return h; }
    public boolean b() { return b; }
    
}
