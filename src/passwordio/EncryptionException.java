package passwordio;

/**
 * This class is an exception that can be thrown by EncryptedBuffer when an error occurs during
 * encryption.
 */
public class EncryptionException extends Exception {

  private static final long serialVersionUID = 6482502283009283419L;

  /**
   * Constructs an instance of EcryptionException.
   * 
   * @param errorMessage is an error message string to raise with this exception.
   */
  public EncryptionException(String errorMessage) {
    super(errorMessage);
  }

}