package DynamicTreeStructures.structure;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.Tuple;

public class RedBlackPropertiesTest {

	final static boolean TEST_DELETES = true;

	/**
	 * First up: Generators of Red Black Trees!
	 */

	/**
	 * This generator creates a random sequence of keys (integers), and adds one by
	 * one the keys to an empty red-black tree.
	 * 
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
	 * This generator creates a random sequence of keys (integers), and adds one by
	 * one the keys to an empty red-black tree.
	 * 
	 * @return The RBT created by inserting a random sequence of integer keys
	 */

	enum TYPE {
		INSERT, DELETEMIN
	}

	@Provide
	Arbitrary<RedBlackTree<Integer>> insertAndDeleteMin() {
		Arbitrary<List<Integer>> keys = Arbitraries.integers().list();
		Arbitrary<TYPE> actions = Arbitraries.frequency(Tuple.of(6, TYPE.INSERT), Tuple.of(1, TYPE.DELETEMIN));
		return keys.map(keysList -> {
			StringBuilder list = new StringBuilder();
			RedBlackTree<Integer> rbt = new RedBlackTree<Integer>();
			try {
				for (int key : keysList) {
					while (actions.sample() == TYPE.DELETEMIN) {
						try {
							list.append("DM; ");
							rbt.deleteMin();
						} catch (Exception e) {
							fail(e);
							e.printStackTrace();
						}
					}
					list.append("I(").append(key).append("); ");
					rbt.insert(key);
				}
				list.append("\n");
				rbt.createdWith = list.toString();
			} catch (Exception e) {
				System.err.println("Error creating RBT");
				System.err.println(list.toString());
			}
			return rbt;
		});
	}

	/**
	 * Provides an arbitrary RedBlackTree from insertions that may have had some
	 * deletions.
	 * 
	 * @return
	 */
	@Provide
	Arbitrary<RedBlackTree<Integer>> insertDeleteFromNull() {
		Arbitrary<List<Integer>> keys = Arbitraries.integers().list();
		return keys.map(keysList -> {
			RedBlackTree<Integer> rbt = new RedBlackTree<>();
			ArrayList<Integer> insertedItems = new ArrayList<>(keysList.size());
			Random r = new Random();
			for (Integer key : keysList) {
				rbt.insert(key);
				insertedItems.add(key);
				// A 1 in 3 chance of deleting something
				if (TEST_DELETES) {
					if (r.nextInt(3) == 0) {
						// A 1 in 2 chance of deleting deliberately something that we inserted
						if (r.nextBoolean()) {
							// Delete something that we inserted
							int ix = r.nextInt(insertedItems.size());
							Integer randomInsertedElement = insertedItems.get(ix);
							rbt.delete(randomInsertedElement);
							// Note that this element is still in the set of inserted elements.
						} else {
							// Delete a random int
							rbt.delete(r.nextInt());
						}
					}
				}
			}
			// To complete
			return rbt;
		});
	}

	/**
	 * TESTS
	 */

	@Property
	boolean isTree(@ForAll("insertFromNull") RedBlackTree<Integer> rbt) {
		try {
			boolean b = checkTreeStructure(rbt);
			if (!b)
				System.err.println(rbt);
			return b;
		} catch (Exception e) {
			System.err.println("Exception printing rbt");
		}
		return checkTreeStructure(rbt);
	}

	@Property
	boolean isTreeDeleteMin(@ForAll("insertAndDeleteMin") RedBlackTree<Integer> rbt) {
		try {
			boolean b = checkTreeStructure(rbt);
			if (!b)
				System.err.println(rbt);
		} catch (Exception e) {
			System.err.println("Exception printing rbt");
		}
		return checkTreeStructure(rbt);
	}

	@Property
	boolean isTreeDelete(@ForAll("insertDeleteFromNull") RedBlackTree<Integer> rbt) {
		if (!TEST_DELETES) {
			return true;
		}
		try {
			boolean b = checkTreeStructure(rbt);
			if (!b)
				System.err.println(rbt);
			return b;
		} catch (Exception e) {
			System.err.println("Exception printing rbt");
		}
		return checkTreeStructure(rbt);
	}

	/**
	 * Returns true if the provided RedBlackTree has a tree structure (acyclic).
	 * 
	 * @param <T> type of the keys
	 * @param rbt the RedBlackTree
	 * @return true iff rbt is acyclic.
	 */
	public static <T extends Comparable<T>> boolean checkTreeStructure(RedBlackTree<T> rbt) {
		/*
		 * The way to check for cycles is to run a BFS search that adds each node to an
		 * IdentityHashMap. This hash map has the reference to the object as key. If we
		 * see more than once the same object in the traversal, the algorithm stops and
		 * returns false.
		 */
		try {
			NodeRedBlackTree<T> root = rbt.getRoot();
			if (root == null)
				return true;
			IdentityHashMap<NodeRedBlackTree<T>, NodeRedBlackTree<T>> set = new IdentityHashMap<>();
			Deque<NodeRedBlackTree<T>> queue = new LinkedList<>();
			queue.add(root);
			while (!queue.isEmpty()) {
				NodeRedBlackTree<T> node = queue.pop();
				if (set.containsKey(node)) {
					test(rbt);
					return false;
				}
				set.put(node, node);
				// Add neighbours to the queue
				NodeRedBlackTree<T> left = node.getLeft();
				NodeRedBlackTree<T> right = node.getRight();
				if (left != null)
					queue.addLast(left);
				if (right != null)
					queue.addLast(right);
			}
		} catch (Exception e) {
			test(rbt);
			return false;
		}
		return true;
	}

	static <T extends Comparable<T>> void test(RedBlackTree<T> rbt) {
		NodeRedBlackTree<T> root = rbt.getRoot();
		IdentityHashMap<NodeRedBlackTree<T>, NodeRedBlackTree<T>> set = new IdentityHashMap<>();
		Deque<NodeRedBlackTree<T>> queue = new LinkedList<>();
		queue.add(root);
		while (!queue.isEmpty()) {
			NodeRedBlackTree<T> node = queue.pop();
			if (set.containsKey(node)) {
				System.out.println(rbt);
			}
			set.put(node, node);
			// Add neighbours to the queue
			NodeRedBlackTree<T> left = node.getLeft();
			NodeRedBlackTree<T> right = node.getRight();
			if (left != null)
				queue.addLast(left);
			if (right != null)
				queue.addLast(right);
		}
	}

	@Property
	boolean RootIsBlack(@ForAll("insertFromNull") RedBlackTree<Integer> rbt) {
		return checkRootBlack(rbt);
	}

	@Property
	boolean RootIsBlackDeleteMins(@ForAll("insertAndDeleteMin") RedBlackTree<Integer> rbt) {
		return checkRootBlack(rbt);
	}

	@Property
	boolean RootIsBlackDeletes(@ForAll("insertDeleteFromNull") RedBlackTree<Integer> rbt) {
		if (!TEST_DELETES)
			return true;
		return checkRootBlack(rbt);
	}

	public static <K extends Comparable<K>> boolean checkRootBlack(RedBlackTree<K> rbt) {
		NodeRedBlackTree<K> root = rbt.getRoot();
		if (root == null)
			return true;
		return root.isRed() == false;
	}

	@Property
	boolean RedNodesHaveBlackChildren(@ForAll("insertFromNull") RedBlackTree<Integer> rbt) {
		return checkRedNodesHaveBlackChildren(rbt);
	}

	@Property
	boolean RedNodesHaveBlackChildrenDeleteMins(@ForAll("insertAndDeleteMin") RedBlackTree<Integer> rbt) {
		return checkRedNodesHaveBlackChildren(rbt);
	}

	@Property
	boolean RedNodesHaveBlackChildrenDeletes(@ForAll("insertDeleteFromNull") RedBlackTree<Integer> rbt) {
		if (!TEST_DELETES)
			return true;
		return checkRedNodesHaveBlackChildren(rbt);
	}

	public static <K extends Comparable<K>> boolean checkRedNodesHaveBlackChildren(RedBlackTree<K> rbt) {
		NodeRedBlackTree<K> root = rbt.getRoot();
		if (root == null) {
			// Tree is empty
			return true;
		}
		// We are going to do a recursive DFS traversal
		return redPropertySatisfied(root);
	}

	private static <K extends Comparable<K>> boolean redPropertySatisfied(NodeRedBlackTree<K> root) {
		if (root == null)
			return true;
		NodeRedBlackTree<K> leftChild = root.getLeft();
		NodeRedBlackTree<K> rightChild = root.getRight();
		// Check for this node and return if false
		if (root.isRed()) {
			if (leftChild != null && leftChild.isRed())
				return false;
			if (rightChild != null && rightChild.isRed())
				return false;
		}
		// Check for children
		boolean redPropertyInLeft = redPropertySatisfied(leftChild);
		if (redPropertyInLeft) {
			return redPropertySatisfied(rightChild);
		} else {
			return false;
		}
	}

	// With only the two properties above, a red-black-tree with only black nodes
	// would pass. We need the following:
	// Every root-to-leaf path has the same number of black nodes, where the leaves
	// are understood to be the null
	// pointers hanging out from the deepest existing nodes, and those leaves are
	// assumed to be black.
	@Property
	boolean rootToLeavesProperty(@ForAll("insertFromNull") RedBlackTree<Integer> rbt) {
		return checkBlackNodesInPath(rbt);
	}

	@Property
	boolean rootToLeavesPropertyDeleteMins(@ForAll("insertAndDeleteMin") RedBlackTree<Integer> rbt) {
		return checkBlackNodesInPath(rbt);
	}

	@Property
	boolean rootToLeavesPropertyDelete(@ForAll("insertDeleteFromNull") RedBlackTree<Integer> rbt) {
		if (!TEST_DELETES)
			return true;
		return checkBlackNodesInPath(rbt);
	}

	public static <K extends Comparable<K>> boolean checkBlackNodesInPath(RedBlackTree<K> rbt) {
		NodeRedBlackTree<K> root = rbt.getRoot();
		return (blackNodesInPath(root) != -1);
	}

	private static <K extends Comparable<K>> int blackNodesInPath(NodeRedBlackTree<K> root) {
		// Leaves that don't exist: count as one black node (including empty root)
		if (root == null)
			return 1;
		// root is not null
		int nBlackNodesInLeft = blackNodesInPath(root.getLeft());
		int nBlackNodesInRight = blackNodesInPath(root.getRight());
		if (nBlackNodesInLeft != nBlackNodesInRight)
			return -1; // Failure!
		int nBlackNodes = nBlackNodesInLeft;
		if (root.isRed() == false)
			nBlackNodes++;
		return nBlackNodes;
	}

	/*
	 * Other API methods
	 */

	@Property
	boolean successorProperty(@ForAll("insertFromNull") RedBlackTree<Integer> rbt) {
		return successorSucceeds(rbt, Integer.class, ThreadLocalRandom.current().nextInt());
	}

	@Property
	boolean successorPropertyDeleteMins(@ForAll("insertAndDeleteMin") RedBlackTree<Integer> rbt) {
		return successorSucceeds(rbt, Integer.class, ThreadLocalRandom.current().nextInt());
	}

	@Property
	boolean successorPropertyDelete(@ForAll("insertDeleteFromNull") RedBlackTree<Integer> rbt) {
		if (!TEST_DELETES)
			return true;
		return successorSucceeds(rbt, Integer.class, ThreadLocalRandom.current().nextInt());
	}

	private static <K extends Comparable<K>> boolean successorSucceeds(RedBlackTree<K> rbt, Class<K> c, K randomValue) {
		try {
			if (rbt == null) {
				System.out.println("GENERATOR GAVE NULL");
			}
			// We trust in this method being correct. If the TreeContentTests pass, strong
			// evidence towards it
			HashSet<K> contents = rbt.toSet();
			@SuppressWarnings("unchecked")
			K[] contentsArray = contents.toArray((K[]) Array.newInstance(c, 0));
			Arrays.sort(contentsArray, K::compareTo);
			int size = contentsArray.length;
			// Check random element
			int i = 0;
			K element = null;
			K expectedSuccessor = null;
			if (size == 0) {
				// Edge case for the test, no value to choose from in the tree. Must choose a
				// random one
				element = randomValue;
				// expected successor is null
			} else {
				ThreadLocalRandom.current().nextInt(size);
				element = contentsArray[i];
				expectedSuccessor = (i + 1) < size ? contentsArray[i + 1] : null;
			}
			K actualSuccessor = rbt.successor(element);
			if (expectedSuccessor == null)
				return actualSuccessor == null;
			else
				return expectedSuccessor.equals(actualSuccessor);

		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
	}

	// Ideas for refactoring the delete generator
	/*
	 * private enum RBTActionType { INSERT, DELETE }
	 * 
	 * private static <K> Arbitrary<List<Tuple2<RBTActionType, K>>>
	 * insertDeleteSeq(Arbitrary<List<K>> items) { Arbitrary<RBTActionType>
	 * actionTypeArbitrary = Arbitraries.of(RBTActionType.class);
	 * Arbitrary<List<Tuple2<RBTActionType, K>>> sequence = items.map(itemList -> {
	 * Random oneInThree = new Random(); int size = itemList.size();
	 * List<Tuple2<RBTActionType, K>> insertDeleteList = new ArrayList<>(size * 2);
	 * ArrayList<K> insertedItems = new ArrayList<>(size); Iterator<K> itemIterator
	 * = itemList.iterator(); for (int i = 0; i < size; i++) { K key =
	 * itemIterator.next(); insertDeleteList.add(Tuple.of(RBTActionType.INSERT,
	 * key)); insertedItems.add(key); if (oneInThree.nextInt(3) == 0) {
	 * 
	 * } } return insertDeleteList; }); return null; }
	 */
	/*
	 * //The integers are the "inserts". Searches and deletes will either be from
	 * the inserted //elements, or from random ones. Arbitrary<List<Integer>> keys =
	 * Arbitraries.integers().list(); Arbitrary<List<Tuple2<RBTActionType,
	 * Integer>>> actionSequence = insertDeleteSeq(keys); return
	 * actionSequence.map((seq) -> { RedBlackTree<Integer> rbt = new
	 * RedBlackTree<>(); for (Tuple2<RBTActionType, Integer> tup : seq) { if
	 * (tup.get1() == RBTActionType.INSERT) { rbt.insert(tup.get2()); } else {
	 * rbt.delete(tup.get2()); } } return rbt; });
	 */
}
