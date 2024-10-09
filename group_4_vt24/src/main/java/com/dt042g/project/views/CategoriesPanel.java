package com.dt042g.project.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Custom Swing component class used to display categories
 * in a scrollable list.
 *
 * @author josef alirani
 */
public class CategoriesPanel extends JScrollPane {
    private final JPanel panel;

    /**
     * Constructor for CategoriesPanel class, initializes
     * the swing component's dimensions and properties.
     */
    public CategoriesPanel() {
        this.setBounds(0, 60, 390, 400);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.setPreferredSize(new Dimension(400, 200));
        this.setViewportView(panel);
        this.setVisible(true);
    }

    /**
     * Adds a JPanel containing the name, description and a delete-button
     * for each category in the hashmap of values to the JPanel
     * 'panel'.
     *
     * @param values hashmap of categories to add.
     * @param listener the ActionListener which will handle the
     * 'delete' event from the delete-buttons.
     */
    public void addCategories(HashMap<String, String> values, ActionListener listener) {
        clearCategoriesPanel();

        int i = 1;
        for(Map.Entry<String, String> category: values.entrySet()) {
            JPanel listPanel = new JPanel();
            JLabel label = new JLabel(i+": ["+ category.getKey()+"]");
            JLabel description = new JLabel(category.getValue());
            JButton button = new JButton("delete");
            button.addActionListener(e -> listener.actionPerformed(new ActionEvent(this,
                    ActionEvent.ACTION_PERFORMED, category.getKey())));
            listPanel.add(label);
            listPanel.add(description);
            listPanel.add(button);
            panel.add(listPanel);
            i++;
        }
        this.setViewportView(panel);
        this.setVisible(true);
    }

    /**
     * Function used to find a category with a specific name
     * in the JPanel 'panel'.
     *
     * @param name the name of the category to search for.
     * @return boolean representing whether the category was
     * found or not.
     */
    public boolean containsCategory(String name) {
        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel jPanel) {
                Component[] subComponents = jPanel.getComponents();
                for(Component subComponent: subComponents) {
                    if (subComponent instanceof JLabel label) {
                        if (label.getText().contains(name)) {
                            return true;
                        }
                        else {
                            break;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Function used to remove all components from the
     * class variable 'panel' which is used to display
     * categories.
     */
    public void clearCategoriesPanel() {
        panel.removeAll();
        panel.revalidate();

        this.setViewportView(panel);

        panel.setVisible(true);
    }
}
