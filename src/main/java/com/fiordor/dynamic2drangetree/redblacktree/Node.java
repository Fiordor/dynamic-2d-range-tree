package com.fiordor.dynamic2drangetree.redblacktree;

public class Node<K extends Comparable<K>> implements Comparable<Node<K>> {

    public static final boolean BLACK = false;
    public static final boolean RED = true;
 
    private K data;
    private Node<K> left;
    private Node<K> right;
    private boolean isRed;

    public Node(K data) {
        this(data, true);
    }

    public Node(K data, boolean isRed) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.isRed = isRed;
    }

    public K getData() {
        return data;
    }

    public Node<K> getLeft() {
        return left;
    }

    public Node<K> getRight() {
        return right;
    }

    public boolean isRed() {
        return isRed;
    }

    public void setData(K data) {
        this.data = data;
    }

    public void setLeft(Node<K> left) {
        this.left = left;
    }

    public void setRight(Node<K> right) {
        this.right = right;
    }

    public void setRed(boolean isRed) {
        this.isRed = isRed;
    }

    @Override
    public int compareTo(Node<K> k) {
        return data.compareTo(k.getData());
    }
}
