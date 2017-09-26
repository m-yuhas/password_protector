package passwordprotector;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class PasswordGenerator {
	

	
	private Random randomNumberGenerator;
	private List<ValidCharacters> validCharacters;

	public PasswordGenerator(long seed, List<ValidCharacters> validChararcters) {
		this.randomNumberGenerator = new Random(seed);
		this.validCharacters = validCharacters;
	}
	
	public void reseed(long seed) {
		this.randomNumberGenerator = new Random(seed);
		return;
	}
	
	public void addValidCharacters(ValidCharacters v) {
		this.validCharacters.add(v);
		return;
	}
	
	public void removeValidCharacters(ValidCharacters v) {
		this.validCharacters.remove(v);
	}
	
	public String generatePassword(int length) {
		char[] validChoices;
		for ( ValidCharacters v : validCharacters) {
			
		}
		return "";
	}
}
