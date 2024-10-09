package com.dt042g.project.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;

/**
 * Custom JScrollPane class used to display all tasks in the
 * form of a list that is scrollable.
 *
 * @author josef alirani
 */
public class TasksPanel  extends JScrollPane {

    private final JPanel panel;

    /**
     * Constructor for TasksPanel class, initializes the
     * panel by setting its layout and adding a JPanel.
     */
    public TasksPanel() {
        this.setBounds(0, 60, 390, 400);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.setPreferredSize(new Dimension(400, 200));
        this.setViewportView(panel);
        this.setVisible(true);
    }

    /**
     * Function used to add tasks to the list displayed on
     * the panel.
     *
     * @param tasks an arrayDeque containing the titles of tasks.
     * @param listener the ActionListener that will be signaled to
     * when a task is pressed.
     */
    public void addTasks(ArrayDeque<String> tasks, ActionListener listener) {

        clearTasksPanel();

        int i = 1;
        for(String task: tasks) {
            JButton label = new JButton(i+": "+ task);
            label.addActionListener(e -> listener.actionPerformed(new ActionEvent(this,
                    ActionEvent.ACTION_PERFORMED, task)));
            panel.add(label);
            i++;
        }
        this.setViewportView(panel);
        this.setVisible(true);
    }

    /**
     * Private function used to reset the panel to an empty list.
     * @test check if the function actually removes the components.
     */
    private void clearTasksPanel() {
        panel.removeAll();
        panel.revalidate();

        this.setViewportView(panel);

        panel.setVisible(true);
    }

    /**
     * Function used to check if a certain task exists.
     *
     * @return a boolean representing if the task exists or not
     */
    public boolean containsTask(String task) {
        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (button.getText().contains(task)) {
                    return true;
                }
            }
        }
        return false;
    }
}
