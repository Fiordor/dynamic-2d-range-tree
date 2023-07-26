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
		Wrapper<K> result = new Wrapper<>();
		//root = delete2Rec234Launch(root, k, result);
		result.item = null;
		return result.item;
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
				root.setLeft(null); 
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
	
	
	private ArrayList<NodeRedBlackTree<K>> extractRight234(NodeRedBlackTree<K> root, boolean rightKeepsLeft) {
		ArrayList<NodeRedBlackTree<K>> ret = new ArrayList<>(2);
		if (root == null) {
			throw new RuntimeException("Tried to extract left (2-3-4 method) in a null node");
		}
		if (root.isRed() == true)
			throw new RuntimeException("Tried to use a 2-3-4 method in a red node");
		NodeRedBlackTree<K> right = root.getRight();
		if (right != null && right.isRed()) {
			right.setRed(false);
			ret.add(right);
			ret.add(root);
			if (rightKeepsLeft) {
				root.setRight(null);
			} else {
				root.setRight(right.getLeft());
				right.setLeft(null);
			}
		} else {
			//the right is the root.
			ret.add(root);
			NodeRedBlackTree<K> left = root.getLeft();
			ret.add(left);
			if (left != null) {
				if (rightKeepsLeft) {
					root.setLeft(left.getRight());
					left.setRight(null);
				} else {
					root.setLeft(null);
				}
				left.setRed(false);
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
						root = doLeftBorrow(leftParent, root, sibling);
					}
				} else {
					//Normal rebalancing without grandparent
					root = rebalance2Node234Left(root, leftParent);
				}
			} 
			//End of 2-node special invariant check
			//Perform deletion
			return deleteMinRec234(root, result);
		}
	}	
	
	private NodeRedBlackTree<K> deleteRec234Launch(NodeRedBlackTree<K> root, K search, Wrapper<K> result) {
		if (root == null) { //Empty tree. result wrapper is null by default.
			return null;
		}
		/*
		 * First we need to find the element to delete. If it is in a (2-3-4) leaf, we will be able to
		 * delete it right away. If not, we need to delete the successor and switch the values.
		 * 
		 * The root need not be checked for the "no 2-node" invariant.
		 */
		NodeRedBlackTree<K> leftChild = leftNode234(root);
		boolean leaf = leftChild == null;
		if (leaf) { //Won't have to do any successor substitution
			root = deleteInLeaf(root, search, result);
			return root;
		} else { 
			//If we find the element in the root 2-3-4 node, we'll have to substitute 
			//by the successor. Else, we start checking the "no 2-node" invariant on 
			//the corresponding child. Successor will have to check the "no 2-node invariant" too
			//First, find if the search data is in this node.
			int compare = search.compareTo(root.getData());
			if (compare == 0) {
				//replace with successor
			} else if (compare < 0) {
				//check if it's in left
				NodeRedBlackTree<K> left = root.getLeft();
				if (left.isRed()) {
					//check in left
					int compareLeft = search.compareTo(left.getData());
					if (compareLeft == 0) {
						//replace with successor.
						//check invariant on the right of left.
						root = rebalance2Node234Middle(root, left.getRight(), true);
						//successor
						//...
					} else if (compareLeft < 0) {
						//check invariant on the left of left and recurse
						root = rebalance2Node234Left(root, left.getLeft());
						//continue delete
						deleteRec234(root, search, result);
					} else {
						//check invariant on the right of left and recurse
						root = rebalance2Node234Middle(root, left.getRight(), true);
						//continue delete
						deleteRec234(root, search, result);
					}
				} else {
					//check invariant on the left, and recurse on left
					//check if root is 2-node
					NodeRedBlackTree<K> right = root.getRight();
					if (!right.isRed()) {
						//root is 2-node. Special rebalance may be needed
						if (is2Node(left)) {
							//special rebalance
							if (is2Node(right)) {
								//merge
								root = mergeNodes234(left, root, right);
								//relaunch, size has changed.
								deleteRec234Launch(root, search, result);
							} else {
								//borrow
								root = doLeftBorrow(left, root, right);
								//continue with delete
								deleteRec234(root, search, result);
							}
						} else {
							//just delete
							deleteRec234(root, search, result);
						}
					} else {
						//root is not 2-node.
						if (is2Node(left)) {
							//normal rebalance
							root = rebalance2Node234Left(root, left);
						}
						deleteRec234(root, search, result);
					}
				}
			} else {
				//same but for right
			}
		}
		return null;
	}
	
	private NodeRedBlackTree<K> delete2Rec234Launch(NodeRedBlackTree<K> root, K search, Wrapper<K> result) {
		if (root == null) { //Empty tree. result wrapper is null by default.
			return null;
		}
		/*
		 * First we need to find the element to delete. If it is in a (2-3-4) leaf, we will be able to
		 * delete it right away. If not, we need to delete the successor and switch the values.
		 * 
		 * The root need not be checked for the "no 2-node" invariant.
		 */
		NodeRedBlackTree<K> left234Child = leftNode234(root);
		boolean leaf = left234Child == null;
		if (leaf) { //Won't have to do any successor substitution
			root = deleteInLeaf(root, search, result);
			return root;
		} //Else: there is a second level
		int compareTo = search.compareTo(root.getData());
		if (is2Node(root)) {
			//We may have to shrink the size of the tree or do a special rebalance
			if (compareTo >= 0) {
				//Check invariant on the right
				NodeRedBlackTree<K> right = root.getRight();
				if (is2Node(right)) {
					//apply invariant
					NodeRedBlackTree<K> leftSibling = root.getLeft();
					if (is2Node(leftSibling)) {
						//shrink and restart (edge case, returns here)
						root = mergeNodes234(leftSibling, root, right);
						return delete2Rec234Launch(root, search, result);
					} else {
						//borrow
						root = doRightBorrow(right, root, leftSibling);
					}
				}
			} else {
				//Check invariant on the left
				NodeRedBlackTree<K> left = root.getLeft();
				if (is2Node(left)) {
					//apply invariant
					NodeRedBlackTree<K> rightSibling = root.getRight();
					if (is2Node(rightSibling)) {
						//shrink and restart (edge case, returns here)
						root = mergeNodes234(left, root, rightSibling);
						return delete2Rec234Launch(root, search, result);
					} else {
						//borrow
						root = doLeftBorrow(left, root, rightSibling);
					}
				}
			}
		} else {
			//Only normal rebalances may happen. We need to check where do we have to 
			//check the invariant
			root = applyNo2NodeInvariant(root, search);
		}
		//continue with delete
		return deleteRec234(root, search, result);
	}
	
	/**
	 * Checks and applies if necessary the "no 2-node" invariant to the appropriate child of root,
	 * following the search of element "search". root must not be a 2-node.
	 * @param root
	 * @param search
	 * @return
	 */
	private NodeRedBlackTree<K> applyNo2NodeInvariant(NodeRedBlackTree<K> root, K search) {
		//check the invariant
		int compareTo = search.compareTo(root.getData());
		if (compareTo < 0) {
			NodeRedBlackTree<K> left = root.getLeft();
			if (left.isRed()) {
				int compareToLeft = search.compareTo(left.getData());
				if (compareToLeft >= 0) {
					//invariant on the middle
					NodeRedBlackTree<K> middleChild = left.getRight();
					if (is2Node(middleChild))
						root = rebalance2Node234Middle(root, middleChild, true);
					//done
				} else {
					//invariant on the left
					NodeRedBlackTree<K> leftChild = left.getLeft();
					if (is2Node(leftChild))
						root = rebalance2Node234Left(root, leftChild);
					//done
				}
			} else if (is2Node(left))
				root = rebalance2Node234Left(root, left);
				//done
		} else if (compareTo == 0) {
			NodeRedBlackTree<K> right = root.getRight();
			if (right.isRed()) {
				NodeRedBlackTree<K> middleChild = root.getLeft();
				if (is2Node(middleChild))
					root = rebalance2Node234Middle(root, middleChild, false);
				//done
			} else if (is2Node(right))
				root = rebalance2Node234Right(root, right);
			//done
		} else {
			NodeRedBlackTree<K> right = root.getRight();
			if (right.isRed()) {
				int compareToRight = search.compareTo(right.getData());
				if (compareToRight >= 0) {
					NodeRedBlackTree<K> rightChild = right.getRight();
					if (is2Node(rightChild)) 
						root = rebalance2Node234Right(root, right);
					//done
				} else {
					NodeRedBlackTree<K> middleChild = right.getLeft();
					if (is2Node(middleChild))
						root = rebalance2Node234Middle(root, middleChild, false);
					//done
				}
			} else if (is2Node(right))
				root = rebalance2Node234Right(root, right);
		}
		return root;
	}
	
	/**
	 * Deletes a key from the 2-3-4 tree rooted at root. Root is not a 2-node, and root is not a leaf.
	 * @param root
	 * @param search
	 * @param result
	 * @return
	 */
	private NodeRedBlackTree<K> deleteRec234(NodeRedBlackTree<K> root, K search, Wrapper<K> result) {
		root = applyNo2NodeInvariant(root, search);
		//GO
		int compareTo = search.compareTo(root.getData());
		if (compareTo == 0) {
			//Replace by successor. Successor is the minimum of the right tree
			NodeRedBlackTree<K> right = root.getRight();
			if (right.isRed()) {
				NodeRedBlackTree<K> left = right.getLeft();
				Wrapper<K> successor = new Wrapper<>();
				left = deleteMinRec234Launch(left, successor);
				right.setLeft(left);
				result.item = root.getData();
				root.changeData(successor.item);
			} else {
				Wrapper<K> successor = new Wrapper<>();
				right = deleteMinRec234Launch(right, successor);
				root.setRight(right);
				result.item = root.getData();
				root.changeData(successor.item);
			}
		} else if (compareTo < 0) {
			//Search in left
			NodeRedBlackTree<K> left = root.getLeft();
			if (left.isRed()) {
				int compareToLeft = search.compareTo(left.getData());
				if (compareToLeft == 0) {
					//Replace by successor
					NodeRedBlackTree<K> right = left.getRight();
					Wrapper<K> successor = new Wrapper<>();
					right = deleteMinRec234Launch(right, successor);
					left.setRight(right);
					result.item = left.getData();
					left.changeData(successor.item);
				} else if (compareToLeft < 0) {
					NodeRedBlackTree<K> leftChild234 = left.getLeft();
					if (is234Leaf(leftChild234)) 
						leftChild234 = deleteInLeaf(leftChild234, search, result);
					else
						leftChild234 = deleteRec234(leftChild234, search, result);
					left.setLeft(leftChild234);
				} else if (compareToLeft > 0) {
					NodeRedBlackTree<K> middleChild234 = left.getRight();
					if (is234Leaf(middleChild234)) 
						middleChild234 = deleteInLeaf(middleChild234, search, result);
					else
						middleChild234 = deleteRec234(middleChild234, search, result);
					left.setRight(middleChild234);
				}
			} else {
				if (is234Leaf(left)) {
					left = deleteInLeaf(left, search, result);
				} else {
					left = deleteRec234(root, search, result);
				}
				root.setLeft(left);
			}
		} else if (compareTo > 0) {
			//Search in right
			NodeRedBlackTree<K> right = root.getRight();
			if (right.isRed()) {
				int compareToRight = search.compareTo(right.getData());
				if (compareToRight == 0) {
					//Replace by successor
					NodeRedBlackTree<K> rightChild = right.getRight();
					Wrapper<K> successor = new Wrapper<>();
					rightChild = deleteMinRec234Launch(rightChild, successor);
					right.setRight(rightChild);
					result.item = right.getData();
					right.changeData(successor.item);
				} else if (compareToRight < 0) {
					NodeRedBlackTree<K> middleChild234 = right.getLeft();
					if (is234Leaf(middleChild234)) 
						middleChild234 = deleteInLeaf(middleChild234, search, result);
					else
						middleChild234 = deleteRec234(middleChild234, search, result);
					right.setLeft(middleChild234);
				} else if (compareToRight > 0) {
					NodeRedBlackTree<K> rightChild234 = right.getRight();
					if (is234Leaf(rightChild234)) 
						rightChild234 = deleteInLeaf(rightChild234, search, result);
					else
						rightChild234 = deleteRec234(rightChild234, search, result);
					right.setRight(rightChild234);
				}
			} else {
				if (is234Leaf(right)) {
					right = deleteInLeaf(right, search, result);
				} else {
					right = deleteRec234(root, search, result);
				}
				root.setLeft(right);
			}
		}
		return root;
	}
	
	private boolean is234Leaf(NodeRedBlackTree<K> node) {
		NodeRedBlackTree<K> left = node.getLeft();
		return left == null || (left.isRed() && (left.getLeft() == null));
	}
	
	private NodeRedBlackTree<K> deleteInLeaf(NodeRedBlackTree<K> root, K search, Wrapper<K> result) {
		int compare = search.compareTo(root.getData());
		if (compare == 0) {
			//We have found the leaf.
			result.item = root.getData();
			NodeRedBlackTree<K> left = root.getLeft();
			if (left == null) {
				NodeRedBlackTree<K> right = root.getRight();
				if (right == null) {
					return null; //empty tree
				} else {
					right.setRed(false); //new root is right
					return right;
				}
			} else {
				left.setRight(root.getRight()); //new root is left
				left.setRed(false);
				return left;
			}
		} else if (compare < 0) {
			NodeRedBlackTree<K> left = root.getLeft();
			if (left != null && (search.compareTo(left.getData()) == 0)) {
				result.item = left.getData();
				root.setLeft(null);
				return root;
			} else {
				//element not found in 2-3-4 leaf
				return root;
			}
		} else {
			NodeRedBlackTree<K> right = root.getRight();
			if (right != null && (search.compareTo(right.getData()) == 0)) {
				result.item = right.getData();
				root.setRight(null);
				return root;
			} else {
				//element not found in 2-3-4 leaf
				return root;
			}
		}
	}
	
	/**
	 * Deletes the minimum leaf on the tree rooted at leftParent. Returns the resulting tree.
	 * leftParent should not be null, or a 2-node, or a 2-3-4 leaf.
	 * @param leftParent
	 * @param result
	 * @return
	 */
	private NodeRedBlackTree<K> deleteMinRec234(NodeRedBlackTree<K> leftParent, Wrapper<K> result) {
		NodeRedBlackTree<K> leftChild = leftNode234(leftParent); //Always exists, launcher deals with this case
		if (is2Node(leftChild)) {
			leftParent = rebalance2Node234Left(leftParent, leftChild);
			leftChild = leftNode234(leftParent);
		}
		if (is234Leaf(leftChild)) {
			//We reached the end! Delete left-most value
			NodeRedBlackTree<K> ret = leftChild.getLeft();
			if (ret == null) {
				leftChild = rotateLeft(leftChild); //is not 2-node by invariant
				leftChild.setRed(false);
				ret = leftChild.getLeft();
			}
			result.item = ret.getData();
			leftChild.setLeft(null);
		} else {
			leftChild = deleteMinRec234(leftChild, result);
		}
		setLeftChild234(leftParent, leftChild);
		return leftParent;
	}
	
	/**
	 * METHODS FOR BORROWING IN THE "NO 2-NODE" INVARIANT
	 */
	
	/**
	 * leftChild borrows from rightSibling a key. The left-most key of root becomes the
	 * rbt-parent of leftChild, and the left-most key of rightSibling substitutes root.
	 * root should not be a 2-node.
	 * @param leftChild
	 * @param root
	 * @param rightSibling
	 * @return the new root.
	 */
	private NodeRedBlackTree<K> leftNodeBorrowFromRight(NodeRedBlackTree<K> leftChild, 
			NodeRedBlackTree<K> root, NodeRedBlackTree<K> rightSibling) {
		
		NodeRedBlackTree<K> lca = root.getLeft();
		if (!lca.isRed()) {
			root = rotateLeft(root);
			lca = root.getLeft();
			root.setRed(false);
		}
		
		lca = doLeftBorrow(leftChild, lca, rightSibling);
		root.setLeft(lca);
		lca.setRed(true);
		return root;
	}
	
	private NodeRedBlackTree<K> rightNodeBorrowFromRight(NodeRedBlackTree<K> leftChild,
			NodeRedBlackTree<K> root, NodeRedBlackTree<K> rightSibling) {
		
		ArrayList<NodeRedBlackTree<K>> rightPartRoot = extractRight234(root, true);
		NodeRedBlackTree<K> lca = rightPartRoot.get(0);
		NodeRedBlackTree<K> newRoot = rightPartRoot.get(1);
		
		lca = doLeftBorrow(leftChild, lca, rightSibling);
		newRoot.setRight(lca);
		lca.setRed(true);
		return newRoot;
	}
	
	private NodeRedBlackTree<K> doLeftBorrow(NodeRedBlackTree<K> leftChild, 
			NodeRedBlackTree<K> parent, NodeRedBlackTree<K> rightSibling) {

		NodeRedBlackTree<K> remainingSibling = rightSibling;
		NodeRedBlackTree<K> leftNodeSibling = rightSibling.getLeft();
		if (leftNodeSibling == null || !leftNodeSibling.isRed()) {
			remainingSibling = rotateLeft(rightSibling);
			remainingSibling.setRed(false);
			leftNodeSibling = remainingSibling.getLeft();
		}
		leftNodeSibling.setRed(false);
		remainingSibling.setLeft(leftNodeSibling.getRight());
		leftNodeSibling.setRight(null);
		
		NodeRedBlackTree<K> zTree = leftNodeSibling.getLeft();
		leftNodeSibling.setRight(remainingSibling);
		leftNodeSibling.setLeft(parent);
		parent.setRed(false);
		parent.setLeft(leftChild);
		leftChild.setRed(true);
		parent.setRight(zTree);
		return leftNodeSibling;
	}
	
	private NodeRedBlackTree<K> leftNodeBorrowFromLeft(NodeRedBlackTree<K> rightChild,
			NodeRedBlackTree<K> root, NodeRedBlackTree<K> leftSibling) {
		
		//ArrayList<NodeRedBlackTree<K>> leftPartRoot = extractLeft234(root, true);
		//NodeRedBlackTree<K> lca = leftPartRoot.get(0);
		//NodeRedBlackTree<K> newRoot = leftPartRoot.get(1);
		
		NodeRedBlackTree<K> lca = root.getLeft();
		if (!lca.isRed()) {
			root = rotateLeft(root);
			root.setRed(false);
			lca = root.getLeft();
		}
		
		lca = doRightBorrow(rightChild, lca, leftSibling);
		root.setLeft(lca);
		lca.setRed(true);
		return root;
	}
	
	private NodeRedBlackTree<K> rightNodeBorrowFromLeft(NodeRedBlackTree<K> rightChild,
			NodeRedBlackTree<K> root, NodeRedBlackTree<K> leftSibling) {
		
		ArrayList<NodeRedBlackTree<K>> rightPartRoot = extractRight234(root, true);
		NodeRedBlackTree<K> lca = rightPartRoot.get(0);
		NodeRedBlackTree<K> newRoot = rightPartRoot.get(1);
		
		lca = doRightBorrow(rightChild, lca, leftSibling);
		newRoot.setRight(lca);
		lca.setRed(true);
		return newRoot;
	}
	
	private NodeRedBlackTree<K> doRightBorrow(NodeRedBlackTree<K> rightChild,
			NodeRedBlackTree<K> parent, NodeRedBlackTree<K> leftSibling) {

		ArrayList<NodeRedBlackTree<K>> rightPartSibling = extractRight234(leftSibling, false);
		NodeRedBlackTree<K> rightNodeSibling = rightPartSibling.get(0);
		NodeRedBlackTree<K> remainingSibling = rightPartSibling.get(1);

		NodeRedBlackTree<K> zTree = rightNodeSibling.getRight();
		rightNodeSibling.setLeft(remainingSibling);
		rightNodeSibling.setRight(parent);
		parent.setRed(false);
		parent.setRight(rightChild);
		rightChild.setRed(true);
		parent.setLeft(zTree);
		return rightNodeSibling;
	}
	
	/**
	 * Applies the "no 2-node" invariant to leftChild. leftChild must be the left-most 2-3-4
	 * child of leftParent. leftParent should not be a 2-node.
	 * Will apply merging or borrowing from the right sibling of leftChild.
	 * @param leftParent the 2-3-4 parent of leftChild
	 * @param leftChild the left-most child of leftParent to rebalance.
	 * @return
	 */
	private NodeRedBlackTree<K> rebalance2Node234Left(NodeRedBlackTree<K> leftParent, NodeRedBlackTree<K> leftChild) {
		NodeRedBlackTree<K> sibling234 = secondNode234(leftParent);
		if (is2Node(sibling234)) {
			// Merge with the left part of leftParent
			
			if (!leftParent.getLeft().isRed()) {
				leftParent = rotateLeft(leftParent);
				leftParent.setRed(false);
			}
			NodeRedBlackTree<K> newLeftRoot = leftParent.getLeft();
			leftChild = mergeNodes234(leftChild, newLeftRoot, sibling234);
			leftParent.setLeft(leftChild);
			return leftParent;
		} else {
			leftParent = leftNodeBorrowFromRight(leftChild, leftParent, sibling234);
			return leftParent;
		}
	}
	
	/**
	 * Applies the "no 2-node" invariant to the middle child of the parent 2-3-4 node.
	 * The parent 2-3-4 node should not be a 2-node.
	 * @param parent
	 * @param middleChild
	 * @param leftLeaning
	 * @return
	 */
	private NodeRedBlackTree<K> rebalance2Node234Middle(NodeRedBlackTree<K> parent, 
			NodeRedBlackTree<K> middleChild, boolean leftLeaning) {
		if (leftLeaning) {
			NodeRedBlackTree<K> lca = parent.getLeft();
			NodeRedBlackTree<K> leftSibling = lca.getLeft();
			//Check if we have to merge
			if (is2Node(leftSibling)) {
				parent.setLeft(mergeNodes234(leftSibling, lca, middleChild));
			} else {
				parent = leftNodeBorrowFromLeft(middleChild, parent, leftSibling);
			}
		} else {
			//Check if merge
			//If not merge:
			NodeRedBlackTree<K> lca = parent.getRight();
			NodeRedBlackTree<K> rightSibling = lca.getRight();
			if (is2Node(rightSibling)) {
				parent.setRight(mergeNodes234(middleChild, lca, rightSibling));
			} else {
				parent = rightNodeBorrowFromRight(middleChild, parent, rightSibling);
			}
		}
		return parent;
	}
	
	private NodeRedBlackTree<K> rebalance2Node234Right(NodeRedBlackTree<K> parent, 
			NodeRedBlackTree<K> rightChild) {
		//Get the left sibling
		NodeRedBlackTree<K> leftSibling = null;
		NodeRedBlackTree<K> right = parent.getRight();
		if (right.isRed()) {
			leftSibling = right.getLeft();
		} else {
			leftSibling = parent.getLeft().getRight();
		}
		if (is2Node(leftSibling)) {
			ArrayList<NodeRedBlackTree<K>> rightPartParent = extractRight234(parent, true);
			NodeRedBlackTree<K> lca = rightPartParent.get(0);
			NodeRedBlackTree<K> newRoot = rightPartParent.get(1);
			newRoot.setRight(mergeNodes234(leftSibling, lca, rightChild));
			return newRoot;
		} else {
			parent = rightNodeBorrowFromRight(rightChild, parent, leftSibling);
			return parent;
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
