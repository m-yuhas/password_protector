package passwordprotector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PasswordGenerator {
	private Random randomNumberGenerator;
	private ArrayList<ValidCharacters> validCharacters;

	public PasswordGenerator(long seed, ArrayList<ValidCharacters> validCharacters) {
		this.randomNumberGenerator = new Random(seed);
		this.validCharacters = validCharacters;
	}
	
	public PasswordGenerator() {
		this.randomNumberGenerator = new Random(System.currentTimeMillis());
		this.validCharacters = new ArrayList<ValidCharacters>();
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
		List<Character> validChoices = new ArrayList<Character>();
		for ( ValidCharacters v : validCharacters) {
			switch(v) {
				case LOWERCASE:
					validChoices.addAll(Arrays.asList('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'));
					break;
				case UPPERCASE:
					validChoices.addAll(Arrays.asList('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'));
					break;
				case NUMBERS:
					validChoices.addAll(Arrays.asList('1','2','3','4','5','6','7','8','9','0'));
					break;
				case SYMBOLS:
					validChoices.addAll(Arrays.asList('~','`','!','@','#','$','%','^','&','*','(',')','-','_','+','=','}','{','[',']','\"','\'','\\','|','/','?','>','<','.',','));
					break;
				default:
					break;
			}
		}
		String password = new String();
		for ( int i = 0; i < length; i++ ) {
			password += validChoices.get(this.randomNumberGenerator.nextInt(validChoices.size()));
		}
		return password;
	}
}
