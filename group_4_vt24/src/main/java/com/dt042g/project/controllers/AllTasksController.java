package com.dt042g.project.controllers;

import com.dt042g.project.Database;
import com.dt042g.project.views.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controller class that implements ControllerInterface, responsible
 * for printing and managing events from the task search bar, task list
 * and the task form.
 *
 * @author josef alirani
 * @author Kevin Rosenbergs
 */
public class AllTasksController implements ControllerInterface, ActionListener {

    private final ActionListener listener;
    private SearchBar searchBar;
    private TasksPanel tasksPanel;
    private TaskForm taskForm;
    private ImportButton importButton;

    /**
     * Constructor for the class, adds an actionListener to the
     * class that will be used to swap to another controller.
     *
     * @param listener ActionListener to add.
     */
    public AllTasksController(ActionListener listener) {
        this.listener = listener;
    }

    /**
     * Function used to add the controller's components to the
     * AppWindow frame.
     *
     * @param window AppWindow JFrame to add components to.
     */
    @Override
    public void doWork(AppWindow window) {
        // retrieves task names from database
        ArrayDeque<String> tasks = Database.getTaskNames();

        SwingUtilities.invokeLater(() -> {
            window.clearWindow();

            importButton = new ImportButton(this);
            window.addPanel(importButton);

            SwitchButton switchButton = new SwitchButton("Categories", this);
            window.addPanel(switchButton);

            searchBar = new SearchBar(this);
            window.addPanel(searchBar);
            tasksPanel = new TasksPanel();

            tasksPanel.addTasks(tasks, this);
            window.addPanel(tasksPanel);

            taskForm = new TaskForm(this);
            window.addPanel(taskForm);


            window.setVisible(true);
        });

    }

    /**
     * Private function used to perform a query to the
     * database and add the result to a "TasksPanel"
     * variable.
     *
     * @param query search word used to find a specific
     * in the database.
     */
    private void performSearch(String query) {
        ArrayDeque<String> tasks = Database.getSearchedTaskNames(query);

        SwingUtilities.invokeLater(() -> {
            tasksPanel.addTasks(tasks, this);
        });
    }

    /**
     * Function used to add a new task to the database.
     *
     */
    private boolean addTask() {
        if(taskForm.isFilledOut() && isCorrectFormat(taskForm.getDate()) && isValidName(taskForm.getName())) {
            List<String> newTask = new ArrayList<>();
            newTask.add(taskForm.getName());
            newTask.add(taskForm.getInfo());
            newTask.add(taskForm.getCategory());
            newTask.add(taskForm.getDate());

            try {
                Database.addTask(newTask);
            } catch (IllegalArgumentException | IllegalStateException e) { // alerts user
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            return true;
        }
        return false;
    }

    /**
     * Function used to add a task with values from a specified JSON file.
     *
     * @param file the file to read from.
     * @test display message that json file doesn't contain correct fields.
     */
    private void importTask(File file) {
        JSONParser parser = new JSONParser();
        try {
            List<String> newTask = new ArrayList<>();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(file));

            if(jsonObject.containsKey("name") && jsonObject.containsKey("date") && jsonObject.containsKey("description")
            && jsonObject.containsKey("category") && jsonObject.size() == 4) {

                if (isValidName((String) jsonObject.get("name")) && isCorrectFormat((String) jsonObject.get("date"))) {
                    newTask.add((String) jsonObject.get("name"));
                    newTask.add((String) jsonObject.get("description"));
                    newTask.add((String) jsonObject.get("category"));
                    newTask.add((String) jsonObject.get("date"));

                    Database.addTask(newTask);
                }
            }

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Function used to validate that a specified date
     * is written in the correct format.
     *
     * @param date the date to validate
     * @return boolean result, specifying whether the
     * date is in the correct format or not.
     */
    private boolean isCorrectFormat(String date) {
        String validFormat = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(validFormat);
        try {
            Date tempDate = format.parse(date);

            if(format.format(tempDate).equals(date)) {
                return true;
            }

        } catch (java.text.ParseException e) {
            return false;
        }
        return false;
    }

    /**
     * Function used to check whether the name of a task is valid or not.
     * Because the program's components uses strings passed with eventListeners to,
     * a task shouldn't be able to be named as one of these events.
     *
     * @param name the string value to check.
     * @return boolean result, specifying whether the
     * name is valid or not.
     */
    private boolean isValidName(String name) {
        String[] array = {"return", "back", "search", "submit_task", "submit_category", "delete", "import_task",
                "export_task", "switch", "switch_to_tasks", "switch_to_categories"};

        for(String invalidName: array) {
            if(name.equals(invalidName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Function used to listen for events from the controller's
     * components.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand().equals("search")) {
            performSearch(searchBar.getQuery());
        }
        else if(e.getActionCommand().equals("submit_task")) {
            addTask();
        }
        else if(e.getActionCommand().equals("import_task")) {
            JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            fileChooser.setFileFilter(new FileNameExtensionFilter("JSON FILES", "json"));

            int result = fileChooser.showOpenDialog(null);
            if(result == JFileChooser.APPROVE_OPTION) {
                importTask(fileChooser.getSelectedFile());
            }
        }
        else if(e.getActionCommand().equals("switch")) {
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "switch_to_categories"));
        }
        else {
            listener.actionPerformed(new ActionEvent(this,
                    ActionEvent.ACTION_PERFORMED, e.getActionCommand()));
        }
    }
}
