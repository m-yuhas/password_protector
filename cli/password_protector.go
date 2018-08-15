// Main package for password protector cli
//
// This package is used to generate and store passwords securely
package main

import (
  "bufio"
  "flag"
  "fmt"
  "golang.org/x/crypto/ssh/terminal"
  "os"
  "password_protector/password_protector"
  "strings"
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
  promptToSave := false
  modified := false
  for {
    fmt.Println("Options:\n1. LIST accounts\n2. ADD account\n3. DELETE account\n4. MODIFY account\n5. CHANGE file password\n6. SAVE\n7. QUIT")
    reader := bufio.NewReader(os.Stdin)
    selection, err := reader.ReadString('\n')
    if err != nil {
      fmt.Println("An error occurred while reading the input.")
      continue
    }
    selectionArray := strings.Fields(selection)
    if len(selectionArray) < 1 {
      fmt.Println("Invalid selection")
      continue
    }
    selection = strings.ToLower(selectionArray[0])
    switch {
    case selection == "list" || selection == "1":
      list(records)
    case selection == "add" || selection == "2":
      records, modified = add(records)
    case selection == "delete" || selection == "3":
      records, modified = delete(records, selectionArray)
    case selection == "modify" || selection == "4":
      records, modified = modify(records, selectionArray)
    case selection == "change" || selection == "5":
      password, modified = change(password)
    case selection == "save" || selection == "6":
      modified = save(records, password)
    case selection == "quit" || selection == "7":
      quit(records, promptToSave)
    default:
      fmt.Println("Sorry, I didn't understand.")
    }
    promptToSave = promptToSave || modified
  }
}

func list(records []password_protector.AccountRecord) {
  return
}

func add(records []password_protector.AccountRecord) ([]password_protector.AccountRecord, bool) {
  return []password_protector.AccountRecord{}, false
}

func delete(records []password_protector.AccountRecord, selectionArray []string) ([]password_protector.AccountRecord, bool) {
  return []password_protector.AccountRecord{}, false
}

func modify(records []password_protector.AccountRecord, selectionArray []string) ([]password_protector.AccountRecord, bool) {
  return []password_protector.AccountRecord{}, false
}

func change(password []byte) ([]byte, bool) {
  return []byte{}, false
}

func save(records []password_protector.AccountRecord, password []byte) bool {
  return false
}

func quit(records []password_protector.AccountRecord, modified bool) {
  os.Exit(0)
}
