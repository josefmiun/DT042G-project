package com.dt042g.project.views;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Custom JButton class used for switching between
 * AllTasksController and CategoriesController.
 *
 * @author josef alirani
 */
public class SwitchButton extends JButton {

    /**
     * Constructor for SwitchButton class, initializes
     * the button's dimensions, text displayed and
     * the ActionListener to communicate to when pressed.
     *
     * @param name the name/text displayed on the button.
     * @param listener the ActionListener to communicate to
     * when pressed.
     */
    public SwitchButton(String name, ActionListener listener) {
        super(name);
        this.setBounds(200,0,200, 30);

        this.addActionListener(e -> listener.actionPerformed(new ActionEvent(this,
                ActionEvent.ACTION_PERFORMED, "switch")));

        this.setVisible(true);
    }
}
