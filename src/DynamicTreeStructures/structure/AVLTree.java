
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

    private void toStringNext(NodeAVLTree<K> node, StringBuilder builder) {

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
