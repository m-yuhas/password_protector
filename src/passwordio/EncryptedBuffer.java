package passwordio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class provides methods to read an encrypted file into a buffer of bytes and decrypt it as a
 * specific data type.  It also provides methods to encrypt a data type or structure into a buffer
 * of bytes.  Encryption and decryption can be achieved with a single password or require multiple
 * passwords.
 *
 * @param <T> the data type of the unencrypted data represented by this class.
 */
public class EncryptedBuffer<T> {

  /**
   * Encrypted bytes resulting from encryption or from creating a buffer with already encrypted
   * data.
   */
  private byte[] encryptedBytes;

  /**
   * Number of iterations to use when generating a key.  This is the number of times the user input
   * (salt and password) is hashed.  A higher number makes it harder to derive the correct key based
   * on a password guess.  A higher number also slows down key generation.  If this field is
   * changed, anything previously encrypted with this package will not be unencryptable with the new
   * version.
   */
  private final int keyGenerationIterations = 65536;

  /**
   * The default key length in bytes.  If this field is changed, anything previously encrypted with
   * this package will not be unencryptable with the new version.
   */
  private final int keyLength = 16;

  /**
   * The length of the initialization vector in bytes.  If this field is changed, anything
   * previously encrypted with this package will not be unencryptable with the new version.
   */
  private final int ivLength = 8;

  /**
   * Constant number of bits per byte.  Used when a function requires a number of bits as input, but
   * the value is stored internally as in bytes.
   */
  private final int bitsPerByte = 8;

  /**
   * Constructs an instance of EncryptedBuffer with some unencrypted data and the passwords that
   * should be used to encrypt it.
   * 
   * @param unencryptedData is the data that will be encrypted.
   * @param passwords is an array of character arrays containing all the passwords that will be used
   *        to encrypt this data.
   * @throws EncryptionException if an error occurs while encrypting the data.
   */
  public EncryptedBuffer(T unencryptedData, char[] ... passwords) throws EncryptionException {
    this.updateContents(unencryptedData, passwords);
  }

  /**
   * Constructs an instance of EncryptedBuffer with some data that is already encrypted.  This
   * constructor does not decrypt the data.
   * 
   * @param encryptedData is a byte array of encrypted data.
   */
  public EncryptedBuffer(byte[] encryptedData) {
    this.encryptedBytes = encryptedData;
  }

  /**
   * Constructs an instance of EncryptedBuffer with some encrypted data read from a file.  This
   * constructor does not decrypt the data.
   * 
   * @param file is a File that will be read in a encrypted data.
   * @throws IOException if an error occurs while reading the file.
   */
  public EncryptedBuffer(File file) throws IOException {
    this.encryptedBytes = Files.readAllBytes(file.toPath());
  }

  /**
   * Validates a password or passwords for this EncryptedBuffer.
   * 
   * @param passwords is an array of character arrays to validate.
   * @return boolean true if the password(s) is correct and false otherwise.
   * @throws DecryptionException if the ciphertext is corrupted.
   */
  public boolean validatePassword(char[] ... passwords) throws DecryptionException {
    try {
      this.decryptInternalBytes(passwords);
      return true;
    } catch (DecryptionException e) {
      if (e.errorCode == DecryptionExceptionCode.INCORRECT_PASSWORD) {
        return false;
      } else {
        throw e;
      }
    }
  }

  /**
   * Decrypt an EncryptedBuffer.
   * 
   * @param passwords is an array of character arrays, each of which is a password for this
   *        EncryptedBuffer.
   * @return the unencrypted data.
   * @throws DecryptionException if an error occurs during decryption.
   * @throws IOException if an IO error occurs while reading from the buffer of bytes.
   * @throws ClassNotFoundException if the generic type is not found.
   */
  @SuppressWarnings("unchecked")
  public T decrypt(char[] ... passwords) throws DecryptionException, ClassNotFoundException {
    try {
      ByteArrayInputStream bytes = new ByteArrayInputStream(this.decryptInternalBytes(passwords));
      return (T) new ObjectInputStream(bytes).readObject();
    } catch (IOException e) {
      throw new DecryptionException(
          "IOException while reading from bytes buffer",
          DecryptionExceptionCode.IO_EXCEPTION);
    }
  }

  /**
   * Update the contents of this EncryptedBuffer.  Take some unencrypted data, encrypt it, and use it to replace the
   * existing buffer contents.  The provided passwords are not validated, so this method can also be used to change the
   * passwords for an existing EncryptedBuffer by providing the same data and different passwords.
   * 
   * @param unencryptedData is the new data to use to overwrite the existing buffer data.
   * @param passwords is an array of character arrays, each of which is a password required for encryption
   * @throws EncryptionException if an error occurs during encryption.
   */
  public void updateContents(T unencryptedData, char[] ... passwords) throws EncryptionException {
    try {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      ObjectOutputStream serialized = new ObjectOutputStream(bytes);
      serialized.writeObject(unencryptedData);
      this.encryptedBytes = this.encrypt(bytes.toByteArray(), passwords);
    } catch (IOException e) {
      throw new EncryptionException("Error occurred converting bytes to object output stream.");
    } 
  }

  /**
   * Write the EncryptedBuffer to a file.
   * 
   * @param file is a File to which the contents of the EncryptedBuffer will be written.
   * @throws IOException if an error occurs while writing the file.
   */
  public void writeToFile(File file) throws IOException {
    FileOutputStream outputStream = new FileOutputStream(file);
    outputStream.write(this.encryptedBytes);
    outputStream.close();
  }

  /**
   * Decrypt the encrypted bytes of this instance of EncryptedBuffer.
   * 
   * @param passwords is an array of character arrays containing the passwords for decryption.
   * @return a byte array of decrypted data.
   * @throws DecryptionException if an error occurs during decryption.
   */
  private byte[] decryptInternalBytes(char[] ... passwords) throws DecryptionException {
    if (this.encryptedBytes.length < this.keyLength + this.ivLength) {
      throw new DecryptionException(
          "Unable to decrypt: ciphertext may be corrumpted.",
          DecryptionExceptionCode.CORRUPTED_CIPHERTEXT);
    }
    try {
      byte[] salt = Arrays.copyOfRange(this.encryptedBytes, 0, this.keyLength);
      byte[] iv = Arrays.copyOfRange(
          this.encryptedBytes,
          this.keyLength,
          this.keyLength + this.ivLength);
      byte[] cryptoText = Arrays.copyOfRange(
          this.encryptedBytes,
          this.keyLength + this.ivLength,
          this.encryptedBytes.length);
      SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      KeySpec keySpec = new PBEKeySpec(
          this.mergePasswords(passwords),
          salt,
          this.keyGenerationIterations,
          this.keyLength * this.bitsPerByte);
      byte[] encoded = secretKeyFactory.generateSecret(keySpec).getEncoded();
      SecretKey secretKey = new SecretKeySpec(encoded, "Blowfish");
      Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
      cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
      return cipher.doFinal(cryptoText);
    } catch (NoSuchPaddingException
        | NoSuchAlgorithmException
        | IllegalBlockSizeException
        | InvalidAlgorithmParameterException
        | InvalidKeySpecException e) {
      throw new DecryptionException(
          "Twofish alogrithm not supported on this JRE.",
          DecryptionExceptionCode.UNSUPPORTED_ENVIRONMENT);
    } catch (InvalidKeyException | BadPaddingException e) {
      throw new DecryptionException(
          "Unable to decrypt: ciphertext may be corrupted.",
          DecryptionExceptionCode.INCORRECT_PASSWORD);
    }
  }

  /**
   * Encrypt a byte array with a set of passwords as the keys.
   * 
   * @param unencrypted is a byte array of unencrypted data.
   * @param passwords is an array of character arrays containing the passwords used for encryption.
   * @return a byte array of encrypted data
   * @throws EncryptionException if an error occurs during encryption.
   */
  private byte[] encrypt(byte[] unencrypted, char[] ... passwords) throws EncryptionException {
    try {
      SecureRandom secureRandom = new SecureRandom();
      byte[] salt = new byte[this.keyLength];
      byte[] iv = new byte[this.ivLength];
      secureRandom.nextBytes(salt);
      secureRandom.nextBytes(iv);
      SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      KeySpec keySpec = new PBEKeySpec(
          this.mergePasswords(passwords),
          salt,
          this.keyGenerationIterations,
          this.keyLength * this.bitsPerByte);
      SecretKey secretKey = new SecretKeySpec(
          secretKeyFactory.generateSecret(keySpec).getEncoded(),
          "Blowfish");
      Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
      byte[] cipherText = cipher.doFinal(unencrypted);
      ByteArrayOutputStream outputByteStream = new ByteArrayOutputStream();
      outputByteStream.write(salt);
      outputByteStream.write(iv);
      outputByteStream.write(cipherText);
      return outputByteStream.toByteArray();
    } catch (NoSuchPaddingException
        | NoSuchAlgorithmException
        | IllegalBlockSizeException
        | InvalidAlgorithmParameterException
        | InvalidKeySpecException e) {
      throw new EncryptionException("Twofish alogrithm not supported on this JRE.");
    } catch (InvalidKeyException | BadPaddingException | IOException e) {
      throw new EncryptionException("Unable to encrypt: check that the password is not null");
    }
  }

  /**
   * Merge all passwords into one character array so that they can be used by the encryption and
   * decryption utilities.  Currently the behavior is to simply concatenate all required passwords.
   * This could be changed to provide additional security, but if it changes, backwards
   * compatibility with previously encrypted text will break. The merged password should be unique
   * for each input.
   * 
   * @param passwords is an array of character arrays for each of the passwords entered by the
   *        users.
   * @return an array of characters for the merged Password.
   */
  private char[] mergePasswords(char[] ... passwords) {
    int mergedLength = 0;
    for (char[] password: passwords) {
      mergedLength = mergedLength + password.length;
    }
    char[] mergedPassword = new char[mergedLength];
    int index = 0;
    for (char[] password: passwords) {
      System.arraycopy(password, 0, mergedPassword, index, password.length);
      index = index + password.length;
    }
    return mergedPassword;
  }

}
