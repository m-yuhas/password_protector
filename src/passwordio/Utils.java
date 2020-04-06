package passwordio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
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

public final class Utils {
  
  private static final int keyGenerationIterations = 65536;
  private static final int keyLength = 16;
  private static final int ivLength = 8;
  
  private Utils() {
    throw new UnsupportedOperationException();
  }
  
  public static boolean validatePassword(byte[] encrypted, char[] ... passwords) throws DecryptionException {
    if (encrypted.length < Utils.keyLength + Utils.ivLength) {
      throw new DecryptionException("Unable to decrypt: ciphertext may be corrupted.");
    }
    try {
      byte[] salt = Arrays.copyOfRange(encrypted, 0, Utils.keyLength);
      byte[] iv = Arrays.copyOfRange(encrypted, Utils.keyLength, Utils.keyLength + Utils.ivLength);
      byte[] cryptoText = Arrays.copyOfRange(encrypted, Utils.keyLength + Utils.ivLength, encrypted.length);
      ArrayList<Character> passwordCharacters = new ArrayList<Character>();
      for (char[] p: passwords) {
        for (char c: p) {
          passwordCharacters.add(c);
        }
      }
      char[] password = new char[passwordCharacters.size()];
      for (int i = 0; i < passwordCharacters.size(); i++) {
        password[i] = passwordCharacters.get(i);
      }
      SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      KeySpec keySpec = new PBEKeySpec(password, salt, Utils.keyGenerationIterations, Utils.keyLength * 8);
      SecretKey secretKey = new SecretKeySpec(secretKeyFactory.generateSecret(keySpec).getEncoded(), "Blowfish");
      Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
      cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
      cipher.doFinal(cryptoText);
      return true;
    } catch (NoSuchPaddingException
        | NoSuchAlgorithmException
        | IllegalBlockSizeException
        | InvalidAlgorithmParameterException
        | InvalidKeySpecException
        | InvalidKeyException
        | BadPaddingException e) {
      return false;
    }
  }

  public static byte[] decrypt(byte[] encrypted, char[] ... passwords) throws DecryptionException {
    if (encrypted.length < Utils.keyLength + Utils.ivLength) {
      throw new DecryptionException("Unable to decrypt: ciphertext may be corrumpted.");
    }
    try {
      byte[] salt = Arrays.copyOfRange(encrypted, 0, Utils.keyLength);
      byte[] iv = Arrays.copyOfRange(encrypted, Utils.keyLength, Utils.keyLength + Utils.ivLength);
      byte[] cryptoText = Arrays.copyOfRange(encrypted, Utils.keyLength + Utils.ivLength, encrypted.length);
      ArrayList<Character> passwordCharacters = new ArrayList<Character>();
      for (char[] p: passwords) {
        for (char c: p) {
          passwordCharacters.add(c);
        }
      }
      char[] password = new char[passwordCharacters.size()];
      for (int i = 0; i < passwordCharacters.size(); i++) {
        password[i] = passwordCharacters.get(i);
      }
      SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      KeySpec keySpec = new PBEKeySpec(password, salt, Utils.keyGenerationIterations, Utils.keyLength * 8);
      SecretKey secretKey = new SecretKeySpec(secretKeyFactory.generateSecret(keySpec).getEncoded(), "Blowfish");
      Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
      cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
      return cipher.doFinal(cryptoText);
    } catch (NoSuchPaddingException
        | NoSuchAlgorithmException
        | IllegalBlockSizeException
        | InvalidAlgorithmParameterException
        | InvalidKeySpecException e) {
      throw new DecryptionException("Twofish alogrithm not supported on this JRE.");
    } catch (InvalidKeyException | BadPaddingException e) {
      throw new DecryptionException("Unable to decrypt: ciphertext may be corrupted.");
    }
  }
  
  public static byte[] encrypt(byte[] unencrypted, char[] ... passwords) throws EncryptionException {
    try {
      SecureRandom rn = new SecureRandom();
      byte[] salt = new byte[Utils.keyLength];
      byte[] iv = new byte[Utils.ivLength];
      rn.nextBytes(salt);
      rn.nextBytes(iv);
      ArrayList<Character> passwordCharacters = new ArrayList<Character>();
      for (char[] p: passwords) {
        for (char c: p) {
          passwordCharacters.add(c);
        }
      }
      char[] password = new char[passwordCharacters.size()];
      for (int i = 0; i < passwordCharacters.size(); i++) {
        password[i] = passwordCharacters.get(i);
      }
      SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      KeySpec keySpec = new PBEKeySpec(password, salt, Utils.keyGenerationIterations, Utils.keyLength * 8);
      SecretKey secretKey = new SecretKeySpec(secretKeyFactory.generateSecret(keySpec).getEncoded(), "Blowfish");
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

}
