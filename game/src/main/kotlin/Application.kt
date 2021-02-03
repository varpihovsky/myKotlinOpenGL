import graphics.Window
import graphics.objects.Mesh
import graphics.objects.Vertex
import input.InputFunction
import input.KeyboardListener
import input.MouseListener
import input.closeWindow
import org.lwjgl.glfw.GLFW.*

class Application {
    private val window = Window(
        width = WIDTH,
        height = HEIGHT,
        title = TITLE
    )

    init {
        window.mesh = Mesh(
            arrayOf(
                Vertex(-0.5f, 0.5f, 0.0f),
                Vertex(0.5f, 0.5f, 0.0f),
                Vertex(0.5f, -0.5f, 0.0f),
                Vertex(-0.5f, -0.5f, 0.0f),
            ),
            arrayOf(
                0, 1, 2,
                0, 3, 2
            )
        )

        val keyboardListener = KeyboardListener()
        val mouseListener = MouseListener()

        Assigner.assign(window, keyboardListener)
        Assigner.assign(window, mouseListener)

        keyboardListener.addKeyboardFunction(
            GLFW_KEY_ESCAPE,
            object : InputFunction {
                override fun execute(window: Long) {
                    closeWindow(window)
                }
            }
        )

        keyboardListener.addKeyboardFunction(
            GLFW_KEY_W,
            object : InputFunction {
                override fun execute(wnd: Long) {
                    window.mesh!!.vertices.forEachIndexed { index, vertex ->
                        window.mesh!!.vertices[index].y += 0.05F
                    }
                }
            }
        )
        keyboardListener.addKeyboardFunction(
            GLFW_KEY_S,
            object : InputFunction {
                override fun execute(wnd: Long) {
                    window.mesh!!.vertices.forEachIndexed { i: Int, vertex: Vertex ->
                        window.mesh!!.vertices[i].y -= 0.05F
                    }
                }
            }
        )
        keyboardListener.addKeyboardFunction(
            GLFW_KEY_A,
            object : InputFunction {
                override fun execute(wnd: Long) {
                    window.mesh!!.vertices.forEachIndexed { i: Int, vertex: Vertex ->
                        window.mesh!!.vertices[i].x -= 0.05F
                    }
                }
            }
        )
        keyboardListener.addKeyboardFunction(
            GLFW_KEY_D,
            object : InputFunction {
                override fun execute(wnd: Long) {
                    window.mesh!!.vertices.forEachIndexed { i: Int, vertex: Vertex ->
                        window.mesh!!.vertices[i].x += 0.05F
                    }
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