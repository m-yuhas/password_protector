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
  inputFile := flag.String("if", "", "path to the input file (required)")
  outputFile := flag.String("of", "", "path to the output file (required)")
  flag.Parse()
  if *inputFile == "" || *outputFile == "" {
    usage()
    return
  }
  fmt.Print("Enter a password to encrypt the file:")
  password, err := terminal.ReadPassword(int(syscall.Stdin))
  if err != nil {
    fmt.Println("\nAn error occurred while reading the password.")
    return
  }
  unencryptedData, err := ioutil.ReadFile(*inputFile)
  if err != nil {
    fmt.Println("\nAn error occurred while reading from the input file.")
    return
  }
  err = password_protector.WriteEncryptedFile(*outputFile, string(unencryptedData), string(password))
  if err != nil {
    fmt.Println("\nAn error occurred while writing to the output file.")
    return
  }
  fmt.Print("\n")
}

// usage()
//
// Print usage instructions for this program.
func usage() {
  fmt.Println("This utility encrypts a file with a user provided password.")
  flag.Usage()
}