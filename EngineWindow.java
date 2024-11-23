package com.wz.engine;

import com.wz.engine.input.Mouse;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

public class EngineWindow {
    //creating varibles for our basic window characteristic//
    private int height;
    public IntBuffer bufferedHeight;
    public IntBuffer bufferedWidth;
    private GLFWVidMode videoMode;
    private int width;
    private String title;
    public long id;
    public static EngineWindow instance;

    public EngineWindow( int width, int height,String title) {
        instance=this;
        this.height = height;
        this.width = width;
        this.title = title;
    }


    public void create(){
       if(!GLFW.glfwInit()){
           System.err.println("GLW wasn't initialized");
           System.exit(-1);
       }
       this.id=GLFW.glfwCreateWindow(this.width,this.height,this.title,0,0);
       if(this.id==0){
           System.err.println("Window error");
           System.exit(-1);
       }
        this.videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
       try (MemoryStack mem = MemoryStack.stackPush()){
           this.bufferedHeight= BufferUtils.createIntBuffer(1);
           this.bufferedWidth = BufferUtils.createIntBuffer(1);
           GLFW.glfwGetWindowSize(this.id,this.bufferedWidth,this.bufferedHeight);

       }catch (Exception e){

       }
           GLFW.glfwSetWindowTitle(this.id,this.title);
           GLFW.glfwSetWindowSize(this.id,this.width,this.height);
           GLFW.glfwSetWindowAspectRatio(this.id,this.width,this.height);
           GLFW.glfwSetWindowPos(this.id,
                   ((this.videoMode.width()-this.bufferedWidth.get(0))/2),
                   ((this.videoMode.height()-this.bufferedHeight.get(0))/2));
           GLFW.glfwSetWindowSizeLimits(this.id,this.width,this.height,1920,1080);
           GLFW.glfwMakeContextCurrent(this.id);
           GL.createCapabilities();
           GL11.glViewport(0,0,this.bufferedWidth.get(0),this.bufferedHeight.get(0));
           Mouse.setMouseCallBacks(this.id);
    }



    public void update() {
        GLFW.glfwPollEvents();
        GLFW.glfwSwapBuffers(this.id);

    }

    public void destroy(){
        GLFW.glfwDestroyWindow(this.id);
    }

    public boolean isCloseRequest(){
        return GLFW.glfwWindowShouldClose(this.id);
    }
    public static EngineWindow getWindow(){
        return instance;
    }


    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
