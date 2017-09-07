package lu.innocence.testing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Created by Fabien Steines
 * Last Update on: 27.02.2017.
 */
public class SWT_Testing {

    private static final Logger LOGGER = LogManager.getLogger(SWT_Testing.class);
    private TestCanvas mapCanvas;

    public static void main(String[] args) {
        new SWT_Testing();
    }

    private SWT_Testing() {

        int minClientWidth = 640;
        int minClientHeight = 480;
        final Display display = new Display();

        final Shell shell = new Shell(display, SWT.SHELL_TRIM);

        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        shell.setLayout(gridLayout);

        try {
            this.mapCanvas = new TestCanvas(display,shell);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        this.createBottomBar(shell);


        int dw = shell.getSize().x - shell.getClientArea().width;
        int dh = shell.getSize().y - shell.getClientArea().height;
        shell.setMinimumSize(minClientWidth + dw, minClientHeight + dh);

        shell.setText("Ignis");
        shell.setSize(800, 600);
        shell.pack();
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }

        display.dispose();
    }

    /**
     * @param shell
     */
    private void createBottomBar(Shell shell) {
        Composite pageComposite = new Composite(shell, SWT.NONE);
        pageComposite.setLayout(new FillLayout());
        GridData gridDataLeft = new GridData();
        gridDataLeft.verticalAlignment = GridData.FILL;
        gridDataLeft.grabExcessVerticalSpace = false;
        gridDataLeft.horizontalAlignment = GridData.FILL;
        gridDataLeft.grabExcessHorizontalSpace = true;
        gridDataLeft.horizontalSpan = 2;
        pageComposite.setLayoutData(gridDataLeft);

        Label label = new Label(pageComposite, SWT.NONE);
        label.setText("Position: ");
    }



}

