package com.fiordor.dynamic2drangetree.structure;

public class RedBlackTree<K extends Comparable<K>> {
    
    private Node<K> root;

    public RedBlackTree() {
        this.root = null;
    }

    public RedBlackTree(K k) {
        this.root = new Node<K>(k);
    }

    public Node<K> getRoot() {
        return root;
    }

    public void insert(K k) {

        if (root == null) {
            root = new Node<K>(k, Node.BLACK);
        } else {
            insertSearch(new Node<K>(k, Node.RED), root);
        }
        
    }

    public void remove() {
        
    }

    public void search() {
        
    }

    public int getDeep() {
        if (root == null) {
            return 0;
        } else {
            return deepSeach(root, 1);
        }
    }

    private int deepSeach(Node<K> leaf, int deep) {

        int left = deep, right = deep;

        if (leaf.getLeft() != null) {
            left = deepSeach(leaf.getLeft(), deep + 1);
        }

        if (leaf.getRight() != null) {
            right = deepSeach(leaf.getRight(), deep + 1);
        }

        return left > right ? left : right;

    }

    private Node<K> insertSearch(Node<K> insert, Node<K> parent) {
        
        if (insert.compareTo(parent) < 0) {

            if (parent.getLeft() == null) {
                parent.setLeft(insert);
                return insert;
            } else {
                return insertSearch(insert, parent.getLeft());
            }

        } else {

            if (parent.getRight() == null) {
                parent.setRight(insert);
                return insert;
            } else {
                return insertSearch(insert, parent.getRight());
            }
        }
    }
}
