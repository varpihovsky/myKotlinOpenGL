package input

import org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose
import org.lwjgl.glfw.GLFW.glfwSetWindowTitle

@FunctionalInterface
interface InputFunction {
    fun execute(window: Long)
}

@FunctionalInterface
interface MouseFunction {
    fun execute(x: Double, y: Double, window: Long)
}

fun closeWindow(window: Long) = glfwSetWindowShouldClose(window, true)

fun setWindowTitle(window: Long, title: String) = glfwSetWindowTitle(window, title)