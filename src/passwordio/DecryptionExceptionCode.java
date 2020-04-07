package passwordio;

/**
 * Error codes for different categories of DecryptionException.
 */
public enum DecryptionExceptionCode {
  IO_EXCEPTION,
  CORRUPTED_CIPHERTEXT,
  UNSUPPORTED_ENVIRONMENT,
  INCORRECT_PASSWORD,
  OTHER_EXCEPTION
}