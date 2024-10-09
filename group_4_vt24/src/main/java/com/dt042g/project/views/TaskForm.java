package com.dt042g.project.views;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.List;

/**
 * Custom JPanel class that is used to display a form containing
 * fields that a user can input to, in order to create new tasks.
 *
 * @author josef alirani
 */
public class TaskForm extends JPanel {
    private final JTextField name;
    private final JTextField info;
    private final JTextField date;
    private final JTextField category;

    /**
     * Constructor for TaskForm class, initializes the
     * panel and adds the text fields that will be used,
     * as well as a button to submit the task.
     *
     * @param listener ActionListener that the submit button
     * will signal to.
     */
    public TaskForm(ActionListener listener) {
        this.setBounds(0, 460, 400, 320);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        name = new JTextField(); // 16
        name.setSize(400, 20);

        info = new JTextField(); // 50
        info.setSize(400, 20);

        date = new JTextField(); // 12
        date.setSize(400, 20);
        JButton submitButton = new JButton("submit");
        submitButton.addActionListener(e -> listener.actionPerformed(new ActionEvent(this,
                ActionEvent.ACTION_PERFORMED,
                "submit_task")));
        category = new JTextField(); // 12
        category.setSize(400, 20);

        JLabel titleLabel = new JLabel("name:");
        this.add(titleLabel);
        this.add(name);

        JLabel infoLabel = new JLabel("description:");
        this.add(infoLabel);
        this.add(info);

        JLabel dateLabel = new JLabel("date (YYYY-MM-DD):");
        this.add(dateLabel);
        this.add(date);

        JLabel catLabel = new JLabel("category:");
        this.add(catLabel);
        this.add(category);

        this.add(submitButton);

        this.setVisible(true);
    }

    /**
     * Getter function used to get the text inside the
     * "title" text field.
     *
     * @return text inside the "title" text field.
     */
    public String getName() {
        return name.getText();
    }

    /**
     * Getter function used to get the text inside the
     * "information" text field.
     *
     * @return text inside the "information" text field.
     */
    public String getInfo() {
        return info.getText();
    }

    /**
     * Getter function used to get the text inside the
     * "date" text field.
     *
     * @return text inside the "date" text field.
     */
    public String getDate() {
        return date.getText();
    }

    /**
     * Getter function used to get the text inside the
     * "categories" text field.
     *
     * @return text inside the "categories" text field.
     */
    public String getCategory() {return category.getText();}

    /**
     * Function used to check if the form is filled out or not.
     *
     * @return boolean value signaling whether the form is filled
     * out or not.
     */
    public boolean isFilledOut(){
        ArrayDeque<JTextField> values = new ArrayDeque<>(List.of(new JTextField[]{name,
                info, date, category}));

        boolean check = true;
        for(JTextField value: values) {
            if(value.getText().isEmpty()) {
                check = false;
            }
        }
        return check;
    }
}
