package passwordio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;

/**
 * This class provides methods to read an encrypted file into a buffer of bytes and decrypt it as a specific data type.
 * It also provides methods to encrypt a data type or structure into a buffer of bytes.  Encryption and decryption can
 * be achieved with a single password or require multiple passwords.
 *
 * @param <T> the data type of the unencrypted data represented by this class.
 */
public class EncryptedBuffer<T> {

  private byte[] encryptedBytes;

  /**
   * Constructs an instance of EncryptedBuffer with some unencrypted data and the passwords that should be used to
   * encrypt it.
   * 
   * @param unencryptedData is the data that will be encrypted.
   * @param passwords is an array of character arrays containing all the passwords that will be used to encrypt this
   *        data.
   * @throws EncryptionException if an error occurs while encrypting the data.
   */
  public EncryptedBuffer(T unencryptedData, char[] ... passwords) throws EncryptionException {
    this.updateContents(unencryptedData, passwords);
  }

  /**
   * Constructs an instance of EncryptedBuffer with some data that is already encrypted.  This constructor does not
   * decrypt the data.
   * 
   * @param encryptedData is a byte array of encrypted data.
   */
  public EncryptedBuffer(byte[] encryptedData) {
    this.encryptedBytes = encryptedData;
  }

  /**
   * Constructs an instance of EncryptedBuffer with some encrypted data read from a file.  This constructor does not
   * decrypt the data.
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
   */
  public boolean validatePassword(char[] ... passwords) {
    try {
      Utils.decrypt(this.encryptedBytes, passwords);
      return true;
    } catch (DecryptionException e) {
      return false;
    }
  }

  /**
   * Decrypt an EncryptedBuffer.
   * 
   * @param passwords is an array of character arrays, each of which is a password for this EncryptedBuffer.
   * @return the unencrypted data.
   * @throws DecryptionException if an error occurs during decryption.
   */
  @SuppressWarnings("unchecked")
  public T decrypt(char[] ... passwords) throws DecryptionException {
    try {
      ByteArrayInputStream bytes = new ByteArrayInputStream(Utils.decrypt(this.encryptedBytes, passwords));
      ObjectInputStream serialized = new ObjectInputStream(bytes);
      return (T) serialized.readObject();
    } catch (IOException e) {
      throw new DecryptionException("Error occurred converting bytes to object input stream.");
    } catch (ClassNotFoundException e) {
      throw new DecryptionException("Generic class T not found on this JRE.");
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
      this.encryptedBytes = Utils.encrypt(bytes.toByteArray(), passwords);
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

}
