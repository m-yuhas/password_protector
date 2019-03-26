package password_protector

import (
    "bytes"
    "io/ioutil"
    "os"
    "reflect"
    "testing"
)

func TestEncryptDecrypt(t *testing.T) {
    inputData := []struct{
        plainText, key []byte
    }{
        {[]byte{'H', 'e', 'l', 'l', 'o'}, []byte{'W', 'o', 'r', 'l', 'd'}},
        {[]byte{}, []byte{'N', 'o', 'D', 'a', 't', 'a'}},
        {[]byte{'N', 'o', 'K', 'e', 'y'}, []byte{}},
        {[]byte{'S', 'p', 'a', 'c', 'e', 's'}, []byte{' ', ' ', ' '}},
        {[]byte{' ', ' ', ' '}, []byte{'S', 'p', 'a', 'c', 'e', 's'}},
        {[]byte{'T', 'a', 'b', 's'}, []byte{'\t', '\t', '\t'}},
        {[]byte{'\t', '\t', '\t'}, []byte{'T', 'a', 'b', 's'}},
        {[]byte{'\\', 'n'}, []byte{'\n'}},
        {[]byte{'\n'}, []byte{'\\', 'n'}},
        {[]byte{'\\', 'r'}, []byte{'\r'}},
        {[]byte{'\r'}, []byte{'\\', 'r'}},
        {[]byte{'N', 'u', 'l', 'l'}, []byte{'\x00', 'n', 'u', 'l', 'l'}},
        {[]byte{'\x00', 'n', 'u', 'l', 'l'}, []byte{'N', 'u', 'l', 'l'}},
        {[]byte{'C', 't', 'r', 'l', '+', 'C'}, []byte{'\x03'}},
        {[]byte{'\x03'}, []byte{'C', 't', 'r', 'l', '+', 'C'}},
        {[]byte{'\xff'}, []byte{'x', 'F', 'F'}},
        {[]byte{'x', 'F', 'F'}, []byte{'\xff'}},
    }
    for _, input := range inputData {
        encData, err := EncryptData(input.plainText, input.key)
        if err != nil {
            t.Errorf(
                "Received %s, while encrypting plainText: %s and key: %s.",
                err,
                input.plainText,
            )
        }
        if bytes.Equal(encData, input.plainText) {
            t.Errorf(
                "Encrypted data is the same as the original text: %s.",
                input.plainText,
            )
        }
        decData, err := DecryptData(encData, input.key)
        if err != nil {
            t.Errorf(
                "Received %s, while decrypting plainText: %s and key: %s.",
                err,
                input.plainText,
            )
        }
        if !bytes.Equal(decData, input.plainText) {
            t.Errorf(
                "Decrypted data: %s does not match original text: %s.",
                decData,
                input.plainText,
            )
        }
    }
}

func TestDecryptErrors(t *testing.T) {
    inputData := []struct{
        cipherText, key []byte
    }{
        {[]byte{'x'}, []byte{'T', 'o', 'o', 'S', 'm', 'a', 'l', 'l'}},
        {[]byte{'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x'}, []byte{'N'}},
    }
    for _ , input := range inputData {
        _, err := DecryptData(input.cipherText, input.key)
        if err == nil {
            t.Errorf(
                "Excpected error for cipher text string: %s.",
                input.cipherText,
            )
        }
    }
}

func TestReadWriteEncryptedFile(t *testing.T) {
    inputData := []struct{
        fileName string
        data, key []byte
    }{
        {
            "foo",
            []byte{'H', 'e', 'l', 'l', 'o', '!'},
            []byte{'W', 'o', 'r', 'l', 'd'},
        },
        {
            "bar",
            []byte{'\n', '\r', '\t'},
            []byte{'\t', '\r', '\n'},
        },
        {
            "国际吗文件名称",
            []byte{'U', 'n', 'i'},
            []byte{'c', 'o', 'd', 'e'},
        },
        {
            "Empty Data",
            []byte{},
            []byte{'N', 'o', 'D', 'a', 't', 'a'},
        },
        {
            "No Password",
            []byte{'N', 'o', 'p', 'a', 's', 's', 'w', 'o', 'r', 'd'},
            []byte{},
        },
        {
            "EOF Character in Data",
            []byte{'E', 'n', 'c', '\x00', 'E', 'n', 'c'},
            []byte{'k', 'e', 'y'},
        },
        {
            "EOF Character in Key",
            []byte{'D', 'a', 't', 'a'},
            []byte{'K', '\x00', 'e', 'y'},
        },
        {
            "xFF",
            []byte{'\xff'},
            []byte{'\xff'},
        },
    }
    for _, input := range inputData {
        err := WriteEncryptedFile(input.fileName, input.data, input.key)
        if err != nil {
            t.Errorf(
                "Error occurred encrypting fileName %s, error: %s.",
                input.fileName,
                err,
            )
        }
        decData, err := OpenEncryptedFile(input.fileName, input.key)
        if err != nil {
            t.Errorf(
                "Error occurred decrypting fileName %s, error: %s.",
                input.fileName,
                err,
            )
        }
        if !bytes.Equal(input.data, decData) {
            t.Errorf(
                "Decrypted data %s, does not match original data %s for %s.",
                decData,
                input.data,
                input.fileName,
            )
        }
        err = os.Remove(input.fileName)
        if err != nil {
            t.Errorf("File %s was never created.", input.fileName)
        }
    }
}

func TestOpenFileErrors(t *testing.T) {
    inputData := []struct {
        fileName string
        fileData, key []byte
    }{
        {
            "Empty File",
            []byte{},
            []byte{'k', 'e', 'y'},
        },
        {
            "File Content Invalid",
            []byte{'x', 'x', 'x'},
            []byte{'k', 'e', 'y'},
        },
    }
    for _, input := range inputData {
        ioutil.WriteFile(input.fileName, input.fileData, 0666)
        _, err := OpenEncryptedFile(input.fileName, input.key)
        if err == nil {
            t.Errorf("File %s did not throw an error.", input.fileName)
        }
        err = os.Remove(input.fileName)
        if err != nil {
            t.Errorf("File %s was never created.", input.fileName)
        }
    }
}

func TestSerialization(t *testing.T) {
    inputData := []map[string][]byte {
        {
            "foo": []byte{'b', 'a', 'r'},
        },
        {
            "New\nLine": []byte{'i', 'n', ' ', 'k', 'e' ,'y'},
        },
        {
            "Carriage\r": []byte{'r', 'e', 't', 'u', 'r', 'n'},
            "EOF\x00in": []byte{'k', 'e', 'y'},
            "EOF in": []byte{'\x00', 'v', 'a', 'l', 'u', 'e'},
            "\x03": []byte{'\x03'},
        },
        {
            "Empty Value": []byte{},
            "": []byte{'e', 'm', 'p', 't', 'y', ' ', 'k', 'e', 'y'},
        },
        {
            "国际吗键": []byte{'z', 'h'},
            "유니코드키": []byte{'k', 'o'},
        },
    }
    for _, input := range inputData {
        JSON, err := RecordToJSON(input)
        if err != nil {
            t.Errorf("Error occurred for record %s.", input)
        }
        output, err := JSONToRecord(JSON)
        if !reflect.DeepEqual(input, output) {
            t.Errorf(
                "Error, input record %s, does not match output %s.",
                input,
                output,
            )
        }
    }
}

func TestEncryptDecryptRecord(t *testing.T) {
    inputData := []struct {
        record map[string][]byte
        key []byte
    }{
        {
            map[string][]byte{
                "foo": []byte{'b', 'a', 'r'},
            },
            []byte{'k', 'e', 'y'},
        },
        {
            map[string][]byte{
                "New\nLine": []byte{'\n', '\n', '\n'},
                "CR\r": []byte{'\r', '\r', '\r'},
                "EOF\x00": []byte{'\x00'},
                "国际吗键": []byte{'z', 'h'},
                "유니코드키": []byte{'k', 'o'},
            },
            []byte{'k', 'e', 'y'},
        },
        {
            map[string][]byte{
                "\x00": []byte{'\x00', '\x01'},
            },
            []byte{},
        },
        {
            map[string][]byte{
                "\x02\x03": []byte{'\x04'},
            },
            []byte{'\x00'},
        },
        {
            map[string][]byte{
                "\x05\x06": []byte{'\x05', '\x06'},
            },
            []byte{'\xff', '\x01', '\x00'},
        },
    }
    for _, input := range inputData {
        encData, err := EncryptRecord(input.record, input.key)
        if err != nil {
            t.Errorf("Error occurred encrypting input combination: %s", input)
        }
        decData, err := DecryptRecord(encData, input.key)
        if err != nil {
            t.Errorf("Error occurred decrypting input combination: %s", input)
        }
        if !reflect.DeepEqual(decData, input.record) {
            t.Errorf(
                "Decrypted record %s does not match original: %s",
                decData,
                input.record,
            )
        }
    }
}
