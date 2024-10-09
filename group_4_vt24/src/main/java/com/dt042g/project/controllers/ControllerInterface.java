package com.dt042g.project.controllers;

import com.dt042g.project.views.AppWindow;

/**
 * Interface to be used by controllers, defines methods that
 * all controller classes must implement.
 *
 * @author josef alirani
 */
public interface ControllerInterface {

    /**
     * Function used to run/initiate a controller.
     *
     * @param window AppWindow instance that the controller will use.
     */
    void doWork(AppWindow window);
}
