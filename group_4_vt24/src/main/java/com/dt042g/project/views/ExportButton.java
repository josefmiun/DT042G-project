package com.dt042g.project.views;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * View class button responsible used to export a task,
 * by triggering an event to a controller.
 *
 * @author josef alirani
 */
public class ExportButton extends JButton {

    /**
     * Constructor for ExportButton class, initializes
     * the button and sets the actionListener who is
     * notified when a user presses this button.
     *
     * @param listener the ActionListener that this
     * class will communicate to.
     */
    public ExportButton(ActionListener listener) {
        super("Export task");

        this.setBounds(0,0,200, 30);

        this.addActionListener(e -> listener.actionPerformed(new ActionEvent(this,
                ActionEvent.ACTION_PERFORMED, "export_task")));

        this.setVisible(true);
    }
}
