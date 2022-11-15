package com.fiordor.dynamic2drangetree.redblacktree;

public class RedBlackTree<K extends Comparable<K>> {

    //private final boolean BLACK = Node.BLACK;
    //private final boolean RED = Node.RED;
    
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

        /*
        if (root == null) {
            root = new Node<K>(k, Node.BLACK);
        } else {
            insertSearch(new Node<K>(k, Node.RED), root);
        }
        */

        insertSecondVersion(k);
        
    }

    public K deleteMin() {
        return null;
    }

    public boolean search(K data) {
        Node<K> node = root;
        while (node != null) {
            K nodeData = node.getData();
            if (nodeData.equals(data)) return true;
            if (nodeData.compareTo(data) < 0) {
                node = node.getRight();
            } else {
                node = node.getLeft();
            }
        }
        //If it hasn't been found, return false.
        return false;
    }

    /**
     * Returns the smallest node K' for which K' > K in the tree.
     * If there is no such K', the result is null.
     * @param data
     * @return
     */
    public K successor(K data) {
        Node<K> res = successor(root, data);
        if (res == null) return null;
        return res.getData();
    }

    private Node<K> successor(Node<K> node, K data) {
        //Base case
        if (node == null) return null;
        //General case
        int cmp = node.getData().compareTo(data);
        if (cmp <= 0) {
            //This node is smaller than or equal to data. It cannot be its successor
            return successor(node.getRight(), data);
        } else {
            //This node could be its successor, or the successor could be in this node's
            //left child
            Node<K> res = successor(node.getLeft(), data);
            //If there was a successor in the left subtree, it is found.
            //Otherwise...
            if (res == null) {
                //All the elements in the left subtree were less than or equal to data.
                //But this node's data is greater than data. Therefore, it is the successor
                res = node;
            }
            return res;
        }
    }

    //Insertion based on the book of algorithms of Sedgewick
    //and the lecture slides of Algorithms and Data Structures II
    //at DTU, Inge Li Gørtz (2020)
    //SECOND VERSION because we split on the way down
    //Lanzadera
    public void insertSecondVersion(K data) {
        root = insertSecondVersion(root, data);
        root.setRed(false); //Root node is defined as black (makes sense)
    }
    //Returns the sub-tree with the inserted node
    private Node<K> insertSecondVersion(Node<K> node, K data) {
        //If the tree is empty (leaf), create the node
        if (node == null) {
            return new Node<K>(data);
        }
        //Else, we shall insert in one of the children.
        //First, check if the current node is a 4-node (split on the way down)
        if (node.getRight() != null && node.getLeft() != null &&
            node.getRight().isRed() && node.getLeft().isRed()) {
            //Current node is a 4-node. To split, make the children black
            //and this one red (this one merges with the parent and the children split)
            //When this one merges with the parent, it could happen that the parent is red
            //too. This will be dealt with in the parent (after this call is done).
            node.getLeft().setRed(false);
            node.getRight().setRed(false);
            node.setRed(true);
        }
        int cmp = data.compareTo(node.getData());
        if (cmp < 0) { 
            //The data goes to the left
            Node<K> leftNode = node.getLeft();
            leftNode = insertSecondVersion(leftNode, data);
            node.setLeft(leftNode);
        } else if (cmp > 0) { //The data goes to the right
            Node<K> rightNode = node.getRight();
            rightNode = insertSecondVersion(rightNode, data);
            node.setRight(rightNode);
        } else { //We are in a replacement!
            node.setData(data);
        }
        //Now we have to see if we need to do a rotation, because
        //4-nodes could have been created unbalanced. In this implementation
        //3-nodes are always balanced by definition (we allow right-leaning
        //as well as left-leaning).
        //There are four cases
        Node<K> left = node.getLeft();
        Node<K> right = node.getRight();
        if (left != null && left.isRed()) {
            if (left.getLeft() != null && left.getLeft().isRed()) {
                node = centerLeft4Node(node);
            } else if (left.getRight() != null && left.getRight().isRed()) {
                //We reduce this case to the left-left leaning case
                left = rotateLeft(left);
                node.setLeft(left);
                node = centerLeft4Node(node);
            }
        } else if (right != null && right.isRed()) {
            if (right.getLeft() != null && right.getLeft().isRed()) {
                //We reduce this case to the right-right leaning case
                right = rotateRight(right);
                node.setRight(right);
                node = centerRight4Node(node);
            } else if (right.getRight() != null && right.getRight().isRed()) {
                node = centerRight4Node(node);
            }
        }
        return node;
    }

    /**
     * Balances a 4-node that is leaning doubly to the left.
     * Changes colors accordingly.
     * @param root
     * @return
     */
    private Node<K> centerLeft4Node(Node<K> root) {
        root = rotateRight(root);
        root.setRed(false);
        root.getRight().setRed(true);
        return root;
    }

    /**
     * Balances a 4-node that is leaning doubly to the right.
     * Changes colors accordingly.
     * @param root
     * @return
     */
    private Node<K> centerRight4Node(Node<K> root) {
        root = rotateLeft(root);
        root.setRed(false);
        root.getLeft().setRed(true);
        return root;
    }
    /**
     * Makes the right node the root of @oldRoot.
     * IMPORTANT: DOES NOT CHANGE COLORS, ONLY POSITIONS
     * [For now we don't have a parameter size. If we
     * include it later, we'll have to re-compute it here]
     * @param oldRoot
     * @return
     */
    private Node<K> rotateRight(Node<K> oldRoot) {
        Node<K> newRoot = oldRoot.getLeft();
        oldRoot.setLeft(newRoot.getRight());
        newRoot.setRight(oldRoot);
        return newRoot;
    }

    /**
     * Makes the left node the root of @oldRoot.
     * IMPORTANT: DOES NOT CHANGE COLORS, ONLY POSITIONS
     * [For now we don't have a parameter size. If we
     * include it later, we'll have to re-compute it here]
     * @param oldRoot
     * @return
     */
    private Node<K> rotateLeft(Node<K> oldRoot) {
        Node<K> newRoot = oldRoot.getRight();
        oldRoot.setRight(newRoot.getLeft());
        newRoot.setLeft(oldRoot);
        return newRoot;
    }

    //https://iq.opengenus.org/red-black-tree-insertion/
    //puede que nodo necesite un attrib parent

    /*
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
    */
}
