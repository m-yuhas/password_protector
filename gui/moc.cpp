

#define protected public
#define private public

#include "moc.h"
#include "_cgo_export.h"

#include <QAction>
#include <QActionEvent>
#include <QByteArray>
#include <QChildEvent>
#include <QCloseEvent>
#include <QContextMenuEvent>
#include <QDockWidget>
#include <QDragEnterEvent>
#include <QDragLeaveEvent>
#include <QDragMoveEvent>
#include <QDropEvent>
#include <QEvent>
#include <QFocusEvent>
#include <QHideEvent>
#include <QIcon>
#include <QInputMethodEvent>
#include <QKeyEvent>
#include <QMainWindow>
#include <QMenu>
#include <QMetaMethod>
#include <QMouseEvent>
#include <QMoveEvent>
#include <QObject>
#include <QPaintDevice>
#include <QPaintEngine>
#include <QPaintEvent>
#include <QPainter>
#include <QPoint>
#include <QResizeEvent>
#include <QShowEvent>
#include <QSize>
#include <QString>
#include <QTabletEvent>
#include <QTimerEvent>
#include <QVariant>
#include <QWheelEvent>
#include <QWidget>


class PasswordProtectorca847c: public QMainWindow
{
Q_OBJECT
public:
	PasswordProtectorca847c(QWidget *parent = Q_NULLPTR, Qt::WindowFlags flags = Qt::WindowFlags()) : QMainWindow(parent, flags) {qRegisterMetaType<quintptr>("quintptr");PasswordProtectorca847c_PasswordProtectorca847c_QRegisterMetaType();PasswordProtectorca847c_PasswordProtectorca847c_QRegisterMetaTypes();callbackPasswordProtectorca847c_Constructor(this);};
	 ~PasswordProtectorca847c() { callbackPasswordProtectorca847c_DestroyPasswordProtector(this); };
	QMenu * createPopupMenu() { return static_cast<QMenu*>(callbackPasswordProtectorca847c_CreatePopupMenu(this)); };
	bool event(QEvent * event) { return callbackPasswordProtectorca847c_Event(this, event) != 0; };
	void contextMenuEvent(QContextMenuEvent * event) { callbackPasswordProtectorca847c_ContextMenuEvent(this, event); };
	void Signal_IconSizeChanged(const QSize & iconSize) { callbackPasswordProtectorca847c_IconSizeChanged(this, const_cast<QSize*>(&iconSize)); };
	void setAnimated(bool enabled) { callbackPasswordProtectorca847c_SetAnimated(this, enabled); };
	void setDockNestingEnabled(bool enabled) { callbackPasswordProtectorca847c_SetDockNestingEnabled(this, enabled); };
	void setUnifiedTitleAndToolBarOnMac(bool set) { callbackPasswordProtectorca847c_SetUnifiedTitleAndToolBarOnMac(this, set); };
	void Signal_TabifiedDockWidgetActivated(QDockWidget * dockWidget) { callbackPasswordProtectorca847c_TabifiedDockWidgetActivated(this, dockWidget); };
	void Signal_ToolButtonStyleChanged(Qt::ToolButtonStyle toolButtonStyle) { callbackPasswordProtectorca847c_ToolButtonStyleChanged(this, toolButtonStyle); };
	bool close() { return callbackPasswordProtectorca847c_Close(this) != 0; };
	bool focusNextPrevChild(bool next) { return callbackPasswordProtectorca847c_FocusNextPrevChild(this, next) != 0; };
	bool nativeEvent(const QByteArray & eventType, void * message, long * result) { return callbackPasswordProtectorca847c_NativeEvent(this, const_cast<QByteArray*>(&eventType), message, result ? *result : 0) != 0; };
	void actionEvent(QActionEvent * event) { callbackPasswordProtectorca847c_ActionEvent(this, event); };
	void changeEvent(QEvent * event) { callbackPasswordProtectorca847c_ChangeEvent(this, event); };
	void closeEvent(QCloseEvent * event) { callbackPasswordProtectorca847c_CloseEvent(this, event); };
	void Signal_CustomContextMenuRequested(const QPoint & pos) { callbackPasswordProtectorca847c_CustomContextMenuRequested(this, const_cast<QPoint*>(&pos)); };
	void dragEnterEvent(QDragEnterEvent * event) { callbackPasswordProtectorca847c_DragEnterEvent(this, event); };
	void dragLeaveEvent(QDragLeaveEvent * event) { callbackPasswordProtectorca847c_DragLeaveEvent(this, event); };
	void dragMoveEvent(QDragMoveEvent * event) { callbackPasswordProtectorca847c_DragMoveEvent(this, event); };
	void dropEvent(QDropEvent * event) { callbackPasswordProtectorca847c_DropEvent(this, event); };
	void enterEvent(QEvent * event) { callbackPasswordProtectorca847c_EnterEvent(this, event); };
	void focusInEvent(QFocusEvent * event) { callbackPasswordProtectorca847c_FocusInEvent(this, event); };
	void focusOutEvent(QFocusEvent * event) { callbackPasswordProtectorca847c_FocusOutEvent(this, event); };
	void hide() { callbackPasswordProtectorca847c_Hide(this); };
	void hideEvent(QHideEvent * event) { callbackPasswordProtectorca847c_HideEvent(this, event); };
	void inputMethodEvent(QInputMethodEvent * event) { callbackPasswordProtectorca847c_InputMethodEvent(this, event); };
	void keyPressEvent(QKeyEvent * event) { callbackPasswordProtectorca847c_KeyPressEvent(this, event); };
	void keyReleaseEvent(QKeyEvent * event) { callbackPasswordProtectorca847c_KeyReleaseEvent(this, event); };
	void leaveEvent(QEvent * event) { callbackPasswordProtectorca847c_LeaveEvent(this, event); };
	void lower() { callbackPasswordProtectorca847c_Lower(this); };
	void mouseDoubleClickEvent(QMouseEvent * event) { callbackPasswordProtectorca847c_MouseDoubleClickEvent(this, event); };
	void mouseMoveEvent(QMouseEvent * event) { callbackPasswordProtectorca847c_MouseMoveEvent(this, event); };
	void mousePressEvent(QMouseEvent * event) { callbackPasswordProtectorca847c_MousePressEvent(this, event); };
	void mouseReleaseEvent(QMouseEvent * event) { callbackPasswordProtectorca847c_MouseReleaseEvent(this, event); };
	void moveEvent(QMoveEvent * event) { callbackPasswordProtectorca847c_MoveEvent(this, event); };
	void paintEvent(QPaintEvent * event) { callbackPasswordProtectorca847c_PaintEvent(this, event); };
	void raise() { callbackPasswordProtectorca847c_Raise(this); };
	void repaint() { callbackPasswordProtectorca847c_Repaint(this); };
	void resizeEvent(QResizeEvent * event) { callbackPasswordProtectorca847c_ResizeEvent(this, event); };
	void setDisabled(bool disable) { callbackPasswordProtectorca847c_SetDisabled(this, disable); };
	void setEnabled(bool vbo) { callbackPasswordProtectorca847c_SetEnabled(this, vbo); };
	void setFocus() { callbackPasswordProtectorca847c_SetFocus2(this); };
	void setHidden(bool hidden) { callbackPasswordProtectorca847c_SetHidden(this, hidden); };
	void setStyleSheet(const QString & styleSheet) { QByteArray t728ae7 = styleSheet.toUtf8(); Moc_PackedString styleSheetPacked = { const_cast<char*>(t728ae7.prepend("WHITESPACE").constData()+10), t728ae7.size()-10 };callbackPasswordProtectorca847c_SetStyleSheet(this, styleSheetPacked); };
	void setVisible(bool visible) { callbackPasswordProtectorca847c_SetVisible(this, visible); };
	void setWindowModified(bool vbo) { callbackPasswordProtectorca847c_SetWindowModified(this, vbo); };
	void setWindowTitle(const QString & vqs) { QByteArray tda39a3 = vqs.toUtf8(); Moc_PackedString vqsPacked = { const_cast<char*>(tda39a3.prepend("WHITESPACE").constData()+10), tda39a3.size()-10 };callbackPasswordProtectorca847c_SetWindowTitle(this, vqsPacked); };
	void show() { callbackPasswordProtectorca847c_Show(this); };
	void showEvent(QShowEvent * event) { callbackPasswordProtectorca847c_ShowEvent(this, event); };
	void showFullScreen() { callbackPasswordProtectorca847c_ShowFullScreen(this); };
	void showMaximized() { callbackPasswordProtectorca847c_ShowMaximized(this); };
	void showMinimized() { callbackPasswordProtectorca847c_ShowMinimized(this); };
	void showNormal() { callbackPasswordProtectorca847c_ShowNormal(this); };
	void tabletEvent(QTabletEvent * event) { callbackPasswordProtectorca847c_TabletEvent(this, event); };
	void update() { callbackPasswordProtectorca847c_Update(this); };
	void updateMicroFocus() { callbackPasswordProtectorca847c_UpdateMicroFocus(this); };
	void wheelEvent(QWheelEvent * event) { callbackPasswordProtectorca847c_WheelEvent(this, event); };
	void Signal_WindowIconChanged(const QIcon & icon) { callbackPasswordProtectorca847c_WindowIconChanged(this, const_cast<QIcon*>(&icon)); };
	void Signal_WindowTitleChanged(const QString & title) { QByteArray t3c6de1 = title.toUtf8(); Moc_PackedString titlePacked = { const_cast<char*>(t3c6de1.prepend("WHITESPACE").constData()+10), t3c6de1.size()-10 };callbackPasswordProtectorca847c_WindowTitleChanged(this, titlePacked); };
	QPaintEngine * paintEngine() const { return static_cast<QPaintEngine*>(callbackPasswordProtectorca847c_PaintEngine(const_cast<void*>(static_cast<const void*>(this)))); };
	QSize minimumSizeHint() const { return *static_cast<QSize*>(callbackPasswordProtectorca847c_MinimumSizeHint(const_cast<void*>(static_cast<const void*>(this)))); };
	QSize sizeHint() const { return *static_cast<QSize*>(callbackPasswordProtectorca847c_SizeHint(const_cast<void*>(static_cast<const void*>(this)))); };
	QVariant inputMethodQuery(Qt::InputMethodQuery query) const { return *static_cast<QVariant*>(callbackPasswordProtectorca847c_InputMethodQuery(const_cast<void*>(static_cast<const void*>(this)), query)); };
	bool hasHeightForWidth() const { return callbackPasswordProtectorca847c_HasHeightForWidth(const_cast<void*>(static_cast<const void*>(this))) != 0; };
	int heightForWidth(int w) const { return callbackPasswordProtectorca847c_HeightForWidth(const_cast<void*>(static_cast<const void*>(this)), w); };
	int metric(QPaintDevice::PaintDeviceMetric m) const { return callbackPasswordProtectorca847c_Metric(const_cast<void*>(static_cast<const void*>(this)), m); };
	void initPainter(QPainter * painter) const { callbackPasswordProtectorca847c_InitPainter(const_cast<void*>(static_cast<const void*>(this)), painter); };
	bool eventFilter(QObject * watched, QEvent * event) { return callbackPasswordProtectorca847c_EventFilter(this, watched, event) != 0; };
	void childEvent(QChildEvent * event) { callbackPasswordProtectorca847c_ChildEvent(this, event); };
	void connectNotify(const QMetaMethod & sign) { callbackPasswordProtectorca847c_ConnectNotify(this, const_cast<QMetaMethod*>(&sign)); };
	void customEvent(QEvent * event) { callbackPasswordProtectorca847c_CustomEvent(this, event); };
	void deleteLater() { callbackPasswordProtectorca847c_DeleteLater(this); };
	void Signal_Destroyed(QObject * obj) { callbackPasswordProtectorca847c_Destroyed(this, obj); };
	void disconnectNotify(const QMetaMethod & sign) { callbackPasswordProtectorca847c_DisconnectNotify(this, const_cast<QMetaMethod*>(&sign)); };
	void Signal_ObjectNameChanged(const QString & objectName) { QByteArray taa2c4f = objectName.toUtf8(); Moc_PackedString objectNamePacked = { const_cast<char*>(taa2c4f.prepend("WHITESPACE").constData()+10), taa2c4f.size()-10 };callbackPasswordProtectorca847c_ObjectNameChanged(this, objectNamePacked); };
	void timerEvent(QTimerEvent * event) { callbackPasswordProtectorca847c_TimerEvent(this, event); };
signals:
public slots:
private:
};

Q_DECLARE_METATYPE(PasswordProtectorca847c*)


void PasswordProtectorca847c_PasswordProtectorca847c_QRegisterMetaTypes() {
}

int PasswordProtectorca847c_PasswordProtectorca847c_QRegisterMetaType()
{
	return qRegisterMetaType<PasswordProtectorca847c*>();
}

int PasswordProtectorca847c_PasswordProtectorca847c_QRegisterMetaType2(char* typeName)
{
	return qRegisterMetaType<PasswordProtectorca847c*>(const_cast<const char*>(typeName));
}

int PasswordProtectorca847c_PasswordProtectorca847c_QmlRegisterType()
{
#ifdef QT_QML_LIB
	return qmlRegisterType<PasswordProtectorca847c>();
#else
	return 0;
#endif
}

int PasswordProtectorca847c_PasswordProtectorca847c_QmlRegisterType2(char* uri, int versionMajor, int versionMinor, char* qmlName)
{
#ifdef QT_QML_LIB
	return qmlRegisterType<PasswordProtectorca847c>(const_cast<const char*>(uri), versionMajor, versionMinor, const_cast<const char*>(qmlName));
#else
	return 0;
#endif
}

void* PasswordProtectorca847c___resizeDocks_docks_atList(void* ptr, int i)
{
	return ({QDockWidget * tmp = static_cast<QList<QDockWidget *>*>(ptr)->at(i); if (i == static_cast<QList<QDockWidget *>*>(ptr)->size()-1) { static_cast<QList<QDockWidget *>*>(ptr)->~QList(); free(ptr); }; tmp; });
}

void PasswordProtectorca847c___resizeDocks_docks_setList(void* ptr, void* i)
{
	static_cast<QList<QDockWidget *>*>(ptr)->append(static_cast<QDockWidget*>(i));
}

void* PasswordProtectorca847c___resizeDocks_docks_newList(void* ptr)
{
	Q_UNUSED(ptr);
	return new QList<QDockWidget *>();
}

int PasswordProtectorca847c___resizeDocks_sizes_atList(void* ptr, int i)
{
	return ({int tmp = static_cast<QList<int>*>(ptr)->at(i); if (i == static_cast<QList<int>*>(ptr)->size()-1) { static_cast<QList<int>*>(ptr)->~QList(); free(ptr); }; tmp; });
}

void PasswordProtectorca847c___resizeDocks_sizes_setList(void* ptr, int i)
{
	static_cast<QList<int>*>(ptr)->append(i);
}

void* PasswordProtectorca847c___resizeDocks_sizes_newList(void* ptr)
{
	Q_UNUSED(ptr);
	return new QList<int>();
}

void* PasswordProtectorca847c___tabifiedDockWidgets_atList(void* ptr, int i)
{
	return ({QDockWidget * tmp = static_cast<QList<QDockWidget *>*>(ptr)->at(i); if (i == static_cast<QList<QDockWidget *>*>(ptr)->size()-1) { static_cast<QList<QDockWidget *>*>(ptr)->~QList(); free(ptr); }; tmp; });
}

void PasswordProtectorca847c___tabifiedDockWidgets_setList(void* ptr, void* i)
{
	static_cast<QList<QDockWidget *>*>(ptr)->append(static_cast<QDockWidget*>(i));
}

void* PasswordProtectorca847c___tabifiedDockWidgets_newList(void* ptr)
{
	Q_UNUSED(ptr);
	return new QList<QDockWidget *>();
}

void* PasswordProtectorca847c___addActions_actions_atList(void* ptr, int i)
{
	return ({QAction * tmp = static_cast<QList<QAction *>*>(ptr)->at(i); if (i == static_cast<QList<QAction *>*>(ptr)->size()-1) { static_cast<QList<QAction *>*>(ptr)->~QList(); free(ptr); }; tmp; });
}

void PasswordProtectorca847c___addActions_actions_setList(void* ptr, void* i)
{
	static_cast<QList<QAction *>*>(ptr)->append(static_cast<QAction*>(i));
}

void* PasswordProtectorca847c___addActions_actions_newList(void* ptr)
{
	Q_UNUSED(ptr);
	return new QList<QAction *>();
}

void* PasswordProtectorca847c___insertActions_actions_atList(void* ptr, int i)
{
	return ({QAction * tmp = static_cast<QList<QAction *>*>(ptr)->at(i); if (i == static_cast<QList<QAction *>*>(ptr)->size()-1) { static_cast<QList<QAction *>*>(ptr)->~QList(); free(ptr); }; tmp; });
}

void PasswordProtectorca847c___insertActions_actions_setList(void* ptr, void* i)
{
	static_cast<QList<QAction *>*>(ptr)->append(static_cast<QAction*>(i));
}

void* PasswordProtectorca847c___insertActions_actions_newList(void* ptr)
{
	Q_UNUSED(ptr);
	return new QList<QAction *>();
}

void* PasswordProtectorca847c___actions_atList(void* ptr, int i)
{
	return ({QAction * tmp = static_cast<QList<QAction *>*>(ptr)->at(i); if (i == static_cast<QList<QAction *>*>(ptr)->size()-1) { static_cast<QList<QAction *>*>(ptr)->~QList(); free(ptr); }; tmp; });
}

void PasswordProtectorca847c___actions_setList(void* ptr, void* i)
{
	static_cast<QList<QAction *>*>(ptr)->append(static_cast<QAction*>(i));
}

void* PasswordProtectorca847c___actions_newList(void* ptr)
{
	Q_UNUSED(ptr);
	return new QList<QAction *>();
}

void* PasswordProtectorca847c___dynamicPropertyNames_atList(void* ptr, int i)
{
	return new QByteArray(({QByteArray tmp = static_cast<QList<QByteArray>*>(ptr)->at(i); if (i == static_cast<QList<QByteArray>*>(ptr)->size()-1) { static_cast<QList<QByteArray>*>(ptr)->~QList(); free(ptr); }; tmp; }));
}

void PasswordProtectorca847c___dynamicPropertyNames_setList(void* ptr, void* i)
{
	static_cast<QList<QByteArray>*>(ptr)->append(*static_cast<QByteArray*>(i));
}

void* PasswordProtectorca847c___dynamicPropertyNames_newList(void* ptr)
{
	Q_UNUSED(ptr);
	return new QList<QByteArray>();
}

void* PasswordProtectorca847c___findChildren_atList2(void* ptr, int i)
{
	return ({QObject* tmp = static_cast<QList<QObject*>*>(ptr)->at(i); if (i == static_cast<QList<QObject*>*>(ptr)->size()-1) { static_cast<QList<QObject*>*>(ptr)->~QList(); free(ptr); }; tmp; });
}

void PasswordProtectorca847c___findChildren_setList2(void* ptr, void* i)
{
	static_cast<QList<QObject*>*>(ptr)->append(static_cast<QObject*>(i));
}

void* PasswordProtectorca847c___findChildren_newList2(void* ptr)
{
	Q_UNUSED(ptr);
	return new QList<QObject*>();
}

void* PasswordProtectorca847c___findChildren_atList3(void* ptr, int i)
{
	return ({QObject* tmp = static_cast<QList<QObject*>*>(ptr)->at(i); if (i == static_cast<QList<QObject*>*>(ptr)->size()-1) { static_cast<QList<QObject*>*>(ptr)->~QList(); free(ptr); }; tmp; });
}

void PasswordProtectorca847c___findChildren_setList3(void* ptr, void* i)
{
	static_cast<QList<QObject*>*>(ptr)->append(static_cast<QObject*>(i));
}

void* PasswordProtectorca847c___findChildren_newList3(void* ptr)
{
	Q_UNUSED(ptr);
	return new QList<QObject*>();
}

void* PasswordProtectorca847c___findChildren_atList(void* ptr, int i)
{
	return ({QObject* tmp = static_cast<QList<QObject*>*>(ptr)->at(i); if (i == static_cast<QList<QObject*>*>(ptr)->size()-1) { static_cast<QList<QObject*>*>(ptr)->~QList(); free(ptr); }; tmp; });
}

void PasswordProtectorca847c___findChildren_setList(void* ptr, void* i)
{
	static_cast<QList<QObject*>*>(ptr)->append(static_cast<QObject*>(i));
}

void* PasswordProtectorca847c___findChildren_newList(void* ptr)
{
	Q_UNUSED(ptr);
	return new QList<QObject*>();
}

void* PasswordProtectorca847c___children_atList(void* ptr, int i)
{
	return ({QObject * tmp = static_cast<QList<QObject *>*>(ptr)->at(i); if (i == static_cast<QList<QObject *>*>(ptr)->size()-1) { static_cast<QList<QObject *>*>(ptr)->~QList(); free(ptr); }; tmp; });
}

void PasswordProtectorca847c___children_setList(void* ptr, void* i)
{
	static_cast<QList<QObject *>*>(ptr)->append(static_cast<QObject*>(i));
}

void* PasswordProtectorca847c___children_newList(void* ptr)
{
	Q_UNUSED(ptr);
	return new QList<QObject *>();
}

void* PasswordProtectorca847c_NewPasswordProtector(void* parent, long long flags)
{
		return new PasswordProtectorca847c(static_cast<QWidget*>(parent), static_cast<Qt::WindowType>(flags));
}

void PasswordProtectorca847c_DestroyPasswordProtector(void* ptr)
{
	static_cast<PasswordProtectorca847c*>(ptr)->~PasswordProtectorca847c();
}

void PasswordProtectorca847c_DestroyPasswordProtectorDefault(void* ptr)
{
	Q_UNUSED(ptr);

}

void* PasswordProtectorca847c_CreatePopupMenuDefault(void* ptr)
{
	return static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::createPopupMenu();
}

char PasswordProtectorca847c_EventDefault(void* ptr, void* event)
{
	return static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::event(static_cast<QEvent*>(event));
}

void PasswordProtectorca847c_ContextMenuEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::contextMenuEvent(static_cast<QContextMenuEvent*>(event));
}

void PasswordProtectorca847c_SetAnimatedDefault(void* ptr, char enabled)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::setAnimated(enabled != 0);
}

void PasswordProtectorca847c_SetDockNestingEnabledDefault(void* ptr, char enabled)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::setDockNestingEnabled(enabled != 0);
}

void PasswordProtectorca847c_SetUnifiedTitleAndToolBarOnMacDefault(void* ptr, char set)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::setUnifiedTitleAndToolBarOnMac(set != 0);
}



char PasswordProtectorca847c_CloseDefault(void* ptr)
{
	return static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::close();
}

char PasswordProtectorca847c_FocusNextPrevChildDefault(void* ptr, char next)
{
	return static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::focusNextPrevChild(next != 0);
}

char PasswordProtectorca847c_NativeEventDefault(void* ptr, void* eventType, void* message, long result)
{
	return static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::nativeEvent(*static_cast<QByteArray*>(eventType), message, &result);
}

void PasswordProtectorca847c_ActionEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::actionEvent(static_cast<QActionEvent*>(event));
}

void PasswordProtectorca847c_ChangeEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::changeEvent(static_cast<QEvent*>(event));
}

void PasswordProtectorca847c_CloseEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::closeEvent(static_cast<QCloseEvent*>(event));
}

void PasswordProtectorca847c_DragEnterEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::dragEnterEvent(static_cast<QDragEnterEvent*>(event));
}

void PasswordProtectorca847c_DragLeaveEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::dragLeaveEvent(static_cast<QDragLeaveEvent*>(event));
}

void PasswordProtectorca847c_DragMoveEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::dragMoveEvent(static_cast<QDragMoveEvent*>(event));
}

void PasswordProtectorca847c_DropEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::dropEvent(static_cast<QDropEvent*>(event));
}

void PasswordProtectorca847c_EnterEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::enterEvent(static_cast<QEvent*>(event));
}

void PasswordProtectorca847c_FocusInEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::focusInEvent(static_cast<QFocusEvent*>(event));
}

void PasswordProtectorca847c_FocusOutEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::focusOutEvent(static_cast<QFocusEvent*>(event));
}

void PasswordProtectorca847c_HideDefault(void* ptr)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::hide();
}

void PasswordProtectorca847c_HideEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::hideEvent(static_cast<QHideEvent*>(event));
}

void PasswordProtectorca847c_InputMethodEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::inputMethodEvent(static_cast<QInputMethodEvent*>(event));
}

void PasswordProtectorca847c_KeyPressEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::keyPressEvent(static_cast<QKeyEvent*>(event));
}

void PasswordProtectorca847c_KeyReleaseEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::keyReleaseEvent(static_cast<QKeyEvent*>(event));
}

void PasswordProtectorca847c_LeaveEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::leaveEvent(static_cast<QEvent*>(event));
}

void PasswordProtectorca847c_LowerDefault(void* ptr)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::lower();
}

void PasswordProtectorca847c_MouseDoubleClickEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::mouseDoubleClickEvent(static_cast<QMouseEvent*>(event));
}

void PasswordProtectorca847c_MouseMoveEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::mouseMoveEvent(static_cast<QMouseEvent*>(event));
}

void PasswordProtectorca847c_MousePressEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::mousePressEvent(static_cast<QMouseEvent*>(event));
}

void PasswordProtectorca847c_MouseReleaseEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::mouseReleaseEvent(static_cast<QMouseEvent*>(event));
}

void PasswordProtectorca847c_MoveEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::moveEvent(static_cast<QMoveEvent*>(event));
}

void PasswordProtectorca847c_PaintEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::paintEvent(static_cast<QPaintEvent*>(event));
}

void PasswordProtectorca847c_RaiseDefault(void* ptr)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::raise();
}

void PasswordProtectorca847c_RepaintDefault(void* ptr)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::repaint();
}

void PasswordProtectorca847c_ResizeEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::resizeEvent(static_cast<QResizeEvent*>(event));
}

void PasswordProtectorca847c_SetDisabledDefault(void* ptr, char disable)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::setDisabled(disable != 0);
}

void PasswordProtectorca847c_SetEnabledDefault(void* ptr, char vbo)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::setEnabled(vbo != 0);
}

void PasswordProtectorca847c_SetFocus2Default(void* ptr)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::setFocus();
}

void PasswordProtectorca847c_SetHiddenDefault(void* ptr, char hidden)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::setHidden(hidden != 0);
}

void PasswordProtectorca847c_SetStyleSheetDefault(void* ptr, struct Moc_PackedString styleSheet)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::setStyleSheet(QString::fromUtf8(styleSheet.data, styleSheet.len));
}

void PasswordProtectorca847c_SetVisibleDefault(void* ptr, char visible)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::setVisible(visible != 0);
}

void PasswordProtectorca847c_SetWindowModifiedDefault(void* ptr, char vbo)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::setWindowModified(vbo != 0);
}

void PasswordProtectorca847c_SetWindowTitleDefault(void* ptr, struct Moc_PackedString vqs)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::setWindowTitle(QString::fromUtf8(vqs.data, vqs.len));
}

void PasswordProtectorca847c_ShowDefault(void* ptr)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::show();
}

void PasswordProtectorca847c_ShowEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::showEvent(static_cast<QShowEvent*>(event));
}

void PasswordProtectorca847c_ShowFullScreenDefault(void* ptr)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::showFullScreen();
}

void PasswordProtectorca847c_ShowMaximizedDefault(void* ptr)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::showMaximized();
}

void PasswordProtectorca847c_ShowMinimizedDefault(void* ptr)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::showMinimized();
}

void PasswordProtectorca847c_ShowNormalDefault(void* ptr)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::showNormal();
}

void PasswordProtectorca847c_TabletEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::tabletEvent(static_cast<QTabletEvent*>(event));
}

void PasswordProtectorca847c_UpdateDefault(void* ptr)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::update();
}

void PasswordProtectorca847c_UpdateMicroFocusDefault(void* ptr)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::updateMicroFocus();
}

void PasswordProtectorca847c_WheelEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::wheelEvent(static_cast<QWheelEvent*>(event));
}

void* PasswordProtectorca847c_PaintEngineDefault(void* ptr)
{
	return static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::paintEngine();
}

void* PasswordProtectorca847c_MinimumSizeHintDefault(void* ptr)
{
	return ({ QSize tmpValue = static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::minimumSizeHint(); new QSize(tmpValue.width(), tmpValue.height()); });
}

void* PasswordProtectorca847c_SizeHintDefault(void* ptr)
{
	return ({ QSize tmpValue = static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::sizeHint(); new QSize(tmpValue.width(), tmpValue.height()); });
}

void* PasswordProtectorca847c_InputMethodQueryDefault(void* ptr, long long query)
{
	return new QVariant(static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::inputMethodQuery(static_cast<Qt::InputMethodQuery>(query)));
}

char PasswordProtectorca847c_HasHeightForWidthDefault(void* ptr)
{
	return static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::hasHeightForWidth();
}

int PasswordProtectorca847c_HeightForWidthDefault(void* ptr, int w)
{
	return static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::heightForWidth(w);
}

int PasswordProtectorca847c_MetricDefault(void* ptr, long long m)
{
	return static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::metric(static_cast<QPaintDevice::PaintDeviceMetric>(m));
}

void PasswordProtectorca847c_InitPainterDefault(void* ptr, void* painter)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::initPainter(static_cast<QPainter*>(painter));
}

char PasswordProtectorca847c_EventFilterDefault(void* ptr, void* watched, void* event)
{
	return static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::eventFilter(static_cast<QObject*>(watched), static_cast<QEvent*>(event));
}

void PasswordProtectorca847c_ChildEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::childEvent(static_cast<QChildEvent*>(event));
}

void PasswordProtectorca847c_ConnectNotifyDefault(void* ptr, void* sign)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::connectNotify(*static_cast<QMetaMethod*>(sign));
}

void PasswordProtectorca847c_CustomEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::customEvent(static_cast<QEvent*>(event));
}

void PasswordProtectorca847c_DeleteLaterDefault(void* ptr)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::deleteLater();
}

void PasswordProtectorca847c_DisconnectNotifyDefault(void* ptr, void* sign)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::disconnectNotify(*static_cast<QMetaMethod*>(sign));
}

void PasswordProtectorca847c_TimerEventDefault(void* ptr, void* event)
{
	static_cast<PasswordProtectorca847c*>(ptr)->QMainWindow::timerEvent(static_cast<QTimerEvent*>(event));
}

#include "moc_moc.h"
