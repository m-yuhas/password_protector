package main

import (
    "github.com/therecipe/qt/core"
    "github.com/therecipe/qt/gui"
	"github.com/therecipe/qt/widgets"
	"os"
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
    fileName string
    records map[string][]byte{}
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
    return this
}

func (p *PasswordProtector) setupFileActions() {
    menu := p.MenuBar().AddMenu2("File")
    p.actionNew = menu.AddAction("New")
    p.actionNew.ConnectTriggered(func(checked bool) { p.foo()})
    p.actionNew.SetShortcuts2(gui.QKeySequence__New)
    p.actionOpen = menu.AddAction("Open")
    p.actionOpen.ConnectTriggered(func(checked bool) { p.foo()})
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
