package DynamicTreeStructures.structure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.Tuple;
import net.jqwik.api.Tuple.Tuple2;
import net.jqwik.api.state.Action;
import net.jqwik.api.state.ActionChain;
import net.jqwik.api.state.Transformer;

public class RedBlackTreeStatefulTests {

	// yee boiii

	class InsertAction implements Action.Independent<Tuple2<RedBlackTree<Integer>, HashSet<Integer>>> {
		@Override
		public Arbitrary<Transformer<Tuple2<RedBlackTree<Integer>, HashSet<Integer>>>> transformer() {
			Arbitrary<Integer> insertElements = Arbitraries.integers();
			return insertElements.map(key -> Transformer.mutate(
					"insert(" + key + ")",
					(rbt_and_set) -> {
						RedBlackTree<Integer> rbt = rbt_and_set.get1();
						HashSet<Integer> model = rbt_and_set.get2();
						//Here we perform the action
						rbt.insert(key);
						model.add(key);
						//Here we check the particular state change provoked by insert (postcondition)
						HashSet<Integer> treeSet = rbt.toSet();
						assertTrue(model.equals(treeSet)); // Java HashSet comparison
						assertEquals(key, rbt.search(key));
			}));
		}
	}

	class SearchAction implements Action.Dependent<Tuple2<RedBlackTree<Integer>, HashSet<Integer>>> {

		@Override
		public boolean precondition(Tuple2<RedBlackTree<Integer>, HashSet<Integer>> rbt_and_set) {
			return !rbt_and_set.get2().isEmpty();
		}
		
		@Override
		public Arbitrary<Transformer<Tuple2<RedBlackTree<Integer>, HashSet<Integer>>>> transformer(
				Tuple2<RedBlackTree<Integer>, HashSet<Integer>> rbt_and_set) {
			HashSet<Integer> model = rbt_and_set.get2();
			//Here we perform the action. The action will be to search for a random value of the set.
			//Note that getting a random value from the set is O(n) time.
			Arbitrary<Integer> searchIndex = Arbitraries.integers().between(0, model.size()-1);
			return searchIndex.map(index -> Transformer.mutate("search ix(" + index + ")", 
					(rbt_and_set_again) -> {
				//Find the key
				RedBlackTree<Integer> rbt = rbt_and_set_again.get1();
				Iterator<Integer> iter = model.iterator();
				for (int i = 0; i < index; i++) {
				    iter.next();
				}
				Integer key = iter.next();
				//Search for it
				Integer data = rbt.search(key);
				//Check result (postcondition)
				assertNotNull(data);
				assertEquals(data, key);
			}));
		}
	}
	
	private Action<Tuple2<RedBlackTree<Integer>, HashSet<Integer>>> deleteMin() {
		  return Action.just(Transformer.mutate("deleteMin", (rbt_and_set) -> {
			  RedBlackTree<Integer> rbt = rbt_and_set.get1();
			  HashSet<Integer> model = rbt_and_set.get2();
			  //We perform the action
			  Integer actualMin = rbt.deleteMin();
			  //We check the result
			  if (model.isEmpty()) {
				  assertNull(actualMin);
			  } else {
				  Integer expectedMin = Collections.min(model);
				  model.remove(expectedMin);
				  assertEquals(String.format("Expected min (%d) and actual minimum (%d) differ", expectedMin, actualMin),
						  actualMin, expectedMin);
				  assertNull(rbt.search(expectedMin));
			  }
		  }));
	}
	
	class DeleteAction implements Action.Dependent<Tuple2<RedBlackTree<Integer>, HashSet<Integer>>> {
		
		@Override
		public boolean precondition(Tuple2<RedBlackTree<Integer>, HashSet<Integer>> rbt_and_set) {
			return !rbt_and_set.get2().isEmpty();
		}
		
		@Override
		public Arbitrary<Transformer<Tuple2<RedBlackTree<Integer>, HashSet<Integer>>>> transformer(
				Tuple2<RedBlackTree<Integer>, HashSet<Integer>> rbt_and_set) {
			HashSet<Integer> model = rbt_and_set.get2();
			//Here we perform the action. The action will be to delete a random value of the set.
			//Note that getting a random value from the set is O(n) time.
			Arbitrary<Integer> deleteIndex = Arbitraries.integers().between(0, model.size()-1);
			return deleteIndex.map(index -> Transformer.mutate("delete ix(" + index + ")", 
					(rbt_and_set_again) -> {
				//Find the key
				RedBlackTree<Integer> rbt = rbt_and_set_again.get1();
				Iterator<Integer> iter = model.iterator();
				for (int i = 0; i < index; i++) {
				    iter.next();
				}
				Integer key = iter.next();
				//Delete it
				Integer data = rbt.delete(key);
				model.remove(key);
				//Check result (postcondition)
				assertNotNull(data);
				assertEquals(data, key);
				assertNull(rbt.search(key));
			}));
		}
	}

	@Property
	void checkMyRBT(@ForAll("rbtActions") ActionChain<Tuple2<RedBlackTree<Integer>, HashSet<Integer>>> chain) {
		chain
		.withInvariant("isTree", rbt_and_set -> {
			assertTrue(RedBlackPropertiesTest.checkTreeStructure(rbt_and_set.get1()));
		})
		.withInvariant("rootIsBlack", rbt_and_set -> {
			assertTrue(RedBlackPropertiesTest.checkRootBlack(rbt_and_set.get1()));
		})
		.withInvariant("redNodesHaveBlackChildren", rbt_and_set -> {
			assertTrue(RedBlackPropertiesTest.checkRedNodesHaveBlackChildren(rbt_and_set.get1()));
		})
		.withInvariant("sameNumberOfBlackNodesOnEachPath", rbt_and_set -> {
			assertTrue(RedBlackPropertiesTest.checkBlackNodesInPath(rbt_and_set.get1()));
		})
		.run();
	}

	@Provide
	Arbitrary<ActionChain<Tuple2<RedBlackTree<Integer>, HashSet<Integer>>>> rbtActions() {
		return ActionChain.startWith(() -> Tuple.of(new RedBlackTree<Integer>(), new HashSet<Integer>()))
				.withAction(new InsertAction())
				//.withAction(new SearchAction())
				.withAction(deleteMin());
	}
}
