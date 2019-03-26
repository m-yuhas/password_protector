package password_generator

import (
    "crypto/rand"
    "encoding/binary"
    "errors"
)

var LowerCaseLetters = []byte{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'}
var UpperCaseLetters = []byte{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'}
var Numbers = []byte{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'}
var Symbols = []byte{'!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '-', '+', '=', '{', '}', '[', ']', '|', '\\', '?', '/', '<', '>', ',', '.', '~', '`', '\'', '"'}

func GeneratePassword(length uint, characterList ...[]byte) ([]byte, error) {
    acceptedCharacters := []byte{}
    for _, slice := range characterList {
        acceptedCharacters = append(acceptedCharacters, slice...)
    }
    if len(acceptedCharacters) == 0 {
        return []byte{}, errors.New("Need non-zero number of allowed chars.")
    }
    password := make([]byte, length)
    for i := uint(0); i < length; i++ {
        b := make([]byte, 8)
        n, err := rand.Read(b)
        if n != 8 {
            panic(n)
        } else if err != nil {
            panic(err)
        }
        index := binary.BigEndian.Uint64(b) % uint64(len(acceptedCharacters))
        password[i] = acceptedCharacters[index]
    }
    return password, nil
}
