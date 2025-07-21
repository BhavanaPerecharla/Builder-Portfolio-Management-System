package org.example.Repository;

import org.example.Model.ProjectExpense;
import org.example.Util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Repository Layer: ProjectExpenseRepository
 * Handles all database operations related to project expenses.
 * This class provides methods to insert and retrieve project expenses.
 */
public class ProjectExpenseRepository {

    private static final Logger logger = Logger.getLogger(ProjectExpenseRepository.class.getName());

    public static boolean insertExpense(ProjectExpense expense) {
        String query = "INSERT INTO project_expenses (project_Id, payment_Date, amount, payment_description) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, expense.getProjectId());
            stmt.setDate(2, expense.getPaymentDate());
            stmt.setDouble(3, expense.getAmount());
            stmt.setString(4, expense.getPaymentDescription());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inserting project expense", e);
            return false;
        }
    }

    /**
     * Retrieves all expenses for a given project ID, ordered by payment date.
     *
     * @param projectId The ID of the project for which expenses are to be retrieved.
     * @return A list of ProjectExpense objects associated with the specified project ID.
     */
    public static List<ProjectExpense> getExpensesByProjectId(String projectId) {
        List<ProjectExpense> expenses = new ArrayList<>();

        String query = "SELECT * FROM project_expenses WHERE project_Id = ? ORDER BY payment_Date";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, projectId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ProjectExpense expense = new ProjectExpense();
                    expense.setProjectId(rs.getString("project_Id"));
                    expense.setPaymentId(rs.getString("payment_Id")); // if auto-generated
                    expense.setPaymentDate(rs.getDate("payment_Date"));
                    expense.setAmount(rs.getDouble("amount"));
                    expense.setPaymentDescription(rs.getString("payment_description"));

                    expenses.add(expense);
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching project expenses", e);
        }

        return expenses;
    }
}
