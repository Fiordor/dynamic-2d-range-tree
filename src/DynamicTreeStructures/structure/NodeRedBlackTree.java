
package DynamicTreeStructures.structure;

/**
 *
 * @author Fiordor
 * @param <K>
 */
public class NodeRedBlackTree<K extends Comparable<K>> implements Comparable<NodeRedBlackTree<K>> {

    public static final boolean BLACK = false;
    public static final boolean RED = true;
 
    private K data;
    private NodeRedBlackTree<K> left;
    private NodeRedBlackTree<K> right;
    private boolean isRed;

    public NodeRedBlackTree(K data) {
        this(data, true);
    }

    public NodeRedBlackTree(K data, boolean isRed) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.isRed = isRed;
    }

    public K getData() {
        return data;
    }

    public NodeRedBlackTree<K> getLeft() {
        return left;
    }

    public NodeRedBlackTree<K> getRight() {
        return right;
    }

    public boolean isRed() {
        return isRed;
    }

    public void setData(K data) {
        this.data = data;
    }

    public void setLeft(NodeRedBlackTree<K> left) {
        this.left = left;
    }

    public void setRight(NodeRedBlackTree<K> right) {
        this.right = right;
    }

    public void setRed(boolean isRed) {
        this.isRed = isRed;
    }
    
    /**
     * Changes the data of this node to a different one.
     * Used in deletions when substituting this node by the successor.
     * Does not change the color of the nodes.
     * @param other the new data
     */
    public void changeData(K otherData) {
    	this.data = otherData;
    }

    @Override
    public int compareTo(NodeRedBlackTree<K> k) {
        return data.compareTo(k.getData());
    }

    @Override
    public String toString() {
        String l = left == null ? "null" : left.data.toString();
        String d = data.toString();
        String c = isRed ? "red" : "black";
        String r = right == null ? "null" : right.data.toString();
        return String.format("%s;%s;%s;%s", d, c, l, r);
    }
}
