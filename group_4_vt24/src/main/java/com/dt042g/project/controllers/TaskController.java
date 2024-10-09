package com.dt042g.project.controllers;

import com.dt042g.project.Database;
import com.dt042g.project.views.AppWindow;
import com.dt042g.project.views.ExportButton;
import com.dt042g.project.views.TaskDisplay;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Controller class responsible for displaying information about a specific task.
 *
 * @author josef alirani
 */
public class TaskController implements ControllerInterface, ActionListener {
    private final List<String> task;
    private final ActionListener listener;

    /**
     * Constructor for TaskController class, sets information
     * about a task that will be displayed.
     *
     * @param newTask List containing information surrounding a task.
     * @param listener ActionListener that this class will send events to.
     */
    public TaskController(List<String> newTask, ActionListener listener) {
        this.task = newTask;
        this.listener = listener;
    }

    /**
     * Function used to add the TaskDisplay component to the AppWindow.
     *
     * @param window AppWindow instance that the controller will use.
     */
    @Override
    public void doWork(AppWindow window) {
        SwingUtilities.invokeLater(() -> {
            window.clearWindow();

            ExportButton exportButton = new ExportButton(this);
            window.addPanel(exportButton);

            TaskDisplay display = new TaskDisplay(task, this);

            window.addPanel(display);

            window.setVisible(true);
        });
    }

    /**
     * Function used to export the currently displayed task to a JSON-file.
     * If the name of the file already exists, a recursive function call is
     * made while adding a '-1' at the end of the name.
     *
     * @param name the name of the file.
     */
    private void exportTask(String name) {
        if(new File("tasks/"+name+".json").isFile()) {
            exportTask(name+"-1");
        }
        else {
            File output = new File("tasks/"+name+".json");
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(output));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", task.get(0));
                jsonObject.put("description", task.get(1));
                jsonObject.put("category", task.get(2));
                jsonObject.put("date", task.get(3));

                writer.write(jsonObject.toJSONString());
                writer.close();

            } catch (IOException e) {
                System.out.println("this file could not be read: "+e.getMessage());
            }
        }
    }

    /**
     * Function used to process events from the TaskDisplay component.
     *
     * @param e the event to be processed
     * @test check whether correct values are displayed.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("return")) {
            listener.actionPerformed(new ActionEvent(this,
                    ActionEvent.ACTION_PERFORMED, "back"));
        }
        else if (e.getActionCommand().equals("delete")) {
            Database.deleteTask(task.get(0));
        }
        else if (e.getActionCommand().equals("export_task")) {
            exportTask(task.get(0));
        }
    }
}
