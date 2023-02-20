package DynamicTreeStructures.structure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.Tuple;
import net.jqwik.api.Tuple.Tuple2;
import net.jqwik.api.stateful.Action;
import net.jqwik.api.stateful.ActionSequence;

/**
 * We use model-based testing to test that the behaviour of the contents of a
 * tree is the same as the behaviour of a set.
 * 
 * @author max
 */
public class TreeContentTests {

	/**
	 * First, we need to define what actions are there in our model (These should be
	 * the methods from the API). In jqwik, these are separate classes
	 */

	abstract class RedBlackTreeAction<K extends Comparable<K>> implements Action<Tuple2<RedBlackTree<K>, HashSet<K>>> {

		protected final K key;

		public abstract void action(RedBlackTree<K> rbt, HashSet<K> model);

		public RedBlackTreeAction(K key) {
			this.key = key;
		}

		@Override
		public Tuple2<RedBlackTree<K>, HashSet<K>> run(Tuple2<RedBlackTree<K>, HashSet<K>> rbt_and_set) {
			RedBlackTree<K> rbt = rbt_and_set.get1();
			HashSet<K> model = rbt_and_set.get2();
			action(rbt, model);
			return rbt_and_set;
		}
	}

	class InsertAction<K extends Comparable<K>> extends RedBlackTreeAction<K> {
		public InsertAction(K key) {
			super(key);
		}

		@Override
		public void action(RedBlackTree<K> rbt, HashSet<K> model) {
			rbt.insert(key);
			model.add(key);
			HashSet<K> treeSet = rbt.toSet();
			assertTrue(model.equals(treeSet));
		}

		// No precondition for insert
		@Override
		public String toString() {
			return "insert key " + key;
		}
	}

	class SearchAction<K extends Comparable<K>> extends RedBlackTreeAction<K> {
		SearchAction(K key) {
			super(key);
		}

		@Override
		public void action(RedBlackTree<K> rbt, HashSet<K> model) {
			K data = rbt.search(key);
			boolean dataNotNull = data != null;
			boolean found = model.contains(key);
			assertEquals(found, dataNotNull);
		}
		// No precondition for search
		
		@Override
		public String toString() {
			return "search for key " + key;
		}
	}

	/**
	 * Now, we create the generator based on a sequence of inserts and searches
	 */

	@Provide
	Arbitrary<ActionSequence<Tuple2<RedBlackTree<Integer>, HashSet<Integer>>>> insertSearchSequences() {
		//First, we generate a list of keys. There may be duplicates, but the RBT should handle them
		Arbitrary<List<Integer>> keyList = Arbitraries.integers().list().ofMinSize(1);
		//Then, given the list, we return a sequence of inserts and deletes on it
		return keyList.flatMap(ints -> {
			//Given the list, we define two actions:
			//insertAction does an insert on an arbitrary int of the list
			Arbitrary<Action<Tuple2<RedBlackTree<Integer>, HashSet<Integer>>>> insertAction = 
					Arbitraries.integers().between(0, ints.size() - 1)
					.map(i -> ints.get(i)).map(InsertAction::new);
			//searchAction does a search on an arbitrary int of the list
			Arbitrary<Action<Tuple2<RedBlackTree<Integer>, HashSet<Integer>>>> searchAction =
					Arbitraries.integers().between(0, ints.size() - 1)
					.map(i -> ints.get(i)).map(SearchAction::new);
			//And then we take a sequence of these actions
			//The action is a combination of inserts and searches
			//I've put more frequency for inserts so that there is higher chance of positive searches
			return Arbitraries.sequences(
						Arbitraries.frequencyOf(
							Tuple.of(3, insertAction),
							Tuple.of(1, searchAction)));
		});
	}
	
	@Property
	void insertSequences(@ForAll("insertSearchSequences") 
		ActionSequence<Tuple2<RedBlackTree<Integer>, HashSet<Integer>>> insertSearchSequence) {
		insertSearchSequence.run(Tuple.of(new RedBlackTree<Integer>(), new HashSet<Integer>()));
	}
}
