package com.dt042g.project.views;

import javax.swing.*;
import java.awt.*;

/**
 * View class extending the JFrame class, represents
 * the window which the content of the program will be
 * shown in.
 *
 * @author josef alirani
 */
public class AppWindow extends JFrame {

    /**
     * Constructor for the AppWindow class, responsible
     * for setting the JFrame's bounds, title and settings.
     */
    public AppWindow() {
        super("TO-DO list app");
        this.setSize(400,810);
        this.getContentPane().setBackground(Color.BLACK);

        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);

        this.setVisible(true);
        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * Function used to add a JPanel component to the window, if
     * successful, it repaints the window.
     *
     * @test check if panel is correctly added or not.
     * @param panel The JPanel component to add.
     * @return returns true if successful, false if not.
     */
    public boolean addPanel(JComponent panel) {
        try {
            this.add(panel);
            this.setVisible(true);
            this.repaint();
            return true;
        }
        catch(NullPointerException err) {
            err.printStackTrace();
            return false;
        }
    }

    /**
     * Function used to remove all components from the window,
     * and set its layout to Null before repainting the window.
     *
     * @test check if components are correctly removed or not.
     * @return returns true if successful, false if there were no
     * components to remove.
     */
    public boolean clearWindow() {
        if(this.getComponentCount() > 0) {
            this.getContentPane().removeAll();
            this.setLayout(null);
            this.repaint();

            return true;
        }
        else {
            return false;
        }
    }
}
