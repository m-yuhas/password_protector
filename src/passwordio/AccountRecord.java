/**
 * 
 */
package passwordio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

/**
 * @author yuhasmj
 *
 */
public class AccountRecord implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3210360126304918081L;
  public String name;
  private byte[] encryptedText;

  public AccountRecord(String name, byte[] encryptedText) {
    this.name = name;
    this.encryptedText = encryptedText;
  }
  
  public AccountRecord(String name, Map<String, String> recordData, char[] password) throws EncryptionException {
    this.name = name;
    this.updateRecord(recordData, password);
  }
  
  @SuppressWarnings("unchecked")
  public Map<String, String> getRecord(char[] password) throws DecryptionException {
    ByteArrayInputStream bytes = null;
    bytes = new ByteArrayInputStream(Utils.decrypt(this.encryptedText, password));
    try {
      ObjectInputStream serialized = new ObjectInputStream(bytes);
      return (Map<String, String>) serialized.readObject();
    } catch (IOException e) {
      throw new DecryptionException("Error occurred converting bytes to object input stream.");
    } catch (ClassNotFoundException e) {
      throw new DecryptionException("Class Map<String, String> not found on this JRE.");
    }
  }
    
  public void updateRecord(Map<String, String> recordData, char[] password) throws EncryptionException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    try {
      ObjectOutputStream serialized = new ObjectOutputStream(bytes);
      serialized.writeObject(recordData);
    } catch (IOException e) {
      throw new EncryptionException("Error occurred writing record data to byte array.");
    }
    System.out.println("len: " + bytes.toByteArray().length);
    this.encryptedText = Utils.encrypt(bytes.toByteArray(), password);
  }
  
}
