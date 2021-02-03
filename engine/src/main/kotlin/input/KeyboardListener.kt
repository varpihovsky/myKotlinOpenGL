package input

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWKeyCallback

class KeyboardListener(var keyboardListener: KeyboardListener? = null) {
    private var window: Long? = null

    private val keys: Array<Boolean> = Array(GLFW_KEY_LAST) { false }
    private var keyFunctions: HashMap<Int, InputFunction> = HashMap()

    private val keyboardCallback: GLFWKeyCallback =
        GLFWKeyCallback.create { window: Long, key: Int, scancode: Int, action: Int, mods: Int ->
            keys[key] = (action != GLFW_RELEASE)
        }

    internal fun init(window: Long) {
        this.window = window
        glfwSetKeyCallback(window, keyboardCallback)
    }

    fun addKeyboardFunction(key: Int, function: InputFunction) {
        if (window == null)
            throw RuntimeException("Window wasn't assigned")

        keyFunctions[key] = function
    }

    internal fun processFunctions() {
        keyFunctions.keys.forEach {
            if (keys[it]) {
                window?.let { it1 -> keyFunctions[it]?.execute(it1) }
                keys[it] = false
            }
        }
    }
}