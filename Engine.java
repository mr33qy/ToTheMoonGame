package com.wz.engine;

import com.wz.engine.input.Keyboard;
import com.wz.engine.input.Mouse;
import com.wz.engine.resources.Shader;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Engine {
    public static final int WIDTH=960;
    public static final int HEIGHT=540;
    public static final  String TITLE ="ENGINE 0.0.1 test";
    private EngineWindow engineWindow;
    private Shader shader;
    public void run() {
        init();
    }

    public void init() {
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_DEPTH_BITS, 24);

        this.engineWindow = new EngineWindow(WIDTH, HEIGHT, TITLE);
        this.engineWindow.create();
        GL46.glEnable(GL46.GL_DEPTH_TEST);
        this.shader = new Shader("/shaders/Rectangle.vert", "/shaders/Rectangle.frag");
        this.update();
    }

    public FloatBuffer storeDataInFloatBuffer(float[] data){

        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
    public IntBuffer storeDataInIntBuffer(int[] data){
        IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }


        public void update() {
            int[] indices = {0, 1, 2, 2, 3, 0};
            float[] vertices = {
                    -0.5f,  0.5f, 0.0f,  // Top-left
                    -0.5f, -0.5f, 0.0f,  // Bottom-left
                    0.5f, -0.5f, 0.0f,  // Bottom-right
                    0.5f,  0.5f, 0.0f   // Top-right
            };

        //Ibo
        int iboId= GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER,iboId);
        IntBuffer intBuffer =this.storeDataInIntBuffer(indices);
        GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER,intBuffer,GL46.GL_STATIC_DRAW);
        MemoryUtil.memFree(intBuffer);
        // Vao and Vbo
        int vaoId = GL46.glGenVertexArrays();
        GL46.glBindVertexArray(vaoId);
        GL46.glEnableVertexAttribArray(0);
        int vboId = GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboId);
        FloatBuffer buffer = storeDataInFloatBuffer(vertices);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, buffer, GL46.GL_STATIC_DRAW);
        GL46.glVertexAttribPointer(0, 3, GL46.GL_FLOAT, false, 0, 0);
        MemoryUtil.memFree(buffer);
        shader.bind();

            while (!this.engineWindow.isCloseRequest()) {
                Keyboard.handleKeyboardInput();
                Mouse.handleMouseInput();

                GL46.glClearColor(0, 1, 1, 1);
                GL46.glClearDepth(1.0);
                GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);

                GL46.glBindVertexArray(vaoId);
                GL46.glEnableVertexAttribArray(0);

                GL46.glDrawElements(GL46.GL_TRIANGLES, indices.length, GL46.GL_UNSIGNED_INT, 0);

                GL46.glDisableVertexAttribArray(0);
                GL46.glBindVertexArray(0);

                this.engineWindow.update();
            }

            this.engineWindow.destroy();
            shader.cleanup();
        }
    public EngineWindow getEngineWindow() {
        return this.engineWindow;
    }




}
