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
  inputFile := flag.String("if", "", "path to the input file (required)")
  outputFile := flag.String("of", "", "path to the output file (required)")
  flag.Parse()
  if *inputFile == "" || *outputFile == "" {
    usage()
    return
  }
  fmt.Print("Enter a password to decrypt the file:")
  password, err := terminal.ReadPassword(int(syscall.Stdin))
  if err != nil {
    fmt.Println("\nAn error occurred while reading the password.")
    return
  }
  unencryptedData, err := password_protector.OpenEncryptedFile(*inputFile, string(password))
  if err != nil {
    fmt.Println("\nAn error occurred while reading from the input file.")
    return
  }
  if ioutil.WriteFile(*outputFile, []byte(unencryptedData), 0666) != nil {
    fmt.Println("\nAn error occurred while writing to the output file.")
    return
  }
  fmt.Print("\n")
}

// usage()
//
// Print usage instructions for this program.
func usage() {
  fmt.Println("This utility decrypts a file that was encrypted with the encrypt_file utility.")
  flag.Usage()
}
