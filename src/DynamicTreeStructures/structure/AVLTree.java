package DynamicTreeStructures.structure;

import DynamicTreeStructures.interfaces.TreeStructure;

/**
 *
 * @author Fiordor
 * @param <K>
 */
public class AVLTree<K extends Comparable<K>> implements TreeStructure<NodeAVLTree, K> {

    private NodeAVLTree<K> root;

    public AVLTree() {
        this.root = null;
    }

    public AVLTree(K data) {
        this.root = new NodeAVLTree<>(data);
    }

    @Override
    public NodeAVLTree<K> getRoot() {
        return root;
    }

    @Override
    public void insert(K data) {

        if (this.root == null) {
            this.root = new NodeAVLTree<>(data);
        } else {
            insert(this.root, data);
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

    @Override
    public String toString() {

        if (root == null) {
            return null;
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append(root.toString());
            toStringNext(root, builder);
            return builder.toString();
        }
    }

    private void insert(NodeAVLTree<K> node, K data) {

        if (data.compareTo(node.getData()) < 0) {

            if (node.getLeft() == null) {
                node.setLeft(new NodeAVLTree<>(data));
            } else {
                insert(node.getLeft(), data);
            }
            node.setDeepLeft(node.getDeepLeft() + 1);
        } else {

            if (node.getRight() == null) {
                node.setRight(new NodeAVLTree<>(data));
            } else {
                insert(node.getRight(), data);
            }
            node.setDeepRight(node.getDeepRight() + 1);
        }

        if (Math.abs(node.getFactor()) > 1) {
            int deep = node.getDeep();

            if (node.getDeepLeft() == deep && node.getRight() == null) {
                NodeAVLTree<K> left = node.getLeft();

                if (left.getLeft() != null && left.getRight() == null) {
                    rotationLL(node);
                } else if (left.getLeft() == null && left.getRight() != null) {
                    rotationLR(node);
                }
            } else if (node.getDeepRight() == deep && node.getLeft() == null) {
                NodeAVLTree<K> right = node.getRight();

                if (right.getLeft() != null && right.getRight() == null) {
                    rotationRL(node);
                } else if (right.getLeft() == null && right.getRight() != null) {
                    rotationRR(node);
                }
            }
        }
    }

    private void rotationLL(NodeAVLTree<K> node) {
        NodeAVLTree<K> center = node.getLeft();
        NodeAVLTree<K> left = node.getLeft().getLeft();
        NodeAVLTree<K> right = new NodeAVLTree<>(node.getData());

        rotationNode(node, center.getData(), left, right, left.getDeep() + 1, 1);
    }

    private void rotationLR(NodeAVLTree<K> node) {
        NodeAVLTree<K> center = node.getLeft().getRight();
        NodeAVLTree<K> left = node.getLeft();
        NodeAVLTree<K> right = new NodeAVLTree<>(node.getData());

        left.setRight(null);
        left.setDeepRight(left.getDeepRight() - 1);

        rotationNode(node, center.getData(), left, right, left.getDeep() + 1, 1);
    }

    private void rotationRR(NodeAVLTree<K> node) {
        NodeAVLTree<K> center = node.getRight();
        NodeAVLTree<K> left = new NodeAVLTree<>(node.getData());
        NodeAVLTree<K> right = node.getRight().getRight();

        rotationNode(node, center.getData(), left, right, 1, right.getDeep() + 1);
    }

    private void rotationRL(NodeAVLTree<K> node) {
        NodeAVLTree<K> center = node.getRight().getLeft();
        NodeAVLTree<K> left = new NodeAVLTree<>(node.getData());
        NodeAVLTree<K> right = node.getRight();

        right.setLeft(null);
        right.setDeepLeft(left.getDeepLeft()- 1);

        rotationNode(node, center.getData(), left, right, 1, right.getDeep() + 1);
    }

    private void rotationNode(NodeAVLTree<K> node, K data, NodeAVLTree<K> left, NodeAVLTree<K> right, int deepLeft, int deepRight) {
        node.setData(data);
        node.setLeft(left);
        node.setRight(right);
        node.setDeepLeft(deepLeft);
        node.setDeepRight(deepRight);
    }

    private void toStringNext(NodeAVLTree<K> node, StringBuilder builder) {

        final char SPLITTER = '\n';
        
        if (node.getLeft() != null) {
            builder.append(SPLITTER).append(node.getLeft().toString());
            toStringNext(node.getLeft(), builder);
        }

        if (node.getRight() != null) {
            builder.append(SPLITTER).append(node.getRight().toString());
            toStringNext(node.getRight(), builder);
        }
    }
}
