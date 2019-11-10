package lSystem;

import java.util.HashMap;

public class BaseGrammar {
	HashMap<Character, String> rules;
	public BaseGrammar(HashMap<Character, String> rules) {
		this.rules = rules;
	}

	
	/***
	 * Applies the grammar to a string
	 * @param n number of times to apply
	 * @param initialString what the string begins as 
	 * @return the result of applying the grammar n times to the initialString
	 */
	public String apply(int n, String initialString) {
		String currentString = initialString;
		String nextString = "";
		for (int iteration = 0; iteration <= n; iteration++) {
			nextString = "";
			for (char letter : currentString.toCharArray()) {
				nextString += rules.get(letter);
			}
			currentString = nextString;
		}
		return currentString;
	}
}
