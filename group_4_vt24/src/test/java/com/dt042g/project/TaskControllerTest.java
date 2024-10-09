package com.dt042g.project;

import com.dt042g.project.controllers.AllTasksController;
import com.dt042g.project.controllers.TaskController;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class used to test the behaviour and functionalities of
 * the TaskController class.
 *
 * @author josef alirani
 */
public class TaskControllerTest {

    /**
     * Test to check if TaskController correctly communicates to the parent
     * controller. The expected result is that the command 'back' is passed
     * to the main controller.
     */
    @Test
    public void testReturnEvent() {
        ActionListener mockListener = e -> {assertEquals("back", e.getActionCommand());};

        TaskController controller = new TaskController(new ArrayList<>(), mockListener);

        controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "return"));
    }

    /**
     * Test used to check what happens when an unknown command is passed to the
     * TaskController class. The expected result is that the parent controller
     * isn't called when the command is triggered.
     */
    @Test
    public void testInvalidEvent() {
        ActionListener mockListener = e -> Assertions.fail();

        TaskController controller = new TaskController(new ArrayList<>(), mockListener);

        controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "explode"));
    }

    /**
     * Test used to determine whether a task can be exported as intended
     * via TaskController. If the new JSON file exists, it means the
     * test is successful.
     */
    @Test
    public void testExportEvent() {
        ActionListener mockListener = e -> {};
        List<String> list = new ArrayList<>();
        list.add("task1test");
        list.add("this is a task");
        list.add("2400-05-14");
        list.add("work");

        TaskController controller = new TaskController(list,mockListener);

        try {
            Method method = controller.getClass().getDeclaredMethod("exportTask", String.class);
            method.setAccessible(true);
            method.invoke(controller, "task1test");

            File file = new File("tasks/task1test.json");

            boolean res = file.isFile();
            if(res) {
                file.delete();
            }
            assertTrue(res);

        } catch (NoSuchMethodException e) {
            System.out.println("method could not be found: "+e.getMessage());
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
     * Test used to determine whether a task can be exported as intended
     * via TaskController, even if there already exists a file with that name.
     * If the new JSON file exists, it means the test is successful.
     */
    @Test
    public void testExportCollisionEvent() {
        ActionListener mockListener = e -> {};
        List<String> list = new ArrayList<>();
        list.add("task1test");
        list.add("this is a task");
        list.add("2400-05-14");
        list.add("work");

        TaskController controller = new TaskController(list,mockListener);

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", "task1test");
            jsonObject.put("description", "task1 is a task");
            jsonObject.put("date", "2400-05-14");
            jsonObject.put("category", "work");

            File input = new File("tasks/task1test.json");
            BufferedWriter writer = new BufferedWriter(new FileWriter(input));
            writer.write(jsonObject.toJSONString());
            writer.close();

            Method method = controller.getClass().getDeclaredMethod("exportTask", String.class);
            method.setAccessible(true);
            method.invoke(controller, "task1test");

            File duplicate = new File("tasks/task1test-1.json");
            File original = new File("tasks/task1test.json");

            boolean res = duplicate.isFile();
            duplicate.delete();
            original.delete();

            assertTrue(res);

        } catch (NoSuchMethodException e) {
            System.out.println("method could not be found: "+e.getMessage());
            assert false;
        } catch (InvocationTargetException e) {
            System.out.println("could not call method: "+e.getMessage());
            assert false;
        } catch (IllegalAccessException e) {
            System.out.println("could not access the method: "+e.getMessage());
            assert false;
        } catch (IOException e) {
            System.out.println("could not write file: "+e.getMessage());
            assert false;
        }
    }
}
