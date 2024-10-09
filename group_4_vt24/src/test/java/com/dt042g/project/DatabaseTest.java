package com.dt042g.project;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing functions used to test features/behaviour
 * related to the Database class.
 *
 * @author Kevin Rosenbergs
 */
public class DatabaseTest {
    String existingTaskName = "A task";
    String existingCategoryName = "A category";
    String newTaskName = "A new task";
    String newCategoryName = "A new category";

    /**
     * A test meant to test if a connection can be successfully established with SQLite.
     * Expected result from calling "Database.isDatabaseConnected()" is true,
     * indicating that the connection was successful.
     */
    @Test
    public void testIsDatabaseConnected() {
        assertTrue(Database.isDatabaseConnected(), "Database should be successfully connected to the app");
    }

    /**
     * A test meant to test if the correct tables exist in the database.
     * Expected result is a match between the expected and actual table names (category, task),
     * indicating that the database is structured correctly.
     */
    @Test
    public void testGetTableNames() {
        // Expected table names
        List<String> expectedTableNames = new ArrayList<>();
        expectedTableNames.add("category");
        expectedTableNames.add("task");

        // Get actual table names
        List<String> actualTableNames = Database.getTableNames();

        assertEquals(expectedTableNames, actualTableNames,
                "Database should contain two tables: category and task");
    }

    /**
     * A test meant to ensure that categoryExists() shows existing categories in the database.
     * Expected result from calling categoryExists() with an existing category is true,
     * indicating that the method is working correctly.
     */
    @Test
    public void testCategoryExists() {
        assertTrue(Database.categoryExists(existingCategoryName),
                "The category '" + existingCategoryName + "' should exist in the category table");
    }

    /**
     * A test meant to ensure that taskExists() shows existing tasks in the database.
     * Expected result from calling taskExists() with an existing tasks is true,
     * indicating that the method is working correctly.
     */
    @Test
    public void testTaskExists() {
        assertTrue(Database.taskExists(existingTaskName),
                "The task '" + existingTaskName + "' should exist in the task table");
    }

    /**
     * A test meant to ensure that addCategory() can create new categories in the database.
     * Expected result from calling categoryExists() after adding the category is true,
     * indicating that the method is working correctly.
     */
    @Test
    public void testAddCategory() {
        List<String> newCategory = new ArrayList<>();
        newCategory.add(newCategoryName);
        newCategory.add("Info about the new category");

        Database.addCategory(newCategory);

        assertTrue(Database.categoryExists(newCategoryName),
                "addCategory() should add the category '" + newCategory.get(0) + "'");
    }

    /**
     * A test meant to ensure that addTask() can create new tasks in the database.
     * Expected result from calling taskExists() after adding the task is true,
     * indicating that the method is working correctly.
     */
    @Test
    public void testAddTask() {
        List<String> newTask = Arrays.asList(newTaskName, "Info about the new task", "A category", "2000-01-01");

        Database.addTask(newTask);

        assertTrue(Database.taskExists(newTaskName),
                "addTask() should add the task '" + newTask.get(0) + "'");
    }

    /**
     * A test meant to test if the table "task" contain any tasks.
     * Expected result is that the received List of names from "Database.getTaskNames()" is not empty,
     * indicating that the table contains rows to display.
     */
    @Test
    public void testGetTaskNames() {
        // Get task names
        ArrayDeque<String> taskNames = Database.getTaskNames();

        assertTrue(taskNames.contains(existingTaskName), "Task name list should contain task '" + existingTaskName + "'");
    }

    /**
     * A test meant to test if the table "category" contain any categories.
     * Expected result is that the received HashMap of categories from "Database.getCategories()" is not empty,
     * indicating that the table contains rows to display.
     */
    @Test
    public void testGetCategories() {
        HashMap<String, String> categories = Database.getCategories();

        assertTrue(categories.containsKey(existingCategoryName), "Category map should contain category '" +
                existingCategoryName + "'");
    }

    /**
     * A test meant to ensure search functionality works as expected for tasks.
     * Expected result is that the resulting queries matches anticipated behaviour,
     * indicating that searching functionality accurately displays database contents.
     */
    @Test
    public void testGetSearchedTaskNames() {
        String nonExisting = "------";

        ArrayDeque<String> query = Database.getSearchedTaskNames(existingTaskName);
        assertTrue(query.contains(existingTaskName), "Search query should contain existing task: " + existingTaskName);

        query = Database.getSearchedTaskNames(existingCategoryName);
        assertTrue(query.contains(existingTaskName), "Search query should contain existing task with category: "
                + existingCategoryName);

        query = Database.getSearchedTaskNames(nonExisting);
        assertFalse(query.contains(nonExisting), "Search query should not contain the non-existing task: "
                + nonExisting);
        assertEquals(0, query.size(), "Search query should be empty");
    }

    /**
     * A test meant to ensure search functionality works as expected for categories.
     * Expected result is that the resulting queries matches anticipated behaviour,
     * indicating that searching functionality accurately displays database contents.
     */
    @Test
    public void testGetSearchedCategories() {
        String nonExisting = "------";

        HashMap<String, String> query = Database.getSearchedCategories(existingCategoryName);
        assertTrue(query.containsKey(existingCategoryName), "Search query should contain existing category: " +
                existingCategoryName);

        query = Database.getSearchedCategories(nonExisting);
        assertFalse(query.containsKey(nonExisting), "Search query should not contain the non-existing category: "
                + nonExisting);
        assertEquals(0, query.size(), "Search query should be empty");
    }

    /**
     * A test meant to ensure getTask() returns existing tasks in the database.
     * Expected result from calling getTask() is a List that is not empty,
     * indicating that the method is working correctly.
     */
    @Test
    public void testGetTask() {
        List<String> task = Database.getTask(existingTaskName);

        assertNotEquals(0, task.size(),
                "getTask() should return the name, description, category and date of task");
    }

    /**
     * A test meant to ensure getCategory() returns existing categories in the database.
     * Expected result from calling getCategory() is a List that is not empty,
     * indicating that the method is working correctly.
     */
    @Test
    public void testGetCategory() {
        List<String> category = Database.getCategory(existingCategoryName);

        assertNotEquals(0, category.size(),
                "getCategory() should return the name and description of category");
    }

    /**
     * A test meant to ensure deleteTask() is successfully deleting a specified task in the database.
     * Expected result from calling taskExists() after deleting a task is false,
     * indicating that the method has successfully deleted the task.
     */
    @Test
    public void testDeleteTask() {
        Database.deleteTask(newTaskName);

        assertFalse(Database.taskExists(newTaskName),
                "deleteTask() should remove the task '" + newTaskName + "'");
    }

    /**
     * A test meant to ensure deleteCategory() is successfully deleting a specified category in the database.
     * Expected result from calling categoryExists() after deleting a category is false,
     * indicating that the method has successfully deleted the category.
     */
    @Test
    public void testDeleteCategory() {
        Database.deleteCategory(newCategoryName);

        assertFalse(Database.categoryExists(newCategoryName),
                "deleteCategory() should remove the category '" + newCategoryName + "'");
    }

    /**
     * A test meant to ensure that adding a task with a non-existing category throws an IllegalArgumentException.
     * Expected result is that calling addTask() with the new task results in an IllegalArgumentException,
     * indicating that the exception is correctly handled.
     */
    @Test
    public void testAddTaskWithNonExistingCategory() {
        List<String> taskValues = Arrays.asList("Test category", "info", "abc", "2000-01-01");

        assertThrows(IllegalArgumentException.class, () -> Database.addTask(taskValues));
    }

    /**
     * A test meant to ensure that adding a task with a name that already exists throws an IllegalStateException.
     * Expected result is that calling addTask() with the new task results in an IllegalStateException,
     * indicating that the exception is correctly handled.
     */
    @Test
    public void testAddTaskWithExistingName() {
        List<String> taskValues = Arrays.asList("A task", "info", "A category", "2000-01-01");

        assertThrows(IllegalStateException.class, () -> Database.addTask(taskValues));
    }

    /**
     * A test meant to ensure that adding a category with a name that already exists throws an IllegalArgumentException.
     * Expected result is that calling addCategory() with the new category results in an IllegalArgumentException,
     * indicating that the exception is correctly handled.
     */
    @Test
    public void testAddCategoryWithExistingName() {
        List<String> categoryValues = Arrays.asList("A category", "info");

        assertThrows(IllegalArgumentException.class, () -> Database.addCategory(categoryValues));
    }

    /**
     * A test meant to ensure that deleting a category that is referenced in a task throws an IllegalStateException.
     * Expected result is that calling deleteCategory() with the referenced category results in an IllegalStateException,
     * indicating that the exception is correctly handled.
     */
    @Test
    public void testDeleteReferencedCategory() {
        assertThrows(IllegalStateException.class, () -> Database.deleteCategory(existingCategoryName));
    }

    /**
     * A test meant to ensure that isCategoryReferenced() can see if a category is referenced in any existing tasks.
     * Expected result from calling isCategoryReferenced() with a referenced category is true, and calling
     * with a non-existing category is false,
     * indicating that the method is working correctly.
     */
    @Test
    public void testIsCategoryReferenced() {
        assertTrue(Database.isCategoryReferenced(existingCategoryName), "Existing category '" +
                existingCategoryName + "' should be referenced in '" + existingTaskName + "'");

        assertFalse(Database.isCategoryReferenced("-----------"),
                "Non-existing category '-----------' should not be referenced in any tasks");
    }
}
