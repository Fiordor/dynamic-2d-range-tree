package DynamicTreeStructures.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tools to read a TikZit graph and
 * convert it to our program's representation.
 * @author MrMCMax
 */
public class TikZitParser {
	
	/**
	 * The name of the black node style in the TikZit format
	 */
	public static String blackNodeStyle = "BlackNode";
	/**
	 * The name of the red node style in the TikZit format
	 */
	public static String redNodeStyle = "RedNode";
	/**
	 * The name of the left-leaning edge style in the TikZit format
	 */
	public static String leftEdgeStyle = "Left";
	/**
	 * The name of the right-leaning edge style in the TikZit format
	 */
	public static String rightEdgeStyle = "Right";
	
	public static Pattern nodeLinePattern;
	public static Pattern edgeLinePattern;
	
	static {
		//Patterns
		//Keys to understand this horrible regex:
		// "\\s*" is 0 or more whitespace
		// "\\\\" parses a backslash
		// Non-escaped parentheses create a group, and later
		//  we can retrieve whatever is parsed inside the group
		// "\\w+" is 1 or more word characters: a-z, A-Z, or 0-9
		// "\\(" and "\\)" are literal parentheses (no grouping)
		// "[^,\\)]+" parses 1 or more of anything that is not a comma
		// or a literal closing parenthesis (I use it to avoid parsing
		// multiple number formats and just parse anything until the closing parenthesis)
		// and so on
		nodeLinePattern = Pattern.compile("\\s*\\\\node \\[style=(\\w+)\\] \\(([^\\)]+)\\) at \\([^,\\)]+, [^,\\)]+\\) \\{([^\\)]+)\\}\\s*;*");
		edgeLinePattern = Pattern.compile("\\s*\\\\draw \\[style=(\\w+)\\] \\(([^\\)]+)\\) to \\(([^\\)]+)\\)\\s*;*");
	}
	
	public static void main(String[] args) {
		//Test regexes
		Pattern p = nodeLinePattern;
		String text = "\t\t\\node [style=BlackNode] (0) at (-0.5, -24.75) {-1}";
		Matcher m = p.matcher(text);
		System.out.printf("Text: %s\n", text);
		System.out.printf("Pattern: %s\n", p.pattern());
		System.out.printf("Matches: %s\n", m.matches());
		System.out.printf("Group 1: %s\n",m.group(1));
		System.out.printf("Group 2: %s\n", m.group(2));
		System.out.printf("Group 3: %s\n", m.group(3));
		
		//Edges:
		text = "		\\draw [style=Right] (0) to (2);";
		Matcher edge = edgeLinePattern.matcher(text);
		boolean b = edge.matches();
		System.out.println("Text: " + text);
		System.out.println("Matches: " + b);
		if (b)
			System.out.printf("Style: %s, from: %s to: %s\n", edge.group(1), edge.group(2), edge.group(3));
		//Parentheses
		text = "(asdf1234-dñk?´´+*ç$^6%)a";
		Pattern parentheses = Pattern.compile("\\(([^\\)]*)\\)a");
		m = parentheses.matcher(text);
		b = m.matches();
		if (b)
			System.out.println(m.group(1));
	}
	
	/**
	 * Parses a TikZit graph by creating a RedBlackTree and printing its toString().
	 * @param s the string to parse
	 */
	public static String parseTikZit(String s) {
		Map<Integer, NodeRedBlackTree<Integer>> nodes = new HashMap<>();
		String[] lines = s.split("\\R+");
		Iterator<String> linesIt = Arrays.asList(lines).iterator();
		//Skip initial two lines
		nextLine(linesIt);
		nextLine(linesIt);
		//Now, we have to parse node lines
		String nodeLine = nextLine(linesIt);
		Matcher nodeMatcher = nodeLinePattern.matcher(nodeLine);
		while (nodeMatcher.matches()) {
			String type = nodeMatcher.group(1);
			int id = Integer.parseInt(nodeMatcher.group(2));
			int value = Integer.parseInt(nodeMatcher.group(3));
			NodeRedBlackTree<Integer> node = new NodeRedBlackTree<>(value);
			if (type.equals(blackNodeStyle)) {
				node.setRed(false);
			} else if (type.equals(redNodeStyle)) {
				node.setRed(true);
			} else {
				System.err.printf("Couldn't read node type %s\n", type);
				return null;
			}
			nodes.put(id, node);
			nodeLine = nextLine(linesIt);
			nodeMatcher.reset(nodeLine);
		}
		//We have all the nodes of the graph. Now, we gotta join them
		//Start of the edge layer
		String startEdgeLayer = nextLine(linesIt);
		if (startEdgeLayer.contains("edgelayer")) {
			String edgeLine = nextLine(linesIt);
			Matcher edgeMatcher = edgeLinePattern.matcher(edgeLine);
			while (edgeMatcher.matches()) {
				String style = edgeMatcher.group(1);
				int from = Integer.parseInt(edgeMatcher.group(2));
				int to = Integer.parseInt(edgeMatcher.group(3));
				NodeRedBlackTree<Integer> origin = nodes.get(from);
				if (style.equals(leftEdgeStyle)) {
					origin.setLeft(nodes.get(to));
				} else if (style.equals(rightEdgeStyle)) {
					origin.setRight(nodes.get(to));
				}
				edgeLine = nextLine(linesIt);
				edgeMatcher.reset(edgeLine);
			}
		}
		//We are done. Root is node 0
		RedBlackTree<Integer> rbt = new RedBlackTree<>(nodes.get(0));
		return(rbt.toString());
	}
	
	private static String nextLine(Iterator<String> it) {
		String line;
		do {
			line = it.next().strip();
		} while (line.equals("") && it.hasNext());
		return line;
	}
}
