package com.dt042g.project.views;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Custom JPanel class used to search for tasks.
 *
 * @author josef alirani
 */
public class SearchBar extends JPanel {

    private final JTextField textField;

    /**
     * Constructor for SearchBar class, initializes the
     * panel by adding a JTextField that the user can input to,
     * and a button that starts the search.
     *
     * @param listener ActionListener that the search button signals to.
     * @test check if clicking the search button signals to the listener.
     */
    public SearchBar(ActionListener listener) {
        this.setBounds(0,30,400, 30);

        textField = new JTextField(24);
        JButton searchButton = new JButton("search");
        searchButton.addActionListener(e -> listener.actionPerformed(new ActionEvent(this,
                ActionEvent.ACTION_PERFORMED, "search")));

        this.add(textField);
        this.add(searchButton);
        this.setVisible(true);
    }

    /**
     * Function used to get the text that
     * a user wrote inside the text field.
     *
     * @return the entered search query.
     */
    public String getQuery() {
        return textField.getText();
    }
}
