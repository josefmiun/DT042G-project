package com.dt042g.project;

import javax.swing.*;

import com.dt042g.project.controllers.MainController;
import com.dt042g.project.views.AppWindow;

/**
 * main class for the project, responsible for initiating the program.
 *
 * @author josef alirani
 */
public class Project {

    /** Private constructor for the class. */
    private Project() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Main function of the program, since the program uses the
     * Swing API, it initiates by calling the SwingUtilities
     * function "invokeLater()".
     *
     * @param args Program's starting parameters
     */
    public static void main(String[] args){

        SwingUtilities.invokeLater(MainController::new);
    }
}
