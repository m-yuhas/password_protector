package passwordio;

/**
 * This class is an exception that can be thrown by EncryptedBuffer when an error occurs during decryption.
 */
public class DecryptionException extends Exception {

  private static final long serialVersionUID = 1285002778724566322L;

  /**
   * Constructs an instance of DecryptionException.
   * 
   * @param errorMessage is an error message string to raise with this exception.
   */
  public DecryptionException(String errorMessage) {
    super(errorMessage);
  }

}