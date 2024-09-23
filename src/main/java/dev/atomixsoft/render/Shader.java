package dev.atomixsoft.render;

import dev.atomixsoft.util.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private final Map<String, Integer> m_Uniforms;

    private final Logger m_Log;
    private final int m_ProgramId;

    public Shader(String path) {
        m_Uniforms = new HashMap<>();
        m_Log = LogManager.getLogger();

        String vertSrc = FileUtils.Read_String_From_Memory(path + ".vert");
        String fragSrc = FileUtils.Read_String_From_Memory(path + ".frag");

        int vertex   = createShader(vertSrc, GL_VERTEX_SHADER);
        int fragment = createShader(fragSrc, GL_FRAGMENT_SHADER);

        m_ProgramId = linkShader(vertex, fragment);

        if(vertex != -1)   glDeleteShader(vertex);
        if(fragment != -1) glDeleteShader(fragment);

        if(m_ProgramId == -1)
            m_Log.error("Failed to properly create a Shader!\nPath: {}.vert/frag", path);
    }

    public void bind() {
        if(m_ProgramId == -1) return;

        glUseProgram(m_ProgramId);
    }

    public void dispose() {
        if(m_ProgramId == -1) return;

        glDeleteProgram(m_ProgramId);
        m_Uniforms.clear();
    }

    public void createUniform(String name) {
        if(m_ProgramId == -1) return;

        int location = glGetUniformLocation(m_ProgramId, name);
        if(location < 0) {
            m_Log.error("Failed to create uniform {}!", name);
            return;
        }

        m_Uniforms.put(name, location);
    }

    public void setUniformiv(String name, int[] values) {
        glUniform1iv(uniformLocation(name), values);
    }

    public void setUniform1i(String name, int value) {
        glUniform1i(uniformLocation(name), value);
    }

    public void setUniformBool(String name, boolean value) {
        setUniform1i(name, value ? GL_TRUE : GL_FALSE);
    }

    public void setUniform1f(String name, float value) {
        glUniform1f(uniformLocation(name), value);
    }

    public void setUniform2f(String name, float value, float value2) {
        glUniform2f(uniformLocation(name), value, value2);
    }

    public void setUniform2f(String name, Vector2f value) {
        setUniform2f(name, value.x, value.y);
    }

    public void setUniform3f(String name, float value, float value2, float value3) {
        glUniform3f(uniformLocation(name), value, value2, value3);
    }

    public void setUniform3f(String name, Vector3f value) {
        setUniform3f(name, value.x, value.y, value.z);
    }

    public void setUniformMat4(String name, Matrix4f value) {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.callocFloat(16);
            value.get(buffer);
            glUniformMatrix4fv(uniformLocation(name), false, buffer);
        }
    }

    private int uniformLocation(String name) {
        return m_Uniforms.get(name);
    }

    private int linkShader(int vert, int frag) {
        int program = glCreateProgram();

        glAttachShader(program, vert);
        glAttachShader(program, frag);

        glLinkProgram(program);
        if(glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
            m_Log.error("Shader Program failed to link properly!\n{}", glGetProgramInfoLog(program, 1024));
            glDeleteProgram(m_ProgramId);
            return -1;
        }

        glValidateProgram(program);
        if(glGetProgrami(program, GL_VALIDATE_STATUS) == GL_FALSE)
            m_Log.error("There was a problem validating the Shader Program!\n{}", glGetProgramInfoLog(program, 1024));

        return program;
    }

    private int createShader(String source, int type) {
        int shader = glCreateShader(type);

        glShaderSource(shader, source);
        glCompileShader(shader);

        if(glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            m_Log.error("Shader failed to compile!\n{}", glGetShaderInfoLog(shader, 1024));
            glDeleteShader(shader);
            return -1;
        }

        return shader;
    }

}
