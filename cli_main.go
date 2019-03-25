package main

import (
    "bufio"
    "fmt"
    "golang.org/x/crypto/ssh/terminal"
    "os"
    "password_protector/password_protector"
    "password_protector/password_generator"
    "strings"
    "syscall"
    "text/tabwriter"
)

func main() {
    if len(os.Args) > 2 ||
    len(os.Args) < 2 ||
    strings.ToLower(os.Args[1]) == "-h" ||
    strings.ToLower(os.Args[1]) == "--help" {
        fmt.Printf("Usage:\n")
        fmt.Printf("password_protector [filename]\n")
        fmt.Printf("Where [filename] is the name of the encrypted file to" + 
            "open if it exists and the name of the file to create if no such" +
            "file exists\n")
        os.Exit(0)
    } else {
        fmt.Print("Enter file password:")
        password, err := terminal.ReadPassword(int(syscall.Stdin))
        if err != nil {
            fmt.Printf("An error occurred while reading the password.\n")
            os.Exit(0)
        }
        records := map[string][]byte{}
        if _, err := os.Stat(os.Args[1]); err == nil {
            fileContents, err := password_protector.OpenEncryptedFile(
                os.Args[1],
                password,
            )
            if err != nil {
                fmt.Printf("An error occurred while decrypting the file.\n")
                os.Exit(0)
            }
            records, err = password_protector.JSONToRecord(fileContents)
            if err != nil {
                fmt.Printf("An error occurred while parsing the file.\n")
                os.Exit(0)
            }
        } else if os.IsNotExist(err) {
            fileContents, err := password_protector.RecordToJSON(records)
            if err != nil {
                fmt.Printf("An error occurred while writing the file; file not saved.\n")
                os.Exit(0)
            }
            err = password_protector.WriteEncryptedFile(
                os.Args[1],
                fileContents,
                password,
            )
            if err != nil {
                fmt.Printf("An error occurred while writing the file; file not saved.\n")
                os.Exit(0)
            }
        }
        mainMenu(records)
    }
    os.Exit(0)
}

func mainMenu(records map[string][]byte) {
    for {
        menu := map[string]func(map[string][]byte, string){
            "list": list,
            "view": view,
            "add": add,
            "delete": deleteRecord,
            "modify": modify,
            "save": save,
            "change": change,
            "quit": quit,
        }
        fmt.Printf("Options: \n1. LIST accounts\n2. VIEW account\n3. ADD " +
            "account\n4. DELETE account\n5. MODIFY account\n6. SAVE file\n" +
            "7.CHANGE file password\n8. QUIT\n")
        reader := bufio.NewReader(os.Stdin)
        selection, err := reader.ReadString('\n')
        if err != nil {
            fmt.Printf("An error occurred while parsing the input.\n")
        }
        selection = strings.TrimSuffix(
            strings.TrimSuffix(selection, "\n"),
            "\r",
        )
        slicedSelection := strings.SplitN(selection, " ", 2)
        if len(slicedSelection) < 2 {
            slicedSelection = append(slicedSelection, "")
        }
        if menuFunction, ok := menu[slicedSelection[0]]; ok {
            menuFunction(records, slicedSelection[1])
        } else {
            fmt.Printf("Invalid selection, please try again.\n")
        }
    }
}

func list(records map[string][]byte, options string) {
    count := 0
    for name, _ := range records {
        count += 1
        fmt.Printf("%d. %s\n", count, name)
    }
    if count == 0 {
        fmt.Printf("No records to list.\n")
    }
}

func view(records map[string][]byte, option string) {
    if _, ok := records[option]; ok {
        fmt.Printf("Enter password to decrypt record:\n")
        password, err := terminal.ReadPassword(int(syscall.Stdin))
        if err != nil {
            fmt.Printf("An error occurred while reading the password.\n")
            return
        }
        record, err := password_protector.DecryptRecord(
            records[option],
            password,
        )
        if err != nil {
            fmt.Printf("An error occurred while decrypting the record.\n")
            return
        }
        tabWriter := tabwriter.NewWriter(os.Stdout, 0, 0, 1, ' ', 0)
        fmt.Fprintf(tabWriter, "KEY\tVALUE\n")
        for key, val := range record {
            fmt.Fprintf(tabWriter, "%s\t%s\n", key, val)
        }
    } else {
        fmt.Printf("Usage: VIEW <RECORD NAME>\n")
    }
}

func add(records map[string][]byte, option string) {
    if strings.TrimSpace(option) == "" {
        fmt.Printf("Usage: ADD <RECORD NAME>\n")
    } else if _, ok := records[option]; !ok {
        record := map[string][]byte{}
        editRecord(record)
        fmt.Printf("Enter the password that will protect this record:\n")
        password, err := terminal.ReadPassword(int(syscall.Stdin))
        if err != nil {
            fmt.Printf("An error occurred while reading the password.\n")
            return
        }
        records[option], err = password_protector.EncryptRecord(
            record,
            password,
        )
        if err != nil {
            fmt.Printf("An error occurred while adding the record.\n")
            return
        }
    } else {
        fmt.Printf("A record with the name \"%s\" already exists.\n", option)
    }
}

func deleteRecord(records map[string][]byte, option string) {
    if _, ok := records[option]; ok {
        fmt.Printf("Enter password to delete record:\n")
        password, err := terminal.ReadPassword(int(syscall.Stdin))
        if err != nil {
            fmt.Printf("An error occurred while reading the password.\n")
            return
        }
        plaintextRecord, err := password_protector.DecryptData(
            records[option],
            password,
        )
        if err != nil {
            fmt.Printf("An error occurred while decrypting the record.\n")
            return
        }
        record, err := password_protector.JSONToRecord(plaintextRecord)
        if err != nil {
            fmt.Printf("An error occurred while parsing the record.\n")
            return
        }
        fmt.Printf("Are you sure you want to delete this record (YES or NO)?\n")
        reader := bufio.NewReader(os.Stdin)
        selection, err := reader.ReadString('\n')
        if err != nil {
            fmt.Printf("An error occurred while parsing the input.\n")
            fmt.Printf("Record was not deleted.\n")
            return
        }
        selection = strings.ToLower(
            strings.TrimSuffix(strings.TrimSuffix(selection, "\n"), "\r"),
        )
        if selection == "yes" {
            delete(record, option)
        } else {
            fmt.Printf("Delete aborted.\n")
        }
    } else {
        fmt.Printf("Usage: DELETE <RECORD NAME>\n")
    }
}

func modify(records map[string][]byte, option string) {
    if _, ok := records[option]; ok {
        fmt.Printf("Enter password to decrypt record:\n")
        password, err := terminal.ReadPassword(int(syscall.Stdin))
        if err != nil {
            fmt.Printf("An error occurred while reading the password.\n")
            return
        }
        record, err := password_protector.DecryptRecord(
            records[option],
            password,
        )
        if err != nil {
            fmt.Printf("An error occurred while decrypting the record.\n")
            return
        }
        tabWriter := tabwriter.NewWriter(os.Stdout, 0, 0, 1, ' ', 0)
        fmt.Fprintf(tabWriter, "KEY\tVALUE\n")
        for key, val := range record {
            fmt.Fprintf(tabWriter, "%s\t%s\n", key, val)
        }
        editRecord(record)
        fmt.Printf("Enter the password that will protect this record:\n")
        password, err = terminal.ReadPassword(int(syscall.Stdin))
        if err != nil {
            fmt.Printf("An error occurred while reading the password.\n")
            return
        }
        records[option], err = password_protector.EncryptRecord(
            record,
            password,
        )
        if err != nil {
            fmt.Printf("An error occurred while modifying the record.\n")
            return
        }
    } else {
        fmt.Printf("Usage: MODIFY <RECORD NAME>\n")
    }
}

func save(records map[string][]byte, option string) {
    fmt.Printf("Enter the file password:\n")
    password, err := terminal.ReadPassword(int(syscall.Stdin))
    if err != nil {
        fmt.Printf("An error occurred while reading the password.\n")
        return
    }
    fileContents, err := password_protector.OpenEncryptedFile(
        os.Args[1],
        password,
    )
    if err != nil {
        fmt.Printf("An error occurred validating the password; file not saved.\n")
        return
    }
    _, err = password_protector.JSONToRecord(fileContents)
    if err != nil {
        fmt.Printf("An error occurred validating the password; file not saved.\n")
        return
    }
    fileContents, err = password_protector.RecordToJSON(records)
    if err != nil {
        fmt.Printf("An error occurred while writing the file; file not saved.\n")
        return
    }
    err = password_protector.WriteEncryptedFile(
        os.Args[1],
        fileContents,
        password,
    )
    if err != nil {
        fmt.Printf("An error occurred while writing the file; file not saved.\n")
        return
    }
}

func change(records map[string][]byte, option string) {
    fmt.Printf("Enter the current file password:\n")
    password, err := terminal.ReadPassword(int(syscall.Stdin))
    if err != nil {
        fmt.Printf("An error occurred while reading the password.\n")
        return
    }
    fileContents, err := password_protector.OpenEncryptedFile(
        os.Args[1],
        password,
    )
    if err != nil {
        fmt.Printf("An error occurred while decrypting the file.\n")
        return
    }
    records, err = password_protector.JSONToRecord(fileContents)
    if err != nil {
        fmt.Printf("An error occurred while parsing the file.\n")
        return
    }
    fmt.Printf("Enter the new file password:\n")
    password, err = terminal.ReadPassword(int(syscall.Stdin))
    if err != nil {
        fmt.Printf("An error occurred while reading the password.\n")
        fmt.Printf("File password not changed.\n")
        return
    }
    fileContents, err = password_protector.RecordToJSON(records)
    if err != nil {
        fmt.Printf("An error occurred while writing the file; changes not saved.\n")
        return
    }
    err = password_protector.WriteEncryptedFile(
        os.Args[1],
        fileContents,
        password,
    )
    if err != nil {
        fmt.Printf("An error occurred while writing the file; changes not saved.\n")
        return
    }
}

func quit(records map[string][]byte, option string) {
    fmt.Printf("Save changes before exiting (YES or NO)?\n")
    reader := bufio.NewReader(os.Stdin)
    selection, err := reader.ReadString('\n')
    if err != nil {
        fmt.Println("An error occurred while parsing the input.")
    }
    selection = strings.ToLower(
        strings.TrimSuffix(strings.TrimSuffix(selection, "\n"), "\r"),
    )
    if selection == "yes" {
        save(records, "")
    }
    fmt.Printf("Bye!\n")
    os.Exit(0)
}

func generate() []byte {
    var nChars int
    fmt.Printf("How many characters to use?\n")
    _, err := fmt.Scanf("%d", &nChars)
    if err != nil {
        fmt.Printf("An error occurred while parsing the input.\n")
    }
    allowedChars := []byte{}
    choices := map[string][]byte{
        "Use lower case letters (YES or NO)?": password_generator.LowerCaseLetters,
        "Use upper case letters (YES or NO)?": password_generator.UpperCaseLetters,
        "Use numbers (YES or NO)?": password_generator.Numbers,
        "Use symbols (YES or NO)?": password_generator.Symbols,
    }
    reader := bufio.NewReader(os.Stdin)
    for {
        for charType, charSlice := range choices {
            fmt.Println(charType)
            selection, err := reader.ReadString('\n')
            if err != nil {
                fmt.Printf("An error occurred while parsing the input.\n")
            }
            selection = strings.ToLower(
                strings.TrimSuffix(strings.TrimSuffix(selection, "\n"), "\r"),
            )
            if selection == "yes" || selection == "y" {
                allowedChars = append(allowedChars, charSlice...)
            }
        }
        if len(allowedChars) != 0 {
            break
        } else {
            fmt.Printf("Please select a non zero amount of allowable characters.\n")
        }
    }
    password := password_generator.GeneratePassword(nChars, allowedChars)
    fmt.Printf("The autogenerated password is:\n%s\nAccept (YES or NO)?\n", password)
    return password
    //TODO: Handle not accept password
}

func editRecord(record map[string][]byte) {
    for {
        fmt.Printf("Enter key name (D for Done):\n")
        reader := bufio.NewReader(os.Stdin)
        keyName, err := reader.ReadString('\n')
        if err != nil {
            fmt.Printf("An error occurred while reading the input.\n")
            continue
        }
        keyName = strings.ToLower(
            strings.TrimSuffix(strings.TrimSuffix(keyName, "\n"), "\r"),
        )
        if keyName == "d" {
            break
        }
        fmt.Printf("Enter the value for key %s (A to AUTOGENERATE password):\n", keyName)
        value, err := reader.ReadString('\n')
        if err != nil {
            fmt.Printf("An error occurred while reading the input.\n")
            fmt.Printf("Please re-enter this key name.\n")
            continue
        }
        value = strings.TrimSuffix(strings.TrimSuffix(value, "\n"), "\r")
        if strings.ToLower(value) == "a" {
            record[keyName] = generate()
        } else {
            record[keyName] = []byte(value)
        }
    }
}

func cleanMap(record map[string][]byte) {
    for _, val := range record {
        for i, _ := range val {
            val[i] = '\x00'
        }
    }
}

func cleanSlice(s []byte) {
    for i, _ := range s {
        s[i] = '\x00'
    }
}

// TODO: Internationalisation
// TODO: Lint + DocStrings
