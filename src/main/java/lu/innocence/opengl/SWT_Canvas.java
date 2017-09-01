package lu.innocence.opengl;

import lu.innocence.opengl.core.DisplayManager;
import lu.innocence.opengl.core.FrameHandler;
import lu.innocence.opengl.core.Renderer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.lwjgl.opengl.GL;
//import org.lwjgl.opengl.GLContext;

import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;

/**
 * Created by Fabien Steines
 * Last Update on: 03.03.2017.
 */
public abstract class SWT_Canvas {

    private final Rectangle rect;

    private GLCanvas canvas;
    private Renderer renderer;
    private static final Logger LOGGER = LogManager.getLogger(SWT_Canvas.class);

    public SWT_Canvas(Display display, Composite parent) {

        LOGGER.info("Created OpenGL Canvas");

        GLData data = new GLData();
        data.doubleBuffer = true;
/*        this.canvas = new GLCanvas(parent, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL |
                SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE, data);*/

        this.canvas = new GLCanvas(parent, SWT.MULTI |
                SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE, data);

        // Show or hide scrollbar when needed
        //ScrollHelper.addScollListener(this.canvas);

        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        canvas.setLayoutData(gridData);

        canvas.setCurrent();
        GL.createCapabilities(false);

        rect = new Rectangle(0, 0, 0, 0);
        canvas.addListener(SWT.Resize, event -> updateOpenGLContext());

        renderer = new Renderer();

        // First init
        this.updateOpenGLContext();

        glClearColor(0.6f, 0.6f, 0.6f, 1.0f);

        // Render
        display.asyncExec(this.createRendering(display));
    }

    private Runnable createRendering(Display display) {
        return new Runnable() {
            long delta = 0;
            @Override
            public void run() {
                if (!canvas.isDisposed()) {
                    long beforeRenderingTime = System.currentTimeMillis();
                    canvas.setCurrent();

                    render(delta,renderer);

                    canvas.swapBuffers();
                    long afterRendering = System.currentTimeMillis();
                    delta = afterRendering - beforeRenderingTime;

                    display.asyncExec(this);
                    FrameHandler.watchRenderTime(delta);
                }
            }
        };
    }

    private void updateOpenGLContext() {
        Rectangle bounds = canvas.getBounds();
        rect.width = bounds.width;
        rect.height = bounds.height;
        DisplayManager.setWindowSize(rect.x,rect.y);
        glViewport(0,0,rect.width,rect.height);
    }

    public abstract void render(long delta,Renderer renderer);

    Canvas getCanvasHandle() {
        return this.canvas;
    }

}
