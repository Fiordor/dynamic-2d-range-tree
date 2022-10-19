package com.fiordor.dynamic2drangetree.redblacktree;

public class Node<K extends Comparable<K>> implements Comparable<Node<K>> {

    public static final boolean BLACK = true;
    public static final boolean RED = true;
 
    private K data;
    private Node<K> left;
    private Node<K> right;
    private boolean isBlack;

    public Node(K data) {
        this(data, false);
    }

    public Node(K data, boolean isBlack) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.isBlack = isBlack;
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

    public boolean isBlack() {
        return isBlack;
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

    public void setBlack(boolean isBlack) {
        this.isBlack = isBlack;
    }

    @Override
    public int compareTo(Node<K> k) {
        return data.compareTo(k.getData());
    }
}
