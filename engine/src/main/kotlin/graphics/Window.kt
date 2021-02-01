package graphics

import debug.Debug
import input.KeyboardListener
import input.MouseListener
import multithreading.ThreadPool
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.glClear
import org.lwjgl.opengl.GL11.glClearColor
import org.lwjgl.system.MemoryUtil.NULL

class Window(val width: Int, val height: Int, val title: String) {
    var changeableTitle = title
        private set

    var isDependentThread = true
    var background = RGBAContainer(0F, 0F, 0F, 0F)

    internal val window: Long
    internal lateinit var keyboardListener: KeyboardListener
    internal lateinit var mouseListener: MouseListener

    private var time: Long = 0

    init {
        if (!glfwInit()) throw RuntimeException("Cant init GLFW")

        window = glfwCreateWindow(width, height, title, NULL, NULL)

        if (window == NULL) throw RuntimeException("Cant create window")

        val videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor())!!
        glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2)

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
        initThread()
        glfwShowWindow(window)

        while (!glfwWindowShouldClose(window)) {
            checkIfInterrupted()

            try {
                Context.initContext(window)
            } catch (e: InterruptedException) {
                break
            }

            updateBackground()
            Context.freeContext()
            glfwSwapBuffers(window)

            glfwPollEvents()
            showDebugInfo()
            keyboardListener.processFunctions()
            mouseListener.processFunctions()
        }
        destructor()
    }

    private fun initThread() {
        if (isDependentThread)
            ThreadPool.addThread(Thread.currentThread())
        time = System.currentTimeMillis()
    }

    private fun checkIfInterrupted() {
        if (Thread.currentThread().isInterrupted)
            glfwSetWindowShouldClose(window, true)
    }

    private fun updateBackground() {
        glClearColor(background.red, background.green, background.blue, background.alfa)
        glClear(GL11.GL_COLOR_BUFFER_BIT)
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
        changeableTitle = title
        changeableTitle = title + " Frame time: " + (System.currentTimeMillis() - time) + "ms"
        glfwSetWindowTitle(window, changeableTitle)
        time = System.currentTimeMillis()
    }
}