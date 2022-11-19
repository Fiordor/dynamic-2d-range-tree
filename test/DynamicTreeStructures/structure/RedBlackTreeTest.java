package DynamicTreeStructures.structure;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RedBlackTreeTest {
	@Test
	public void testBasic() {
		RedBlackTree<Integer> rbt = new RedBlackTree<>();
		rbt.insert(5);
		assertEquals(rbt.getRoot().getData(), (Integer) 5);
	}
}
