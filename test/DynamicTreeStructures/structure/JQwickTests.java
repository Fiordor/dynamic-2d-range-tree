package DynamicTreeStructures.structure;

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

public class JQwickTests {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> values = Arrays.asList("string1", "string2", "string3");
		Arbitrary<String> strings = Arbitraries.of(values);
		Stream<String> streamOfStrings = strings.sampleStream().limit(100);
		streamOfStrings.forEach(s -> System.out.println(s));
	}

}
