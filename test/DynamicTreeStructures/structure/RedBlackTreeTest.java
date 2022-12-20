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
    
    @Test
    public void testString() {
    	RedBlackTree<Integer> rbt = new RedBlackTree<>();
    	rbt.insert(1);
    	rbt.insert(2);
    	rbt.insert(3);
    	rbt.insert(-5);
    	System.out.println(rbt.toString());
    }
    
    @Test
    public void fromString() {
    	RedBlackTree<Integer> rbt;
    	String description = "2;black;1;3\n"
    			+ "1;black;-5;null\n"
    			+ "-5;red;null;null\n"
    			+ "3;black;null;null";
    	rbt = RedBlackTree.fromString(description);
    	System.out.println("Printing generated rbt");
    	System.out.println(rbt.toString());
    }
}