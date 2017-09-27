/**
 * 
 */
package passwordprotector;


import javax.crypto.Cipher;
/**
 * @author user
 *
 */
public class PasswordProtector {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello World");
		PasswordGenerator pg = new PasswordGenerator();
		pg.addValidCharacters(ValidCharacters.LOWERCASE);
		pg.addValidCharacters(ValidCharacters.UPPERCASE);
		System.out.println(pg.generatePassword(10));
		pg.addValidCharacters(ValidCharacters.NUMBERS);
		System.out.println(pg.generatePassword(12));
	}
	
	private static void parseArgs(String[] args) {
		return;
	}

}
