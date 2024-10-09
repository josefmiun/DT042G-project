package com.dt042g.project;

import com.dt042g.project.views.AppWindow;
import com.dt042g.project.views.SearchBar;
import com.dt042g.project.views.TaskDisplay;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing functions used to test features/behaviour
 * related to the AppWindow class.
 *
 * @author josef alirani
 */
public class ViewTest {

    private static AppWindow window;

    /**
     * Static method to be executed before all other tests, initiates the
     * window using "SwingUtilities.invokeLater()".
     */
    @BeforeAll
    public static void createWindow() {
        //EventQueue.invokeLater(()-> window = new AppWindow());
        window = new AppWindow();
    }

    /**
     * A test meant to test if a JPanel can be added to the window.
     * Expected result from calling "AppWindow.addPanel()" is true,
     * indicating that the panel was correctly added.
     */
    @Test
    public void addPanelToWindow() {
        EventQueue.invokeLater(()-> {
            JPanel panel = new JPanel();
            panel.setBounds(0, 100, 400, 200);
            panel.setBackground(Color.BLUE);
            panel.setVisible(true);

            assertTrue(window.addPanel(panel));
        });
    }

    /**
     * A test meant to check if the window's components can be removed.
     * Expected result from calling "AppWindow.clearWindow()" is true,
     * indicating that components were correctly removed.
     */
    @Test
    public void removePanelsFromWindow() {
        EventQueue.invokeLater(() -> {
            JPanel panel = new JPanel();
            panel.setBounds(0, 100, 400, 200);
            panel.setBackground(Color.BLUE);
            panel.setVisible(true);
            window.addPanel(panel);

            assertTrue(window.clearWindow());
        });
    }

    @Test
    public void testEmptyTaskDisplay() {
        List<String> emptyList = new ArrayList<>();

        ActionListener mockListener = e -> {};

        assertDoesNotThrow(()->new TaskDisplay(emptyList, mockListener));
    }

    @Test
    public void testSearchQuery() {
        ActionListener mockListener = e -> {};
        SearchBar searchBar = new SearchBar(mockListener);

        assertEquals("", searchBar.getQuery());
    }

    @Test
    public void testNonEmptySearchQuery() {
        ActionListener mockListener = e -> {};
        SearchBar searchBar = new SearchBar(mockListener);
        try {
            Field field = searchBar.getClass().getDeclaredField("textField");
            field.setAccessible(true);
            JTextField textField = (JTextField) field.get(searchBar);

            textField.setText("testing");
            field.set(searchBar, textField);

            assertEquals("testing", searchBar.getQuery());
        }
        catch (NoSuchFieldException err) {
            System.out.println("no such field found: "+err.getMessage());
            assert false;
        }
        catch (IllegalAccessException err2) {
            System.out.println("cannot access field: "+err2.getMessage());
            assert false;
        }
    }
}
