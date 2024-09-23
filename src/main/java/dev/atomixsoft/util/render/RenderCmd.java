package dev.atomixsoft.util.render;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class RenderCmd {

    private static final Logger LOG = LogManager.getLogger();

    public static void Init() {
        // For 3D
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        // For 2D
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void Clear(int bitmask) {
        glClear(bitmask);
    }

    public static void Clear() {
        Clear(GL_COLOR_BUFFER_BIT);
    }

    public static void ClearColor(float r, float g, float b) {
        glClearColor(r, g, b, 1.0f);
    }

    public static void ClearColor(int r, int g, int b) {
        float red   = r / 255f;
        float green = g / 255f;
        float blue  = b / 255f;

        ClearColor(red, green, blue);
    }

    public static void SetViewport(int x, int y, int width, int height) {
        glViewport(x, y, width, height);
    }

    public static void DrawIndex(int vao, int indexCount) {
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0L);
    }

}
