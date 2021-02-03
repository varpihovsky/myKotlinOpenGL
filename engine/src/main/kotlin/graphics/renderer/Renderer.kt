package graphics.renderer

import graphics.objects.Mesh
import org.lwjgl.opengl.GL30.*

class Renderer {
    fun renderMesh(mesh: Mesh) {
        glBegin(GL_POLYGON)
        glColor3f(1F, 1F, 1F)
        mesh.vertices.forEach { glVertex3f(it.x, it.y, it.z) }
        glEnd()
        glFlush()
    }
}