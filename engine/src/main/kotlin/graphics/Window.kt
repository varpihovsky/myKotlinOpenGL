package graphics

import debug.Debug
import graphics.objects.Mesh
import graphics.objects.RGBAContainer
import graphics.renderer.Renderer
import input.KeyboardListener
import input.MouseListener
import multithreading.ThreadPool
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL

class Window(internal var width: Int, internal var height: Int, var title: String) {
    var mesh: Mesh? = null
    var changeableTitle = title
        private set
    var isDependentThread = true
    var fullscreen = false
        set(value) {
            field = value
            glfwSetWindowMonitor(window, if (value) glfwGetPrimaryMonitor() else NULL, 0, 0, width, height, 0)
        }
    var background = RGBAContainer(0F, 0F, 0F, 0F)
    var renderer = Renderer()

    internal var window: Long
    internal var resized = false
    internal lateinit var keyboardListener: KeyboardListener
    internal lateinit var mouseListener: MouseListener

    private var time: Long = 0
    private var resizeEvent = ResizeEvent(this)

    init {
        if (!glfwInit()) throw RuntimeException("Cant init GLFW")

        window = glfwCreateWindow(width, height, title, NULL, NULL)

        if (window == NULL) throw RuntimeException("Cant create window")

        val videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor())!!
        glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2)

        initStandardListeners()
    }

    private fun initStandardListeners() {
        keyboardListener = KeyboardListener()
        keyboardListener.init(window)

        mouseListener = MouseListener()
        mouseListener.init(this)

        resizeEvent.init()
    }

    fun show() {
        initThread()
        glfwShowWindow(window)

        while (!glfwWindowShouldClose(window)) {
            checkIfInterrupted()

            try {
                Context.initContext(this)
            } catch (e: InterruptedException) {
                break
            }

            updateBackground()

            renderer.renderMesh(mesh!!)

            glfwSwapBuffers(window)

            Context.freeContext()


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
        if (resized) {
            glViewport(0, 0, width, height)
            resized = false
        }
        glClearColor(background.red, background.green, background.blue, background.alfa)
        glClear(GL_COLOR_BUFFER_BIT)
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