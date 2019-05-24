package main

import (
    "github.com/therecipe/qt/core"
    "github.com/therecipe/qt/gui"
	"github.com/therecipe/qt/widgets"
	"os"
    "password_protector/password_protector"
)

type PasswordProtector struct {
    widgets.QMainWindow
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
    //layout *widgets.QVBoxLayout
    viewButton *widgets.QPushButton
    addButton *widgets.QPushButton
    editButton *widgets.QPushButton
    deleteButton *widgets.QPushButton
    changeButton *widgets.QPushButton
    listWidget *widgets.QListWidget
    entryWindow *AccountEntryWindow
    saved bool
    modified bool
    fileName string
    records map[string][]byte
}

type AccountEntryWindow struct {
    widgets.QMainWindow
    layout *widgets.QFormLayout
    entryName *widgets.QLineEdit
    entryTable *widgets.QTableWidget
    addButton *widgets.QPushButton
    caller *PasswordProtector
    passwordWindow *PasswordEntryWindow
    record map[string][]byte
}

type PasswordEntryWindow struct {
    widgets.QMainWindow
    layout *widgets.QFormLayout
    passwordLabel *widgets.QLabel
    passwordEntry *widgets.QLineEdit
    submitButton *widgets.QPushButton
    caller *AccountEntryWindow
}

func main() {
	app := widgets.NewQApplication(len(os.Args), os.Args)
    core.QCoreApplication_SetApplicationName("Password Protector")
    core.QCoreApplication_SetApplicationVersion("0.0.1")
    mainWindow := initPasswordProtector()
    /* TODO: Work on parsing args for drag and drop
    if !mainWindow.open(args[0]) {
        mainWindow.newFile()
    }*/
    mainWindow.Show()
    app.Exec()
}

func initPasswordProtector() *PasswordProtector {
    var this = NewPasswordProtector(nil, 0)
    this.SetWindowTitle(core.QCoreApplication_ApplicationName())
    this.SetMinimumSize2(640, 480)
    this.setupFileActions()
    this.setupSecurityActions()
    this.setupHelpActions()
    this.setupLayout()
    this.setupBackend()
    return this
}

func (p *PasswordProtector) setupFileActions() {
    menu := p.MenuBar().AddMenu2("File")
    p.actionNew = menu.AddAction("New")
    p.actionNew.ConnectTriggered(func(checked bool) { p.fileOpen()})
    p.actionNew.SetShortcuts2(gui.QKeySequence__New)
    p.actionOpen = menu.AddAction("Open")
    p.actionOpen.ConnectTriggered(func(checked bool) { p.fileOpen()})
    p.actionOpen.SetShortcuts2(gui.QKeySequence__Open)
    p.actionSave = menu.AddAction("Save")
    p.actionSave.ConnectTriggered(func(checked bool) { p.foo()})
    p.actionSave.SetShortcuts2(gui.QKeySequence__Save)
    p.actionChangePassword = menu.AddAction("Change File Password")
    p.actionChangePassword.ConnectTriggered(func(checked bool) { p.foo()})
    p.actionQuit = menu.AddAction("Quit")
    p.actionQuit.ConnectTriggered(func(checked bool) { p.foo() })
    p.actionQuit.SetShortcuts2(gui.QKeySequence__Quit)
}

func (p *PasswordProtector) setupSecurityActions() {
    menu := p.MenuBar().AddMenu2("Security")
    p.actionEncryptFile = menu.AddAction("Encrypt File")
    p.actionEncryptFile.ConnectTriggered(func(checked bool) {p.foo()})
    p.actionDecryptFile = menu.AddAction("Decrypt File")
    p.actionDecryptFile.ConnectTriggered(func(checked bool) {p.foo()})
    p.actionGeneratePassword = menu.AddAction("Generate Password")
    p.actionGeneratePassword.ConnectTriggered(func(checked bool) {p.foo()})
}

func (p *PasswordProtector) setupHelpActions() {
    menu := p.MenuBar().AddMenu2("Help")
    p.actionHelp = menu.AddAction("Password Protector Help")
    p.actionHelp.ConnectTriggered(func(checked bool) {p.foo()})
    p.actionAbout = menu.AddAction("About")
    p.actionAbout.ConnectTriggered(func(checked bool) {p.about()})
}

func (p *PasswordProtector) setupLayout() {
    widget := widgets.NewQWidget(nil, 0)
    p.SetCentralWidget(widget)
    //p.layout = widgets.NewQVBoxLayout2(widget)
    p.layout = widgets.NewQGridLayout(widget)
    p.addButton = widgets.NewQPushButton2("Add New Record", nil)
    p.addButton.ConnectClicked(func(checked bool) {p.addAccount()})
    p.addButton.SetMinimumWidth(100)
    p.viewButton = widgets.NewQPushButton2("View Existing Record", nil)
    p.viewButton.SetMinimumWidth(100)
    p.editButton = widgets.NewQPushButton2("Edit Existing Record", nil)
    p.editButton.SetMinimumWidth(100)
    p.deleteButton = widgets.NewQPushButton2("Delete Record", nil)
    p.deleteButton.SetMinimumWidth(100)
    p.changeButton = widgets.NewQPushButton2("Change Record Password", nil)
    p.changeButton.SetMinimumWidth(100)
    p.listWidget = widgets.NewQListWidget(nil)
    p.layout.AddWidget3(p.listWidget, 1, 1, 5, 1, core.Qt__AlignJustify)
    p.layout.AddWidget(p.addButton, 1, 2, core.Qt__AlignCenter)
    p.layout.AddWidget(p.viewButton, 2, 2, core.Qt__AlignCenter)
    p.layout.AddWidget(p.editButton, 3, 2, core.Qt__AlignCenter)
    p.layout.AddWidget(p.deleteButton, 4, 2, core.Qt__AlignCenter)
    p.layout.AddWidget(p.changeButton, 5, 2, core.Qt__AlignCenter)
    //p.layout.AddWidget(p.addButton, 1, core.Qt__AlignLeft)
    //p.layout.AddWidget(p.scrollArea, 10, core.Qt__AlignLeft)
}

func (p *PasswordProtector) setupBackend() {
    p.records = map[string][]byte{}
}

func (p *PasswordProtector) foo() {
    widgets.QMessageBox_About(p, "About", "This is a test")
}

func (p *PasswordProtector) about() {
    widgets.QMessageBox_About(
        p,
        "About Password Protector",
        "This application encrypts passwords for management. " +
        "<a href='https://github.com/m-yuhas/password_protector'>click</a>",
    )
}

func (p *PasswordProtector) fileOpen() {
    fileDialog := widgets.NewQFileDialog2(p, "Open File...", "", "")
    //fileDialog.SetAcceptMode(widgets.QFileDialog__AcceptOpen)
    //fileDialog.SetFileMode(widgets.QFileDialog__ExistingFile)
    if fileDialog.Exec() != int(widgets.QDialog__Accepted) {
        return
    }
    p.fileName = fileDialog.SelectedFiles()[0]
}

func (p *PasswordProtector) addAccount() {
    p.entryWindow = initAccountEntryWindow(p)
    p.entryWindow.Show()
}

func (p *PasswordProtector) onAddAccount(recordName string, encryptedRecord []byte) {
    p.entryWindow.DestroyQMainWindow()
    p.records[recordName] = encryptedRecord
}

func initAccountEntryWindow(caller *PasswordProtector) *AccountEntryWindow {
    var this = NewAccountEntryWindow(nil, 0)
    widget := widgets.NewQWidget(nil, 0)
    this.SetCentralWidget(widget)
    this.layout = widgets.NewQFormLayout(widget)
    this.entryName = widgets.NewQLineEdit2("Account Name", nil)
    this.entryTable = widgets.NewQTableWidget2(3, 2, nil)
    this.addButton = widgets.NewQPushButton2("Add", nil)
    this.addButton.ConnectClicked(func(checked bool) {this.addAccount()})
    this.layout.AddWidget(this.entryName)
    this.layout.AddWidget(this.entryTable)
    this.layout.AddWidget(this.addButton)
    this.caller = caller
    return this
}

func (a *AccountEntryWindow) addAccount() {
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
    a.passwordWindow = initPasswordWindow(a)
}

func (a *AccountEntryWindow) onPasswordReturn(password []byte) {
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
    a.caller.onAddAccount(a.entryName.Text(), encryptedRecord)
}

func initPasswordWindow(caller *AccountEntryWindow) *PasswordEntryWindow {
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

func (p *PasswordEntryWindow) submit() {
    p.caller.onPasswordReturn([]byte(p.passwordEntry.Text()))
}
