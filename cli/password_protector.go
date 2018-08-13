// Main package for password protector cli
//
// This package is used to generate and store passwords securely
package main

import (
  "flag"
  "fmt"
  "golang.org/x/crypto/ssh/terminal"
  "os"
  "password_protector/password_protector"
  "syscall"
)

// main()
//
// Command line arguments:
// <file to open or write to>
func main() {
  flag.Parse()
  if len(flag.Args()) != 1 {
    usage()
    return
  }
  newFile := false
  password := []byte{}
  records := []password_protector.AccountRecord{}
  if _, err := os.Stat(flag.Args()[0]); os.IsNotExist(err) {
    newFile = true
  } else {
    fmt.Print("Enter the file password:")
    password, err = terminal.ReadPassword(int(syscall.Stdin))
    if err != nil {
      fmt.Println("\nAn error occurred while reading the password.")
      return
    }
    data, err := password_protector.OpenEncryptedFile(flag.Args()[0], string(password))
    if err != nil {
      fmt.Println("\nAn error occurred while opening the file.")
      return
    }
    records, err = password_protector.JSONtoAccountRecordList(data)
    if err != nil {
      fmt.Println("\nAn error occurred while parsing the account records.")
      return
    }
  }
  mainMenu(records, newFile, password)
}

func usage() {
  fmt.Println("This utility stores passwords from multiple accounts")
  flag.Usage()
}

func mainMenu(records []password_protector.AccountRecord, newFile bool, password []byte) {

}
