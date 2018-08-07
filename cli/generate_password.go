// Main package for "decrypt_file"
//
// This package is used to generate a password according to the desired parameters
package main

import (
  "flag"
  "fmt"
  "password_protector/password_generator"
)

// main()
//
// Command line arguments:
// l - password length (required)
// uc - use upper case letters in password
// lc - use lower case letters in password
// n - use numbers in password
// s - use symbols in password
func main() {
  length := flag.Int("l",0,"Password length")
  upperCase := flag.Bool("uc",false,"Use upper case letters in password")
  lowerCase := flag.Bool("lc",false,"Use lower case letters in password")
  numbers := flag.Bool("n",false,"Use numbers in password")
  symbols := flag.Bool("s",false,"Use symbols in password")
  flag.Parse()
  if *length <= 0 || !(*upperCase || *lowerCase || *numbers || *symbols) {
    usage()
    return
  }
  allowedCharacters := []byte{}
  if *upperCase {
    allowedCharacters = append(allowedCharacters, password_generator.UpperCaseLetters...)
  }
  if *lowerCase {
    allowedCharacters = append(allowedCharacters, password_generator.LowerCaseLetters...)
  }
  if *numbers {
    allowedCharacters = append(allowedCharacters, password_generator.Numbers...)
  }
  if *symbols {
    allowedCharacters = append(allowedCharacters, password_generator.Symbols...)
  }
  password, err := password_generator.GeneratePassword(*length, allowedCharacters)
  if err != nil {
    fmt.Println("An error occurred while generating a password.")
    return
  }
  fmt.Println(string(password))
}

// usage()
//
// Print usage instructions for this program.
func usage() {
  fmt.Println("This utility generates a password according to the selected parameters.")
  flag.Usage()
}
