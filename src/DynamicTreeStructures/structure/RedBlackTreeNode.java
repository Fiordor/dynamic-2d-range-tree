
package DynamicTreeStructures.structure;

/**
 *
 * @author Fiordor
 */
public class RedBlackTreeNode<K extends Comparable<K>> implements Comparable<RedBlackTreeNode<K>> {

    public static final boolean BLACK = false;
    public static final boolean RED = true;
 
    private K data;
    private RedBlackTreeNode<K> left;
    private RedBlackTreeNode<K> right;
    private boolean isRed;

    public RedBlackTreeNode(K data) {
        this(data, true);
    }

    public RedBlackTreeNode(K data, boolean isRed) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.isRed = isRed;
    }

    public K getData() {
        return data;
    }

    public RedBlackTreeNode<K> getLeft() {
        return left;
    }

    public RedBlackTreeNode<K> getRight() {
        return right;
    }

    public boolean isRed() {
        return isRed;
    }

    public void setData(K data) {
        this.data = data;
    }

    public void setLeft(RedBlackTreeNode<K> left) {
        this.left = left;
    }

    public void setRight(RedBlackTreeNode<K> right) {
        this.right = right;
    }

    public void setRed(boolean isRed) {
        this.isRed = isRed;
    }

    @Override
    public int compareTo(RedBlackTreeNode<K> k) {
        return data.compareTo(k.getData());
    }
}
