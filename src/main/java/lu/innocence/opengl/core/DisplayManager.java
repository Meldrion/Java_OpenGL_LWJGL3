package lu.innocence.opengl.core;

import lu.innocence.opengl.core.exception.GLFWException;
import lu.innocence.opengl.core.maths.Vector2f;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class DisplayManager {

    private static final Logger LOGGER = LogManager.getLogger(DisplayManager.class);
    private long window;
    private static Vector2f windowSize;
    private static final int MIN_RENDER_TIME = 16;

    @SuppressWarnings("FieldCanBeLocal")
    private Renderer renderer;
    private RenderInterface renderInterface;

    private void init(int width,int height, String title) throws GLFWException {

        DisplayManager.setWindowSize(width,height);

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);


        // Create the window
        this.window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (this.window == NULL)
            throw new GLFWException("Failed to create the GLFW window");

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );

        } // the stack frame is popped automatically

        // Resize Callback
        glfwSetWindowSizeCallback(window,new GLFWWindowSizeCallback(){
            @Override
            public void invoke(long window, int width, int height) {
                DisplayManager.setWindowSize(width,height);
                glViewport(0,0,width,height);
            }

        });

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

    }

    public void run(RenderInterface renderInterface,int width,int height,String title) throws GLFWException {

        LOGGER.info("LWJGL - {} ",Version.getVersion());

        this.renderInterface = renderInterface;

        init(width,height,title);
        loop(width,height);

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
    }

    private void loop(int width,int height) {

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        glfwSwapInterval(1);
        glfwMakeContextCurrent(this.window);

        this.renderer = new Renderer();
        renderer.prepare();

        try {
            this.renderInterface.create();
        } catch (Exception e) {
            LOGGER.error(e);
            System.exit(-1);
        }

        // Make the window visible
        glfwShowWindow(this.window);
        long delta;

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {
            long beforeRenderingTime = System.currentTimeMillis();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            if (this.renderInterface != null)
                this.renderInterface.render(this.renderer);

            glfwSwapBuffers(window); // swap the color buffers
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();

            // Get Delta Value and Pause the rendering if needed
            long afterRendering = System.currentTimeMillis();
            delta = afterRendering - beforeRenderingTime;
            this.watchRenderTime(delta);
        }
    }

    private void watchRenderTime(long delta) {
        // Dont use to much CPU
        if (delta < MIN_RENDER_TIME) {
            try {
                Thread.sleep(MIN_RENDER_TIME - delta);
            } catch (InterruptedException e) {
                LOGGER.error(e);
                Thread.currentThread().interrupt();
            }
        }
        // Frame Skipping
        if (delta > MIN_RENDER_TIME) {
            try {
                Thread.sleep(delta - MIN_RENDER_TIME);
            } catch (InterruptedException e) {
                LOGGER.error(e);
                Thread.currentThread().interrupt();
            }
        }
    }

    public static Vector2f getWindowSize() {
        return windowSize;
    }

    public static void setWindowSize(int width,int height) {
        DisplayManager.windowSize = new Vector2f(width,height);
    }

    public Vector2f getCursorPosition() {
        DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(this.window, xBuffer, yBuffer);
        float x = (float) xBuffer.get(0);
        float y = (float) yBuffer.get(0);

        return new Vector2f(x,y);
    }

}
