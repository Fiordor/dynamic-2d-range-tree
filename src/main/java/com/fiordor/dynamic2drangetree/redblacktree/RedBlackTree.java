package com.fiordor.dynamic2drangetree.redblacktree;

public class RedBlackTree<K extends Comparable<K>> {

    private final boolean BLACK = Node.BLACK;
    private final boolean RED = Node.RED;
    
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

    //https://iq.opengenus.org/red-black-tree-insertion/
    //puede que nodo necesite un attrib parent

    private boolean insertSearch(Node<K> insert, Node<K> parent) {
        
        if (insert.compareTo(parent) < 0) {

            if (parent.getLeft() == null) {
                parent.setLeft(insert);
                return parent.isRed();
            } else {
                parent.setRed( insertSearch(insert, parent.getLeft()) );

                if ( parent.isRed() && parent.getLeft().isRed() ) {

                    parent.setRed(BLACK);

                    if (parent.getRight() != null && parent.getRight().isRed()) {
                        parent.getRight().setRed(BLACK);
                    }

                    return RED;
                    
                } else {
                    return BLACK;
                }
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
