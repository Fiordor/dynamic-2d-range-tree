
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
    
    private int factor;

    public NodeAVLTree(K data) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.factor = 0;
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
    
    @Override
    public int compareTo(NodeAVLTree<K> data) {
        return this.data.compareTo(data.getData());
    }

    @Override
    public String toString() {
        String l = left == null ? "null" : left.getData().toString();
        String d = data.toString();
        String r = right == null ? "null" : right.getData().toString();
        return String.format("%s;%s;%s", l, d, r);
    }
}
