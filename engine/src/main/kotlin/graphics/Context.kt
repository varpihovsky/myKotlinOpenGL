package graphics

import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL

object Context {
    private var isContextTaken = false

    private val lock = Object()

    internal fun initContext(window: Long) {
        synchronized(lock) {
            if (isContextTaken) {
                try {
                    lock.wait()
                } catch (e: InterruptedException) {
                    throw e
                }
            }

            GLFW.glfwMakeContextCurrent(window)
            GL.createCapabilities()
            isContextTaken = true
        }
    }

    internal fun freeContext() {
        synchronized(lock) {
            lock.notify()
            isContextTaken = false
        }
    }
}