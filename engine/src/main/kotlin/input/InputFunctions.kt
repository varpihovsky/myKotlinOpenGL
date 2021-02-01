package input

import org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose

@FunctionalInterface
interface InputFunction {
    fun execute(window: Long? = null)
}

@FunctionalInterface
interface MouseFunction {
    fun execute(x: Double? = null, y: Double? = null)
}

class CloseWindow : InputFunction {
    override fun execute(window: Long?) {
        glfwSetWindowShouldClose(window!!, true)
    }
}