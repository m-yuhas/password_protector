/**
 * The passwordprotectorcli package can be bundled as a jar to run the passwordprotector application on any system with JRE1.8 or higher installed.
 * 
 * @author	Michael Yuhas
 * @since	0.1
 * @version	0.1
 */
package passwordprotectorcli;

/**
 * The InvalidSelectionException class is an exception that can be thrown when the user provides an invalid selection.
 * 
 * @author 	Michael Yuhas
 * @since	0.1
 * @version	0.1
 */
public class InvalidSelectionException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * The InvalidSelectionException constructor creates an InvalidSelectionException object.
	 * 
	 * @author	Michael Yuhas
	 * @since	0.1
	 * @version	0.1
	 */
	public InvalidSelectionException(String message) {
		super(message);
	}
}
