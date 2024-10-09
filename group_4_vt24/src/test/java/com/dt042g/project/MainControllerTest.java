package com.dt042g.project;

import com.dt042g.project.controllers.ControllerInterface;
import com.dt042g.project.controllers.MainController;
import com.dt042g.project.views.AppWindow;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Class used to test functionalities/behaviour in the MainController class.
 *
 * @author josef alirani
 */
public class MainControllerTest {

    /**
     * Mock controller class used in testing.
     */
    private static class MockController implements ControllerInterface {

        /**
         * Concrete implementation of the 'doWork' method, the function
         * has no content as it is used primarily for testing.
         *
         * @param window AppWindow instance that the controller will use.
         */
        @Override
        public void doWork(AppWindow window) {}
    }

    /**
     * Test used to determine whether the class can run a concrete
     * controller class using the 'runController' method.
     * The expected result is that no errors are thrown when
     * attempting to run the MockController.
     */
    @Test
    public void testRunningController() {
        MainController mainController = new MainController();

        try {
            Method method = MainController.class.getDeclaredMethod("runController", ControllerInterface.class);
            method.setAccessible(true);

            MockController tasks = new MockController();
            assertDoesNotThrow(()->method.invoke(mainController, tasks));

            //assert true;
        }
        catch(NoSuchMethodException err) {
            System.out.println("method could not be found: "+err.getMessage());
            assert false;
        }

    }

}
