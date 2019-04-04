// Main package for "decrypt_file"
//
// This package is used to decrypt a file that had been encrypted with encrypt_file
package main

import (
    "flag"
    "fmt"
    "golang.org/x/crypto/ssh/terminal"
    "io/ioutil"
    "password_protector/password_protector"
    "syscall"
)

// main()
//
// Command line arguments:
// if - path to the input file (required)
// of - path to the output file (required)
func main() {
    flag.Usage = func() {
        fmt.Print("This utility decrypts a file that was encrypted with the encrypt_file utility.\n")
        fmt.Print("Usage: ./decrypt_file -if=<input filename> -of=<output filename>\n")
        flag.PrintDefaults()
    }
    inputFile := flag.String("if", "", "path to the input file (required)")
    outputFile := flag.String("of", "", "path to the output file (required)")
    flag.Parse()
    if *inputFile == "" || *outputFile == "" {
        flag.Usage()
        return
    }
    fmt.Print("Enter a password to decrypt the file:\n")
    password, err := terminal.ReadPassword(int(syscall.Stdin))
    if err != nil {
        fmt.Print("An error occurred while reading the password.\n")
        return
    }
    unencryptedData, err := password_protector.OpenEncryptedFile(
        *inputFile,
        password,
    )
    if err != nil {
        fmt.Print("An error occurred while reading from the input file.\n")
        return
    }
    if ioutil.WriteFile(*outputFile, unencryptedData, 0666) != nil {
        fmt.Println("An error occurred while writing to the output file.\n")
        return
    }
}
