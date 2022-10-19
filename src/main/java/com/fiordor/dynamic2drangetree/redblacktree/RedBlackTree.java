package com.fiordor.dynamic2drangetree.redblacktree;

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
