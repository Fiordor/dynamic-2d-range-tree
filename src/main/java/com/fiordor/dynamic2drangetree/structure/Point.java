package com.fiordor.dynamic2drangetree.structure;

public class Point<K extends Comparable<K>> {
    
    private K x;
    private K y;

    public Point() {
        this(null, null);
    }

    public Point(K x, K y) {
        this.x = x;
        this.y = y;
    }


    public K getX() { return x; }

    public K getY() { return y; }

    public void setX(K x) { this.x = x; }

    public void setY(K y) { this.y = y; }

}
