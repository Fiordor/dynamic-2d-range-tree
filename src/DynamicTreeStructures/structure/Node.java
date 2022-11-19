/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DynamicTreeStructures.structure;

/**
 *
 * @author Fiordor
 * @param <K>
 */
public class Node<K extends Comparable<K>> implements Comparable<Node<K>> {

    private K data;
    private Node<K> left;
    private Node<K> right;

    public Node(K data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    public K getData() {
        return data;
    }

    public void setData(K data) {
        this.data = data;
    }

    public Node<K> getLeft() {
        return left;
    }

    public void setLeft(Node<K> left) {
        this.left = left;
    }

    public Node<K> getRight() {
        return right;
    }

    public void setRight(Node<K> right) {
        this.right = right;
    }

    @Override
    public int compareTo(Node<K> data) {
        return this.data.compareTo(data.getData());
    }

    @Override
    public String toString() {
        String l = left == null ? "null" : left.data.toString();
        String d = data.toString();
        String r = right == null ? "null" : right.data.toString();
        return String.format("[%s,%s,%s]", l, d, r);
    }
}
