package DynamicTreeStructures.structure;

import DynamicTreeStructures.interfaces.TreeStructure;
import java.util.ArrayList;

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
        return toString(false);
    }

    @Override
    public String toString(boolean formated) {

        if (root == null) {
            return null;
        } else {

            NodeAVLTree<K>[] nodes = toArray();
            StringBuilder builder = new StringBuilder();

            if (formated) {

                int max = 4;
                for (int i = 0; i < nodes.length; i++) {
                    String value = nodes[i].getData().toString();
                    if (value.length() > max) {
                        max = value.length();
                    }
                }

                String format = "%" + max + "s;%" + max + "d;%" + max + "d;%" + max + "d;%" + max + "d;%" + max + "s;%" + max + "s";

                for (int i = 0; i < nodes.length; i++) {
                    String v = nodes[i].getData().toString();
                    int f = nodes[i].getFactor();
                    int d = nodes[i].getDeep();
                    int deepL = nodes[i].getDeepLeft();
                    int deepR = nodes[i].getDeepRight();
                    String l = nodes[i].getLeft() != null ? nodes[i].getLeft().getData().toString() : "null";
                    String r = nodes[i].getRight() != null ? nodes[i].getRight().getData().toString() : "null";
                    builder.append(String.format(format, v, f, d, deepL, deepR, l, r)).append('\n');
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

    private boolean insert(NodeAVLTree<K> node, K data) {

        boolean insertLeft = false;
        if (data.compareTo(node.getData()) < 0) {

            if (node.getLeft() == null) {
                node.setLeft(new NodeAVLTree<>(data));
                insertLeft = true;
            } else {
                insertLeft = insert(node.getLeft(), data);
            }
            node.setDeepLeft(node.getLeft().getDeep() + 1);
        } else {

            if (node.getRight() == null) {
                node.setRight(new NodeAVLTree<>(data));
                insertLeft = false;
            } else {
                insertLeft = insert(node.getRight(), data);
            }
            node.setDeepRight(node.getRight().getDeep() + 1);
        }

        int factor = node.getFactor();
        System.out.printf("node: %s, f: %d | insertLeft: %b\n", node.getData().toString(), node.getFactor(), insertLeft);
        if (Math.abs(factor) > 1) {

            if (factor > 1) {
                NodeAVLTree<K> left = node.getLeft();
                int insert = data.compareTo(left.getData());
                System.out.printf("l insert %d ", insert);
                if (left.getLeft() != null && insert < 0) {
                    System.out.println("rotationLL");
                    rotationLL(node);
                } else if (left.getRight() != null && insert > 0) {
                    System.out.println("rotationLR");
                    rotationLR(node);
                }
            } else if (factor < -1) {
                NodeAVLTree<K> right = node.getRight();
                int insert = data.compareTo(right.getData());
                System.out.printf("r insert %d ", insert);
                if (right.getLeft() != null && insert < 0) {
                    System.out.println("rotationRL");
                    rotationRL(node);
                } else if (right.getRight() != null && insert > 0) {
                    System.out.println("rotationRR");
                    rotationRR(node);
                }
            }
        }

        return insertLeft;
    }

    private void rotationLL(NodeAVLTree<K> node) {
        NodeAVLTree<K> center = node.getLeft();
        NodeAVLTree<K> left = node.getLeft().getLeft();
        NodeAVLTree<K> right = new NodeAVLTree<>(node.getData());

        if (node.getRight() != null) {
            right.setRight(node.getRight());
            right.setDeepRight(node.getRight().getDeep() + 1);
        }

        if (left.getRight() != null) {
            right.setLeft(left.getRight());
            right.setDeepLeft(left.getRight().getDeep() + 1);
        }

        rotationNode(node, center.getData(), left, right, left.getDeep() + 1, right.getDeep() + 1);
    }

    //esto https://www.youtube.com/watch?v=vRwi_UcZGjU&ab_channel=BackToBackSWE
    private void rotationLR(NodeAVLTree<K> node) {
        NodeAVLTree<K> center = node.getLeft().getRight();
        NodeAVLTree<K> left = node.getLeft();
        NodeAVLTree<K> right = new NodeAVLTree<>(node.getData());

        if (center.getLeft() != null) {
            left.setRight(center.getLeft());
            left.setDeepRight(center.getLeft().getDeep() + 1);
        } else {
            left.setRight(null);
            left.setDeepRight(0);
        }

        if (center.getRight() != null) {
            right.setLeft(center.getRight());
            right.setDeepLeft(center.getDeepRight() + 1);
        } else {
            right.setLeft(null);
            right.setDeepLeft(0);
        }

        if (node.getRight() != null) {
            right.setRight(node.getRight());
            right.setDeepRight(node.getRight().getDeep() + 1);
        } else {
            right.setRight(null);
            right.setDeepRight(0);
        }

        rotationNode(node, center.getData(), left, right, left.getDeep() + 1, right.getDeep() + 1);
    }

    private void rotationRR(NodeAVLTree<K> node) {
        NodeAVLTree<K> center = node.getRight();
        NodeAVLTree<K> left = new NodeAVLTree<>(node.getData());
        NodeAVLTree<K> right = node.getRight().getRight();

        if (node.getLeft() != null) {
            left.setLeft(node.getLeft());
            left.setDeepLeft(node.getLeft().getDeep() + 1);
        }

        if (right.getLeft() != null) {
            left.setRight(right.getLeft());
            left.setDeepRight(right.getLeft().getDeep() + 1);
        }

        rotationNode(node, center.getData(), left, right, left.getDeep() + 1, right.getDeep() + 1);
    }

    //esto
    private void rotationRL(NodeAVLTree<K> node) {
        NodeAVLTree<K> center = node.getRight().getLeft();
        NodeAVLTree<K> left = new NodeAVLTree<>(node.getData());
        NodeAVLTree<K> right = node.getRight();

        if (center.getLeft() != null) {
            left.setRight(center.getLeft());
            left.setDeepRight(center.getLeft().getDeep() + 1);
        } else {
            left.setRight(null);
            left.setDeepRight(0);
        }

        if (center.getRight() != null) {
            right.setLeft(center.getRight());
            right.setDeepLeft(center.getDeepRight() + 1);
        } else {
            right.setLeft(null);
            right.setDeepLeft(0);
        }

        if (node.getLeft() != null) {
            left.setLeft(node.getLeft());
            left.setDeepLeft(node.getLeft().getDeep() + 1);
        } else {
            left.setLeft(null);
            left.setDeepLeft(0);
        }

        rotationNode(node, center.getData(), left, right, 1, right.getDeep() + 1);
    }

    private void rotationNode(NodeAVLTree<K> node, K data, NodeAVLTree<K> left, NodeAVLTree<K> right, int deepLeft, int deepRight) {
        node.setData(data);
        node.setLeft(left);
        node.setRight(right);
        node.setDeepLeft(deepLeft);
        node.setDeepRight(deepRight);
    }

    private NodeAVLTree<K>[] toArray() {

        if (this.root == null) {
            return new NodeAVLTree[0];
        }

        ArrayList<NodeAVLTree<K>> list = new ArrayList<>();
        list.add(this.root);
        toArrayNext(root, list);

        NodeAVLTree<K>[] array = new NodeAVLTree[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }

        return array;
    }

    private void toArrayNext(NodeAVLTree<K> node, ArrayList<NodeAVLTree<K>> list) {

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
