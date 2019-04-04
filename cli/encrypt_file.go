// Main package for "encrypt_file"
//
// This package is used to encrypt a file with a user provided password.
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
        fmt.Print("This utility encrypts a file with a user provided password.\n")
        fmt.Print("Usage: ./encrypt_file -if=<input filename> -of=<output filename>\n")
        flag.PrintDefaults()
    }
    inputFile := flag.String("if", "", "path to the input file (required)")
    outputFile := flag.String("of", "", "path to the output file (required)")
    flag.Parse()
    if *inputFile == "" || *outputFile == "" {
        flag.Usage()
        return
    }
    fmt.Print("Enter a password to encrypt the file:\n")
    password, err := terminal.ReadPassword(int(syscall.Stdin))
    if err != nil {
        fmt.Print("An error occurred while reading the password.\n")
        return
    }
    unencryptedData, err := ioutil.ReadFile(*inputFile)
    if err != nil {
    fmt.Print("An error occurred while reading from the input file.\n")
    return
    }
    err = password_protector.WriteEncryptedFile(
        *outputFile,
        unencryptedData,
        password,
    )
    if err != nil {
        fmt.Print("An error occurred while writing to the output file.\n")
        return
    }
}
