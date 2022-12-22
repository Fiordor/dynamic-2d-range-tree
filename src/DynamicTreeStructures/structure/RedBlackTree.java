package DynamicTreeStructures.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import DynamicTreeStructures.interfaces.TreeStructure;

/**
 *
 * @author Fiordor
 * @param <K>
 */
public class RedBlackTree<K extends Comparable<K>> implements TreeStructure<NodeRedBlackTree<K>, K> {

    private NodeRedBlackTree<K> root;

    public RedBlackTree() {
        this.root = null;
    }

    public RedBlackTree(K k) {
        this.root = new NodeRedBlackTree<>(k);
    }

    /**
     * Get root of the tree.
     *
     * @return root node or null
     */
    @Override
    public NodeRedBlackTree<K> getRoot() {
        return root;
    }

    /**
     * Insertion of value in defined red black tree. Insertion based on the book
     * of algorithms of Sedgewick and the lecture slides of Algorithms and Data
     * Structures II at DTU, Inge LiGørtz (2020).
     *
     * //SECOND VERSION because we split on the way down.
     *
     * @param k value to insert
     */
    @Override
    public void insert(K k) {
        root = insert(root, k);
        root.setRed(false);
    }

    /**
     * Not implemented already
     *
     * @return
     */
    @Override
    public K delete(K k) {
        return null;
    }

    /**
     * Search the value in the tree. Up to down look for the value on each node
     * like binary seach. If exists return true, if not, return false.
     *
     * @param data value to search
     * @return if the search has been success
     */
    @Override
    public K search(K data) {
        NodeRedBlackTree<K> node = root;
        while (node != null) {
            K nodeData = node.getData();
            if (nodeData.equals(data)) {
                return nodeData;
            }
            if (nodeData.compareTo(data) < 0) {
                node = node.getRight();
            } else {
                node = node.getLeft();
            }
        }
        return null;
    }

    /**
     * Provides a String representation of the tree,
     * in pre-order, where each node appears in a new line.
     * It uses the toString() method of the node type K to print it.
     * @return The string representing this tree
     */
    @Override
    public String toString() {
        return toString(false);
    }

    @Override
    public String toString(boolean formated) {
        
        if (root == null) {
            return null;
        } else {

            NodeRedBlackTree<K>[] nodes = toArray();
            StringBuilder builder = new StringBuilder();

            if (formated) {

                int max = 4;
                for (int i = 0; i < nodes.length; i++) {
                    String value = nodes[i].getData().toString();
                    if (value.length() > max) {
                        max = value.length();
                    }
                }

                String format = "%" + max + "s;%" + max + "s;%" + max + "s;%" + max + "s";

                for (int i = 0; i < nodes.length; i++) {
                    String v = nodes[i].getData().toString();
                    String c = nodes[i].isRed() ? "r" : "b";
                    String l = nodes[i].getLeft() != null ? nodes[i].getLeft().getData().toString() : "null";
                    String r = nodes[i].getRight() != null ? nodes[i].getRight().getData().toString() : "null";
                    builder.append(String.format(format, v, c, l, r)).append('\n');
                }
                builder.deleteCharAt(builder.length() - 1);

            } else {

                for (int i = 0; i < nodes.length; i++) {
                    builder.append(nodes[i].toString()).append('\n');
                }
                builder.deleteCharAt(builder.length() - 1);
            }
            return builder.toString();
        }
    }
    
    /**
     * Returns a RedBlackTree<Integer> made from the toString() unformatted description of a RBTree.
     * @param s the string
     * @return The corresponding RedBlackTree<K>
     */
    public static RedBlackTree<Integer> fromString(String s) {
    	String[] lines = s.split("\\R+");
    	Iterator<String> linesIt = Arrays.asList(lines).iterator();
    	
    	RedBlackTree<Integer> rbt = new RedBlackTree<>();
    	if (linesIt.hasNext()) {
    		NodeRedBlackTree<Integer> root = parseRecursive(linesIt);
    		rbt.root = root;
    	}
    	return rbt;
    }
    
    private static NodeRedBlackTree<Integer> parseRecursive(Iterator<String> linesIt) {
    	String[] parts = linesIt.next().strip().split(";");
    	NodeRedBlackTree<Integer> node = new NodeRedBlackTree<Integer>(Integer.parseInt(parts[0]));
    	node.setRed(parts[1].equals("red"));
    	if (!parts[2].equals("null")) {
    		node.setLeft(parseRecursive(linesIt));
    	}
    	if (!parts[3].equals("null")) {
    		node.setRight(parseRecursive(linesIt));
    	}
    	return node;
    }

    /**
     * Returns the smallest node K' for which K' > K in the tree. If there is no
     * such K', the result is null.
     *
     * @param data
     * @return
     */
    public K successor(K data) {
        NodeRedBlackTree<K> res = successor(root, data);
        if (res == null) {
            return null;
        }
        return res.getData();
    }

    private NodeRedBlackTree<K> successor(NodeRedBlackTree<K> node, K data) {
        //Base case
        if (node == null) {
            return null;
        }
        //General case
        int cmp = node.getData().compareTo(data);
        if (cmp <= 0) {
            //This node is smaller than or equal to data. It cannot be its successor
            return successor(node.getRight(), data);
        } else {
            //This node could be its successor, or the successor could be in this node's
            //left child
            NodeRedBlackTree<K> res = successor(node.getLeft(), data);
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

    //Returns the sub-tree with the inserted node
    private NodeRedBlackTree<K> insert(NodeRedBlackTree<K> node, K data) {
        //If the tree is empty (leaf), create the node
        if (node == null) {
            return new NodeRedBlackTree<>(data);
        }
        //Else, we shall insert in one of the children.
        //First, check if the current node is a 4-node (split on the way down)
        if (node.getRight() != null && node.getLeft() != null
                && node.getRight().isRed() && node.getLeft().isRed()) {
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
            NodeRedBlackTree<K> leftNode = node.getLeft();
            leftNode = insert(leftNode, data);
            node.setLeft(leftNode);
        } else if (cmp > 0) { //The data goes to the right
            NodeRedBlackTree<K> rightNode = node.getRight();
            rightNode = insert(rightNode, data);
            node.setRight(rightNode);
        } else { //We are in a replacement!
            node.setData(data);
        }
        //Now we have to see if we need to do a rotation, because
        //4-nodes could have been created unbalanced. In this implementation
        //3-nodes are always balanced by definition (we allow right-leaning
        //as well as left-leaning).
        //There are four cases
        NodeRedBlackTree<K> left = node.getLeft();
        NodeRedBlackTree<K> right = node.getRight();
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
     * Center to left and recolor it. Balances a 4-node that is leaning doubly
     * to the left. Changes colors accordingly.
     *
     * @param root node to center
     * @return centered left node
     */
    private NodeRedBlackTree<K> centerLeft4Node(NodeRedBlackTree<K> root) {
        root = rotateRight(root);
        root.setRed(false);
        root.getRight().setRed(true);
        return root;
    }

    /**
     * Center to right and recolor it. Balances a 4-node that is leaning doubly
     * to the right. Changes colors accordingly.
     *
     * @param root node to center
     * @return centered right node
     */
    private NodeRedBlackTree<K> centerRight4Node(NodeRedBlackTree<K> root) {
        root = rotateLeft(root);
        root.setRed(false);
        root.getLeft().setRed(true);
        return root;
    }

    /**
     * Makes the right node the root of @oldRoot. IMPORTANT: DOES NOT CHANGE
     * COLORS, ONLY POSITIONS [For now we don't have a parameter size. If we
     * include it later, we'll have to re-compute it here]
     *
     * @param oldRoot node to rotate
     * @return rotated right node
     */
    private NodeRedBlackTree<K> rotateRight(NodeRedBlackTree<K> oldRoot) {
        NodeRedBlackTree<K> newRoot = oldRoot.getLeft();
        oldRoot.setLeft(newRoot.getRight());
        newRoot.setRight(oldRoot);
        return newRoot;
    }

    /**
     * Makes the left node the root of @oldRoot. IMPORTANT: DOES NOT CHANGE
     * COLORS, ONLY POSITIONS [For now we don't have a parameter size. If we
     * include it later, we'll have to re-compute it here].
     *
     * @param oldRoot node to rotate
     * @return retated left node
     */
    private NodeRedBlackTree<K> rotateLeft(NodeRedBlackTree<K> oldRoot) {
        NodeRedBlackTree<K> newRoot = oldRoot.getRight();
        oldRoot.setRight(newRoot.getLeft());
        newRoot.setLeft(oldRoot);
        return newRoot;
    }

    private NodeRedBlackTree<K>[] toArray() {

        if (this.root == null) {
            return new NodeRedBlackTree[0];
        }

        ArrayList<NodeRedBlackTree<K>> list = new ArrayList<>();
        list.add(this.root);
        toArrayNext(root, list);

        NodeRedBlackTree<K>[] array = new NodeRedBlackTree[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }

        return array;
    }

    private void toArrayNext(NodeRedBlackTree<K> node, ArrayList<NodeRedBlackTree<K>> list) {

        if (node.getLeft() != null) {
            list.add(node.getLeft());
            toArrayNext(node.getLeft(), list);
        }

        if (node.getRight() != null) {
            list.add(node.getRight());
            toArrayNext(node.getRight(), list);
        }
    }
}
