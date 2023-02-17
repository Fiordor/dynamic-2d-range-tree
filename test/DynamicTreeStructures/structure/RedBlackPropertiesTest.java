package DynamicTreeStructures.structure;
import java.util.List;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

public class RedBlackPropertiesTest {
	
	/**
	 * First up: Generators of Red Black Trees!
	 */

	/**
	 * This generator creates a random sequence of keys (integers), and
	 * adds one by one the keys to an empty red-black tree.
	 * @return The RBT created by inserting a random sequence of integer keys
	 */
	@Provide
	Arbitrary<RedBlackTree<Integer>> insertFromNull() {
		Arbitrary<Integer> keys = Arbitraries.integers();
		Arbitrary<List<Integer>> keySequence = keys.list().uniqueElements();
		return keySequence.map(keysList -> {
			RedBlackTree<Integer> rbt = new RedBlackTree<>();
			for (Integer key : keysList) {
				rbt.insert(key);
			}
			return rbt;
		});
	}
	
	/**
	 * TESTS
	 */
	
	@Property
	boolean RootIsBlack(@ForAll("insertFromNull") RedBlackTree<Integer> rbt) {
		NodeRedBlackTree<Integer> root = rbt.getRoot();
		if (root == null) return true;
		return root.isRed() == false;
	}

	@Property
	boolean RedNodesHaveBlackChildren(@ForAll("insertFromNull") RedBlackTree<Integer> rbt) {
		NodeRedBlackTree<Integer> root = rbt.getRoot();
		if (root == null) {
			//Tree is empty
			return true;
		}
		//We are going to do a recursive DFS traversal
		return redPropertySatisfied(root);
	}
	
	static <K extends Comparable<K>> boolean redPropertySatisfied(NodeRedBlackTree<K> root) {
		if (root == null) return true;
		NodeRedBlackTree<K> leftChild = root.getLeft();
		NodeRedBlackTree<K> rightChild = root.getRight();
		//Check for this node and return if false
		if (root.isRed()) {
			if (leftChild != null && leftChild.isRed()) return false;
			if (rightChild != null && rightChild.isRed()) return false;
		}
		//Check for children
		boolean redPropertyInLeft = redPropertySatisfied(leftChild);
		if (redPropertyInLeft) {
			return redPropertySatisfied(rightChild);
		} else {
			return false;
		}
	}
	
	//With only the two properties above, a red-black-tree with only black nodes would pass. We need the following:
	//Every root-to-leaf path has the same number of black nodes, where the leaves are understood to be the null
	//pointers hanging out from the deepest existing nodes, and those leaves are assumed to be black.
	@Property
	boolean rootToLeavesProperty(@ForAll("insertFromNull") RedBlackTree<Integer> rbt) {
		NodeRedBlackTree<Integer> root = rbt.getRoot();
		return (blackNodesInPath(root) != -1);
	}
	
	private static <K extends Comparable<K>> int blackNodesInPath(NodeRedBlackTree<K> root) {
		//Leaves that don't exist: count as one black node (including empty root)
		if (root == null) return 1;
		//root is not null
		int nBlackNodesInLeft = blackNodesInPath(root.getLeft());
		int nBlackNodesInRight = blackNodesInPath(root.getRight());
		if (nBlackNodesInLeft != nBlackNodesInRight) return -1; //Failure!
		int nBlackNodes = nBlackNodesInLeft;
		if (root.isRed() == false) nBlackNodes++;
		return nBlackNodes;
	}
}
