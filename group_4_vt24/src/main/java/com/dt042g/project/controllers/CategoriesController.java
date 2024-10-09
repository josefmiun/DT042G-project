package com.dt042g.project.controllers;

import com.dt042g.project.Database;
import com.dt042g.project.views.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Controller class used to manage/view the categories stored
 * in the database.
 *
 * @author josef alirani
 */
public class CategoriesController implements ControllerInterface, ActionListener {

    private final ActionListener listener;
    private SearchBar searchBar;
    private SwitchButton switchButton;
    private CategoryForm categoryForm;
    private CategoriesPanel categoriesPanel;

    /**
     * Constructor class for CategoriesController class, sets
     * the ActionListener to communicate to when attempting to
     * switch to another controller.
     *
     * @param listener the actionListener to communicate to.
     */
    public CategoriesController(ActionListener listener) {
        this.listener = listener;
    }

    /**
     * Function used to add the view components to the AppWindow.
     *
     * @param window AppWindow instance that the controller will use.
     */
    @Override
    public void doWork(AppWindow window) {
        // retrieves category names from database
        HashMap<String, String> categories = Database.getCategories();

        SwingUtilities.invokeLater(()-> {
            window.clearWindow();

            searchBar = new SearchBar(this);
            window.addPanel(searchBar);

            switchButton = new SwitchButton("Tasks", this);
            window.addPanel(switchButton);

            categoriesPanel = new CategoriesPanel();
            categoriesPanel.addCategories(categories, this);

            window.addPanel(categoriesPanel);

            categoryForm = new CategoryForm(this);
            window.addPanel(categoryForm);

            window.setVisible(true);
        });
    }

    /**
     * Function used to add a new category to the database.
     *
     */
    private void addCategory() {
        if(categoryForm.isFilledOut()) {
            List<String> newCategory = new ArrayList<>();
            newCategory.add(categoryForm.getName());
            newCategory.add(categoryForm.getInfo());

            try {
                Database.addCategory(newCategory);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Function used to search for categories containing a
     * specified string in the database, and adding the results to
     * the list of categories.
     *
     * @param query search word used to find a specific
     * in the database.
     */
    private void performSearch(String query) {
        SwingUtilities.invokeLater(() -> {
            categoriesPanel.addCategories(Database.getSearchedCategories(query), this);
        });
    }

    /**
     * Function used to delete a specified category from the database.
     *
     * @param name the name of the category to delete.
     * @return boolean representing the result of attempting to delete
     * the category.
     */
    private boolean deleteCategory(String name) {
        if(!Database.getCategory(name).isEmpty()) {
            try {
                Database.deleteCategory(name);
            } catch (IllegalArgumentException | IllegalStateException e) { // alerts user
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            return true;
        }
        return false;
    }

    /**
     * Method used to process events from the view-components.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("switch")) {
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "switch_to_tasks"));
        }
        else if(e.getActionCommand().equals("submit_category")) {
            addCategory();
        }
        else if(e.getActionCommand().equals("search")) {
            performSearch(searchBar.getQuery());
        }
        else {
            boolean result = deleteCategory(e.getActionCommand());
        }
    }
}
