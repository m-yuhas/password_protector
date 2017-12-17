/**
 * The passwordprotectorcli package can be bundled as a jar to run the passwordprotector application on any system with JRE1.8 or higher installed.
 * 
 * @author	Michael Yuhas
 * @since	0.1
 * @version	0.1
 */
package passwordprotectorcli;

/**
 * The CouldNotInstantiateConsoleException class is an exception that can be thrown if a console object could not be instantiated in the current runtime environment.
 * 
 * @author 	Michael Yuhas
 * @since	0.1
 * @version	0.1
 */
public class CouldNotInstantiateConsoleException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 * The CouldNotInstantiateConsoleException constructor creates a CouldNotInstantiateConsoleException object.
	 * 
	 * @author	Michael Yuhas
	 * @since	0.1
	 * @version	0.1
	 */
	public CouldNotInstantiateConsoleException(String message) {
		super(message);
	}
}
