package main

import (
    "github.com/therecipe/qt/gui"
	"github.com/therecipe/qt/widgets"
	"os"
)

func main() {
	app := widgets.NewQApplication(len(os.Args), os.Args)
	window := widgets.NewQMainWindow(nil, 0)
	window.SetWindowTitle("Password Protector")
	window.SetMinimumSize2(640, 480)
    menu := widgets.NewQMenuBar(window)
    fileMenu := widgets.NewQMenu2("File", menu)
    fileMenu.AddAction3("New", nil, "test", gui.NewQKeySequence5(gui.QKeySequence__New))
    fileMenu.AddAction("Open")
    fileMenu.AddAction("Save")
    fileMenu.AddAction("Change File Password")
    fileMenu.AddAction("Quit")
    passwordMenu := widgets.NewQMenu2("Password", menu)
    passwordMenu.AddAction("Generate Password")
    //helpMenu := widgets.NewQMenu2("Help", menu)
    //helpMenu.AddAction("Documentation")
    //helpMenu.AddAction("About")
    menu.AddMenu(fileMenu)
    menu.AddMenu(passwordMenu)
    //menu.AddMenu(helpMenu)
    window.SetMenuBar(menu)


	layout := widgets.NewQVBoxLayout()
	mainWidget := widgets.NewQWidget(nil, 0)
	mainWidget.SetLayout(layout)

	// Set main widget as the central widget of the window
	window.SetCentralWidget(mainWidget)
	window.Show()
	app.Exec()
}
