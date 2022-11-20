/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DynamicTreeStructures.structure;

import DynamicTreeStructures.interfaces.TreeStructure;

/**
 *
 * @author Fiordor
 * @param <K>
 */
public class RootedBinaryTree<K extends Comparable<K>> implements TreeStructure<K> {

    private Node<K> root;

    public RootedBinaryTree() {
        this.root = null;
    }

    public RootedBinaryTree(K data) {
        this.root = new Node<>(data);
    }

    @Override
    public Node<K> getRoot() {
        return root;
    }

    @Override
    public void insert(K data) {
        if (this.root != null) {
            insertNext(this.root, data);
        } else {
            this.root = new Node<>(data);
        }
    }

    @Override
    public K search(K data) {
        return null;
    }

    @Override
    public K delete(K data) {
        return null;
    }

    private void insertNext(Node<K> node, K data) {

        if (data.compareTo(node.getData()) < 0) {

            if (node.getLeft() == null) {
                node.setLeft(new Node<>(data));
            } else {
                insertNext(node.getLeft(), data);
            }
        } else {

            if (node.getRight() == null) {
                node.setRight(new Node<>(data));
            } else {
                insertNext(node.getRight(), data);
            }
        }
    }
}
