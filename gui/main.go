package main

import (
    "github.com/therecipe/qt/core"
    "github.com/therecipe/qt/gui"
	"github.com/therecipe/qt/widgets"
	"os"
    "password_protector/password_protector"
)

type Window interface {
    onPasswordReturn(password []byte)
}

type PasswordProtectorMainWindow interface {
    Window
    about()
    accountAdded()
    addAccount()
    openFile()
    refresh()
    saveFile()
    viewAccount()
}

type AccountEntryWindow interface {
    Window
    submit()
} 

type AccountViewWindow interface {
    Window 
}

type PasswordEntryWindow interface {
    Window
    submit()
}

type window struct {
    widgets.QMainWindow
}

type passwordProtectorMainWindow struct {
    window
    actionNew *widgets.QAction
    actionOpen *widgets.QAction
    actionSave *widgets.QAction
    actionChangePassword *widgets.QAction
    actionQuit *widgets.QAction
    actionEncryptFile *widgets.QAction
    actionDecryptFile *widgets.QAction
    actionGeneratePassword *widgets.QAction
    actionHelp *widgets.QAction
    actionAbout *widgets.QAction
    layout *widgets.QGridLayout
    viewButton *widgets.QPushButton
    addButton *widgets.QPushButton
    editButton *widgets.QPushButton
    deleteButton *widgets.QPushButton
    changeButton *widgets.QPushButton
    listWidget *widgets.QListWidget
    entryWindow *accountEntryWindow
    viewWindow *accountViewWindow
    saved bool
    modified bool
    fileName string
    records map[string][]byte
}

type accountEntryWindow struct {
    window 
    layout *widgets.QFormLayout
    entryName *widgets.QLineEdit
    entryTable *widgets.QTableWidget
    addButton *widgets.QPushButton
    caller *passwordProtectorMainWindow
    passwordWindow *passwordEntryWindow
    record map[string][]byte
}

type accountViewWindow struct {
    window 
    layout *widgets.QFormLayout
    recordLabel *widgets.QLabel
    recordName *widgets.QLabel
    recordTable *widgets.QTableWidget
    doneButton *widgets.QPushButton
    caller *passwordProtectorMainWindow
    passwordWindow *passwordEntryWindow
    name string
    record []byte
}

type accountEditWindow struct {
    window
    layout *widgets.QFormLayout
    recordLabel *widgets.QLabel
    recordName *widgets.QTableWidget
    submitButton *widgets.QPushButton
    caller *passwordProtectorMainWindow
    passwordWindow *passwordEntryWindow
    name string
    record []byte
}

type passwordEntryWindow struct {
    window
    layout *widgets.QFormLayout
    passwordLabel *widgets.QLabel
    passwordEntry *widgets.QLineEdit
    submitButton *widgets.QPushButton
    caller func([]byte)
}

func main() {
	app := widgets.NewQApplication(len(os.Args), os.Args)
    core.QCoreApplication_SetApplicationName("Password Protector")
    core.QCoreApplication_SetApplicationVersion("0.0.1")
    mainWindow := initPasswordProtectorMainWindow()
    /* TODO: Work on parsing args for drag and drop
    if !mainWindow.open(args[0]) {
        mainWindow.newFile()
    }*/
    mainWindow.Show()
    app.Exec()
}

func initPasswordProtectorMainWindow() *passwordProtectorMainWindow {
    var this = NewPasswordProtectorMainWindow(nil, 0)
    this.SetWindowTitle(core.QCoreApplication_ApplicationName())
    this.SetMinimumSize2(640, 480)

    //this.setupFileActions()
    fileMenu := this.MenuBar().AddMenu2("File")
    this.actionNew = fileMenu.AddAction("New")
    this.actionNew.ConnectTriggered(func(checked bool) { this.openFile()})
    this.actionNew.SetShortcuts2(gui.QKeySequence__New)
    this.actionOpen = fileMenu.AddAction("Open")
    this.actionOpen.ConnectTriggered(func(checked bool) { this.openFile()})
    this.actionOpen.SetShortcuts2(gui.QKeySequence__Open)
    this.actionSave = fileMenu.AddAction("Save")
    //this.actionSave.ConnectTriggered(func(checked bool) { this.foo()})
    this.actionSave.SetShortcuts2(gui.QKeySequence__Save)
    this.actionChangePassword = fileMenu.AddAction("Change File Password")
    //this.actionChangePassword.ConnectTriggered(func(checked bool) { this.foo()})
    this.actionQuit = fileMenu.AddAction("Quit")
    //this.actionQuit.ConnectTriggered(func(checked bool) { this.foo() })
    this.actionQuit.SetShortcuts2(gui.QKeySequence__Quit)

    //this.setupSecurityActions()
    securityMenu := this.MenuBar().AddMenu2("Security")
    this.actionEncryptFile = securityMenu.AddAction("Encrypt File")
    //this.actionEncryptFile.ConnectTriggered(func(checked bool) {this.foo()})
    this.actionDecryptFile = securityMenu.AddAction("Decrypt File")
    //this.actionDecryptFile.ConnectTriggered(func(checked bool) {this.foo()})
    this.actionGeneratePassword = securityMenu.AddAction("Generate Password")
    //this.actionGeneratePassword.ConnectTriggered(func(checked bool) {this.foo()})

    //this.setupHelpActions()
    helpMenu := this.MenuBar().AddMenu2("Help")
    this.actionHelp = helpMenu.AddAction("Password Protector Help")
    //this.actionHelp.ConnectTriggered(func(checked bool) {this.foo()})
    this.actionAbout = helpMenu.AddAction("About")
    this.actionAbout.ConnectTriggered(func(checked bool) {this.about()})

    //this.setupLayout()
    widget := widgets.NewQWidget(this, 0)
    this.SetCentralWidget(widget)
    this.layout = widgets.NewQGridLayout(widget)
    this.addButton = widgets.NewQPushButton2("Add New Record", nil)
    this.addButton.ConnectClicked(func(checked bool) {this.addAccount()})
    this.addButton.SetMinimumWidth(200)
    this.viewButton = widgets.NewQPushButton2("View Existing Record", nil)
    this.viewButton.ConnectClicked(func(checked bool) {this.viewAccount()})
    this.viewButton.SetMinimumWidth(200)
    this.editButton = widgets.NewQPushButton2("Edit Existing Record", nil)
    this.editButton.SetMinimumWidth(200)
    this.deleteButton = widgets.NewQPushButton2("Delete Record", nil)
    this.deleteButton.SetMinimumWidth(200)
    this.changeButton = widgets.NewQPushButton2("Change Record Password", nil)
    this.changeButton.SetMinimumWidth(200)
    this.listWidget = widgets.NewQListWidget(nil)
    this.layout.AddWidget3(this.listWidget, 1, 1, 5, 1, core.Qt__AlignJustify)
    this.layout.AddWidget(this.addButton, 1, 2, core.Qt__AlignCenter)
    this.layout.AddWidget(this.viewButton, 2, 2, core.Qt__AlignCenter)
    this.layout.AddWidget(this.editButton, 3, 2, core.Qt__AlignCenter)
    this.layout.AddWidget(this.deleteButton, 4, 2, core.Qt__AlignCenter)
    this.layout.AddWidget(this.changeButton, 5, 2, core.Qt__AlignCenter)

    this.records = map[string][]byte{}

    return this
}

func (p *passwordProtectorMainWindow) about() {
    widgets.QMessageBox_About(
        p,
        "About Password Protector",
        "This application encrypts passwords for management. " +
        "<a href='https://github.com/m-yuhas/password_protector'>click</a>",
    )
}

func (p *passwordProtectorMainWindow) openFile() {
    fileDialog := widgets.NewQFileDialog2(p, "Open File...", "", "")
    //fileDialog.SetAcceptMode(widgets.QFileDialog__AcceptOpen)
    //fileDialog.SetFileMode(widgets.QFileDialog__ExistingFile)
    if fileDialog.Exec() != int(widgets.QDialog__Accepted) {
        return
    }
    p.fileName = fileDialog.SelectedFiles()[0]
}

func (p *passwordProtectorMainWindow) saveFile() {
    return
}

func (p *passwordProtectorMainWindow) addAccount() {
    p.entryWindow = initAccountEntryWindow(p)
    p.entryWindow.Show()
}

func (p *passwordProtectorMainWindow) viewAccount() {
    p.viewWindow = initAccountViewWindow(p, p.listWidget.CurrentItem().Text(), p.records)
}

func (p *passwordProtectorMainWindow) accountAdded(recordName string, encryptedRecord []byte) {
    p.entryWindow.DestroyQMainWindow()
    p.records[recordName] = encryptedRecord
    p.refresh()
}

func (p *passwordProtectorMainWindow) refresh() {
    p.listWidget.Clear()
    for account, _ := range p.records {
        p.listWidget.AddItem(account)
    }
}

func initAccountEntryWindow(caller *passwordProtectorMainWindow) *accountEntryWindow {
    var this = NewAccountEntryWindow(nil, 0)
    widget := widgets.NewQWidget(nil, 0)
    this.SetCentralWidget(widget)
    this.layout = widgets.NewQFormLayout(widget)
    this.entryName = widgets.NewQLineEdit2("Account Name", nil)
    this.entryTable = widgets.NewQTableWidget2(3, 2, nil)
    this.addButton = widgets.NewQPushButton2("Add", nil)
    this.addButton.ConnectClicked(func(checked bool) {this.submit()})
    this.layout.AddWidget(this.entryName)
    this.layout.AddWidget(this.entryTable)
    this.layout.AddWidget(this.addButton)
    this.caller = caller
    return this
}

func (a *accountEntryWindow) submit() {
    a.record = map[string][]byte{}
    for i := 0; i <= a.entryTable.RowCount(); i++ {
        if a.entryTable.Item(i, 0).Text() == "" && a.entryTable.Item(i, 1).Text() != "" {
            widgets.QMessageBox_About(
                a,
                "Incomplete Entry",
                "Missing key; please make sure each value has an associated key.",
            )
            return
        }
        if a.entryTable.Item(i, 0).Text() == "" {
            continue
        } else if _, ok := a.record[a.entryTable.Item(i, 0).Text()]; !ok {
            a.record[a.entryTable.Item(i, 0).Text()] = []byte(a.entryTable.Item(i, 1).Text())
        } else {
            widgets.QMessageBox_About(
                a,
                "Duplicate Entry",
                "Duplicate key " + a.entryTable.Item(i, 0).Text() + "; please remove the duplicate entry.",
            )
            return
        }
    }
    if len(a.record) == 0 {
        widgets.QMessageBox_About(
            a,
            "No Data",
            "No data entered.",
        )
        return
    }
    a.passwordWindow = initPasswordEntryWindow(a.onPasswordReturn)
}

func (a *accountEntryWindow) onPasswordReturn(password []byte) {
    a.passwordWindow.DestroyQMainWindow()
    encryptedRecord, err := password_protector.EncryptRecord(
        a.record,
        password,
    )
    if err != nil {
        widgets.QMessageBox_About(
            a,
            "Encryption Error",
            "An error occurred while encrypting the record",
        )
        return
    }
    a.caller.accountAdded(a.entryName.Text(), encryptedRecord)
}

func initAccountViewWindow(caller *passwordProtectorMainWindow, recordName string, records map[string][]byte) *accountViewWindow {
    var this = NewAccountViewWindow(nil, 0)
    //initPasswordEntryWindow(this.onPasswordReturn)

    //this.recordTable = widgets.NewQTableWidget2(len(record), 2, nil)
    //this.recordTable.SetFlags(this.recordTable.Flags() ^ core.Qt__ItemIsEditable)

    this.passwordWindow = initPasswordEntryWindow(this.onPasswordReturn)
    this.name = recordName
    this.record = records[recordName]
    return this
}

func (a *accountViewWindow) onPasswordReturn(password []byte) {
    a.passwordWindow.DestroyQMainWindow()
    decryptedRecord, err := password_protector.DecryptRecord(
        a.record,
        password,
    )
    if err == nil {
        widget := widgets.NewQWidget(a, 0)
        a.SetCentralWidget(widget)
        a.layout = widgets.NewQFormLayout(widget)
        a.recordLabel = widgets.NewQLabel2("Account Name:", a, 0)
        a.recordName = widgets.NewQLabel2("<b>"+a.name+"</b>", a, 0)
        a.recordTable = widgets.NewQTableWidget2(len(decryptedRecord), 2, nil)
        i := 0
        for key, value := range decryptedRecord {
            a.recordTable.SetItem(i, 0, widgets.NewQTableWidgetItem2(key, 0))
            a.recordTable.SetItem(i, 1, widgets.NewQTableWidgetItem2(string(value), 0))
            a.recordTable.Item(i, 0).SetFlags(a.recordTable.Item(i, 0).Flags() ^ core.Qt__ItemIsEditable)
            a.recordTable.Item(i, 1).SetFlags(a.recordTable.Item(i, 1).Flags() ^ core.Qt__ItemIsEditable)
            i++
        }
        a.doneButton = widgets.NewQPushButton2("Done", a)
        a.doneButton.ConnectClicked(func(checked bool) {a.done()})
        a.layout.AddWidget(a.recordLabel)
        a.layout.AddWidget(a.recordName)
        a.layout.AddWidget(a.recordTable)
        a.layout.AddWidget(a.doneButton)
        a.Show()
        return
    } else {
        widgets.QMessageBox_About(
            a,
            "Decryption Error",
            "An error occurred while decrypting the record",
        )
    }
    return
}

func (a *accountViewWindow) done() {
    a.DestroyQMainWindow()
}

func initAccountEditWindow(caller *passwordProtectorMainWindow, recordName string, record map[string][]byte) *accountEditWindow {
    var this = NewAccountEditWindow(nil, 0)
    this.passwordWindow = initPasswordEntryWindow(this.onPasswordReturn)
    this.name = recordName
    this.record = records[recordName]
    return this
}

func (a *accountEditWindow) onPasswordReturn(password []byte) {
    a.passwordWindow.DestroyQMainWindow()
    decryptedRecord, err := passwordProtector.DecryptRecord(
        a.record,
        a.password,
    )
    if err == nil {
        widget := widgets.NewQWidget(a, 0)
        a.SetCentralWidget(widget)
        a.layout = widgets.NewQFormLayout(widgets)
        a.recordLabel = widgets.NewQLabel2("Account Name:", a, 0)
        a.recordName = widgets.NewQLineEdit2(a.name, a, 0)
        a.recordTable = widgets.NewQTableWidget2(len(decryptedRecord), 2, nil)
        i := 0
        for key, value := range decryptRecord {
            a.RecordTable.SetItem(i, 0, widgets.NewQTableWidgetItem2(key, 0))
            a.recordTable.SetItem(i, 1, widgets.NewQTableWidgetItem2(string(value), 0))
            i++
        }
        a.submitButton = widgets.NewQPushButton2("Submit", a)
        a.submitButton.ConnectClicked(func(checked bool) {a.submit()})
        a.layout.AddWidget(a.recordLabel)
        a.layout.AddWidget(a.recordName)
        a.layout.AddWidget(a.recordTable)
        a.layout.AddWidget(a.submitButton)
        a.Show()
        return
    } else {
        widgets.QMessageBox_About(
            a,
            "Decryption Error",
            "An error occurred while decrypting the record",
        )
    }
    return
}

func (a *accountEditWindow) submit() {
    // TODO: Add decrypt record to struct, rewrite this for edit window
    a.record = map[string][]byte{}
    for i := 0; i <= a.entryTable.RowCount(); i++ {
        if a.entryTable.Item(i, 0).Text() == "" && a.entryTable.Item(i, 1).Text() != "" {
            widgets.QMessageBox_About(
                a,
                "Incomplete Entry",
                "Missing key; please make sure each value has an associated key.",
            )
            return
        }
        if a.entryTable.Item(i, 0).Text() == "" {
            continue
        } else if _, ok := a.record[a.entryTable.Item(i, 0).Text()]; !ok {
            a.record[a.entryTable.Item(i, 0).Text()] = []byte(a.entryTable.Item(i, 1).Text())
        } else {
            widgets.QMessageBox_About(
                a,
                "Duplicate Entry",
                "Duplicate key " + a.entryTable.Item(i, 0).Text() + "; please remove the duplicate entry.",
            )
            return
        }
    }
    if len(a.record) == 0 {
        widgets.QMessageBox_About(
            a,
            "No Data",
            "No data entered.",
        )
        return
    }
    a.passwordWindow = initPasswordEntryWindow(a.onPasswordReturn)
}

func (a *accountEditWindow) onPasswordReturn(password []byte) {
    // TODO: Rewrite for Edit Window
    a.passwordWindow.DestroyQMainWindow()
    encryptedRecord, err := password_protector.EncryptRecord(
        a.record,
        password,
    )
    if err != nil {
        widgets.QMessageBox_About(
            a,
            "Encryption Error",
            "An error occurred while encrypting the record",
        )
        return
    }
    a.caller.accountAdded(a.entryName.Text(), encryptedRecord)
}

func initPasswordEntryWindow(caller func([]byte)) *passwordEntryWindow {
    var this = NewPasswordEntryWindow(nil, 0)
    widget := widgets.NewQWidget(this, 0)
    this.SetCentralWidget(widget)
    this.layout = widgets.NewQFormLayout(widget)
    this.passwordLabel = widgets.NewQLabel2("Password:", this, 0)
    this.passwordEntry = widgets.NewQLineEdit2("", this)
    this.passwordEntry.SetEchoMode(widgets.QLineEdit__Password)
    this.submitButton = widgets.NewQPushButton2("Submit", this)
    this.submitButton.ConnectClicked(func(checked bool) {this.submit()})
    this.layout.AddWidget(this.passwordLabel)
    this.layout.AddWidget(this.passwordEntry)
    this.layout.AddWidget(this.submitButton)
    this.caller = caller
    this.Show()
    return this
}

func (p *passwordEntryWindow) submit() {
    //p.caller.onPasswordReturn([]byte(p.passwordEntry.Text()))
    p.caller([]byte(p.passwordEntry.Text()))
}
