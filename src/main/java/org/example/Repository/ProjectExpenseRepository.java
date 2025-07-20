package org.example.Repository;


import org.example.Model.ProjectExpense;
import org.example.Util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectExpenseRepository {

    // Add Expense
    public static boolean addExpense(ProjectExpense expense) {
        String query = "INSERT INTO project_expenses (project_Id, payment_Id, payment_Date, amount, payment_description) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, expense.getProjectId());
            stmt.setString(2, expense.getPaymentId());
            stmt.setDate(3, expense.getPaymentDate());
            stmt.setDouble(4, expense.getAmount());
            stmt.setString(5, expense.getPaymentDescription());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update Expense
    public static boolean updateExpense(ProjectExpense expense) {
        String query = "UPDATE project_expenses SET payment_Date = ?, amount = ?, payment_description = ? " +
                "WHERE project_Id = ? AND payment_Id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, expense.getPaymentDate());
            stmt.setDouble(2, expense.getAmount());
            stmt.setString(3, expense.getPaymentDescription());
            stmt.setString(4, expense.getProjectId());
            stmt.setString(5, expense.getPaymentId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete Expense
    public static boolean deleteExpense(String projectId, String paymentId) {
        String query = "DELETE FROM project_expenses WHERE project_Id = ? AND payment_Id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, projectId);
            stmt.setString(2, paymentId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get Single Expense
    public static ProjectExpense getExpense(String projectId, String paymentId) {
        String query = "SELECT * FROM project_expenses WHERE project_Id = ? AND payment_Id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, projectId);
            stmt.setString(2, paymentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ProjectExpense(
                            rs.getString("project_Id"),
                            rs.getString("payment_Id"),
                            rs.getDate("payment_Date"),
                            rs.getDouble("amount"),
                            rs.getString("payment_description")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Get All Expenses for a Project
    public static List<ProjectExpense> getExpensesByProject(String projectId) {
        List<ProjectExpense> expenses = new ArrayList<>();
        String query = "SELECT * FROM project_expenses WHERE project_Id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, projectId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ProjectExpense expense = new ProjectExpense(
                            rs.getString("project_Id"),
                            rs.getString("payment_Id"),
                            rs.getDate("payment_Date"),
                            rs.getDouble("amount"),
                            rs.getString("payment_description")
                    );
                    expenses.add(expense);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expenses;
    }
}
