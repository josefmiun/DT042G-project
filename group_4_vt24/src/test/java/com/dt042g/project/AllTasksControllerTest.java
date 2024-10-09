package com.dt042g.project;

import com.dt042g.project.controllers.AllTasksController;
import com.dt042g.project.views.*;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Class containing functions used to test features/behaviour
 * related to the AllTasksController class.
 *
 * @author Kevin Rosenbergs
 * @author josef alirani
 */
public class AllTasksControllerTest {

    /**
     * A test meant to test if a task can be added to the GUI.
     * Expected result from calling "tasksPanel.containsTask()" is true,
     * indicating that tasks can be added to the GUI.
     */
    @Test
    public void testAddingTasks() {
        ArrayDeque<String> mockTasks = new ArrayDeque<>();
        mockTasks.add("work");
        TasksPanel tasksPanel = new TasksPanel();

        ActionListener mockListener = e -> {};

        tasksPanel.addTasks(mockTasks, mockListener);

        assertTrue(tasksPanel.containsTask("work"));
    }

    /**
     * Test used to check if AllTasksController correctly communicates to
     * the main controller when attempting to switch to TaskController.
     * The expected result is that the actionEvent command is the same as expected.
     */
    @Test
    public void testViewIndividualTask() {
        ActionListener mockListener = e -> {assertEquals("task",e.getActionCommand());};

        AllTasksController controller = new AllTasksController(mockListener);
        controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "task"));
    }

    /**
     * Test used to see if the 'search' command is handled correctly. The expected
     * result is that 'fail()' is not called when communicating to the AllTasksController.
     */
    @Test
    public void testSearching() {
        ActionListener mockListener = e -> fail();

        AllTasksController controller = new AllTasksController(mockListener);
        SearchBar searchBar = new SearchBar(mockListener);
        TasksPanel panel = new TasksPanel();

        EventQueue.invokeLater(() -> {
            try {
                Field searchField = controller.getClass().getDeclaredField("searchBar");
                Field TasksField = controller.getClass().getDeclaredField("tasksPanel");

                searchField.setAccessible(true);
                searchField.set(controller, searchBar);
                TasksField.setAccessible(true);
                TasksField.set(controller, panel);

                controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "search"));

            } catch (NoSuchFieldException e) {
                System.out.println("field could not be found: "+e.getMessage());
                assert false;
            } catch (IllegalAccessException e) {
                System.out.println("cannot access field: "+e.getMessage());
                assert false;
            }
        });
    }

    /**
     * Test to determine whether the program operates as intended when
     * attempting to submit an empty form as a task. The expected
     * result is that the form is marked as 'not filled out'
     */
    @Test
    public void testEmptyTaskForm() {
        ActionListener mockListener = e -> {};

        TaskForm form = new TaskForm(mockListener);

        assertFalse(form.isFilledOut());
    }

    /**
     * Test for submitting a filled out form for a new task. The expected result
     * is that the form is marked as 'filled out'.
     */
    @Test
    public void testFilledTaskForm() {
        ActionListener mockListener = e -> {};

        TaskForm form = new TaskForm(mockListener);
        try {
            Field[] fields = form.getClass().getDeclaredFields();
            for(Field field: fields) {
                JTextField textField;
                if(field.getName().equals("date")) {
                    textField = new JTextField("2000-01-14");
                }
                else {
                    textField = new JTextField("task");
                }
                field.setAccessible(true);

                field.set(form, textField);
            }

            assertTrue(form.isFilledOut());
        } catch (IllegalAccessException e) {
            System.out.println("cannot access field: "+e.getMessage());
            assert false;
        }
    }

    /**
     * Test used to check whether a form containing an invalid date
     * can be submitted. The expected result is that the form is not accepted
     * because of the wrong date format.
     */
    @Test
    public void testInvalidDate() {
        ActionListener mockListener = e -> {};

        TaskForm form = new TaskForm(mockListener);
        AllTasksController controller = new AllTasksController(mockListener);
        try {
            Field[] fields = form.getClass().getDeclaredFields();
            for(Field field: fields) {
                field.setAccessible(true);
                JTextField textField = new JTextField("task");
                field.set(form, textField);
            }

            Field formField = controller.getClass().getDeclaredField("taskForm");
            formField.setAccessible(true);
            formField.set(controller, form);

            Method method = controller.getClass().getDeclaredMethod("addTask");
            method.setAccessible(true);

            assertFalse((boolean) method.invoke(controller));
        } catch (IllegalAccessException e) {
            System.out.println("cannot access field: "+e.getMessage());
            assert false;
        } catch (NoSuchFieldException e) {
            System.out.println("field could not be found: "+e.getMessage());
            assert false;
        } catch (NoSuchMethodException e) {
            System.out.println("method could not be found: "+e.getMessage());
            assert false;
        } catch (InvocationTargetException e) {
            System.out.println("method could not be called: "+e.getMessage());
            assert false;
        }
    }

    /**
     * Test to check whether an empty category form can be submitted.
     * The expected result is that the form is not accepted.
     */
    @Test
    public void testEmptyCategoryForm() {
        ActionListener mockListener = e -> {};

        CategoryForm form = new CategoryForm(mockListener);
        try {
            Field field = form.getClass().getDeclaredField("name");
            field.setAccessible(true);
            JTextField textField = new JTextField("task");
            field.set(form, textField);

            assertFalse(form.isFilledOut());
        } catch (NoSuchFieldException e) {
            System.out.println("field could not be found: "+e.getMessage());
            assert false;
        } catch (IllegalAccessException e) {
            System.out.println("cannot access field: "+e.getMessage());
            assert false;
        }
    }

    /**
     * Test for submitting a filled out category form.
     * The expected result is that the form is accepted.
     */
    @Test
    public void testFilledCategoryForm() {
        ActionListener mockListener = e -> {};

        CategoryForm form = new CategoryForm(mockListener);
        try {
            Field[] fields = form.getClass().getDeclaredFields();
            for(Field field: fields) {
                field.setAccessible(true);
                JTextField textField = new JTextField("task");
                field.set(form, textField);
            }

            assertTrue(form.isFilledOut());
        } catch (IllegalAccessException e) {
            System.out.println("cannot access field: "+e.getMessage());
            assert false;
        }
    }

    /**
     * Parameterized test to check whether invalid task names can be submitted
     * in forms. The expected result is that no form is accepted.
     *
     * @param name the invalid task name used in the test.
     */
    @ParameterizedTest
    @CsvSource({"return", "back", "search", "submit_task", "submit_category", "delete", "import_task",
            "export_task", "switch", "switch_to_tasks", "switch_to_categories"})
    public void testInvalidTaskNames(String name) {
        ActionListener mockListener = e -> {};

        TaskForm form = new TaskForm(mockListener);
        AllTasksController controller = new AllTasksController(mockListener);
        try {
            Field[] fields = form.getClass().getDeclaredFields();
            for(Field field: fields) {
                JTextField textField;
                if(field.getName().equals("name")) {
                    textField = new JTextField(name);
                }
                else if(field.getName().equals("date")) {
                    textField = new JTextField("2000-01-01");
                }
                else {
                    textField = new JTextField("task");
                }
                field.setAccessible(true);

                field.set(form, textField);
            }

            Field formField = controller.getClass().getDeclaredField("taskForm");
            formField.setAccessible(true);
            formField.set(controller, form);

            Method method = controller.getClass().getDeclaredMethod("addTask");
            method.setAccessible(true);

            assertFalse((boolean)method.invoke(controller));
        } catch (IllegalAccessException e) {
            System.out.println("cannot access field: "+e.getMessage());
            assert false;
        } catch (NoSuchFieldException e) {
            System.out.println("field could not be found: "+e.getMessage());
            assert false;
        } catch (NoSuchMethodException e) {
            System.out.println("method could not be found: "+e.getMessage());
            assert false;
        } catch (InvocationTargetException e) {
            System.out.println("method could not be called: "+e.getMessage());
            assert false;
        }
    }

    /**
     * Test to determine whether the AllTasksController can import
     * a task from a JSON file.
     * By checking the database for the task, it indicates that
     * the functionality works as intended.
     */
    @Test
    public void testImportEvent() {
        ActionListener mockListener = e -> {};
        AllTasksController controller = new AllTasksController(mockListener);

        try {
            Method method = controller.getClass().getDeclaredMethod("importTask", File.class);
            method.setAccessible(true);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", "task1test");
            jsonObject.put("description", "task1 is a task");
            jsonObject.put("category", "Work");
            jsonObject.put("date", "2500-05-12");

            File input = new File("tasks/task1.json");
            BufferedWriter writer = new BufferedWriter(new FileWriter(input));
            writer.write(jsonObject.toJSONString());
            writer.close();

            method.invoke(controller, input);

            boolean res = !(Database.getTask("task1test").isEmpty());

            if(res) {
                Database.deleteTask("task1test");
            }
            File tempInput = new File("tasks/task1.json");
            tempInput.delete();

            assert res;

        } catch (NoSuchMethodException e) {
            System.out.println("method could not be found: "+e.getMessage());
            assert false;
        } catch (IOException e) {
            System.out.println("could not write to file: "+e.getMessage());
            assert false;
        } catch (InvocationTargetException e) {
            System.out.println("could not call method: "+e.getMessage());
            e.printStackTrace();
            assert false;
        } catch (IllegalAccessException e) {
            System.out.println("could not access the method: "+e.getMessage());
            assert false;
        }
    }

    /**
     * Test to determine whether the AllTasksController does not import
     * a task from a JSON file which contains invalid values in fields.
     * By checking the database for the task, it indicates that
     * the functionality works as intended in an empty list is returned.
     */
    @Test
    public void testInvalidImportEvent() {
        ActionListener mockListener = e -> {};
        AllTasksController controller = new AllTasksController(mockListener);

        try {
            Method method = controller.getClass().getDeclaredMethod("importTask", File.class);
            method.setAccessible(true);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", "return");
            jsonObject.put("description", "task1 is a task");
            jsonObject.put("date", "not a date");
            jsonObject.put("category", "work");

            File input = new File("tasks/task1.json");
            BufferedWriter writer = new BufferedWriter(new FileWriter(input));
            writer.write(jsonObject.toJSONString());
            writer.close();

            method.invoke(controller, input);

            boolean res = (Database.getTask("return").isEmpty());
            if(!res) {
                Database.deleteTask("return");
            }
            input.deleteOnExit();

            assert res;

            //Database.deleteTask("task1test");

        } catch (NoSuchMethodException e) {
            System.out.println("method could not be found: "+e.getMessage());
            assert false;
        } catch (IOException e) {
            System.out.println("could not write to file: "+e.getMessage());
            assert false;
        } catch (InvocationTargetException e) {
            System.out.println("could not call method: "+e.getMessage());
            assert false;
        } catch (IllegalAccessException e) {
            System.out.println("could not access the method: "+e.getMessage());
            assert false;
        }
    }

    @Test
    public void testInvalidJSONImportEvent() {
        ActionListener mockListener = e -> {};
        AllTasksController controller = new AllTasksController(mockListener);

        try {
            Method method = controller.getClass().getDeclaredMethod("importTask", File.class);
            method.setAccessible(true);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("notName", "Tomas");
            jsonObject.put("error", "404");
            jsonObject.put("password", "qwe");
            jsonObject.put("favouriteNumber", "42");

            File input = new File("tasks/task1.json");
            BufferedWriter writer = new BufferedWriter(new FileWriter(input));
            writer.write(jsonObject.toJSONString());
            writer.close();

            method.invoke(controller, input);

            boolean res = (Database.getTask("return").isEmpty());
            if(!res) {
                Database.deleteTask("return");
            }
            input.deleteOnExit();

            assert res;

            Database.deleteTask("task1test");

        } catch (NoSuchMethodException e) {
            System.out.println("method could not be found: "+e.getMessage());
            assert false;
        } catch (IOException e) {
            System.out.println("could not write to file: "+e.getMessage());
            assert false;
        } catch (InvocationTargetException e) {
            System.out.println("could not call method: "+e.getMessage());
            assert false;
        } catch (IllegalAccessException e) {
            System.out.println("could not access the method: "+e.getMessage());
            assert false;
        }
    }

    /**
     * Test to check that AllTasksController sends the correct event command to
     * the main controller. The expected result is that the command 'switch_to_categories'
     * is sent.
     */
    @Test
    public void testSwitchEvent() {
        ActionListener mockListener = e -> {assertEquals("switch_to_categories", e.getActionCommand());};

        AllTasksController controller = new AllTasksController(mockListener);
        controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "switch"));
    }
}
