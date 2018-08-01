package password_protector

import (
  "crypto/cipher"
  "crypto/rand"
  "errors"
  "strings"
  //"fmt"
  "encoding/json"
  "golang.org/x/crypto/twofish"
  "golang.org/x/crypto/argon2"
  "io/ioutil"
  //"os"
)

type AccountRecorder interface {
  ReadPassword(plainTextKey string) (string, error)
  StorePassword(passwordData string, plainTextKey string) error
  ReadRecoveryCodes(plainTextKey string) (string, error)
  StoreRecoveryCodes(recoveryCodes string, plainTextKey string) error
  ReadPin(plainTextKey string) (string, error)
  StorePin(pin string, plainTextKey string) error
  ReadAccountType() string
  StoreAccountType(accountType string)
  ReadUserName() (string, error)
  StoreUserName(username string)
  //String()
}

type AccountRecord struct {
  AccountType string
  AccountAttributes map[string]string
}

func encryptData(plainText string, key string) ([]byte, error) {
  salt := make([]byte, 8)
  _, err := rand.Read(salt)
  if err != nil {
    return []byte{}, err
  }
  cipherTextKey := argon2.IDKey([]byte(key), salt, 1, 64 * 1024, 4, 32)
  block, err := twofish.NewCipher(cipherTextKey)
  if err != nil {
    return []byte{}, err
  }
  iv := make([]byte, twofish.BlockSize)
  _, err = rand.Read(iv)
  if err != nil {
    return []byte{}, err
  }
  resizedPlainText := append([]byte(plainText), make([]byte, twofish.BlockSize - len([]byte(plainText)) % twofish.BlockSize)...)
  cipherText := make([]byte, len(resizedPlainText))
  mode := cipher.NewCBCEncrypter(block, iv)
  mode.CryptBlocks(cipherText, resizedPlainText)
  return append(append(salt, iv...), cipherText...), nil
}

func decryptData(cipherText []byte, plainTextKey string) (string, error) {
  if len(cipherText) <= 8 {
    return "", errors.New("Invalid Slice: Too Small to Decrypt")
  }
  key := argon2.IDKey([]byte(plainTextKey), cipherText[0:8], 1, 64 * 1024, 4, 32)
  block, err := twofish.NewCipher(key)
  if err != nil {
    return "", err
  }
  iv := cipherText[8:8+twofish.BlockSize]
  if len(cipherText[8+twofish.BlockSize:]) % twofish.BlockSize != 0 {
    return "", errors.New("Slice size is not a multiple of Twofish Blocksize")
  }
  plainText := make([]byte, len(cipherText[8+twofish.BlockSize:]))
  mode := cipher.NewCBCDecrypter(block, iv)
  mode.CryptBlocks(plainText, cipherText[8+twofish.BlockSize:])
  var finalIndex int
  for finalIndex = len(plainText) - 1; finalIndex >= 0; finalIndex-- {
    if plainText[finalIndex] != '\x00' {
      break
    }
  }
  return strings.TrimSuffix(string(plainText), string(make([]byte, len(plainText) - finalIndex))), nil
}


func WriteEncryptedFile(filename string, data string, plainTextKey string) error {
  cipherText, err := encryptData(data, plainTextKey)
  if err != nil {
    return err
  }
  if ioutil.WriteFile(filename, cipherText, 0666) != nil {
    return errors.New("Error Occurred Writing to File")
  }
  return nil
}

func OpenEncryptedFile(filename string, plainTextKey string) (string, error) {
  encryptedData, err := ioutil.ReadFile(filename)
  if err != nil {
    return "", err
  }
  return decryptData(encryptedData, plainTextKey)
}


func JSONtoAccountRecordList(jsonText string) ([]AccountRecord, error) {
  var recordList []AccountRecord
  err := json.Unmarshal([]byte(jsonText), &recordList)
  if err != nil {
    return recordList, err
  }
  return recordList, nil
}

func AccountRecordListToJSONString(recordList []AccountRecord) (string, error) {
  jsonText, err := json.Marshal(recordList)
  if err != nil {
    return "", err
  }
  return string(jsonText), nil
}

func (ar *AccountRecord) StorePassword(passwordData string, plainTextKey string) error {
  cipherTextPassword, err := encryptData(passwordData, plainTextKey)
  if err != nil {
    return err
  }
  ar.AccountAttributes["password"] = string(cipherTextPassword)
  return nil
}

func (ar *AccountRecord) ReadPassword(plainTextKey string) (string, error) {
  if val, ok := ar.AccountAttributes["password"]; ok {
    return decryptData([]byte(val), plainTextKey)
  } else {
    return "", errors.New("No password for this account record")
  }
}

func (ar *AccountRecord) StoreAccountType(accountType string) {
  ar.AccountType = accountType
}

func (ar *AccountRecord) ReadAccountType() string {
  return ar.AccountType
}

func (ar *AccountRecord) StoreUserName(username string) {
  ar.AccountAttributes["username"] = username
}

func (ar *AccountRecord) ReadUserName() (string, error) {
  if val, ok := ar.AccountAttributes["username"]; ok {
    return val, nil
  } else {
    return "", errors.New("No username exists for this account record")
  }
}

func (ar *AccountRecord) StoreRecoveryCodes(recoveryCodes string, plainTextKey string) error {
  cipherTextRecoveryCodes, err := encryptData(recoveryCodes, plainTextKey)
  if err != nil {
    return err
  }
  ar.AccountAttributes["recoveryCodes"] = string(cipherTextRecoveryCodes)
  return nil
}

func (ar *AccountRecord) ReadRecoveryCodes(plainTextKey string) (string, error) {
  if val, ok := ar.AccountAttributes["recoveryCode"]; ok {
    return decryptData([]byte(val), plainTextKey)
  } else {
    return "", errors.New("No recovery codes exist for this account record")
  }
}

func (ar *AccountRecord) StorePin(pin string, plainTextKey string) error {
  cipherTextPin, err := encryptData(pin, plainTextKey)
  if err != nil {
    return err
  }
  ar.AccountAttributes["pin"] = string(cipherTextPin)
  return nil
}

func (ar *AccountRecord) ReadPin(plainTextKey string) (string, error) {
  if val, ok := ar.AccountAttributes["pin"]; ok {
    return decryptData([]byte(val), plainTextKey)
  } else {
    return "", errors.New("No pin exists for this account record")
  }
}

func (ar *AccountRecord) StoreEmail(email string) error {
  ar.AccountAttributes["email"] = email
  return nil
}

func (ar *AccountRecord) ReadEmail() (string, error) {
  if val, ok := ar.AccountAttributes["email"]; ok {
    return val, nil
  } else {
    return "", errors.New("No email exists for this account record")
  }
}

func (ar *AccountRecord) StorePhoneNumber(phoneNumber string) error {
  ar.AccountAttributes["phoneNumber"] = phoneNumber
  return nil
}

func (ar *AccountRecord) ReadPhoneNumber() (string, error) {
  if val, ok := ar.AccountAttributes["phoneNumber"]; ok {
    return val, nil
  } else {
    return "", errors.New("No phone number exists for this account record")
  }
}
