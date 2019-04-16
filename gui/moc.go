package main

//#include <stdint.h>
//#include <stdlib.h>
//#include <string.h>
//#include "moc.h"
import "C"
import (
	"runtime"
	"unsafe"

	"github.com/therecipe/qt"
	std_core "github.com/therecipe/qt/core"
	std_gui "github.com/therecipe/qt/gui"
	std_widgets "github.com/therecipe/qt/widgets"
)

func cGoUnpackString(s C.struct_Moc_PackedString) string {
	if int(s.len) == -1 {
		return C.GoString(s.data)
	}
	return C.GoStringN(s.data, C.int(s.len))
}
func cGoUnpackBytes(s C.struct_Moc_PackedString) []byte {
	if int(s.len) == -1 {
		return []byte(C.GoString(s.data))
	}
	return C.GoBytes(unsafe.Pointer(s.data), C.int(s.len))
}

type PasswordProtector_ITF interface {
	std_widgets.QMainWindow_ITF
	PasswordProtector_PTR() *PasswordProtector
}

func (ptr *PasswordProtector) PasswordProtector_PTR() *PasswordProtector {
	return ptr
}

func (ptr *PasswordProtector) Pointer() unsafe.Pointer {
	if ptr != nil {
		return ptr.QMainWindow_PTR().Pointer()
	}
	return nil
}

func (ptr *PasswordProtector) SetPointer(p unsafe.Pointer) {
	if ptr != nil {
		ptr.QMainWindow_PTR().SetPointer(p)
	}
}

func PointerFromPasswordProtector(ptr PasswordProtector_ITF) unsafe.Pointer {
	if ptr != nil {
		return ptr.PasswordProtector_PTR().Pointer()
	}
	return nil
}

func NewPasswordProtectorFromPointer(ptr unsafe.Pointer) (n *PasswordProtector) {
	if gPtr, ok := qt.Receive(ptr); !ok {
		n = new(PasswordProtector)
		n.SetPointer(ptr)
	} else {
		switch deduced := gPtr.(type) {
		case *PasswordProtector:
			n = deduced

		case *std_widgets.QMainWindow:
			n = &PasswordProtector{QMainWindow: *deduced}

		default:
			n = new(PasswordProtector)
			n.SetPointer(ptr)
		}
	}
	return
}

//export callbackPasswordProtectorca847c_Constructor
func callbackPasswordProtectorca847c_Constructor(ptr unsafe.Pointer) {
	this := NewPasswordProtectorFromPointer(ptr)
	qt.Register(ptr, this)
}

func PasswordProtector_QRegisterMetaType() int {
	return int(int32(C.PasswordProtectorca847c_PasswordProtectorca847c_QRegisterMetaType()))
}

func (ptr *PasswordProtector) QRegisterMetaType() int {
	return int(int32(C.PasswordProtectorca847c_PasswordProtectorca847c_QRegisterMetaType()))
}

func PasswordProtector_QRegisterMetaType2(typeName string) int {
	var typeNameC *C.char
	if typeName != "" {
		typeNameC = C.CString(typeName)
		defer C.free(unsafe.Pointer(typeNameC))
	}
	return int(int32(C.PasswordProtectorca847c_PasswordProtectorca847c_QRegisterMetaType2(typeNameC)))
}

func (ptr *PasswordProtector) QRegisterMetaType2(typeName string) int {
	var typeNameC *C.char
	if typeName != "" {
		typeNameC = C.CString(typeName)
		defer C.free(unsafe.Pointer(typeNameC))
	}
	return int(int32(C.PasswordProtectorca847c_PasswordProtectorca847c_QRegisterMetaType2(typeNameC)))
}

func PasswordProtector_QmlRegisterType() int {
	return int(int32(C.PasswordProtectorca847c_PasswordProtectorca847c_QmlRegisterType()))
}

func (ptr *PasswordProtector) QmlRegisterType() int {
	return int(int32(C.PasswordProtectorca847c_PasswordProtectorca847c_QmlRegisterType()))
}

func PasswordProtector_QmlRegisterType2(uri string, versionMajor int, versionMinor int, qmlName string) int {
	var uriC *C.char
	if uri != "" {
		uriC = C.CString(uri)
		defer C.free(unsafe.Pointer(uriC))
	}
	var qmlNameC *C.char
	if qmlName != "" {
		qmlNameC = C.CString(qmlName)
		defer C.free(unsafe.Pointer(qmlNameC))
	}
	return int(int32(C.PasswordProtectorca847c_PasswordProtectorca847c_QmlRegisterType2(uriC, C.int(int32(versionMajor)), C.int(int32(versionMinor)), qmlNameC)))
}

func (ptr *PasswordProtector) QmlRegisterType2(uri string, versionMajor int, versionMinor int, qmlName string) int {
	var uriC *C.char
	if uri != "" {
		uriC = C.CString(uri)
		defer C.free(unsafe.Pointer(uriC))
	}
	var qmlNameC *C.char
	if qmlName != "" {
		qmlNameC = C.CString(qmlName)
		defer C.free(unsafe.Pointer(qmlNameC))
	}
	return int(int32(C.PasswordProtectorca847c_PasswordProtectorca847c_QmlRegisterType2(uriC, C.int(int32(versionMajor)), C.int(int32(versionMinor)), qmlNameC)))
}

func (ptr *PasswordProtector) __resizeDocks_docks_atList(i int) *std_widgets.QDockWidget {
	if ptr.Pointer() != nil {
		tmpValue := std_widgets.NewQDockWidgetFromPointer(C.PasswordProtectorca847c___resizeDocks_docks_atList(ptr.Pointer(), C.int(int32(i))))
		if !qt.ExistsSignal(tmpValue.Pointer(), "destroyed") {
			tmpValue.ConnectDestroyed(func(*std_core.QObject) { tmpValue.SetPointer(nil) })
		}
		return tmpValue
	}
	return nil
}

func (ptr *PasswordProtector) __resizeDocks_docks_setList(i std_widgets.QDockWidget_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c___resizeDocks_docks_setList(ptr.Pointer(), std_widgets.PointerFromQDockWidget(i))
	}
}

func (ptr *PasswordProtector) __resizeDocks_docks_newList() unsafe.Pointer {
	return C.PasswordProtectorca847c___resizeDocks_docks_newList(ptr.Pointer())
}

func (ptr *PasswordProtector) __resizeDocks_sizes_atList(i int) int {
	if ptr.Pointer() != nil {
		return int(int32(C.PasswordProtectorca847c___resizeDocks_sizes_atList(ptr.Pointer(), C.int(int32(i)))))
	}
	return 0
}

func (ptr *PasswordProtector) __resizeDocks_sizes_setList(i int) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c___resizeDocks_sizes_setList(ptr.Pointer(), C.int(int32(i)))
	}
}

func (ptr *PasswordProtector) __resizeDocks_sizes_newList() unsafe.Pointer {
	return C.PasswordProtectorca847c___resizeDocks_sizes_newList(ptr.Pointer())
}

func (ptr *PasswordProtector) __tabifiedDockWidgets_atList(i int) *std_widgets.QDockWidget {
	if ptr.Pointer() != nil {
		tmpValue := std_widgets.NewQDockWidgetFromPointer(C.PasswordProtectorca847c___tabifiedDockWidgets_atList(ptr.Pointer(), C.int(int32(i))))
		if !qt.ExistsSignal(tmpValue.Pointer(), "destroyed") {
			tmpValue.ConnectDestroyed(func(*std_core.QObject) { tmpValue.SetPointer(nil) })
		}
		return tmpValue
	}
	return nil
}

func (ptr *PasswordProtector) __tabifiedDockWidgets_setList(i std_widgets.QDockWidget_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c___tabifiedDockWidgets_setList(ptr.Pointer(), std_widgets.PointerFromQDockWidget(i))
	}
}

func (ptr *PasswordProtector) __tabifiedDockWidgets_newList() unsafe.Pointer {
	return C.PasswordProtectorca847c___tabifiedDockWidgets_newList(ptr.Pointer())
}

func (ptr *PasswordProtector) __addActions_actions_atList(i int) *std_widgets.QAction {
	if ptr.Pointer() != nil {
		tmpValue := std_widgets.NewQActionFromPointer(C.PasswordProtectorca847c___addActions_actions_atList(ptr.Pointer(), C.int(int32(i))))
		if !qt.ExistsSignal(tmpValue.Pointer(), "destroyed") {
			tmpValue.ConnectDestroyed(func(*std_core.QObject) { tmpValue.SetPointer(nil) })
		}
		return tmpValue
	}
	return nil
}

func (ptr *PasswordProtector) __addActions_actions_setList(i std_widgets.QAction_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c___addActions_actions_setList(ptr.Pointer(), std_widgets.PointerFromQAction(i))
	}
}

func (ptr *PasswordProtector) __addActions_actions_newList() unsafe.Pointer {
	return C.PasswordProtectorca847c___addActions_actions_newList(ptr.Pointer())
}

func (ptr *PasswordProtector) __insertActions_actions_atList(i int) *std_widgets.QAction {
	if ptr.Pointer() != nil {
		tmpValue := std_widgets.NewQActionFromPointer(C.PasswordProtectorca847c___insertActions_actions_atList(ptr.Pointer(), C.int(int32(i))))
		if !qt.ExistsSignal(tmpValue.Pointer(), "destroyed") {
			tmpValue.ConnectDestroyed(func(*std_core.QObject) { tmpValue.SetPointer(nil) })
		}
		return tmpValue
	}
	return nil
}

func (ptr *PasswordProtector) __insertActions_actions_setList(i std_widgets.QAction_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c___insertActions_actions_setList(ptr.Pointer(), std_widgets.PointerFromQAction(i))
	}
}

func (ptr *PasswordProtector) __insertActions_actions_newList() unsafe.Pointer {
	return C.PasswordProtectorca847c___insertActions_actions_newList(ptr.Pointer())
}

func (ptr *PasswordProtector) __actions_atList(i int) *std_widgets.QAction {
	if ptr.Pointer() != nil {
		tmpValue := std_widgets.NewQActionFromPointer(C.PasswordProtectorca847c___actions_atList(ptr.Pointer(), C.int(int32(i))))
		if !qt.ExistsSignal(tmpValue.Pointer(), "destroyed") {
			tmpValue.ConnectDestroyed(func(*std_core.QObject) { tmpValue.SetPointer(nil) })
		}
		return tmpValue
	}
	return nil
}

func (ptr *PasswordProtector) __actions_setList(i std_widgets.QAction_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c___actions_setList(ptr.Pointer(), std_widgets.PointerFromQAction(i))
	}
}

func (ptr *PasswordProtector) __actions_newList() unsafe.Pointer {
	return C.PasswordProtectorca847c___actions_newList(ptr.Pointer())
}

func (ptr *PasswordProtector) __dynamicPropertyNames_atList(i int) *std_core.QByteArray {
	if ptr.Pointer() != nil {
		tmpValue := std_core.NewQByteArrayFromPointer(C.PasswordProtectorca847c___dynamicPropertyNames_atList(ptr.Pointer(), C.int(int32(i))))
		runtime.SetFinalizer(tmpValue, (*std_core.QByteArray).DestroyQByteArray)
		return tmpValue
	}
	return nil
}

func (ptr *PasswordProtector) __dynamicPropertyNames_setList(i std_core.QByteArray_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c___dynamicPropertyNames_setList(ptr.Pointer(), std_core.PointerFromQByteArray(i))
	}
}

func (ptr *PasswordProtector) __dynamicPropertyNames_newList() unsafe.Pointer {
	return C.PasswordProtectorca847c___dynamicPropertyNames_newList(ptr.Pointer())
}

func (ptr *PasswordProtector) __findChildren_atList2(i int) *std_core.QObject {
	if ptr.Pointer() != nil {
		tmpValue := std_core.NewQObjectFromPointer(C.PasswordProtectorca847c___findChildren_atList2(ptr.Pointer(), C.int(int32(i))))
		if !qt.ExistsSignal(tmpValue.Pointer(), "destroyed") {
			tmpValue.ConnectDestroyed(func(*std_core.QObject) { tmpValue.SetPointer(nil) })
		}
		return tmpValue
	}
	return nil
}

func (ptr *PasswordProtector) __findChildren_setList2(i std_core.QObject_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c___findChildren_setList2(ptr.Pointer(), std_core.PointerFromQObject(i))
	}
}

func (ptr *PasswordProtector) __findChildren_newList2() unsafe.Pointer {
	return C.PasswordProtectorca847c___findChildren_newList2(ptr.Pointer())
}

func (ptr *PasswordProtector) __findChildren_atList3(i int) *std_core.QObject {
	if ptr.Pointer() != nil {
		tmpValue := std_core.NewQObjectFromPointer(C.PasswordProtectorca847c___findChildren_atList3(ptr.Pointer(), C.int(int32(i))))
		if !qt.ExistsSignal(tmpValue.Pointer(), "destroyed") {
			tmpValue.ConnectDestroyed(func(*std_core.QObject) { tmpValue.SetPointer(nil) })
		}
		return tmpValue
	}
	return nil
}

func (ptr *PasswordProtector) __findChildren_setList3(i std_core.QObject_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c___findChildren_setList3(ptr.Pointer(), std_core.PointerFromQObject(i))
	}
}

func (ptr *PasswordProtector) __findChildren_newList3() unsafe.Pointer {
	return C.PasswordProtectorca847c___findChildren_newList3(ptr.Pointer())
}

func (ptr *PasswordProtector) __findChildren_atList(i int) *std_core.QObject {
	if ptr.Pointer() != nil {
		tmpValue := std_core.NewQObjectFromPointer(C.PasswordProtectorca847c___findChildren_atList(ptr.Pointer(), C.int(int32(i))))
		if !qt.ExistsSignal(tmpValue.Pointer(), "destroyed") {
			tmpValue.ConnectDestroyed(func(*std_core.QObject) { tmpValue.SetPointer(nil) })
		}
		return tmpValue
	}
	return nil
}

func (ptr *PasswordProtector) __findChildren_setList(i std_core.QObject_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c___findChildren_setList(ptr.Pointer(), std_core.PointerFromQObject(i))
	}
}

func (ptr *PasswordProtector) __findChildren_newList() unsafe.Pointer {
	return C.PasswordProtectorca847c___findChildren_newList(ptr.Pointer())
}

func (ptr *PasswordProtector) __children_atList(i int) *std_core.QObject {
	if ptr.Pointer() != nil {
		tmpValue := std_core.NewQObjectFromPointer(C.PasswordProtectorca847c___children_atList(ptr.Pointer(), C.int(int32(i))))
		if !qt.ExistsSignal(tmpValue.Pointer(), "destroyed") {
			tmpValue.ConnectDestroyed(func(*std_core.QObject) { tmpValue.SetPointer(nil) })
		}
		return tmpValue
	}
	return nil
}

func (ptr *PasswordProtector) __children_setList(i std_core.QObject_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c___children_setList(ptr.Pointer(), std_core.PointerFromQObject(i))
	}
}

func (ptr *PasswordProtector) __children_newList() unsafe.Pointer {
	return C.PasswordProtectorca847c___children_newList(ptr.Pointer())
}

func NewPasswordProtector(parent std_widgets.QWidget_ITF, flags std_core.Qt__WindowType) *PasswordProtector {
	tmpValue := NewPasswordProtectorFromPointer(C.PasswordProtectorca847c_NewPasswordProtector(std_widgets.PointerFromQWidget(parent), C.longlong(flags)))
	if !qt.ExistsSignal(tmpValue.Pointer(), "destroyed") {
		tmpValue.ConnectDestroyed(func(*std_core.QObject) { tmpValue.SetPointer(nil) })
	}
	return tmpValue
}

//export callbackPasswordProtectorca847c_DestroyPasswordProtector
func callbackPasswordProtectorca847c_DestroyPasswordProtector(ptr unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "~PasswordProtector"); signal != nil {
		signal.(func())()
	} else {
		NewPasswordProtectorFromPointer(ptr).DestroyPasswordProtectorDefault()
	}
}

func (ptr *PasswordProtector) ConnectDestroyPasswordProtector(f func()) {
	if ptr.Pointer() != nil {

		if signal := qt.LendSignal(ptr.Pointer(), "~PasswordProtector"); signal != nil {
			qt.ConnectSignal(ptr.Pointer(), "~PasswordProtector", func() {
				signal.(func())()
				f()
			})
		} else {
			qt.ConnectSignal(ptr.Pointer(), "~PasswordProtector", f)
		}
	}
}

func (ptr *PasswordProtector) DisconnectDestroyPasswordProtector() {
	if ptr.Pointer() != nil {

		qt.DisconnectSignal(ptr.Pointer(), "~PasswordProtector")
	}
}

func (ptr *PasswordProtector) DestroyPasswordProtector() {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_DestroyPasswordProtector(ptr.Pointer())
		ptr.SetPointer(nil)
		runtime.SetFinalizer(ptr, nil)
	}
}

func (ptr *PasswordProtector) DestroyPasswordProtectorDefault() {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_DestroyPasswordProtectorDefault(ptr.Pointer())
		ptr.SetPointer(nil)
		runtime.SetFinalizer(ptr, nil)
	}
}

//export callbackPasswordProtectorca847c_CreatePopupMenu
func callbackPasswordProtectorca847c_CreatePopupMenu(ptr unsafe.Pointer) unsafe.Pointer {
	if signal := qt.GetSignal(ptr, "createPopupMenu"); signal != nil {
		return std_widgets.PointerFromQMenu(signal.(func() *std_widgets.QMenu)())
	}

	return std_widgets.PointerFromQMenu(NewPasswordProtectorFromPointer(ptr).CreatePopupMenuDefault())
}

func (ptr *PasswordProtector) CreatePopupMenuDefault() *std_widgets.QMenu {
	if ptr.Pointer() != nil {
		tmpValue := std_widgets.NewQMenuFromPointer(C.PasswordProtectorca847c_CreatePopupMenuDefault(ptr.Pointer()))
		if !qt.ExistsSignal(tmpValue.Pointer(), "destroyed") {
			tmpValue.ConnectDestroyed(func(*std_core.QObject) { tmpValue.SetPointer(nil) })
		}
		return tmpValue
	}
	return nil
}

//export callbackPasswordProtectorca847c_Event
func callbackPasswordProtectorca847c_Event(ptr unsafe.Pointer, event unsafe.Pointer) C.char {
	if signal := qt.GetSignal(ptr, "event"); signal != nil {
		return C.char(int8(qt.GoBoolToInt(signal.(func(*std_core.QEvent) bool)(std_core.NewQEventFromPointer(event)))))
	}

	return C.char(int8(qt.GoBoolToInt(NewPasswordProtectorFromPointer(ptr).EventDefault(std_core.NewQEventFromPointer(event)))))
}

func (ptr *PasswordProtector) EventDefault(event std_core.QEvent_ITF) bool {
	if ptr.Pointer() != nil {
		return int8(C.PasswordProtectorca847c_EventDefault(ptr.Pointer(), std_core.PointerFromQEvent(event))) != 0
	}
	return false
}

//export callbackPasswordProtectorca847c_ContextMenuEvent
func callbackPasswordProtectorca847c_ContextMenuEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "contextMenuEvent"); signal != nil {
		signal.(func(*std_gui.QContextMenuEvent))(std_gui.NewQContextMenuEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).ContextMenuEventDefault(std_gui.NewQContextMenuEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) ContextMenuEventDefault(event std_gui.QContextMenuEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_ContextMenuEventDefault(ptr.Pointer(), std_gui.PointerFromQContextMenuEvent(event))
	}
}

//export callbackPasswordProtectorca847c_IconSizeChanged
func callbackPasswordProtectorca847c_IconSizeChanged(ptr unsafe.Pointer, iconSize unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "iconSizeChanged"); signal != nil {
		signal.(func(*std_core.QSize))(std_core.NewQSizeFromPointer(iconSize))
	}

}

//export callbackPasswordProtectorca847c_SetAnimated
func callbackPasswordProtectorca847c_SetAnimated(ptr unsafe.Pointer, enabled C.char) {
	if signal := qt.GetSignal(ptr, "setAnimated"); signal != nil {
		signal.(func(bool))(int8(enabled) != 0)
	} else {
		NewPasswordProtectorFromPointer(ptr).SetAnimatedDefault(int8(enabled) != 0)
	}
}

func (ptr *PasswordProtector) SetAnimatedDefault(enabled bool) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_SetAnimatedDefault(ptr.Pointer(), C.char(int8(qt.GoBoolToInt(enabled))))
	}
}

//export callbackPasswordProtectorca847c_SetDockNestingEnabled
func callbackPasswordProtectorca847c_SetDockNestingEnabled(ptr unsafe.Pointer, enabled C.char) {
	if signal := qt.GetSignal(ptr, "setDockNestingEnabled"); signal != nil {
		signal.(func(bool))(int8(enabled) != 0)
	} else {
		NewPasswordProtectorFromPointer(ptr).SetDockNestingEnabledDefault(int8(enabled) != 0)
	}
}

func (ptr *PasswordProtector) SetDockNestingEnabledDefault(enabled bool) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_SetDockNestingEnabledDefault(ptr.Pointer(), C.char(int8(qt.GoBoolToInt(enabled))))
	}
}

//export callbackPasswordProtectorca847c_SetUnifiedTitleAndToolBarOnMac
func callbackPasswordProtectorca847c_SetUnifiedTitleAndToolBarOnMac(ptr unsafe.Pointer, set C.char) {
	if signal := qt.GetSignal(ptr, "setUnifiedTitleAndToolBarOnMac"); signal != nil {
		signal.(func(bool))(int8(set) != 0)
	} else {
		NewPasswordProtectorFromPointer(ptr).SetUnifiedTitleAndToolBarOnMacDefault(int8(set) != 0)
	}
}

func (ptr *PasswordProtector) SetUnifiedTitleAndToolBarOnMacDefault(set bool) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_SetUnifiedTitleAndToolBarOnMacDefault(ptr.Pointer(), C.char(int8(qt.GoBoolToInt(set))))
	}
}

//export callbackPasswordProtectorca847c_TabifiedDockWidgetActivated
func callbackPasswordProtectorca847c_TabifiedDockWidgetActivated(ptr unsafe.Pointer, dockWidget unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "tabifiedDockWidgetActivated"); signal != nil {
		signal.(func(*std_widgets.QDockWidget))(std_widgets.NewQDockWidgetFromPointer(dockWidget))
	}

}

//export callbackPasswordProtectorca847c_ToolButtonStyleChanged
func callbackPasswordProtectorca847c_ToolButtonStyleChanged(ptr unsafe.Pointer, toolButtonStyle C.longlong) {
	if signal := qt.GetSignal(ptr, "toolButtonStyleChanged"); signal != nil {
		signal.(func(std_core.Qt__ToolButtonStyle))(std_core.Qt__ToolButtonStyle(toolButtonStyle))
	}

}

//export callbackPasswordProtectorca847c_Close
func callbackPasswordProtectorca847c_Close(ptr unsafe.Pointer) C.char {
	if signal := qt.GetSignal(ptr, "close"); signal != nil {
		return C.char(int8(qt.GoBoolToInt(signal.(func() bool)())))
	}

	return C.char(int8(qt.GoBoolToInt(NewPasswordProtectorFromPointer(ptr).CloseDefault())))
}

func (ptr *PasswordProtector) CloseDefault() bool {
	if ptr.Pointer() != nil {
		return int8(C.PasswordProtectorca847c_CloseDefault(ptr.Pointer())) != 0
	}
	return false
}

//export callbackPasswordProtectorca847c_FocusNextPrevChild
func callbackPasswordProtectorca847c_FocusNextPrevChild(ptr unsafe.Pointer, next C.char) C.char {
	if signal := qt.GetSignal(ptr, "focusNextPrevChild"); signal != nil {
		return C.char(int8(qt.GoBoolToInt(signal.(func(bool) bool)(int8(next) != 0))))
	}

	return C.char(int8(qt.GoBoolToInt(NewPasswordProtectorFromPointer(ptr).FocusNextPrevChildDefault(int8(next) != 0))))
}

func (ptr *PasswordProtector) FocusNextPrevChildDefault(next bool) bool {
	if ptr.Pointer() != nil {
		return int8(C.PasswordProtectorca847c_FocusNextPrevChildDefault(ptr.Pointer(), C.char(int8(qt.GoBoolToInt(next))))) != 0
	}
	return false
}

//export callbackPasswordProtectorca847c_NativeEvent
func callbackPasswordProtectorca847c_NativeEvent(ptr unsafe.Pointer, eventType unsafe.Pointer, message unsafe.Pointer, result C.long) C.char {
	if signal := qt.GetSignal(ptr, "nativeEvent"); signal != nil {
		return C.char(int8(qt.GoBoolToInt(signal.(func(*std_core.QByteArray, unsafe.Pointer, int) bool)(std_core.NewQByteArrayFromPointer(eventType), message, int(int32(result))))))
	}

	return C.char(int8(qt.GoBoolToInt(NewPasswordProtectorFromPointer(ptr).NativeEventDefault(std_core.NewQByteArrayFromPointer(eventType), message, int(int32(result))))))
}

func (ptr *PasswordProtector) NativeEventDefault(eventType std_core.QByteArray_ITF, message unsafe.Pointer, result int) bool {
	if ptr.Pointer() != nil {
		return int8(C.PasswordProtectorca847c_NativeEventDefault(ptr.Pointer(), std_core.PointerFromQByteArray(eventType), message, C.long(int32(result)))) != 0
	}
	return false
}

//export callbackPasswordProtectorca847c_ActionEvent
func callbackPasswordProtectorca847c_ActionEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "actionEvent"); signal != nil {
		signal.(func(*std_gui.QActionEvent))(std_gui.NewQActionEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).ActionEventDefault(std_gui.NewQActionEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) ActionEventDefault(event std_gui.QActionEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_ActionEventDefault(ptr.Pointer(), std_gui.PointerFromQActionEvent(event))
	}
}

//export callbackPasswordProtectorca847c_ChangeEvent
func callbackPasswordProtectorca847c_ChangeEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "changeEvent"); signal != nil {
		signal.(func(*std_core.QEvent))(std_core.NewQEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).ChangeEventDefault(std_core.NewQEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) ChangeEventDefault(event std_core.QEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_ChangeEventDefault(ptr.Pointer(), std_core.PointerFromQEvent(event))
	}
}

//export callbackPasswordProtectorca847c_CloseEvent
func callbackPasswordProtectorca847c_CloseEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "closeEvent"); signal != nil {
		signal.(func(*std_gui.QCloseEvent))(std_gui.NewQCloseEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).CloseEventDefault(std_gui.NewQCloseEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) CloseEventDefault(event std_gui.QCloseEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_CloseEventDefault(ptr.Pointer(), std_gui.PointerFromQCloseEvent(event))
	}
}

//export callbackPasswordProtectorca847c_CustomContextMenuRequested
func callbackPasswordProtectorca847c_CustomContextMenuRequested(ptr unsafe.Pointer, pos unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "customContextMenuRequested"); signal != nil {
		signal.(func(*std_core.QPoint))(std_core.NewQPointFromPointer(pos))
	}

}

//export callbackPasswordProtectorca847c_DragEnterEvent
func callbackPasswordProtectorca847c_DragEnterEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "dragEnterEvent"); signal != nil {
		signal.(func(*std_gui.QDragEnterEvent))(std_gui.NewQDragEnterEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).DragEnterEventDefault(std_gui.NewQDragEnterEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) DragEnterEventDefault(event std_gui.QDragEnterEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_DragEnterEventDefault(ptr.Pointer(), std_gui.PointerFromQDragEnterEvent(event))
	}
}

//export callbackPasswordProtectorca847c_DragLeaveEvent
func callbackPasswordProtectorca847c_DragLeaveEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "dragLeaveEvent"); signal != nil {
		signal.(func(*std_gui.QDragLeaveEvent))(std_gui.NewQDragLeaveEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).DragLeaveEventDefault(std_gui.NewQDragLeaveEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) DragLeaveEventDefault(event std_gui.QDragLeaveEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_DragLeaveEventDefault(ptr.Pointer(), std_gui.PointerFromQDragLeaveEvent(event))
	}
}

//export callbackPasswordProtectorca847c_DragMoveEvent
func callbackPasswordProtectorca847c_DragMoveEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "dragMoveEvent"); signal != nil {
		signal.(func(*std_gui.QDragMoveEvent))(std_gui.NewQDragMoveEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).DragMoveEventDefault(std_gui.NewQDragMoveEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) DragMoveEventDefault(event std_gui.QDragMoveEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_DragMoveEventDefault(ptr.Pointer(), std_gui.PointerFromQDragMoveEvent(event))
	}
}

//export callbackPasswordProtectorca847c_DropEvent
func callbackPasswordProtectorca847c_DropEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "dropEvent"); signal != nil {
		signal.(func(*std_gui.QDropEvent))(std_gui.NewQDropEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).DropEventDefault(std_gui.NewQDropEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) DropEventDefault(event std_gui.QDropEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_DropEventDefault(ptr.Pointer(), std_gui.PointerFromQDropEvent(event))
	}
}

//export callbackPasswordProtectorca847c_EnterEvent
func callbackPasswordProtectorca847c_EnterEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "enterEvent"); signal != nil {
		signal.(func(*std_core.QEvent))(std_core.NewQEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).EnterEventDefault(std_core.NewQEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) EnterEventDefault(event std_core.QEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_EnterEventDefault(ptr.Pointer(), std_core.PointerFromQEvent(event))
	}
}

//export callbackPasswordProtectorca847c_FocusInEvent
func callbackPasswordProtectorca847c_FocusInEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "focusInEvent"); signal != nil {
		signal.(func(*std_gui.QFocusEvent))(std_gui.NewQFocusEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).FocusInEventDefault(std_gui.NewQFocusEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) FocusInEventDefault(event std_gui.QFocusEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_FocusInEventDefault(ptr.Pointer(), std_gui.PointerFromQFocusEvent(event))
	}
}

//export callbackPasswordProtectorca847c_FocusOutEvent
func callbackPasswordProtectorca847c_FocusOutEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "focusOutEvent"); signal != nil {
		signal.(func(*std_gui.QFocusEvent))(std_gui.NewQFocusEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).FocusOutEventDefault(std_gui.NewQFocusEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) FocusOutEventDefault(event std_gui.QFocusEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_FocusOutEventDefault(ptr.Pointer(), std_gui.PointerFromQFocusEvent(event))
	}
}

//export callbackPasswordProtectorca847c_Hide
func callbackPasswordProtectorca847c_Hide(ptr unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "hide"); signal != nil {
		signal.(func())()
	} else {
		NewPasswordProtectorFromPointer(ptr).HideDefault()
	}
}

func (ptr *PasswordProtector) HideDefault() {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_HideDefault(ptr.Pointer())
	}
}

//export callbackPasswordProtectorca847c_HideEvent
func callbackPasswordProtectorca847c_HideEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "hideEvent"); signal != nil {
		signal.(func(*std_gui.QHideEvent))(std_gui.NewQHideEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).HideEventDefault(std_gui.NewQHideEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) HideEventDefault(event std_gui.QHideEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_HideEventDefault(ptr.Pointer(), std_gui.PointerFromQHideEvent(event))
	}
}

//export callbackPasswordProtectorca847c_InputMethodEvent
func callbackPasswordProtectorca847c_InputMethodEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "inputMethodEvent"); signal != nil {
		signal.(func(*std_gui.QInputMethodEvent))(std_gui.NewQInputMethodEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).InputMethodEventDefault(std_gui.NewQInputMethodEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) InputMethodEventDefault(event std_gui.QInputMethodEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_InputMethodEventDefault(ptr.Pointer(), std_gui.PointerFromQInputMethodEvent(event))
	}
}

//export callbackPasswordProtectorca847c_KeyPressEvent
func callbackPasswordProtectorca847c_KeyPressEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "keyPressEvent"); signal != nil {
		signal.(func(*std_gui.QKeyEvent))(std_gui.NewQKeyEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).KeyPressEventDefault(std_gui.NewQKeyEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) KeyPressEventDefault(event std_gui.QKeyEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_KeyPressEventDefault(ptr.Pointer(), std_gui.PointerFromQKeyEvent(event))
	}
}

//export callbackPasswordProtectorca847c_KeyReleaseEvent
func callbackPasswordProtectorca847c_KeyReleaseEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "keyReleaseEvent"); signal != nil {
		signal.(func(*std_gui.QKeyEvent))(std_gui.NewQKeyEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).KeyReleaseEventDefault(std_gui.NewQKeyEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) KeyReleaseEventDefault(event std_gui.QKeyEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_KeyReleaseEventDefault(ptr.Pointer(), std_gui.PointerFromQKeyEvent(event))
	}
}

//export callbackPasswordProtectorca847c_LeaveEvent
func callbackPasswordProtectorca847c_LeaveEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "leaveEvent"); signal != nil {
		signal.(func(*std_core.QEvent))(std_core.NewQEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).LeaveEventDefault(std_core.NewQEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) LeaveEventDefault(event std_core.QEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_LeaveEventDefault(ptr.Pointer(), std_core.PointerFromQEvent(event))
	}
}

//export callbackPasswordProtectorca847c_Lower
func callbackPasswordProtectorca847c_Lower(ptr unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "lower"); signal != nil {
		signal.(func())()
	} else {
		NewPasswordProtectorFromPointer(ptr).LowerDefault()
	}
}

func (ptr *PasswordProtector) LowerDefault() {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_LowerDefault(ptr.Pointer())
	}
}

//export callbackPasswordProtectorca847c_MouseDoubleClickEvent
func callbackPasswordProtectorca847c_MouseDoubleClickEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "mouseDoubleClickEvent"); signal != nil {
		signal.(func(*std_gui.QMouseEvent))(std_gui.NewQMouseEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).MouseDoubleClickEventDefault(std_gui.NewQMouseEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) MouseDoubleClickEventDefault(event std_gui.QMouseEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_MouseDoubleClickEventDefault(ptr.Pointer(), std_gui.PointerFromQMouseEvent(event))
	}
}

//export callbackPasswordProtectorca847c_MouseMoveEvent
func callbackPasswordProtectorca847c_MouseMoveEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "mouseMoveEvent"); signal != nil {
		signal.(func(*std_gui.QMouseEvent))(std_gui.NewQMouseEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).MouseMoveEventDefault(std_gui.NewQMouseEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) MouseMoveEventDefault(event std_gui.QMouseEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_MouseMoveEventDefault(ptr.Pointer(), std_gui.PointerFromQMouseEvent(event))
	}
}

//export callbackPasswordProtectorca847c_MousePressEvent
func callbackPasswordProtectorca847c_MousePressEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "mousePressEvent"); signal != nil {
		signal.(func(*std_gui.QMouseEvent))(std_gui.NewQMouseEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).MousePressEventDefault(std_gui.NewQMouseEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) MousePressEventDefault(event std_gui.QMouseEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_MousePressEventDefault(ptr.Pointer(), std_gui.PointerFromQMouseEvent(event))
	}
}

//export callbackPasswordProtectorca847c_MouseReleaseEvent
func callbackPasswordProtectorca847c_MouseReleaseEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "mouseReleaseEvent"); signal != nil {
		signal.(func(*std_gui.QMouseEvent))(std_gui.NewQMouseEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).MouseReleaseEventDefault(std_gui.NewQMouseEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) MouseReleaseEventDefault(event std_gui.QMouseEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_MouseReleaseEventDefault(ptr.Pointer(), std_gui.PointerFromQMouseEvent(event))
	}
}

//export callbackPasswordProtectorca847c_MoveEvent
func callbackPasswordProtectorca847c_MoveEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "moveEvent"); signal != nil {
		signal.(func(*std_gui.QMoveEvent))(std_gui.NewQMoveEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).MoveEventDefault(std_gui.NewQMoveEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) MoveEventDefault(event std_gui.QMoveEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_MoveEventDefault(ptr.Pointer(), std_gui.PointerFromQMoveEvent(event))
	}
}

//export callbackPasswordProtectorca847c_PaintEvent
func callbackPasswordProtectorca847c_PaintEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "paintEvent"); signal != nil {
		signal.(func(*std_gui.QPaintEvent))(std_gui.NewQPaintEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).PaintEventDefault(std_gui.NewQPaintEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) PaintEventDefault(event std_gui.QPaintEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_PaintEventDefault(ptr.Pointer(), std_gui.PointerFromQPaintEvent(event))
	}
}

//export callbackPasswordProtectorca847c_Raise
func callbackPasswordProtectorca847c_Raise(ptr unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "raise"); signal != nil {
		signal.(func())()
	} else {
		NewPasswordProtectorFromPointer(ptr).RaiseDefault()
	}
}

func (ptr *PasswordProtector) RaiseDefault() {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_RaiseDefault(ptr.Pointer())
	}
}

//export callbackPasswordProtectorca847c_Repaint
func callbackPasswordProtectorca847c_Repaint(ptr unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "repaint"); signal != nil {
		signal.(func())()
	} else {
		NewPasswordProtectorFromPointer(ptr).RepaintDefault()
	}
}

func (ptr *PasswordProtector) RepaintDefault() {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_RepaintDefault(ptr.Pointer())
	}
}

//export callbackPasswordProtectorca847c_ResizeEvent
func callbackPasswordProtectorca847c_ResizeEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "resizeEvent"); signal != nil {
		signal.(func(*std_gui.QResizeEvent))(std_gui.NewQResizeEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).ResizeEventDefault(std_gui.NewQResizeEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) ResizeEventDefault(event std_gui.QResizeEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_ResizeEventDefault(ptr.Pointer(), std_gui.PointerFromQResizeEvent(event))
	}
}

//export callbackPasswordProtectorca847c_SetDisabled
func callbackPasswordProtectorca847c_SetDisabled(ptr unsafe.Pointer, disable C.char) {
	if signal := qt.GetSignal(ptr, "setDisabled"); signal != nil {
		signal.(func(bool))(int8(disable) != 0)
	} else {
		NewPasswordProtectorFromPointer(ptr).SetDisabledDefault(int8(disable) != 0)
	}
}

func (ptr *PasswordProtector) SetDisabledDefault(disable bool) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_SetDisabledDefault(ptr.Pointer(), C.char(int8(qt.GoBoolToInt(disable))))
	}
}

//export callbackPasswordProtectorca847c_SetEnabled
func callbackPasswordProtectorca847c_SetEnabled(ptr unsafe.Pointer, vbo C.char) {
	if signal := qt.GetSignal(ptr, "setEnabled"); signal != nil {
		signal.(func(bool))(int8(vbo) != 0)
	} else {
		NewPasswordProtectorFromPointer(ptr).SetEnabledDefault(int8(vbo) != 0)
	}
}

func (ptr *PasswordProtector) SetEnabledDefault(vbo bool) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_SetEnabledDefault(ptr.Pointer(), C.char(int8(qt.GoBoolToInt(vbo))))
	}
}

//export callbackPasswordProtectorca847c_SetFocus2
func callbackPasswordProtectorca847c_SetFocus2(ptr unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "setFocus2"); signal != nil {
		signal.(func())()
	} else {
		NewPasswordProtectorFromPointer(ptr).SetFocus2Default()
	}
}

func (ptr *PasswordProtector) SetFocus2Default() {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_SetFocus2Default(ptr.Pointer())
	}
}

//export callbackPasswordProtectorca847c_SetHidden
func callbackPasswordProtectorca847c_SetHidden(ptr unsafe.Pointer, hidden C.char) {
	if signal := qt.GetSignal(ptr, "setHidden"); signal != nil {
		signal.(func(bool))(int8(hidden) != 0)
	} else {
		NewPasswordProtectorFromPointer(ptr).SetHiddenDefault(int8(hidden) != 0)
	}
}

func (ptr *PasswordProtector) SetHiddenDefault(hidden bool) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_SetHiddenDefault(ptr.Pointer(), C.char(int8(qt.GoBoolToInt(hidden))))
	}
}

//export callbackPasswordProtectorca847c_SetStyleSheet
func callbackPasswordProtectorca847c_SetStyleSheet(ptr unsafe.Pointer, styleSheet C.struct_Moc_PackedString) {
	if signal := qt.GetSignal(ptr, "setStyleSheet"); signal != nil {
		signal.(func(string))(cGoUnpackString(styleSheet))
	} else {
		NewPasswordProtectorFromPointer(ptr).SetStyleSheetDefault(cGoUnpackString(styleSheet))
	}
}

func (ptr *PasswordProtector) SetStyleSheetDefault(styleSheet string) {
	if ptr.Pointer() != nil {
		var styleSheetC *C.char
		if styleSheet != "" {
			styleSheetC = C.CString(styleSheet)
			defer C.free(unsafe.Pointer(styleSheetC))
		}
		C.PasswordProtectorca847c_SetStyleSheetDefault(ptr.Pointer(), C.struct_Moc_PackedString{data: styleSheetC, len: C.longlong(len(styleSheet))})
	}
}

//export callbackPasswordProtectorca847c_SetVisible
func callbackPasswordProtectorca847c_SetVisible(ptr unsafe.Pointer, visible C.char) {
	if signal := qt.GetSignal(ptr, "setVisible"); signal != nil {
		signal.(func(bool))(int8(visible) != 0)
	} else {
		NewPasswordProtectorFromPointer(ptr).SetVisibleDefault(int8(visible) != 0)
	}
}

func (ptr *PasswordProtector) SetVisibleDefault(visible bool) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_SetVisibleDefault(ptr.Pointer(), C.char(int8(qt.GoBoolToInt(visible))))
	}
}

//export callbackPasswordProtectorca847c_SetWindowModified
func callbackPasswordProtectorca847c_SetWindowModified(ptr unsafe.Pointer, vbo C.char) {
	if signal := qt.GetSignal(ptr, "setWindowModified"); signal != nil {
		signal.(func(bool))(int8(vbo) != 0)
	} else {
		NewPasswordProtectorFromPointer(ptr).SetWindowModifiedDefault(int8(vbo) != 0)
	}
}

func (ptr *PasswordProtector) SetWindowModifiedDefault(vbo bool) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_SetWindowModifiedDefault(ptr.Pointer(), C.char(int8(qt.GoBoolToInt(vbo))))
	}
}

//export callbackPasswordProtectorca847c_SetWindowTitle
func callbackPasswordProtectorca847c_SetWindowTitle(ptr unsafe.Pointer, vqs C.struct_Moc_PackedString) {
	if signal := qt.GetSignal(ptr, "setWindowTitle"); signal != nil {
		signal.(func(string))(cGoUnpackString(vqs))
	} else {
		NewPasswordProtectorFromPointer(ptr).SetWindowTitleDefault(cGoUnpackString(vqs))
	}
}

func (ptr *PasswordProtector) SetWindowTitleDefault(vqs string) {
	if ptr.Pointer() != nil {
		var vqsC *C.char
		if vqs != "" {
			vqsC = C.CString(vqs)
			defer C.free(unsafe.Pointer(vqsC))
		}
		C.PasswordProtectorca847c_SetWindowTitleDefault(ptr.Pointer(), C.struct_Moc_PackedString{data: vqsC, len: C.longlong(len(vqs))})
	}
}

//export callbackPasswordProtectorca847c_Show
func callbackPasswordProtectorca847c_Show(ptr unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "show"); signal != nil {
		signal.(func())()
	} else {
		NewPasswordProtectorFromPointer(ptr).ShowDefault()
	}
}

func (ptr *PasswordProtector) ShowDefault() {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_ShowDefault(ptr.Pointer())
	}
}

//export callbackPasswordProtectorca847c_ShowEvent
func callbackPasswordProtectorca847c_ShowEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "showEvent"); signal != nil {
		signal.(func(*std_gui.QShowEvent))(std_gui.NewQShowEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).ShowEventDefault(std_gui.NewQShowEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) ShowEventDefault(event std_gui.QShowEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_ShowEventDefault(ptr.Pointer(), std_gui.PointerFromQShowEvent(event))
	}
}

//export callbackPasswordProtectorca847c_ShowFullScreen
func callbackPasswordProtectorca847c_ShowFullScreen(ptr unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "showFullScreen"); signal != nil {
		signal.(func())()
	} else {
		NewPasswordProtectorFromPointer(ptr).ShowFullScreenDefault()
	}
}

func (ptr *PasswordProtector) ShowFullScreenDefault() {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_ShowFullScreenDefault(ptr.Pointer())
	}
}

//export callbackPasswordProtectorca847c_ShowMaximized
func callbackPasswordProtectorca847c_ShowMaximized(ptr unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "showMaximized"); signal != nil {
		signal.(func())()
	} else {
		NewPasswordProtectorFromPointer(ptr).ShowMaximizedDefault()
	}
}

func (ptr *PasswordProtector) ShowMaximizedDefault() {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_ShowMaximizedDefault(ptr.Pointer())
	}
}

//export callbackPasswordProtectorca847c_ShowMinimized
func callbackPasswordProtectorca847c_ShowMinimized(ptr unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "showMinimized"); signal != nil {
		signal.(func())()
	} else {
		NewPasswordProtectorFromPointer(ptr).ShowMinimizedDefault()
	}
}

func (ptr *PasswordProtector) ShowMinimizedDefault() {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_ShowMinimizedDefault(ptr.Pointer())
	}
}

//export callbackPasswordProtectorca847c_ShowNormal
func callbackPasswordProtectorca847c_ShowNormal(ptr unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "showNormal"); signal != nil {
		signal.(func())()
	} else {
		NewPasswordProtectorFromPointer(ptr).ShowNormalDefault()
	}
}

func (ptr *PasswordProtector) ShowNormalDefault() {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_ShowNormalDefault(ptr.Pointer())
	}
}

//export callbackPasswordProtectorca847c_TabletEvent
func callbackPasswordProtectorca847c_TabletEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "tabletEvent"); signal != nil {
		signal.(func(*std_gui.QTabletEvent))(std_gui.NewQTabletEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).TabletEventDefault(std_gui.NewQTabletEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) TabletEventDefault(event std_gui.QTabletEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_TabletEventDefault(ptr.Pointer(), std_gui.PointerFromQTabletEvent(event))
	}
}

//export callbackPasswordProtectorca847c_Update
func callbackPasswordProtectorca847c_Update(ptr unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "update"); signal != nil {
		signal.(func())()
	} else {
		NewPasswordProtectorFromPointer(ptr).UpdateDefault()
	}
}

func (ptr *PasswordProtector) UpdateDefault() {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_UpdateDefault(ptr.Pointer())
	}
}

//export callbackPasswordProtectorca847c_UpdateMicroFocus
func callbackPasswordProtectorca847c_UpdateMicroFocus(ptr unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "updateMicroFocus"); signal != nil {
		signal.(func())()
	} else {
		NewPasswordProtectorFromPointer(ptr).UpdateMicroFocusDefault()
	}
}

func (ptr *PasswordProtector) UpdateMicroFocusDefault() {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_UpdateMicroFocusDefault(ptr.Pointer())
	}
}

//export callbackPasswordProtectorca847c_WheelEvent
func callbackPasswordProtectorca847c_WheelEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "wheelEvent"); signal != nil {
		signal.(func(*std_gui.QWheelEvent))(std_gui.NewQWheelEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).WheelEventDefault(std_gui.NewQWheelEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) WheelEventDefault(event std_gui.QWheelEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_WheelEventDefault(ptr.Pointer(), std_gui.PointerFromQWheelEvent(event))
	}
}

//export callbackPasswordProtectorca847c_WindowIconChanged
func callbackPasswordProtectorca847c_WindowIconChanged(ptr unsafe.Pointer, icon unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "windowIconChanged"); signal != nil {
		signal.(func(*std_gui.QIcon))(std_gui.NewQIconFromPointer(icon))
	}

}

//export callbackPasswordProtectorca847c_WindowTitleChanged
func callbackPasswordProtectorca847c_WindowTitleChanged(ptr unsafe.Pointer, title C.struct_Moc_PackedString) {
	if signal := qt.GetSignal(ptr, "windowTitleChanged"); signal != nil {
		signal.(func(string))(cGoUnpackString(title))
	}

}

//export callbackPasswordProtectorca847c_PaintEngine
func callbackPasswordProtectorca847c_PaintEngine(ptr unsafe.Pointer) unsafe.Pointer {
	if signal := qt.GetSignal(ptr, "paintEngine"); signal != nil {
		return std_gui.PointerFromQPaintEngine(signal.(func() *std_gui.QPaintEngine)())
	}

	return std_gui.PointerFromQPaintEngine(NewPasswordProtectorFromPointer(ptr).PaintEngineDefault())
}

func (ptr *PasswordProtector) PaintEngineDefault() *std_gui.QPaintEngine {
	if ptr.Pointer() != nil {
		return std_gui.NewQPaintEngineFromPointer(C.PasswordProtectorca847c_PaintEngineDefault(ptr.Pointer()))
	}
	return nil
}

//export callbackPasswordProtectorca847c_MinimumSizeHint
func callbackPasswordProtectorca847c_MinimumSizeHint(ptr unsafe.Pointer) unsafe.Pointer {
	if signal := qt.GetSignal(ptr, "minimumSizeHint"); signal != nil {
		return std_core.PointerFromQSize(signal.(func() *std_core.QSize)())
	}

	return std_core.PointerFromQSize(NewPasswordProtectorFromPointer(ptr).MinimumSizeHintDefault())
}

func (ptr *PasswordProtector) MinimumSizeHintDefault() *std_core.QSize {
	if ptr.Pointer() != nil {
		tmpValue := std_core.NewQSizeFromPointer(C.PasswordProtectorca847c_MinimumSizeHintDefault(ptr.Pointer()))
		runtime.SetFinalizer(tmpValue, (*std_core.QSize).DestroyQSize)
		return tmpValue
	}
	return nil
}

//export callbackPasswordProtectorca847c_SizeHint
func callbackPasswordProtectorca847c_SizeHint(ptr unsafe.Pointer) unsafe.Pointer {
	if signal := qt.GetSignal(ptr, "sizeHint"); signal != nil {
		return std_core.PointerFromQSize(signal.(func() *std_core.QSize)())
	}

	return std_core.PointerFromQSize(NewPasswordProtectorFromPointer(ptr).SizeHintDefault())
}

func (ptr *PasswordProtector) SizeHintDefault() *std_core.QSize {
	if ptr.Pointer() != nil {
		tmpValue := std_core.NewQSizeFromPointer(C.PasswordProtectorca847c_SizeHintDefault(ptr.Pointer()))
		runtime.SetFinalizer(tmpValue, (*std_core.QSize).DestroyQSize)
		return tmpValue
	}
	return nil
}

//export callbackPasswordProtectorca847c_InputMethodQuery
func callbackPasswordProtectorca847c_InputMethodQuery(ptr unsafe.Pointer, query C.longlong) unsafe.Pointer {
	if signal := qt.GetSignal(ptr, "inputMethodQuery"); signal != nil {
		return std_core.PointerFromQVariant(signal.(func(std_core.Qt__InputMethodQuery) *std_core.QVariant)(std_core.Qt__InputMethodQuery(query)))
	}

	return std_core.PointerFromQVariant(NewPasswordProtectorFromPointer(ptr).InputMethodQueryDefault(std_core.Qt__InputMethodQuery(query)))
}

func (ptr *PasswordProtector) InputMethodQueryDefault(query std_core.Qt__InputMethodQuery) *std_core.QVariant {
	if ptr.Pointer() != nil {
		tmpValue := std_core.NewQVariantFromPointer(C.PasswordProtectorca847c_InputMethodQueryDefault(ptr.Pointer(), C.longlong(query)))
		runtime.SetFinalizer(tmpValue, (*std_core.QVariant).DestroyQVariant)
		return tmpValue
	}
	return nil
}

//export callbackPasswordProtectorca847c_HasHeightForWidth
func callbackPasswordProtectorca847c_HasHeightForWidth(ptr unsafe.Pointer) C.char {
	if signal := qt.GetSignal(ptr, "hasHeightForWidth"); signal != nil {
		return C.char(int8(qt.GoBoolToInt(signal.(func() bool)())))
	}

	return C.char(int8(qt.GoBoolToInt(NewPasswordProtectorFromPointer(ptr).HasHeightForWidthDefault())))
}

func (ptr *PasswordProtector) HasHeightForWidthDefault() bool {
	if ptr.Pointer() != nil {
		return int8(C.PasswordProtectorca847c_HasHeightForWidthDefault(ptr.Pointer())) != 0
	}
	return false
}

//export callbackPasswordProtectorca847c_HeightForWidth
func callbackPasswordProtectorca847c_HeightForWidth(ptr unsafe.Pointer, w C.int) C.int {
	if signal := qt.GetSignal(ptr, "heightForWidth"); signal != nil {
		return C.int(int32(signal.(func(int) int)(int(int32(w)))))
	}

	return C.int(int32(NewPasswordProtectorFromPointer(ptr).HeightForWidthDefault(int(int32(w)))))
}

func (ptr *PasswordProtector) HeightForWidthDefault(w int) int {
	if ptr.Pointer() != nil {
		return int(int32(C.PasswordProtectorca847c_HeightForWidthDefault(ptr.Pointer(), C.int(int32(w)))))
	}
	return 0
}

//export callbackPasswordProtectorca847c_Metric
func callbackPasswordProtectorca847c_Metric(ptr unsafe.Pointer, m C.longlong) C.int {
	if signal := qt.GetSignal(ptr, "metric"); signal != nil {
		return C.int(int32(signal.(func(std_gui.QPaintDevice__PaintDeviceMetric) int)(std_gui.QPaintDevice__PaintDeviceMetric(m))))
	}

	return C.int(int32(NewPasswordProtectorFromPointer(ptr).MetricDefault(std_gui.QPaintDevice__PaintDeviceMetric(m))))
}

func (ptr *PasswordProtector) MetricDefault(m std_gui.QPaintDevice__PaintDeviceMetric) int {
	if ptr.Pointer() != nil {
		return int(int32(C.PasswordProtectorca847c_MetricDefault(ptr.Pointer(), C.longlong(m))))
	}
	return 0
}

//export callbackPasswordProtectorca847c_InitPainter
func callbackPasswordProtectorca847c_InitPainter(ptr unsafe.Pointer, painter unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "initPainter"); signal != nil {
		signal.(func(*std_gui.QPainter))(std_gui.NewQPainterFromPointer(painter))
	} else {
		NewPasswordProtectorFromPointer(ptr).InitPainterDefault(std_gui.NewQPainterFromPointer(painter))
	}
}

func (ptr *PasswordProtector) InitPainterDefault(painter std_gui.QPainter_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_InitPainterDefault(ptr.Pointer(), std_gui.PointerFromQPainter(painter))
	}
}

//export callbackPasswordProtectorca847c_EventFilter
func callbackPasswordProtectorca847c_EventFilter(ptr unsafe.Pointer, watched unsafe.Pointer, event unsafe.Pointer) C.char {
	if signal := qt.GetSignal(ptr, "eventFilter"); signal != nil {
		return C.char(int8(qt.GoBoolToInt(signal.(func(*std_core.QObject, *std_core.QEvent) bool)(std_core.NewQObjectFromPointer(watched), std_core.NewQEventFromPointer(event)))))
	}

	return C.char(int8(qt.GoBoolToInt(NewPasswordProtectorFromPointer(ptr).EventFilterDefault(std_core.NewQObjectFromPointer(watched), std_core.NewQEventFromPointer(event)))))
}

func (ptr *PasswordProtector) EventFilterDefault(watched std_core.QObject_ITF, event std_core.QEvent_ITF) bool {
	if ptr.Pointer() != nil {
		return int8(C.PasswordProtectorca847c_EventFilterDefault(ptr.Pointer(), std_core.PointerFromQObject(watched), std_core.PointerFromQEvent(event))) != 0
	}
	return false
}

//export callbackPasswordProtectorca847c_ChildEvent
func callbackPasswordProtectorca847c_ChildEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "childEvent"); signal != nil {
		signal.(func(*std_core.QChildEvent))(std_core.NewQChildEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).ChildEventDefault(std_core.NewQChildEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) ChildEventDefault(event std_core.QChildEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_ChildEventDefault(ptr.Pointer(), std_core.PointerFromQChildEvent(event))
	}
}

//export callbackPasswordProtectorca847c_ConnectNotify
func callbackPasswordProtectorca847c_ConnectNotify(ptr unsafe.Pointer, sign unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "connectNotify"); signal != nil {
		signal.(func(*std_core.QMetaMethod))(std_core.NewQMetaMethodFromPointer(sign))
	} else {
		NewPasswordProtectorFromPointer(ptr).ConnectNotifyDefault(std_core.NewQMetaMethodFromPointer(sign))
	}
}

func (ptr *PasswordProtector) ConnectNotifyDefault(sign std_core.QMetaMethod_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_ConnectNotifyDefault(ptr.Pointer(), std_core.PointerFromQMetaMethod(sign))
	}
}

//export callbackPasswordProtectorca847c_CustomEvent
func callbackPasswordProtectorca847c_CustomEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "customEvent"); signal != nil {
		signal.(func(*std_core.QEvent))(std_core.NewQEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).CustomEventDefault(std_core.NewQEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) CustomEventDefault(event std_core.QEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_CustomEventDefault(ptr.Pointer(), std_core.PointerFromQEvent(event))
	}
}

//export callbackPasswordProtectorca847c_DeleteLater
func callbackPasswordProtectorca847c_DeleteLater(ptr unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "deleteLater"); signal != nil {
		signal.(func())()
	} else {
		NewPasswordProtectorFromPointer(ptr).DeleteLaterDefault()
	}
}

func (ptr *PasswordProtector) DeleteLaterDefault() {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_DeleteLaterDefault(ptr.Pointer())
		ptr.SetPointer(nil)
		runtime.SetFinalizer(ptr, nil)
	}
}

//export callbackPasswordProtectorca847c_Destroyed
func callbackPasswordProtectorca847c_Destroyed(ptr unsafe.Pointer, obj unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "destroyed"); signal != nil {
		signal.(func(*std_core.QObject))(std_core.NewQObjectFromPointer(obj))
	}

}

//export callbackPasswordProtectorca847c_DisconnectNotify
func callbackPasswordProtectorca847c_DisconnectNotify(ptr unsafe.Pointer, sign unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "disconnectNotify"); signal != nil {
		signal.(func(*std_core.QMetaMethod))(std_core.NewQMetaMethodFromPointer(sign))
	} else {
		NewPasswordProtectorFromPointer(ptr).DisconnectNotifyDefault(std_core.NewQMetaMethodFromPointer(sign))
	}
}

func (ptr *PasswordProtector) DisconnectNotifyDefault(sign std_core.QMetaMethod_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_DisconnectNotifyDefault(ptr.Pointer(), std_core.PointerFromQMetaMethod(sign))
	}
}

//export callbackPasswordProtectorca847c_ObjectNameChanged
func callbackPasswordProtectorca847c_ObjectNameChanged(ptr unsafe.Pointer, objectName C.struct_Moc_PackedString) {
	if signal := qt.GetSignal(ptr, "objectNameChanged"); signal != nil {
		signal.(func(string))(cGoUnpackString(objectName))
	}

}

//export callbackPasswordProtectorca847c_TimerEvent
func callbackPasswordProtectorca847c_TimerEvent(ptr unsafe.Pointer, event unsafe.Pointer) {
	if signal := qt.GetSignal(ptr, "timerEvent"); signal != nil {
		signal.(func(*std_core.QTimerEvent))(std_core.NewQTimerEventFromPointer(event))
	} else {
		NewPasswordProtectorFromPointer(ptr).TimerEventDefault(std_core.NewQTimerEventFromPointer(event))
	}
}

func (ptr *PasswordProtector) TimerEventDefault(event std_core.QTimerEvent_ITF) {
	if ptr.Pointer() != nil {
		C.PasswordProtectorca847c_TimerEventDefault(ptr.Pointer(), std_core.PointerFromQTimerEvent(event))
	}
}
