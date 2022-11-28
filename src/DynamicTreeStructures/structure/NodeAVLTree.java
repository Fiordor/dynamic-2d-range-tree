package DynamicTreeStructures.structure;

/**
 *
 * @author Fiordor
 * @param <K>
 */
public class NodeAVLTree<K extends Comparable<K>> implements Comparable<NodeAVLTree<K>> {

    private K data;
    private NodeAVLTree<K> left;
    private NodeAVLTree<K> right;

    private int deepLeft;
    private int deepRight;

    public NodeAVLTree(K data) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.deepLeft = 0;
        this.deepRight = 0;
    }

    public K getData() {
        return data;
    }

    public void setData(K data) {
        this.data = data;
    }

    public NodeAVLTree<K> getLeft() {
        return left;
    }

    public void setLeft(NodeAVLTree<K> left) {
        this.left = left;
    }

    public NodeAVLTree<K> getRight() {
        return right;
    }

    public void setRight(NodeAVLTree<K> right) {
        this.right = right;
    }
    
    public int getFactor() {
        return deepLeft - deepRight;
    }
    
    public int getDeep() {
        return Math.max(deepLeft, deepRight);
    }

    public int getDeepLeft() {
        return deepLeft;
    }

    public void setDeepLeft(int deepLeft) {
        this.deepLeft = deepLeft;
    }

    public int getDeepRight() {
        return deepRight;
    }

    public void setDeepRight(int deepRight) {
        this.deepRight = deepRight;
    }

    @Override
    public int compareTo(NodeAVLTree<K> data) {
        return this.data.compareTo(data.getData());
    }

    @Override
    public String toString() {
        String l = left == null ? "null" : left.getData().toString();
        String d = data.toString();
        String r = right == null ? "null" : right.getData().toString();
        return String.format("%s;%s;%d;%s", l, d, deepLeft - deepRight, r);
    }
}
