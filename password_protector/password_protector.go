package password_protector

import (
    "crypto/cipher"
    "crypto/rand"
    "errors"
    //"fmt"
    "encoding/json"
    "golang.org/x/crypto/twofish"
    "golang.org/x/crypto/argon2"
    "io/ioutil"
)

func EncryptData(plainText []byte, key []byte) ([]byte, error) {
    salt := make([]byte, 8)
    _, err := rand.Read(salt)
    if err != nil {
        return []byte{}, err
    }
    cipherTextKey := argon2.IDKey(key, salt, 1, 64 * 1024, 4, 32)
    block, err := twofish.NewCipher(cipherTextKey)
    if err != nil {
        return []byte{}, err
    }
    iv := make([]byte, twofish.BlockSize)
    _, err = rand.Read(iv)
    if err != nil {
        return []byte{}, err
    }
    resizedPlainText := append(
        plainText,
        make(
            []byte,
            twofish.BlockSize - len(plainText) % twofish.BlockSize,
        )...
    )
    cipherText := make([]byte, len(resizedPlainText))
    mode := cipher.NewCBCEncrypter(block, iv)
    mode.CryptBlocks(cipherText, resizedPlainText)
    return append(append(salt, iv...), cipherText...), nil
}

func DecryptData(cipherText []byte, key []byte) ([]byte, error) {
    if len(cipherText) <= 8 + twofish.BlockSize {
        return []byte{}, errors.New("Invalid Slice: Too Small to Decrypt")
    }
    if (len(cipherText) - 8) % twofish.BlockSize != 0 {
        return []byte{}, errors.New(
            "Slice size is not a multiple of Twofish Blocksize",
        )
    }
    key = argon2.IDKey(key, cipherText[0:8], 1, 64 * 1024, 4, 32)
    block, err := twofish.NewCipher(key)
    if err != nil {
        return []byte{}, err
    }
    iv := cipherText[8:8 + twofish.BlockSize]
    plainText := make([]byte, len(cipherText[8 + twofish.BlockSize:]))
    mode := cipher.NewCBCDecrypter(block, iv)
    mode.CryptBlocks(plainText, cipherText[8 + twofish.BlockSize:])
    var finalIndex int
    for finalIndex = len(plainText) - 1; finalIndex >= 0; finalIndex-- {
        if plainText[finalIndex] != '\x00' {
            break
        }
    }
    return plainText[:finalIndex + 1], nil
}

func WriteEncryptedFile(filename string, data []byte, plainTextKey []byte) error {
    cipherText, err := EncryptData(data, plainTextKey)
    if err != nil {
        return err
    }
    if ioutil.WriteFile(filename, cipherText, 0666) != nil {
        return errors.New("Error Occurred Writing to File")
    }
    return nil
}

func OpenEncryptedFile(filename string, plainTextKey []byte) ([]byte, error) {
    encryptedData, err := ioutil.ReadFile(filename)
    if err != nil {
        return []byte{}, err
    }
    return DecryptData(encryptedData, plainTextKey)
}

func JSONToRecord(jsonText []byte) (map[string][]byte, error) {
    var records map[string][]byte
    err := json.Unmarshal(jsonText, &records)
    if err != nil {
        return records, err
    }
    return records, nil
}

func RecordToJSON(record map[string][]byte) ([]byte, error) {
    jsonText, err := json.Marshal(record)
    if err != nil {
        return []byte{}, err
    }
    return jsonText, nil
}

func EncryptRecord(record map[string][]byte, key []byte) ([]byte, error) {
    jsonText, err := RecordToJSON(record)
    if err != nil {
        return jsonText, err
    }
    cypherText, err := EncryptData(jsonText, key)
    if err != nil {
        return cypherText, err
    }
    return cypherText, nil
}

func DecryptRecord(rawText []byte, key []byte) (map[string][]byte, error) {
    plainTextRecord, err := DecryptData(rawText, key)
    if err != nil {
        return map[string][]byte{}, err
    }
    record, err := JSONToRecord(plainTextRecord)
    if err != nil {
        return record, err
    }
    return record, nil
}
