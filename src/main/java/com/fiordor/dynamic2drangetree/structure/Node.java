package com.fiordor.dynamic2drangetree.structure;

public class Node<K extends Comparable<K>> {
 
    private Point<K>[] data;
    private Node left;
    private Node right;

    public Node(Point<K>[] data) {
        this.data = data;
    }
    
}
