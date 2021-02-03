package graphics

import org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback
import org.lwjgl.glfw.GLFWWindowSizeCallback

internal class ResizeEvent(private val window: Window) {
    private val resize: GLFWWindowSizeCallback =
        GLFWWindowSizeCallback.create { wnd: Long, width: Int, height: Int ->
            for (i in window.mouseListener.getPositionFunctions().keys) {
                val function = window.mouseListener.getPositionFunctions()[i]

                window.mouseListener.getPositionFunctions().remove(i)

                var first = i.first
                var second = i.second

                first = Pair(first.first / window.width * width, first.second / window.width * width)
                second = Pair(second.first / window.height * height, second.second / window.height * height)

                window.mouseListener.getPositionFunctions()[Pair(first, second)] = function!!

                window.width = width
                window.height = height
                window.resized = true
            }
        }

    fun init() {
        glfwSetWindowSizeCallback(window.window, resize)
    }
}