package graphics

import debug.Debug
import input.KeyboardListener
import input.MouseListener
import multithreading.ThreadPool
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.system.MemoryUtil.NULL

class Window(val width: Int, val height: Int, val title: String) {
    internal val window: Long

    private var time: Long = 0

    internal lateinit var keyboardListener: KeyboardListener
    internal lateinit var mouseListener: MouseListener

    init {
        if (!glfwInit()) throw RuntimeException("Cant init GLFW")

        window = glfwCreateWindow(width, height, title, NULL, NULL)

        if (window == NULL) throw RuntimeException("Cant create window")

        val videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor())!!
        glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2)

        glfwMakeContextCurrent(window)

        glfwSwapInterval(1)

        initEmptyListeners()
    }

    private fun initEmptyListeners() {
        keyboardListener = KeyboardListener()
        keyboardListener.init(window)

        mouseListener = MouseListener()
        mouseListener.init(this)
    }

    fun show() {
        glfwShowWindow(window)

        time = System.currentTimeMillis()

        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents()

            showDebugInfo()

            keyboardListener.processFunctions()
            mouseListener.processFunctions()

            glfwSwapBuffers(window)
        }

        destructor()
    }

    private fun destructor() {
        glfwDestroyWindow(window)
        ThreadPool.interruptAll()
    }

    private fun showDebugInfo() {
        if (Debug.showFPS)
            updateFrameTimeCounter()
        if (Debug.showThreadAmount)
            println(Thread.activeCount())
    }

    private fun updateFrameTimeCounter() {
        glfwSetWindowTitle(window, title + " Frame time: " + (System.currentTimeMillis() - time) + "ms")
        time = System.currentTimeMillis()
    }
}