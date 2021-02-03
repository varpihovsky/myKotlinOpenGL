package input

import graphics.Window
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWCursorPosCallback
import org.lwjgl.glfw.GLFWMouseButtonCallback

class MouseListener {
    private var window: Window? = null

    private val buttons: Array<Boolean> = Array(GLFW_MOUSE_BUTTON_LAST) { false }
    private val buttonFunctions: HashMap<Int, InputFunction> = HashMap()
    private val positionFunctions: HashMap<Pair<Pair<Float, Float>, Pair<Float, Float>>, MouseFunction> =
        HashMap()

    private val mouseCallback: GLFWMouseButtonCallback =
        GLFWMouseButtonCallback.create { window: Long, key: Int, action: Int, mods: Int ->
            buttons[key] = (action != GLFW_RELEASE)
        }

    private val mousePositionCallback: GLFWCursorPosCallback =
        GLFWCursorPosCallback.create { window: Long, x: Double, y: Double ->
            this.x = x
            this.y = y
        }

    var x: Double = .0
    var y: Double = .0

    internal fun init(window: Window) {
        this.window = window
        glfwSetMouseButtonCallback(window.window, mouseCallback)
        glfwSetCursorPosCallback(window.window, mousePositionCallback)
    }

    fun addMouseButtonFunction(button: Int, buttonFunction: InputFunction) {
        checkAssign()

        buttonFunctions[button] = buttonFunction
    }

    fun addMousePositionFunction(
        x: Pair<Float, Float>,
        y: Pair<Float, Float>,
        positionFunction: MouseFunction
    ) {
        checkAssign()

        var newX = x
        var newY = y

        newX = if (newX.first < 0)
            Pair(0F, newX.second)
        else
            Pair(window!!.width * newX.first, newX.second)

        newX = if (newX.second > 1)
            Pair(newX.first, window!!.width.toFloat())
        else
            Pair(newX.first, window!!.width * newX.second)

        newY = if (newY.first < 0)
            Pair(0F, newY.second)
        else
            Pair(window!!.height * newY.first, newY.second)

        newY = if (newY.second > 1)
            Pair(newY.first, window!!.height.toFloat())
        else
            Pair(newY.first, window!!.height * newY.second)

        positionFunctions.put(Pair(newX, newY), positionFunction)
    }

    private fun checkAssign() {
        if (window == null)
            throw RuntimeException("Window wasn't assigned")
    }

    internal fun processFunctions() {
        buttonFunctions.keys.forEach {
            if (buttons[it])
                window?.let { it1 -> buttonFunctions[it]?.execute(window!!.window) }
        }

        positionFunctions.keys.forEach {
            val xFrom = it.first.first
            val xTo = it.first.second
            val yFrom = it.second.first
            val yTo = it.second.second

            if (x in xFrom..xTo && y in yFrom..yTo)
                positionFunctions[it]?.execute(x, y, window!!.window)
        }
    }

    internal fun getPositionFunctions() = positionFunctions
}