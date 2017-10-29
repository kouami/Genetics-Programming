package com.genetics.programming.gpgraphics;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 *
 * @author ekakoll
 */
public class GPGraphics extends JFrame {

    private GPanel mainPanel;
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 550;
    private static Point point = new Point();

    public GPGraphics() {
        this.setUndecorated(true);

        mainPanel = new GPanel();
        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));

        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setTitle("GP");
        centerOnScreen(this);
        setResizable(false);
        setVisible(true);
        setFocusable(true);

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new MyDispatcher());
    }

    private void centerOnScreen(Window w) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        w.setLocation(screenSize.width / 2 - (w.getWidth() / 2), screenSize.height / 2 - (w.getHeight() / 2));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        /*
         * SwingUtilities.invokeLater(new Runnable() {
         *
         * @Override public void run() { new GPGraphics();
         *
         * }
         * });
         */
        final GPGraphics gp = new GPGraphics();
        gp.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                point.x = e.getX();
                point.y = e.getY();
            }
        });
        gp.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                Point p = gp.getLocation();
                gp.setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);
            }
        });
    }

    private class MyDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                processKey(e);
            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                processKey(e);
            } else if (e.getID() == KeyEvent.KEY_TYPED) {
                processKey(e);
            }
            return false;
        }
    }

    private void processKey(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_C) {
            dispose();
            System.exit(0);
        }
    }
}

