package dev.atomixsoft.util;

import dev.atomixsoft.util.input.Input;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Window {

    private long m_Handle;

    private final Logger m_Log;

    private final String m_Title;
    private final int m_Width, m_Height;
    private final boolean m_Resizable, m_VSync;

    public Window(String title, int width, int height, boolean resizable, boolean vSync) {
        m_Title = title;

        m_Width = width;
        m_Height = height;

        m_Resizable = resizable;
        m_VSync = vSync;

        m_Log = LogManager.getLogger();
    }

    public Window(String title, int width, int height, boolean resizable) {
        this(title, width, height, resizable, true);
    }

    public Window(String title, int width, int height) {
        this(title, width, height, false);
    }

    public void initialize() {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, m_Resizable ? GLFW_TRUE : GLFW_FALSE);

        m_Handle = glfwCreateWindow(m_Width, m_Height, m_Title, MemoryUtil.NULL, MemoryUtil.NULL);
        if(m_Handle == MemoryUtil.NULL) {
            m_Log.error("Failed to create a window [{}, {}, {}]!", m_Title, m_Width, m_Handle);
            return;
        }

        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width  = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);

            glfwGetWindowSize(m_Handle, width, height);

            GLFWVidMode vid_mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if(vid_mode == null) {
                m_Log.error("Failed to find a valid Video Mode!");
                glfwDestroyWindow(m_Handle);
                return;
            }

            glfwSetWindowPos(m_Handle, (vid_mode.width() - width.get()) / 2, (vid_mode.height() - height.get()) / 2);
        }

        glfwMakeContextCurrent(m_Handle);
        glfwSwapInterval(m_VSync ? 1 : 0);
        GL.createCapabilities();

        glfwSetInputMode(m_Handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        setup_callbacks();
        glfwShowWindow(m_Handle);
    }

    public void update() {
        glfwSwapBuffers(m_Handle);
        glfwPollEvents();
    }

    public void dispose() {
        if(m_Handle != MemoryUtil.NULL) {
            if(!shouldClose()) close();
            glfwDestroyWindow(m_Handle);
        }
    }

    public void close() {
        glfwSetWindowShouldClose(m_Handle, true);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(m_Handle);
    }

    private void setup_callbacks() {
        glfwSetKeyCallback(m_Handle,         Input::key_callback);
        glfwSetCursorPosCallback(m_Handle,   Input::cursor_pos_callback);
        glfwSetMouseButtonCallback(m_Handle, Input::mouse_button_callback);
    }

    public boolean isResizable() {
        return m_Resizable;
    }

    public boolean usingVSync() {
        return m_VSync;
    }

    public float getWidth() {
        return m_Width;
    }

    public float getHeight() {
        return m_Height;
    }
}
