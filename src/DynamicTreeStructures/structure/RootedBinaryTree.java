
package DynamicTreeStructures.structure;

import DynamicTreeStructures.interfaces.TreeStructure;

/**
 *
 * @author Fiordor
 * @param <K>
 */
public class RootedBinaryTree<K extends Comparable<K>> implements TreeStructure<NodeRootedBinaryTree<K>, K> {

    private NodeRootedBinaryTree<K> root;

    public RootedBinaryTree() {
        this.root = null;
    }

    public RootedBinaryTree(K data) {
        this.root = new NodeRootedBinaryTree<>(data);
    }

    @Override
    public NodeRootedBinaryTree<K> getRoot() {
        return root;
    }

    @Override
    public void insert(K data) {
        if (this.root != null) {
            insertNext(this.root, data);
        } else {
            this.root = new NodeRootedBinaryTree<>(data);
        }
    }

    @Override
    public K search(K data) {
        return this.root == null ? null : searchNext(this.root, data);
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

    private void insertNext(NodeRootedBinaryTree<K> node, K data) {

        if (data.compareTo(node.getData()) < 0) {

            if (node.getLeft() == null) {
                node.setLeft(new NodeRootedBinaryTree<>(data));
            } else {
                insertNext(node.getLeft(), data);
            }
        } else {

            if (node.getRight() == null) {
                node.setRight(new NodeRootedBinaryTree<>(data));
            } else {
                insertNext(node.getRight(), data);
            }
        }
    }

    private K searchNext(NodeRootedBinaryTree<K> node, K data) {

        if (data.compareTo(node.getData()) < 0) {
            return node.getLeft() == null ? null : searchNext(node.getLeft(), data);
        } else if (data.compareTo(node.getData()) > 0) {
            return node.getRight() == null ? null : searchNext(node.getRight(), data);
        } else {
            return data;
        }
    }

    private void toStringNext(NodeRootedBinaryTree<K> node, StringBuilder builder) {

        if (node.getLeft() != null) {
            builder.append('|').append(node.getLeft().toString());
            toStringNext(node.getLeft(), builder);
        }

        if (node.getRight() != null) {
            builder.append('|').append(node.getRight().toString());
            toStringNext(node.getRight(), builder);
        }
    }
}
