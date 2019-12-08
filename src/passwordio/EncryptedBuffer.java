package passwordio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;

public class EncryptedBuffer<T> {

  private byte[] encryptedBytes;
  
  public EncryptedBuffer(T unencryptedData, char[] password) throws EncryptionException {
    this.updateContents(unencryptedData, password);
  }

  public EncryptedBuffer(byte[] encryptedData) {
    this.encryptedBytes = encryptedData;
  }
  
  public EncryptedBuffer(File file) throws IOException {
    this.encryptedBytes = Files.readAllBytes(file.toPath());
  }
  
  public boolean validatePassword(char[] password) {
    try {
      Utils.decrypt(this.encryptedBytes, password);
      return true;
    } catch (DecryptionException e) {
      return false;
    }
  }
  
  @SuppressWarnings("unchecked")
  public T decrypt(char[] password) throws DecryptionException {
    try {
      ByteArrayInputStream bytes = new ByteArrayInputStream(Utils.decrypt(this.encryptedBytes, password));
      ObjectInputStream serialized = new ObjectInputStream(bytes);
      return (T) serialized.readObject();
    } catch (IOException e) {
      throw new DecryptionException("Error occurred converting bytes to object input stream.");
    } catch (ClassNotFoundException e) {
      throw new DecryptionException("Generic class T not found on this JRE.");
    }
  }
  
  public void updateContents(T unencryptedData, char[] password) throws EncryptionException {
    try {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      ObjectOutputStream serialized = new ObjectOutputStream(bytes);
      serialized.writeObject(unencryptedData);
      this.encryptedBytes = Utils.encrypt(bytes.toByteArray(), password);
    } catch (IOException e) {
      throw new EncryptionException("Error occurred converting bytes to object output stream.");
    } 
  }
  
  public void writeToFile(File file) throws IOException {
    FileOutputStream outputStream = new FileOutputStream(file);
    outputStream.write(this.encryptedBytes);
    outputStream.close();
  }

}
