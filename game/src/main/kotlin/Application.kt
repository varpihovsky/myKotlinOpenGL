import graphics.RGBAContainer
import graphics.Window
import input.*
import org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE

class Application {
    private val window = Window(
        width = WIDTH,
        height = HEIGHT,
        title = TITLE
    )

    init {
        window.background = RGBAContainer(
            red = 1.0f,
            green = 0.5f,
            blue = 1.0f,
            alfa = 1.0f
        )

        val keyboardListener = KeyboardListener()
        val mouseListener = MouseListener()

        InputAssigner.assign(window, keyboardListener)
        InputAssigner.assign(window, mouseListener)

        keyboardListener.addKeyboardFunction(
            GLFW_KEY_ESCAPE,
            object : InputFunction {
                override fun execute(window: Long) {
                    closeWindow(window)
                }
            }
        )

        mouseListener.addMousePositionFunction(
            x = Pair(.0, WIDTH.toDouble()),
            y = Pair(.0, HEIGHT.toDouble()),
            object : MouseFunction {
                override fun execute(x: Double, y: Double, wnd: Long) {
                    setWindowTitle(wnd, window.changeableTitle + " x: $x, y: $y")

                    window.background.red = (x / 2000).toFloat()
                    window.background.green = (y / 2000).toFloat()
                    window.background.blue = (x / 2000 + y / 2000).toFloat()
                }
            }
        )
    }

    fun run() {
        Thread { window.show() }.start()
    }

    companion object {
        private const val WIDTH = 800
        private const val HEIGHT = 600
        private const val TITLE = "Hello World!"
    }
}