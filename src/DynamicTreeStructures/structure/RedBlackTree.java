package DynamicTreeStructures.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import DynamicTreeStructures.interfaces.TreeStructure;

/**
 *
 * @author Fiordor
 * @param <K>
 */
public class RedBlackTree<K extends Comparable<K>> implements TreeStructure<NodeRedBlackTree<K>, K> {

	private NodeRedBlackTree<K> root;

	public String createdWith;

	public RedBlackTree() {
		this.root = null;
	}

	public RedBlackTree(K k) {
		this.root = new NodeRedBlackTree<>(k);
		this.root.setRed(false);
	}

	protected RedBlackTree(NodeRedBlackTree<K> root) {
		this.root = root;
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
	 * Insertion of value in defined red black tree. Insertion based on the book of
	 * algorithms of Sedgewick and the lecture slides of Algorithms and Data
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
	 * Search the value in the tree. Up to down look for the value on each node like
	 * binary seach. If exists return true, if not, return false.
	 *
	 * @param data value to search
	 * @return the data if the search has been successful, or null otherwise
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
	 * Not implemented already
	 *
	 * @return The element K if it is in the RedBlackTree, or null if it is not
	 */
	@Override
	public K delete(K k) {
		return null;
	}
	
	class Wrapper<T> {
		T item = null;

		Wrapper() {
		}

		Wrapper(T item) {
			this.item = item;
		}
	}
	
	public K deleteMin() {
		Wrapper<K> result = new Wrapper<>();
		// root = deleteMin(root, result); //old
		root = deleteMinRec234Launch(root, result);
		return result.item;
	}

	/**
	 * Provides a String representation of the tree, in pre-order, where each node
	 * appears in a new line. It uses the toString() method of the node type K to
	 * print it.
	 * 
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
			if (createdWith != null)
				builder.append(createdWith);
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

	/**
	 * Returns the set represented by the RedBlackTree. Used for stateful testing.
	 * 
	 * @return A HashSet<K> containing all the values of this tree
	 */
	public HashSet<K> toSet() {
		HashSet<K> set = new HashSet<>();
		if (root == null)
			return set;
		toSet(set, root);
		return set;
	}

	/**
	 * Recursive method to traverse the tree and make a set.
	 * 
	 * @param set  reference to the set to be filled.
	 * @param root the current node
	 */
	private void toSet(HashSet<K> set, NodeRedBlackTree<K> root) {
		if (root == null)
			return; // Null leaf base case
		set.add(root.getData());
		toSet(set, root.getLeft());
		toSet(set, root.getRight());
	}

	/**
	 * Returns a RedBlackTree<Integer> made from the toString() unformatted
	 * description of a RBTree.
	 * 
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
		String color = parts[1].strip();
		node.setRed(color.equals("red") || color.equals("r"));
		if (!parts[2].strip().equals("null")) {
			node.setLeft(parseRecursive(linesIt));
		}
		if (!parts[3].strip().equals("null")) {
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
		// Base case
		if (node == null) {
			return null;
		}
		// General case
		int cmp = node.getData().compareTo(data);
		if (cmp <= 0) {
			// This node is smaller than or equal to data. It cannot be its successor
			return successor(node.getRight(), data);
		} else {
			// This node could be its successor, or the successor could be in this node's
			// left child
			NodeRedBlackTree<K> res = successor(node.getLeft(), data);
			// If there was a successor in the left subtree, it is found.
			// Otherwise...
			if (res == null) {
				// All the elements in the left subtree were less than or equal to data.
				// But this node's data is greater than data. Therefore, it is the successor
				res = node;
			}
			return res;
		}
	}

	/**
	 * Returns the sub-tree with the inserted node.
	 * @param node the root of the tree
	 * @param data the key to insert
	 * @return the tree with the key inserted
	 */
	private NodeRedBlackTree<K> insert(NodeRedBlackTree<K> node, K data) {
		// If the tree is empty (leaf), create the node
		if (node == null) {
			return new NodeRedBlackTree<>(data);
		}
		// Else, we shall insert in one of the children.
		// First, check if the current node is a 4-node (split on the way down)
		if (node.getRight() != null && node.getLeft() != null && node.getRight().isRed() && node.getLeft().isRed()) {
			// Current node is a 4-node. To split, make the children black
			// and this one red (this one merges with the parent and the children split)
			// When this one merges with the parent, it could happen that the parent is red
			// too. This will be dealt with in the parent (after this call is done).
			node.getLeft().setRed(false);
			node.getRight().setRed(false);
			node.setRed(true);
		}
		int cmp = data.compareTo(node.getData());
		if (cmp < 0) {
			// The data goes to the left
			NodeRedBlackTree<K> leftNode = node.getLeft();
			leftNode = insert(leftNode, data);
			node.setLeft(leftNode);
		} else if (cmp > 0) { // The data goes to the right
			NodeRedBlackTree<K> rightNode = node.getRight();
			rightNode = insert(rightNode, data);
			node.setRight(rightNode);
		} else { // We are in a replacement!
			node.setData(data);
		}
		// Now we have to see if we need to do a rotation, because
		// 4-nodes could have been created unbalanced. In this implementation
		// 3-nodes are always balanced by definition (we allow right-leaning
		// as well as left-leaning).
		// There are four cases
		NodeRedBlackTree<K> left = node.getLeft();
		NodeRedBlackTree<K> right = node.getRight();
		if (left != null && left.isRed()) {
			if (left.getLeft() != null && left.getLeft().isRed()) {
				node = centerLeft4Node(node);
			} else if (left.getRight() != null && left.getRight().isRed()) {
				// We reduce this case to the left-left leaning case
				left = rotateLeft(left);
				node.setLeft(left);
				node = centerLeft4Node(node);
			}
		} else if (right != null && right.isRed()) {
			if (right.getLeft() != null && right.getLeft().isRed()) {
				// We reduce this case to the right-right leaning case
				right = rotateRight(right);
				node.setRight(right);
				node = centerRight4Node(node);
			} else if (right.getRight() != null && right.getRight().isRed()) {
				node = centerRight4Node(node);
			}
		}
		return node;
	}

	/*********************
	 * INSERTION METHODS
	 *********************/
	
	/**
	 * Center to left and recolor it. Balances a 4-node that is leaning doubly to
	 * the left. Changes colors accordingly.
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
	 * Center to right and recolor it. Balances a 4-node that is leaning doubly to
	 * the right. Changes colors accordingly.
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
	 * Makes the right node the root of @oldRoot. IMPORTANT: DOES NOT CHANGE COLORS,
	 * ONLY POSITIONS [For now we don't have a parameter size. If we include it
	 * later, we'll have to re-compute it here]
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
	 * Makes the left node the root of @oldRoot. IMPORTANT: DOES NOT CHANGE COLORS,
	 * ONLY POSITIONS [For now we don't have a parameter size. If we include it
	 * later, we'll have to re-compute it here].
	 *
	 * @param oldRoot node to rotate
	 * @return rotated left node
	 */
	private NodeRedBlackTree<K> rotateLeft(NodeRedBlackTree<K> oldRoot) {
		NodeRedBlackTree<K> newRoot = oldRoot.getRight();
		oldRoot.setRight(newRoot.getLeft());
		newRoot.setLeft(oldRoot);
		return newRoot;
	}

	/******************
	 * DELETION METHODS 
	 ******************/

	/******************************************* 
	 * Methods to handle the RBT as a 2-3-4 tree
	 ********************************************/
	
	// This had better be non-null
	/**
	 * Returns true if the given black node is a 2-node.
	 * @param node the black, root node
	 * @return if node is a 2-node.
	 */
	private boolean is2Node(NodeRedBlackTree<K> node) {
		if (node == null)
			throw new RuntimeException("Tried to check for 2 node to a null node");
		NodeRedBlackTree<K> leftChild = node.getLeft();
		NodeRedBlackTree<K> rightChild = node.getRight();
		return (leftChild == null || !leftChild.isRed()) && (rightChild == null || !rightChild.isRed());
	}
	
	/**
	 * Given the root of a 2-3-4 node, it removes its left-most element and returns
	 * it. The left-most RBT node is returned in the first element of the array. The
	 * remaining 2-3-4 node is returned in the second element of the array. The
	 * colors are changed accordingly. The left node is returned as black.
	 * 
	 * @param root           the root of the 2-3-4 node
	 * @param leftKeepsRight establishes if the left node keeps the right child or
	 *                       instead it is kept by the parent.
	 * @return [left_node, node_without_left]
	 */
	private ArrayList<NodeRedBlackTree<K>> extractLeft234(NodeRedBlackTree<K> root, boolean leftKeepsRight) {
		ArrayList<NodeRedBlackTree<K>> ret = new ArrayList<NodeRedBlackTree<K>>(2);
		if (root == null) {
			throw new RuntimeException("Tried to extract left (2-3-4 method) in a null node");
		}
		if (root.isRed() == true)
			throw new RuntimeException("Tried to use a 2-3-4 method in a red node");
		NodeRedBlackTree<K> left = root.getLeft();
		if (left != null && left.isRed()) {
			// We return the left
			ret.add(left);
			left.setRed(false);
			ret.add(root);
			if (leftKeepsRight) {
				root.setLeft(null); // Isn't it setLeft??? before it was setRight
			} else {
				root.setLeft(left.getRight());
				left.setRight(null);
			}
		} else {
			// The left is the root.
			ret.add(root);
			NodeRedBlackTree<K> right = root.getRight();
			ret.add(right);
			if (right != null) {
				if (leftKeepsRight) { // Might want to check this
					root.setRight(right.getLeft());
					right.setLeft(null);
				} else {
					root.setRight(null);
				}
				right.setRed(false);
			}
		}
		return ret;
	}

	/**
	 * Adds the given left node to the root of a 2-3-4 tree. the left node should
	 * not have a right child (it will be lost, the right child will be the current
	 * left child of the 2-3-4 tree).
	 * 
	 * @param root The root of the 2-3-4 node
	 * @param left The node to add to the left
	 */
	private void addLeft234(NodeRedBlackTree<K> root, NodeRedBlackTree<K> left) {
		NodeRedBlackTree<K> leftChild = root.getLeft();
		if (leftChild != null && leftChild.isRed()) {
			throw new RuntimeException("The 2-3-4 node provided to addLeft234 already has a left child");
		}
		if (root.isRed())
			throw new RuntimeException("Tried to use addLeft234 in a red node");
		root.setLeft(left);
		left.setRight(leftChild);
		left.setRed(true);
	}

	/**
	 * Sets the given left child as the left child of the 2-3-4 node of root
	 * 
	 * @param root the root node
	 * @param left the left child
	 */
	private void setLeftChild234(NodeRedBlackTree<K> root, NodeRedBlackTree<K> left) {
		if (root.isRed())
			throw new NullPointerException("Gave a red node to setLeftChild234");
		NodeRedBlackTree<K> rbtLeft = root.getLeft();
		if (rbtLeft != null && rbtLeft.isRed()) {
			rbtLeft.setLeft(left);
		} else {
			root.setLeft(left);
		}
	}

	/**
	 * Merges the given three RBT nodes into a 2-3-4 node. The middle node should
	 * not have left and right children, since they will be lost (the children of
	 * left and right will be used). The colors are set accordingly.
	 */
	private NodeRedBlackTree<K> mergeNodes234(NodeRedBlackTree<K> left, NodeRedBlackTree<K> middle,
			NodeRedBlackTree<K> right) {
		middle.setLeft(left);
		middle.setRight(right);
		middle.setRed(false);
		left.setRed(true);
		right.setRed(true);
		return middle;
	}
	
	/**
	 * Merges the left or right part of the parent 2-3-4 node with the left-most
	 * or right-most children.
	 * @param parent the root of the parent 2-3-4 tree.
	 * @param leftChild if we should merge with the left-most children or the right-most ones.
	 * @return the root of the parent after the merging operation.
	 */
	private NodeRedBlackTree<K> mergeWithParent(NodeRedBlackTree<K> parent, boolean leftChild) {
		if (leftChild) {
			NodeRedBlackTree<K> firstChild = leftNode234(parent);
			NodeRedBlackTree<K> secondChild = secondNode234(parent);
			List<NodeRedBlackTree<K>> splitParent = extractLeft234(parent, false);
			NodeRedBlackTree<K> leftParent = splitParent.get(0);
			NodeRedBlackTree<K> remainingParent = splitParent.get(1);
			NodeRedBlackTree<K> mergedChild = mergeNodes234(firstChild, leftParent, secondChild);
			setLeftChild234(remainingParent, mergedChild);
			return remainingParent;
		} else {
			return parent;
		}
	}

	/**
	 * Returns the left 2-3-4 child node, or the left black node, of root.
	 * 
	 * @param root
	 * @return
	 */
	private NodeRedBlackTree<K> leftNode234(NodeRedBlackTree<K> root) {
		if (root.isRed())
			throw new RuntimeException("Gave a red root to leftNode234");
		NodeRedBlackTree<K> rbtLeft = root.getLeft();
		if (rbtLeft != null && rbtLeft.isRed()) {
			rbtLeft = rbtLeft.getLeft();
		}
		return rbtLeft;
	}

	/**
	 * Returns the second 2-3-4 child of root.
	 * 
	 * @param root
	 * @return
	 */
	private NodeRedBlackTree<K> secondNode234(NodeRedBlackTree<K> root) {
		if (root.isRed())
			throw new RuntimeException("Gave a red root to secondNode234");
		NodeRedBlackTree<K> rbtLeft = root.getLeft();
		if (rbtLeft != null && rbtLeft.isRed()) {
			return rbtLeft.getRight();
		} else {
			NodeRedBlackTree<K> rbtRight = root.getRight();
			if (rbtRight != null && rbtRight.isRed()) {
				return rbtRight.getLeft();
			} else {
				return rbtRight;
			}
		}
	}
	
	/**
	 * Deletes the minimum node in the 2-3-4 tree of root. Returns the new tree root,
	 * and includes the deleted value in result. If the tree is empty, the result is null.
	 * @param root
	 * @param result
	 * @return
	 */
	private NodeRedBlackTree<K> deleteMinRec234Launch(NodeRedBlackTree<K> root, Wrapper<K> result) {
		if (root == null)
			//result has not been set, so it is null
			return null;
		//We check if there is any 2-3-4 node to the left of root
		NodeRedBlackTree<K> leftParent = leftNode234(root);
		if (leftParent == null) {
			// Only the root 2-3-4 node exists
			// We must check if there is a left red node
			NodeRedBlackTree<K> rootLeft = root.getLeft();
			if (rootLeft == null) {
				// Root is the left-most node
				result.item = root.getData();
				// The right element is the new root (whether it exists or not)
				NodeRedBlackTree<K> rootRight = root.getRight();
				if (rootRight != null)
					rootRight.setRed(false);
				return rootRight;
			} else {
				// The left-most element is the minimum
				result.item = rootLeft.getData();
				// We delete the left node (has no children by 2-3-4 property)
				root.setLeft(null);
				// Root does not change
				return root;
			}
		} else {
			//There exists a second level on the tree. We need to check the special
			//rebalancing condition that could be needed at the root.
			//The root can be a 2-node. The problem is its left child.
			if (is2Node(leftParent)) {
				//We need rebalancing
				if (is2Node(root)) {
					//Perform edge case for the rebalancing (root is 2-node)
					NodeRedBlackTree<K> sibling = root.getRight(); //remember root is 2-node
					if (is2Node(sibling)) {// Merge the three top nodes
						root = mergeNodes234(leftParent, root, sibling);
						// The tree has shrunk in size. We should re-start the algorithm
						// in case we need rebalancing from the root again
						return deleteMinRec234Launch(root, result);
					} else {
						// Borrow from the sibling
						ArrayList<NodeRedBlackTree<K>> extractSibling = extractLeft234(sibling, false);
						NodeRedBlackTree<K> siblingLeft = extractSibling.get(0);
						NodeRedBlackTree<K> newSibling = extractSibling.get(1);
						// Build 234 node for the left.
						// The current root will be the root of the left 234 node.
						//root.setLeft(leftParent);
						leftParent.setRed(true);
						// Move the subtree of the left part of the sibling to the right part of the
						// left node
						root.setRight(siblingLeft.getLeft());
						siblingLeft.setLeft(root);
						siblingLeft.setRed(false);
						siblingLeft.setRight(newSibling);
						// fix our pointers
						leftParent = root;
						root = siblingLeft;
					}
				} else {
					//Normal rebalancing without grandparent
					NodeRedBlackTree<K> leftParentParent = null;
					NodeRedBlackTree<K> rightSibling = null;
					NodeRedBlackTree<K> leftRoot = root.getLeft();
					NodeRedBlackTree<K> rightRoot = root.getRight();
					rightSibling = secondNode234(root);
					if (is2Node(rightSibling)) {
						ArrayList<NodeRedBlackTree<K>> parts = extractLeft234(root, true);
						NodeRedBlackTree<K> leftPart = parts.get(0);
						NodeRedBlackTree<K> newRoot = parts.get(1);
						newRoot.setLeft(mergeNodes234(leftParent, leftPart, rightSibling));
						root = newRoot;
					} else {
						root = leftNodeBorrowFromRight234Node(leftParent, root, rightSibling);
					}
					//root = rebalance2Node234(root, leftParent);
				}
			} 
			//End of 2-node special invariant check
			//Perform deletion
			return deleteMinRec234(root, result);
		}
	}

	/**
	 * Deletes the minimum of the sub-tree of grandParent. grandParent should not be
	 * null.
	 * 
	 * @param grandParent
	 * @param result
	 */
	private NodeRedBlackTree<K> deleteMinRec234(NodeRedBlackTree<K> grandParent, Wrapper<K> result) {
		NodeRedBlackTree<K> leftParent = leftNode234(grandParent);
		if (leftParent == null) {
			// what if grandParent has a left red child? The minimum will be there
			List<NodeRedBlackTree<K>> leftExtraction = extractLeft234(grandParent, false); // want the rest in the tree
			result.item = leftExtraction.get(0).getData(); // Since grandParent is not null, this should not be null.
			return leftExtraction.get(1); // The rest
		}
		NodeRedBlackTree<K> leftChild = leftNode234(leftParent);
		if (leftChild == null) {
			// We reached the end!
			List<NodeRedBlackTree<K>> splitLeftParent = extractLeft234(leftParent, false); // left node does not keep
																							// the right node
			NodeRedBlackTree<K> ret = splitLeftParent.get(0);
			NodeRedBlackTree<K> newLeftParent = splitLeftParent.get(1);
			result.item = ret.getData();
			setLeftChild234(grandParent, newLeftParent);
			return grandParent;
		} else {
			if (is2Node(leftChild)) {
				leftParent = rebalance2Node234(leftParent, leftChild);
			}
			leftParent = deleteMinRec234(leftParent, result);
			setLeftChild234(grandParent, leftParent);
			return grandParent;
		}
	}
	
	/**
	 * leftChild borrows from rightSibling a key. The left-most key of root becomes the
	 * rbt-parent of leftChild, and the left-most key of rightSibling substitutes root.
	 * @param leftChild
	 * @param root
	 * @param rightSibling
	 * @return the new root.
	 */
	private NodeRedBlackTree<K> leftNodeBorrowFromRight234Node(NodeRedBlackTree<K> leftChild, 
			NodeRedBlackTree<K> root, NodeRedBlackTree<K> rightSibling) {
		
		ArrayList<NodeRedBlackTree<K>> leftPartRoot = extractLeft234(root, true);
		NodeRedBlackTree<K> lca = leftPartRoot.get(0);
		NodeRedBlackTree<K> newRoot = leftPartRoot.get(1);
		
		ArrayList<NodeRedBlackTree<K>> leftPartSibling = extractLeft234(rightSibling, false);
		NodeRedBlackTree<K> leftNodeSibling = leftPartSibling.get(0);
		NodeRedBlackTree<K> remainingSibling = leftPartSibling.get(1);
		
		NodeRedBlackTree<K> zTree = leftNodeSibling.getLeft();
		
		newRoot.setLeft(leftNodeSibling);
		leftNodeSibling.setRed(true);
		leftNodeSibling.setRight(remainingSibling);
		leftNodeSibling.setLeft(lca);
		lca.setRed(false);
		lca.setLeft(leftChild);
		leftChild.setRed(true);
		lca.setRight(zTree);
		return newRoot;
	}
	
	private NodeRedBlackTree<K> borrowFromLeft234Node(NodeRedBlackTree<K> origin,
			NodeRedBlackTree<K> parent, NodeRedBlackTree<K> sibling) {
		
		return null;
	}
	
	private NodeRedBlackTree<K> mergeWithSibling(NodeRedBlackTree<K> origin, 
			NodeRedBlackTree<K> root, NodeRedBlackTree<K> sibling) {
		
		return null;
	}
	
	private NodeRedBlackTree<K> rebalance2Node234(NodeRedBlackTree<K> leftParent, NodeRedBlackTree<K> leftChild) {
		NodeRedBlackTree<K> sibling234 = secondNode234(leftParent);
		if (is2Node(sibling234)) {
			// Merge with the left part of leftParent
			
			List<NodeRedBlackTree<K>> ret = extractLeft234(leftParent, true);
			NodeRedBlackTree<K> newLeftRoot = ret.get(0);
			leftParent = ret.get(1);
			leftChild = mergeNodes234(leftChild, newLeftRoot, newLeftRoot.getRight()); // last one could also be
																						// sibling234
			setLeftChild234(leftParent, leftChild);
			return leftParent;
			
			/*leftParent = mergeWithParent(leftParent, true);
			return leftParent; */
		} else {
			/*
			// Merge with the left part of leftParent; borrow from left part of sibling
			List<NodeRedBlackTree<K>> splitLeftParent = extractLeft234(leftParent, false);
			NodeRedBlackTree<K> leftBranch = splitLeftParent.get(0);
			leftChild.setRed(true); // The RBT child of the leftBranch
			leftParent = splitLeftParent.get(1);
			// now, sibling is the left child of the new leftParent
			List<NodeRedBlackTree<K>> splitSibling = extractLeft234(sibling234, false);
			NodeRedBlackTree<K> leftPartSibling = splitSibling.get(0);
			NodeRedBlackTree<K> rightPartSibling = splitSibling.get(1);
			// leftPartSibling is now the left part of the 234 node of leftParent
			leftParent.setLeft(leftPartSibling);
			leftPartSibling.setRed(true);
			NodeRedBlackTree<K> zTree = leftPartSibling.getLeft();
			leftPartSibling.setLeft(leftBranch);
			leftBranch.setRight(zTree);
			leftPartSibling.setRight(rightPartSibling);
			/*
			 * setLeftChild234(leftParent, rightPartSibling); //the root of sibling might
			 * have changed //a bit of dirty pointer manipulation so that the left 234 child
			 * of sibling be now the //right 234 child of the left branch
			 * leftBranch.setRight(leftPartSibling.getLeft()); //Recheck this
			 * addLeft234(leftParent, leftPartSibling); setLeftChild234(leftParent,
			 * leftBranch);
			 */
			leftParent = leftNodeBorrowFromRight234Node(leftChild, leftParent, sibling234);
			return leftParent;
		}
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
