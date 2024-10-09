package com.dt042g.project.views;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.List;

/**
 * Custom JPanel used to display all the information about a task,
 * includes two buttons. One button that is used to return to
 * the list of all tasks, and another one to delete the task.
 *
 * @author josef alirani
 */
public class TaskDisplay extends JPanel {

    /**
     * Constructor for the TaskDisplay class, initializes the panel
     * with information about the task received from parameters.
     *
     * @param task the task to display
     * @param listener an ActionListener that the buttons will
     * communicate with.
     * @test check if clicking the buttons signals to the listener.       
     */
    public TaskDisplay(List<String> task, ActionListener listener) {
        this.setBounds(0, 0, 400, 400);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane();
        this.add(scrollPane);

        JLabel titleText;
        JButton returnButton = new JButton("return");
        returnButton.addActionListener(e -> {
            listener.actionPerformed(new ActionEvent(this,
                    ActionEvent.ACTION_PERFORMED, "return"));
        });

        JButton deleteButton = new JButton("delete task");
        deleteButton.addActionListener(e -> {
            listener.actionPerformed(new ActionEvent(this,
                    ActionEvent.ACTION_PERFORMED, "delete"));
        });
        this.add(returnButton);

        if(task.size() != 4) {
            titleText = new JLabel("this task does not exist!");

            this.add(titleText);
        }
        else {
            titleText = new JLabel(task.get(0));
            JLabel infoText = new JLabel(task.get(1));
            JLabel dateText = new JLabel(task.get(2));
            JLabel categoryText = new JLabel(task.get(3));

            this.add(titleText);
            this.add(infoText);
            this.add(dateText);
            this.add(categoryText);
        }

        this.add(deleteButton);
    }
}
