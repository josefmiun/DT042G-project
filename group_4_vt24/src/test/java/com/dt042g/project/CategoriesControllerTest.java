package com.dt042g.project;

import com.dt042g.project.controllers.CategoriesController;
import com.dt042g.project.views.CategoriesPanel;
import com.dt042g.project.views.CategoryForm;
import com.dt042g.project.views.SearchBar;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Class containing tests for the CategoriesController class and its
 * components.
 *
 * @author josef alirani
 */
public class CategoriesControllerTest {

    /**
     * Test for checking if the controller sends the correct
     * event command to the main controller. The expected result
     * is that the command 'switch_to_tasks' is sent.
     */
    @Test
    public void testSwitchEvent() {
        ActionListener mockListener = e -> {assertEquals("switch_to_tasks",e.getActionCommand());};

        CategoriesController controller = new CategoriesController(mockListener);

        controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "switch"));
    }

    /**
     * Test for adding a category to the CategoriesPanel class. The expected result
     * is that the panel contains the added category 'work'.
     */
    @Test
    public void testAddingCategories() {
        HashMap<String, String> mockCategories = new HashMap<>();
        mockCategories.put("work", "this is a description");
        CategoriesPanel panel = new CategoriesPanel();

        ActionListener mockListener = e -> {};

        panel.addCategories(mockCategories, mockListener);

        assertTrue(panel.containsCategory("work"));
    }

    /**
     * Test for removing the content inside CategoriesPanel, the
     * expected result is that the panel has no child components.
     */
    @Test
    public void testRemovingCategories() {
        HashMap<String, String> mockCategories = new HashMap<>();
        mockCategories.put("work", "this is a description");
        CategoriesPanel panel = new CategoriesPanel();

        ActionListener mockListener = e -> {};

        panel.addCategories(mockCategories, mockListener);

        panel.clearCategoriesPanel();

        try {
            Field field = panel.getClass().getDeclaredField("panel");
            field.setAccessible(true);
            JPanel tempPanel = (JPanel) field.get(panel);
            assertEquals(0, tempPanel.getComponentCount());
        } catch (NoSuchFieldException e) {
            System.out.println("field could not be found: "+e.getMessage());
            assert false;
        } catch (IllegalAccessException e) {
            System.out.println("field could not be accessed: "+e.getMessage());
            assert false;
        }
    }

    /**
     * Test for deleting an existing category from the database via
     * the CategoriesController class. The expected result of this test
     * are: 1. a new category is added and exists in the database,
     * 2. the new category is deleted and does not exist in the database.
     */
    @Test
    public void testDeleteEvent() {
        ActionListener mockListener = e -> {};
        CategoriesController controller = new CategoriesController(mockListener);

        CategoryForm form = new CategoryForm(mockListener);
        Field[] fields = form.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                JTextField textField = new JTextField("TaskTesting");
                field.setAccessible(true);

                field.set(form, textField);
            }
            Field formField = controller.getClass().getDeclaredField("categoryForm");
            formField.setAccessible(true);
            formField.set(controller, form);

            Method method = controller.getClass().getDeclaredMethod("addCategory");
            method.setAccessible(true);

            method.invoke(controller);

            List<String> res = Database.getCategory("TaskTesting");

            Method deleteMethod = controller.getClass().getDeclaredMethod("deleteCategory", String.class);
            deleteMethod.setAccessible(true);
            deleteMethod.invoke(controller, "TaskTesting");

            List<String> res2 = Database.getCategory("TaskTesting");

            assertAll(()-> {
                assertFalse(res.isEmpty());
            }, ()-> {
                assertTrue(res2.isEmpty());
            });
        } catch (IllegalAccessException e) {
            System.out.println("field could not be accessed: "+e.getMessage());
            assert false;
        } catch (NoSuchMethodException e) {
            System.out.println("method could not be found: "+e.getMessage());
            assert false;
        } catch (NoSuchFieldException e) {
            System.out.println("field could not be found: "+e.getMessage());
            assert false;
        } catch (InvocationTargetException e) {
            System.out.println("method could not be invoked: "+e.getMessage());
            assert false;
        }
    }

    /**
     * Test for searching for a category using the searchbar. The
     * expected result is that a newly added category exists in
     * the CategoriesPanel after searching for it.
     */
    @Test
    public void testSearching() {
        ActionListener mockListener = e -> {};

        CategoriesController controller = new CategoriesController(mockListener);
        SearchBar searchBar = new SearchBar(mockListener);
        CategoriesPanel categoriesPanel = new CategoriesPanel();
        try {
            // prepare searchbar
            Field field = searchBar.getClass().getDeclaredField("textField");
            field.setAccessible(true);

            field.set(searchBar, new JTextField("taskTesting"));
            Field searchField = controller.getClass().getDeclaredField("searchBar");
            searchField.setAccessible(true);

            searchField.set(controller, searchBar);

            // prepare categoriesPanel
            Field catField = controller.getClass().getDeclaredField("categoriesPanel");
            catField.setAccessible(true);

            catField.set(controller, categoriesPanel);

            // add testing category to database
            List<String> list = new ArrayList<>();
            list.add("taskTesting");
            list.add("description");
            Database.addCategory(list);

            // perform search
            Method searchMethod = controller.getClass().getDeclaredMethod("performSearch", String.class);
            searchMethod.setAccessible(true);
            searchMethod.invoke(controller, "taskTesting");

            // check if testing category is in CategoriesPanel
            Field panelField = controller.getClass().getDeclaredField("categoriesPanel");
            panelField.setAccessible(true);
            CategoriesPanel panel = (CategoriesPanel) panelField.get(controller);

            Database.deleteCategory("taskTesting");
            EventQueue.invokeLater(() -> {
                assertTrue(panel.containsCategory("taskTesting"));
            });

        } catch (NoSuchFieldException e) {
            System.out.println("field could not be found: "+e.getMessage());
            assert false;
        } catch (InvocationTargetException e) {
            System.out.println("method could not be invoked: "+e.getMessage());
            assert false;
        } catch (IllegalAccessException e) {
            System.out.println("field could not be accessed: "+e.getMessage());
            assert false;
        } catch (NoSuchMethodException e) {
            System.out.println("method could not be found: "+e.getMessage());
            assert false;
        }
    }

    /**
     * Test for checking whether each category displayed in the list has
     * a delete button. The expected result is that a delete button is found in each
     * item.
     */
    @Test
    public void testListFormat() {
        ActionListener mockListener = e -> {};
        CategoriesPanel panel = new CategoriesPanel();

        HashMap<String, String> categories = new HashMap<>();
        categories.put("taskTesting", "this is a description");
        categories.put("task2", "this is a description");

        panel.addCategories(categories, mockListener);
        try {
            Field field = panel.getClass().getDeclaredField("panel");
            field.setAccessible(true);
            JPanel listPanel = (JPanel) field.get(panel);

            Component[] components = listPanel.getComponents();
            boolean check = false;

            for (Component component : components) {
                if (component instanceof JPanel) {
                    Component[] subComponents = ((JPanel) component).getComponents();
                    for (Component subComponent : subComponents) {
                        if (subComponent instanceof JButton) {
                            check = true;
                            break;
                        }
                    }
                }
            }

            assertTrue(check);
        } catch (NoSuchFieldException e) {
            System.out.println("field could not be found: "+e.getMessage());
            assert false;
        } catch (IllegalAccessException e) {
            System.out.println("field could not be accessed: "+e.getMessage());
            assert false;
        }
    }

    /**
     * Test for attempting to delete a non-existing category from the database.
     * The expected result is that the delete-method returns false, as the
     * category couldn't be found.
     */
    @Test
    public void testDeleteInvalidCategory() {
        ActionListener mockListener = e -> {};
        CategoriesController controller = new CategoriesController(mockListener);

        try {
            Method deleteMethod = controller.getClass().getDeclaredMethod("deleteCategory", String.class);
            deleteMethod.setAccessible(true);
            deleteMethod.invoke(controller, "taskTesting");
            boolean res = (boolean)deleteMethod.invoke(controller, "taskTesting");

            assertFalse(res);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
