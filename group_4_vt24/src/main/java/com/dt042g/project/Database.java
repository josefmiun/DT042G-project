package com.dt042g.project;

import org.sqlite.SQLiteException;

import java.sql.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class handling connection to the SQLite database for the application.
 *
 * @author Kevin Rosenbergs
 */
public class Database {
    private static final String url = "jdbc:sqlite:todo_list_database.db";
    private static final String user = "";
    private static final String password = "";

    /**
     * Checks whether the database has been successfully connected.
     *
     * @return a boolean stating whether the connection was successful or not
     */
    public static boolean isDatabaseConnected() {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            return true; // Connection is successful
        } catch (SQLException e) {
            // Error connecting to the database
            return false;
        }
    }

    /**
     * Function used to retrieve the names of all tables in the database.
     *
     * @return a List of table names
     */
    public static List<String> getTableNames() {
        List<String> tableNames = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // SQL query to retrieve table names
            String sql = "SELECT name FROM sqlite_master WHERE type='table'";

            // Execute the query
            ResultSet rs = stmt.executeQuery(sql);

            // Iterate through the result set and add table names to the deque
            while (rs.next()) {
                tableNames.add(rs.getString("name"));
            }

        } catch (SQLException e) {
            // Handle any errors
            e.printStackTrace();
        }
        return tableNames;
    }

    /**
     * Function used to retrieve the name values from all rows in the "task" table.
     *
     * @return a Deque of task names
     */
    public static ArrayDeque<String> getTaskNames() {
        ArrayDeque<String> taskNames = new ArrayDeque<>();

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // SQL query to retrieve all task names
            String sql = "SELECT name FROM task";

            // Execute the query
            ResultSet rs = stmt.executeQuery(sql);

            // Iterate through the result set and add task names to the deque
            while (rs.next()) {
                taskNames.add(rs.getString("name"));
            }

        } catch (SQLException e) {
            // Handle any errors
            e.printStackTrace();
        }
        return taskNames;
    }

    /**
     * Function used to retrieve the name and description values from all rows in the "category" table.
     *
     * @return a HashMap of categories
     */
    public static HashMap<String, String> getCategories() {
        HashMap<String, String> categories = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // SQL query to retrieve all task names
            String sql = "SELECT name, description FROM category";

            // Execute the query
            ResultSet rs = stmt.executeQuery(sql);

            // Iterate through the result set and add task names to the deque
            while (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");
                categories.put(name, description);
            }

        } catch (SQLException e) {
            // Handle any errors
            e.printStackTrace();
        }
        return categories;
    }

    /**
     * Function used to retrieve name values in the "task" table using a search query.
     *
     * @param query the query to use when searching the database
     * @return a Deque of task names matching the query
     */
    public static ArrayDeque<String> getSearchedTaskNames(String query) {
        ArrayDeque<String> taskNames = new ArrayDeque<>();
        PreparedStatement preparedStatement;

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // SQL query to retrieve all task names
            String sql = "SELECT name FROM task WHERE name LIKE ? OR category LIKE ?";
            // prepared statement to use query variable
            preparedStatement = conn.prepareStatement(sql);
            String likePattern = "%" + query + "%";
            preparedStatement.setString(1, likePattern);
            preparedStatement.setString(2, likePattern);

            // Execute the query
            ResultSet rs = preparedStatement.executeQuery();

            // Iterate through the result set and add task names to the deque
            while (rs.next()) {
                taskNames.add(rs.getString("name"));
            }

        } catch (SQLException e) {
            // Handle any errors
            e.printStackTrace();
        }
        return taskNames;
    }

    /**
     * Function used to retrieve name values in the "task" table using a search query.
     *
     * @param query the query to use when searching the database
     * @return a Deque of task names matching the query
     */
    public static HashMap<String, String> getSearchedCategories(String query) {
        HashMap<String, String> categories = new HashMap<>();
        PreparedStatement preparedStatement;

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // SQL query to retrieve all categories
            String sql = "SELECT name, description FROM category WHERE name LIKE ?";
            // prepared statement to use query variable
            preparedStatement = conn.prepareStatement(sql);
            String likePattern = "%" + query + "%";
            preparedStatement.setString(1, likePattern);

            // Execute the query
            ResultSet rs = preparedStatement.executeQuery();

            // Iterate through the result set and add task names to the deque
            while (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");
                categories.put(name, description);
            }

        } catch (SQLException e) {
            // Handle any errors
            e.printStackTrace();
        }
        return categories;
    }

    /**
     * Function used to retrieve all values from a specified row in the "task" table.
     *
     * @param taskName the name of the task to retrieve
     * @return a List containing row values of a task
     */
    public static List<String> getTask(String taskName) {
        List<String> task = new ArrayList<>();
        PreparedStatement preparedStatement;

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // SQL query to retrieve all task names
            String sql = "SELECT * FROM task WHERE name = ?";
            // prepared statement to use query variable
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, taskName);

            // Execute the query
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                // Get the values of all columns in the row
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    task.add(rs.getString(i));
                }
            }

        } catch (SQLException e) {
            // Handle any errors
            e.printStackTrace();
        }
        return task;
    }

    /**
     * Function used to retrieve a category from the database by name.
     *
     * @param categoryName the name of the category to get
     * @return list of column values from the row of the category
     */
    public static List<String> getCategory(String categoryName) {
        List<String> category = new ArrayList<>();
        PreparedStatement preparedStatement;

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // SQL query to get a category by name
            String sql = "SELECT * FROM category WHERE name = ?";
            // prepared statement to use query variable
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, categoryName);

            // Execute the query
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                // Get the values of all columns in the row
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    category.add(rs.getString(i));
                }
            }

        } catch (SQLException e) {
            // Handle any errors
            e.printStackTrace();
        }
        return category;
    }

    /**
     * Function used to add a new row to the "task" table.
     *
     * @param taskValues a list of values to add to the row
     */
    public static void addTask(List<String> taskValues) {
        PreparedStatement preparedStatement;

        // Handle invalid input
        if (!categoryExists(taskValues.get(2))) throw new IllegalArgumentException("Category does not exist!");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // SQL query to insert a new row into the task table
            String sql = "INSERT INTO task (name, information, category, due_date) VALUES (?, ?, ?, ?)";
            // prepared statement to use query variable
            preparedStatement = conn.prepareStatement(sql);

            // Set values from the list
            for (int i = 0; i < taskValues.size(); i++) {
                preparedStatement.setString(i + 1, taskValues.get(i));
            }

            // Execute the update
            preparedStatement.executeUpdate();

        } catch (SQLiteException e) {
            // Handle constraint violation
            throw new IllegalStateException("Task name already exists!", e);
        } catch (SQLException e) {
            // Handle any errors
            e.printStackTrace();
        }
    }

    /**
     * Function used to add a new row to the "category" table.
     *
     * @param categoryValues a list of values to add to the row
     */
    public static void addCategory(List<String> categoryValues) {
        PreparedStatement preparedStatement;

        // Check if the category already exists
        if (categoryExists(categoryValues.get(0))) throw new IllegalArgumentException("Category name must be unique!");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // SQL query to insert a new row into the task table
            String sql = "INSERT INTO category (name, description) VALUES (?, ?)";
            // prepared statement to use query variable
            preparedStatement = conn.prepareStatement(sql);

            // Set values from the list
            for (int i = 0; i < categoryValues.size(); i++) {
                preparedStatement.setString(i + 1, categoryValues.get(i));
            }

            // Execute the update
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            // Handle any errors
            e.printStackTrace();
        }
    }

    /**
     * Function used to delete a specified row to the "task" table.
     *
     * @param taskName the name of the task to delete
     */
    public static void deleteTask(String taskName) {
        PreparedStatement preparedStatement;

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // SQL query to delete a task by name
            String sql = "DELETE FROM task WHERE name = ?";
            // prepared statement to use query variable
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, taskName);

            // Execute the update
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            // Handle any errors
            e.printStackTrace();
        }
    }

    /**
     * Function used to delete a specified row to the "category" table.
     *
     * @param categoryName the name of the task to delete
     */
    public static void deleteCategory(String categoryName) {
        PreparedStatement preparedStatement;

        // Check if category is referenced
        if (isCategoryReferenced(categoryName))
            throw new IllegalStateException("Category is referenced by an existing task and cannot be deleted!");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // SQL query to delete a task by name
            String sql = "DELETE FROM category WHERE name = ?";
            // prepared statement to use query variable
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, categoryName);

            // Execute the update
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            // Handle any errors
            e.printStackTrace();
        }
    }

    /**
     * Function used to check if a task exists in the "task" table.
     *
     * @param taskName the name of the task to check
     * @return true if the task exists, false otherwise
     */
    public static boolean taskExists(String taskName) {
        PreparedStatement preparedStatement;

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // SQL query to check if category exists
            String sql = "SELECT 1 FROM task WHERE name = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, taskName);

            // Execute the query
            ResultSet rs = preparedStatement.executeQuery();

            // Check if a result was returned
            return rs.next();

        } catch (SQLException e) {
            // Handle any errors
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Function used to check if a category exists in the "category" table.
     *
     * @param categoryName the name of the category to check
     * @return true if the category exists, false otherwise
     */
    public static boolean categoryExists(String categoryName) {
        PreparedStatement preparedStatement;

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // SQL query to check if category exists
            String sql = "SELECT 1 FROM category WHERE name = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, categoryName);

            // Execute the query
            ResultSet rs = preparedStatement.executeQuery();

            // Check if a result was returned
            return rs.next();

        } catch (SQLException e) {
            // Handle any errors
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Function used to check if a category is referenced by any tasks.
     *
     * @param categoryName the name of the category to check
     * @return true if the category is referenced by any tasks, false otherwise
     */
    public static boolean isCategoryReferenced(String categoryName) {
        PreparedStatement preparedStatement;

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // SQL query to check if category is referenced by any tasks
            String sql = "SELECT 1 FROM task WHERE category = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, categoryName);

            // Execute the query
            ResultSet rs = preparedStatement.executeQuery();

            // Check if a result was returned
            return rs.next();

        } catch (SQLException e) {
            // Handle any errors
            e.printStackTrace();
        }
        return false;
    }
}
