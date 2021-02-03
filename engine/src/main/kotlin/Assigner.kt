import graphics.Window
import input.KeyboardListener
import input.MouseListener

object Assigner {
    @Synchronized
    fun assign(window: Window, keyboardListener: KeyboardListener) {
        window.keyboardListener = keyboardListener
        keyboardListener.init(window.window)
    }

    @Synchronized
    fun assign(window: Window, mouseListener: MouseListener) {
        window.mouseListener = mouseListener
        mouseListener.init(window)
    }
}