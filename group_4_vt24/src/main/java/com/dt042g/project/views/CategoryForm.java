package com.dt042g.project.views;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.List;

/**
 * Custom JPanel class that is used to display a form containing
 * fields that a user can input to, in order to create new categories.
 *
 * @author Kevin Rosenbergs
 * @author josef alirani
 */
public class CategoryForm extends JPanel {

    private final JTextField name;
    private final JTextField info;

    /**
     * Constructor for CategoryForm class, initializes the
     * panel and adds the text fields that will be used,
     * as well as a button to submit the category.
     *
     * @param listener ActionListener that the submit button
     * will signal to.
     */
    public CategoryForm(ActionListener listener) {
        this.setBounds(0, 460, 400, 320);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        name = new JTextField();
        name.setSize(400, 20);

        info = new JTextField();
        info.setSize(400, 20);

        JButton submitButton = new JButton("submit");
        submitButton.addActionListener(e -> listener.actionPerformed(new ActionEvent(this,
                ActionEvent.ACTION_PERFORMED,
                "submit_category")));

        JLabel titleLabel = new JLabel("name:");
        this.add(titleLabel);
        this.add(name);
        JLabel infoLabel = new JLabel("description:");
        this.add(infoLabel);
        this.add(info);

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
     * Function used to check if the form is filled out or not.
     *
     * @return boolean value signaling whether the form is filled
     * out or not.
     */
    public boolean isFilledOut(){
        ArrayDeque<JTextField> values = new ArrayDeque<>(List.of(new JTextField[]{name, info}));

        boolean check = true;
        for(JTextField value: values) {
            if(value.getText().isEmpty()) {
                check = false;
            }
        }
        return check;
    }
}

