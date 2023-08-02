package DynamicTreeStructures.structure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import org.junit.Test;

public class RedBlackTreeTest {

	public static String TestTreesFolder = "./test/DynamicTreeStructures/structure/TestTrees/";
	
    @Test
    public void testBasic() {
        RedBlackTree<Integer> rbt = new RedBlackTree<>();
        rbt.insert(5);
        assertEquals(rbt.getRoot().getData(), (Integer) 5);
    }
    
    @Test
    public void testDelete() {
        RedBlackTree<Integer> rbt = new RedBlackTree<>();
        rbt.deleteMin();
        rbt.insert(17);
        rbt.insert(-1200000000);
        rbt.insert(1700000000);
        rbt.insert(690000000);
        rbt.insert(-177000000);
        rbt.insert(2691165);
        
    }
    
    @Test
    public void root_1_r() {
    	String inputPath = TestTreesFolder + "input_root_1_r.tikz";
    	String outputPath = TestTreesFolder + "output_root_1_r.tikz";
    	testRBT(inputPath, outputPath, (rbt) -> {
    		rbt.insert(2);
    	});
    }
    
    @Test
    public void root_1_l() {
    	String inputPath = TestTreesFolder + "input_root_1_l.tikz";
    	String outputPath = TestTreesFolder + "output_root_1_l.tikz";
    	testRBT(inputPath, outputPath, (rbt) -> {
    		rbt.insert(0);
    	});
    }
    
    @Test
    public void root_2_r() {
    	String inputPath = TestTreesFolder + "input_root_2_r.tikz";
    	String outputPath = TestTreesFolder + "output_root_2_r.tikz";
    	testRBT(inputPath, outputPath, (rbt) -> {
    		rbt.insert(3);
    	});
    }
    
    @Test
    public void root_2_l() {
    	String inputPath = TestTreesFolder + "input_root_2_l.tikz";
    	String outputPath = TestTreesFolder + "output_root_2_l.tikz";
    	testRBT(inputPath, outputPath, (rbt) -> {
    		rbt.insert(1);
    	});
    }
    
    @Test
    public void root_2_c() {
    	String inputPath = TestTreesFolder + "input_root_2_c.tikz";
    	String outputPath = TestTreesFolder + "output_root_2_c.tikz";
    	testRBT(inputPath, outputPath, (rbt) -> {
    		rbt.insert(2);
    	});
    }
    
    
    public static void testRBT(String inputTest, String outputTest, 
    		Consumer<RedBlackTree<Integer>> ops) {
    	String inputPath = inputTest;
    	String outputPath = outputTest;
    	String inputRBT = getRBTStringFromTikZit(inputPath);
    	String expectedRBT = getRBTStringFromTikZit(outputPath);
    	RedBlackTree<Integer> rbt = RedBlackTree.fromString(inputRBT);
    	ops.accept(rbt);
    	String actualRBT = rbt.toString();
    	assertEquals(expectedRBT, actualRBT);
    	System.out.println("Passed test!");
    }
    
    public static String getRBTStringFromTikZit(String inputFile) {
    	Path inputPath = Path.of(inputFile);
    	try {
    		String inputTikZ = Files.readString(inputPath);
    		String inputRBT = TikZitParser.parseTikZit(inputTikZ);
    		RedBlackTree<Integer> rbt = RedBlackTree.fromString(inputRBT);
    		return rbt.toString();
    	} catch (IOException e) {
    		fail("Couldn't find tests at location " + inputPath.toAbsolutePath());
    		return null;
    	}
    }
}