package main

import (
  "bufio"
  "flag"
  "fmt"
  "golang.org/x/crypto/ssh/terminal"
  "io/ioutil"
  "os"
  "password_protector/password_protector"
  "strings"
  "syscall"
)

func main() {
  mode := flag.String("mode", "P", "Program Mode: [D] to decrypt a file, [E] to encrypt a file, [P] to view password data")
  flag.Parse()
  switch strings.ToLower(*mode) {
  case "d":
    decryptMode(flag.Args())
    return
  case "e":
    encryptMode(flag.Args())
    return
  case "p":
    passwordMode(flag.Args())
    return
  default:
    fmt.Println("Error, invalid selection.")
    return
  }
}

func decryptMode(args []string) {
  fmt.Print("Password to decrypt file(s):")
  password, err := terminal.ReadPassword(int(syscall.Stdin))
  if err != nil {
    fmt.Println("Error occured while reading password.")
    return
  }
  for _, file := range args {
    data, err := password_protector.OpenEncryptedFile(file, string(password))
    if err != nil {
      fmt.Println("Error occcurred while reading encrypted file.")
      return
    }
    if ioutil.WriteFile("decrypted_" + file, []byte(data), 0666) != nil {
      fmt.Println("Error occurred while writing decrypted file.")
      return
    }
  }
}

func encryptMode(args []string) {
  fmt.Print("Password to encrypt file(s):")
  password, err := terminal.ReadPassword(int(syscall.Stdin))
  if err != nil {
    fmt.Println("Error occurred while reading password.")
    return
  }
  for _, file := range args {
    data, err := ioutil.ReadFile(file)
    if err != nil {
      fmt.Println("Error occurred while reading unencrypted file.")
      return
    }
    err = password_protector.WriteEncryptedFile("encrypted_" + file, string(data), string(password))
    if err != nil {
      fmt.Println("Error occurred while writing encrypted file.")
      return
    }
  }
}

func passwordMode(args []string) {
  if len(args) > 1 {
    fmt.Println("Only one file can be opened at a time in password protector mode.")
    return
  }
  var records []password_protector.AccountRecord
  if len(args) == 1 {
    fmt.Println("Enter file password:")
    password, err := terminal.ReadPassword(int(syscall.Stdin))
    if err != nil {
      fmt.Println("Error occurred while reading password.")
      return
    }
    fmt.Println(password)
    data, err := password_protector.OpenEncryptedFile(args[0], string(password))
    if err != nil {
      fmt.Println("Error ocurred while opening accounts file.")
      return
    }
    fmt.Println(data)
    records, err = password_protector.JSONtoAccountRecordList(data)
    if err != nil {
      fmt.Println("Error ocurred while parsing file contents.")
      return
    }
  }
  for {
    fmt.Println("Options:\n1. LIST accounts\n2. ADD account\n3. DELETE account\n4. MODIFY account\n5. SAVE file\n6. CHANGE file password\n7. QUIT")
    reader := bufio.NewReader(os.Stdin)
    selection, err := reader.ReadString('\n')
    if err != nil {
      fmt.Println("Error occurred while parsing input.")
    }
    switch strings.ToLower(strings.TrimSpace(selection)) {
    case "list":
      for i, account := range records {
        fmt.Printf("%d. %s\n", i+1, account.ReadAccountType())
      }
    case "add":
      newRecord := password_protector.AccountRecord{}
      /*
      fmt.Println("Enter retrieval password for this record")
      recordPassword, err := terminal.ReadPassword(syscall.Stdin)
      if err != nil {
        fmt.Println("Error occurred while reading password")
      }*/
      fmt.Print("Account Name:")
      name, err := reader.ReadString('\n')
      if err != nil {
        fmt.Println("Error occurred while parsing input")
      }
      newRecord.StoreAccountType(name)
      /*
      fmt.Print("Account Password (GENERATE to generate new, NONE for none):")
      password, err := reader.ReadString('\n')
      if err != nil {
        fmt.Println("Error occurred while parsing input")
      }
      if strings.ToLower(password) == "generate" {
        //TODO: Generate password
      }
      if strings.ToLower(password) != "none" {
        newRecord.StorePassword(string(password), string(recordPassword))
      }
      fmt.Print("Recovery Codes:")
      recoveryCodes, err := reader.ReadString('\n')
      if err != nil {
        fmt.Println("Error occurred while parsing input")
      }
      newRecord.StoreRecoveryCodes(string(recoveryCodes), string(recordPassword))
      pin, err := reader.ReadString('\n')
      if err != nil {
        fmt.Println("Error occurred while parsing input")
      }
      newRecord.StorePin(string(pin), string(recordPassword))*/
      records = append(records, newRecord)
    case "delete":
      //
    case "modify":
      //TODO: modify account
    case "save":
      data, err := password_protector.AccountRecordListToJSONString(records)
      if err != nil {
        fmt.Println("Error occurred while attempting to save.")
      }
      fmt.Println(data)
      fmt.Println("Enter file password:")
      password1, err := terminal.ReadPassword(int(syscall.Stdin))
      if err != nil {
        fmt.Println("Error occurred while reading password.")
        break
      }
      fmt.Println("Enter file password again:")
      password2, err := terminal.ReadPassword(int(syscall.Stdin))
      if err != nil {
        fmt.Println("Error occurred while reading password.")
        break
      }
      if string(password1) != string(password2) {
        fmt.Println("Passwords do not match, please try again.")
        break
      }
      fmt.Println(password1)
      err = password_protector.WriteEncryptedFile("testfile", data, string(password1))
      if err != nil {
        fmt.Println("Error occurred while writing encrypted file.")
      }
    case "change":
      //TODO: change file password
    case "quit":
      //TODO: add check for saving
      return
    default:
      fmt.Println("I'm sorry I didn't understand.")
    }
  }
}
