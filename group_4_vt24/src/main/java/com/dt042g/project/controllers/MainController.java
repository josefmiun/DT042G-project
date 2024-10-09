package com.dt042g.project.controllers;

import com.dt042g.project.Database;
import com.dt042g.project.views.AppWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Class used to manage and initialize the specific controllers
 *
 * @author Josef alirani
 */
public class MainController implements ActionListener {

    private final AppWindow window;

    /**
     * Constructor for class, initiates the AppWindow,
     * and finally, calls runController to start the program.
     */
    public MainController() {
        window = new AppWindow();

        runController(new AllTasksController(this));
    }

    /**
     * Function used to run a specified controller that's
     * implementing the ControllerInterface.
     *
     * @param controller The controller to run.
     */
    private void runController(ControllerInterface controller) {
        controller.doWork(window);
    }

    /**
     * Method for handling events created in child controllers.
     * Used for swapping which controller to run, or closing the
     * program.
     *
     * @param e the event to be processed
     * @todo add events to process.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("back")) {
            runController(new AllTasksController(this));
        }
        else if(e.getActionCommand().equals("switch_to_categories")) {
            runController(new CategoriesController(this));
        }
        else if(e.getActionCommand().equals("switch_to_tasks")) {
            runController(new AllTasksController(this));
        }
        else {
            List<String> tasks = Database.getTask(e.getActionCommand());
            runController(new TaskController(tasks, this));
        }
    }
}
