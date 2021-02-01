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
        val keyboardListener = KeyboardListener()
        val mouseListener = MouseListener()

        InputAssigner.assign(window, keyboardListener)
        InputAssigner.assign(window, mouseListener)

        keyboardListener.addKeyboardFunction(GLFW_KEY_ESCAPE, CloseWindow())

        mouseListener.addMousePositionFunction(
            x = Pair(.0, WIDTH.toDouble()),
            y = Pair(.0, HEIGHT.toDouble()),
            object : MouseFunction {
                override fun execute(x: Double?, y: Double?) {
                    println("x:$x, y:$y")
                }
            }
        )
    }

    fun run() {
        window.show()
    }

    companion object {
        private const val WIDTH = 800
        private const val HEIGHT = 600
        private const val TITLE = "Hello World!"
    }
}