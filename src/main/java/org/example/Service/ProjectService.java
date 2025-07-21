package org.example.Service;

import org.example.Model.Manager;
import org.example.Model.Project;
import org.example.Model.ProjectDocument;
import org.example.Model.ProjectExpense;
import org.example.Repository.*;
import org.example.Util.InputValidator;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;


/**
 * Service class for managing projects.
 * Provides methods to add, view, edit, and delete projects,
 * as well as manage project documents and payments.
 */
public class ProjectService {

    private static final Scanner sc = new Scanner(System.in);

    /**
     * Adds a new project for the builder.
     *
     * @param builderEmail Email of the builder creating the project.
     */
    public static void addProject(String builderEmail) {
        String builderId = BuilderRepository.getBuilderIdByEmail(builderEmail);

        if (builderId == null) {
            System.out.println("❌ Builder not found. Cannot create project.");
            return;
        }

        System.out.println("\n📋===== Add New Project =====");
       // Prompt for project details
        String projectName = InputValidator.promptNonEmpty(sc, "Enter Project Name");
        String projectDescription = InputValidator.promptNonEmpty(sc, "Enter Project Description");
        Date startDate = InputValidator.promptValidDate(sc, "Enter Start Date (YYYY-MM-DD)");
        Date estCompleteDate = InputValidator.promptValidDate(sc, "Enter Estimated Completion Date (YYYY-MM-DD)");
        Date actualCompleteDate = InputValidator.promptOptionalDate(sc, "Enter Actual Completion Date (YYYY-MM-DD)");
        String projectStatus = promptValidProjectStatus();
        String managerId = promptValidId("Enter Manager ID", "manager");
        String clientId = promptValidId("Enter Client ID", "client");

        BigDecimal estimatedCost = InputValidator.promptValidAmount(sc, "Enter Estimated Cost");

        String projectId = builderId + "_PRJ" + String.format("%03d", ProjectRepository.getNextProjectNumberForBuilder(builderId));

        Project project = new Project(projectId, projectName, projectDescription, startDate,
                estCompleteDate, actualCompleteDate, projectStatus, managerId, clientId, builderId, estimatedCost);

        boolean success = ProjectRepository.insertProject(project);

        if (success) {
            System.out.println("✅ Project created successfully. ID: " + projectId);

            Manager assignedManager = ManagerRepository.getManagerById(managerId);

            if (assignedManager != null) {
                if ("Completed".equalsIgnoreCase(projectStatus)) {
                    assignedManager.setPmStatus("BENCH");
                } else if ("In Progress".equalsIgnoreCase(projectStatus)) {
                    assignedManager.setPmStatus("WORKING");
                }
                ManagerRepository.updateManagerStatus(assignedManager);
            } else {
                System.out.println("❌ Failed to create project.");
            }
        }
    }

    /**
     * Views all documents uploaded for projects associated with a specific client.
     * Displays project details along with the documents uploaded for each project.
     *
     * @param clientEmail Email of the client whose project documents are to be viewed.
     */
    public static void viewDocumentsForClientProjects(String clientEmail) {
        List<Project> clientProjects = ProjectRepository.getProjectsByClientEmail(clientEmail);

        if (clientProjects.isEmpty()) {
            System.out.println("⚠️ You don't have any projects.");
            return;
        }

        System.out.println("\n📋===== Project Documents =====");

        for (Project project : clientProjects) {
            System.out.println("\n📌 Project ID: " + project.getProjectId() + " | Project Name: " + project.getProjectName());

            List<ProjectDocument> documents = ProjectDocumentRepository.getDocumentsByProjectId(project.getProjectId());

            if (documents.isEmpty()) {
                System.out.println("   ❌ No documents uploaded for this project.");
            } else {
                for (ProjectDocument doc : documents) {
                    System.out.println("   📄 Document ID: " + doc.getDocumentId()
                            + " | Name: " + doc.getFileName()
                            + " | Type: " + doc.getFileType()
                            + " | Uploaded On: " + doc.getUploadedOn());
                }
            }
        }
    }

    /**
     * Views and edits an existing project by its ID.
     * Allows the user to modify various project details.
     */

    public static void viewAndEditProject() {
        System.out.print("\nEnter the Project ID to view/edit (or type '0' to exit): ");
        String projectId = sc.nextLine().trim();

        if ("0".equals(projectId)) {
            return;
        }

        Project project = ProjectRepository.getProjectById(projectId);

        if (project == null) {
            System.out.println("❌ Project not found with ID: " + projectId);
            return;
        }

        System.out.println("\nEditing project: " + project.getProjectId());
        showProjectDetails(project);

        while (true) {
            System.out.println("\nWhich detail would you like to edit?");
            System.out.println("1. Project Name");
            System.out.println("2. Project Description");
            System.out.println("3. Start Date");
            System.out.println("4. Estimated Completion Date");
            System.out.println("5. Actual Completion Date");
            System.out.println("6. Project Status");
            System.out.println("7. Estimated Cost");
            System.out.println("0. Save and Exit");
            System.out.print("👉 Your choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> project.setProjectName(InputValidator.promptNonEmpty(sc, "Enter new Project Name:"));
                case "2" -> project.setProjectDescription(InputValidator.promptNonEmpty(sc, "Enter new Project Description:"));
                case "3" -> project.setProjectStartDate(InputValidator.promptValidDate(sc, "Enter new Start Date (YYYY-MM-DD):"));
                case "4" -> project.setProjectEstCompleteDate(InputValidator.promptValidDate(sc, "Enter new Estimated Completion Date (YYYY-MM-DD):"));
                case "5" -> project.setProjectActualCompleteDate(InputValidator.promptValidDate(sc, "Enter new Actual Completion Date (YYYY-MM-DD):"));

                case "6" -> {
                        String newStatus = promptValidProjectStatus();
                        project.setProjectStatus(newStatus);

                        Manager assignedManager = ManagerRepository.getManagerById(project.getManagerId());

                        if (assignedManager != null) {
                            if ("Completed".equalsIgnoreCase(newStatus)) {
                                assignedManager.setPmStatus("BENCH");
                            } else if ("In Progress".equalsIgnoreCase(newStatus)) {
                                assignedManager.setPmStatus("WORKING");
                            }
                            ManagerRepository.updateManagerStatus(assignedManager);
                        }
                    }

                    case "7" -> project.setEstimatedCost(InputValidator.promptValidAmount(sc, "Enter new Estimated Cost:"));
                case "0" -> {
                    boolean updated = ProjectRepository.updateProject(project);
                    if (updated) {
                        System.out.println("✅ Project details updated successfully.");
                    } else {
                        System.out.println("❌ Failed to update project.");
                    }
                    return;
                }
                default -> System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }


    /**
     * Deletes a project by its ID.
     * Prompts the user for confirmation before deletion.
     */
    public static void deleteProjectByMenu() {
        System.out.print("\nEnter the Project ID to delete (or type '0' to exit): ");
        String projectId = sc.nextLine().trim();

        if ("0".equals(projectId)) {
            return;
        }

        Project project = ProjectRepository.getProjectById(projectId);

        if (project == null) {
            System.out.println("❌ Project not found with ID: " + projectId);
            return;
        }

        System.out.print("⚠️ Are you sure you want to delete this project? (yes/no): ");
        if (sc.nextLine().trim().equalsIgnoreCase("yes")) {
            boolean deleted = ProjectRepository.deleteProject(projectId);
            System.out.println(deleted ? "🗑️ Project deleted successfully." : "❌ Failed to delete project.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    /**
     * Prompts the user for a valid project status.
     * Validates if the input is one of the allowed statuses.
     *
     * @return A valid project status (Upcoming, In Progress, Completed).
     */
    private static String promptValidProjectStatus() {
        while (true) {
            System.out.print("Enter Project Status (Upcoming / In Progress / Completed): ");
            String status = sc.nextLine().trim();
            if (status.equalsIgnoreCase("Upcoming") ||
                    status.equalsIgnoreCase("In Progress") ||
                    status.equalsIgnoreCase("Completed")) {
                return status;
            } else {
                System.out.println("❌ Invalid status. Choose: Upcoming, In Progress, or Completed.");
            }
        }
    }

    /**
     * Prompts the user for a valid ID based on the specified table.
     * Validates if the ID exists in the corresponding repository.
     *
     * @param prompt The prompt message to display.
     * @param table  The table to check against (manager, client, builder).
     * @return A valid ID from the specified table.
     */
    private static String promptValidId(String prompt, String table) {
        while (true) {
            String id = InputValidator.promptNonEmpty(sc, prompt);
            boolean exists = false;

            switch (table) {
                case "manager" -> {
                    Manager manager = ManagerRepository.getManagerById(id);
                    if (manager == null) {
                        System.out.println("❌ Invalid Manager ID. No such manager exists.");
                    } else if (!"bench".equalsIgnoreCase(manager.getPmStatus())) {
                        System.out.println("❌ Manager is currently working on another project. Cannot assign.");
                    } else {
                        exists = true;  // Manager exists and is available
                    }
                }
                case "client" -> {
                    if (ClientRepository.getClientById(id) != null) {
                        exists = true;
                    } else {
                        System.out.println("❌ Invalid Client ID. No such client exists.");
                    }
                }
                case "builder" -> {
                    if (BuilderRepository.getBuilderById(id) != null) {
                        exists = true;
                    } else {
                        System.out.println("❌ Invalid Builder ID. No such builder exists.");
                    }
                }
                default ->
                    System.out.println("❌ Unknown table specified.");

            }

            if (exists) {
                return id;
            }
        }
    }

    /**
     * Views all payments made for projects associated with a specific client.
     * Prompts the user for the client's email and displays all payments for their projects.
     *
     * @param clientEmail Email of the client whose project payments are to be viewed.
     */
    public static void viewPaymentsForClientProjects(String clientEmail) {
        String clientId = ClientService.getClientIdByEmail(clientEmail);
        if (clientId == null) {
            System.out.println("❌ Client not found.");
            return;
        }

        List<Project> projects = ProjectRepository.getProjectsByClientId(clientId);

        if (projects.isEmpty()) {
            System.out.println("⚠️  No projects found for this client.");
            return;
        }

        System.out.println("\n📋 Your Projects:");
        for (Project project : projects) {
            System.out.println(project.getProjectId() + " - " + project.getProjectName());
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("👉 Enter Project ID to view payments: ");
        String projectId = sc.nextLine().trim();

        Project selectedProject = projects.stream()
                .filter(p -> p.getProjectId().equalsIgnoreCase(projectId))
                .findFirst()
                .orElse(null);

        if (selectedProject == null) {
            System.out.println("❌ Invalid Project ID.");
            return;
        }

        List<ProjectExpense> expenses = ProjectExpenseRepository.getExpensesByProjectId(projectId);


        if (expenses.isEmpty()) {
            System.out.println("💡 No payments recorded for this project.");
        } else {
            System.out.println("\n💰 ===== Payments for Project: " + selectedProject.getProjectName() + " (ID: " + projectId + ") =====");
            System.out.printf("%-15s %-15s %-12s %s\n", "🧾 Payment ID", "📅 Date", "💵 Amount", "📝 Description");
            System.out.println("--------------------------------------------------------------------------");

            for (ProjectExpense expense : expenses) {
                System.out.printf("%-15s %-15s ₹%-10.2f %s\n",
                        expense.getPaymentId(),
                        expense.getPaymentDate(),
                        expense.getAmount(),
                        expense.getPaymentDescription());
            }
        }
    }


    /**
     * Views all projects associated with a specific client by their email.
     * Displays project details including ID, name, description, dates, status, and cost.
     *
     * @param clientEmail Email of the client whose projects are to be viewed.
     */
    public static void viewProjectsByClientEmail(String clientEmail) {
        String clientId = ClientRepository.getClientIdByEmail(clientEmail);

        if (clientId == null) {
            System.out.println("❌ Client not found. Cannot fetch projects.");
            return;
        }

        List<Project> projects = ProjectRepository.getProjectsByClientId(clientId);

        if (projects.isEmpty()) {
            System.out.println("❌ No projects found for your account.");
            return;
        }

        System.out.println("\n📋===== Your Projects =====");

        for (Project project : projects) {
            System.out.println("------------------------------------------------------------");
            System.out.println("🛠️  Project ID       : " + project.getProjectId());
            System.out.println("📛  Name             : " + project.getProjectName());
            System.out.println("📝  Description      : " + project.getProjectDescription());
            System.out.println("📅  Start Date       : " + project.getProjectStartDate());
            System.out.println("📆  Est. Completion  : " + project.getProjectEstCompleteDate());
            System.out.println("✅  Actual Completion: " + (project.getProjectActualCompleteDate() == null ? "-" : project.getProjectActualCompleteDate()));
            System.out.println("📊  Status           : " + project.getProjectStatus());
            System.out.println("👨‍💼 Manager ID       : " + project.getManagerId());
            System.out.println("👤  Client ID        : " + project.getClientId());
            System.out.println("🏗️  Builder ID       : " + project.getBuilderId());
            System.out.println("💰 Estimated Cost    : ₹" + project.getEstimatedCost());
            System.out.println("------------------------------------------------------------\n");
        }
    }

    /**
     * Views all projects assigned to a specific manager by their email.
     * Displays projects categorized by their status (Upcoming, In Progress, Completed).
     *
     * @param managerEmail Email of the manager whose projects are to be viewed.
     */

    public static void viewProjectsByManagerEmail(String managerEmail) {
        String managerId = ManagerRepository.getManagerIdByEmail(managerEmail);

        if (managerId == null) {
            System.out.println("❌ Manager not found. Cannot fetch projects.");
            return;
        }

        List<Project> projects = ProjectRepository.getProjectsByManagerId(managerId);

        if (projects.isEmpty()) {
            System.out.println("❌ No projects assigned to you.");
            return;
        }

        displayProjectsByCategory(projects, "Upcoming");
        displayProjectsByCategory(projects, "In Progress");
        displayProjectsByCategory(projects, "Completed");
    }

    /**
     * Displays projects based on their status category.
     *
     * @param projects       List of projects to filter and display.
     * @param statusCategory The status category to filter by (e.g., "Upcoming", "In Progress", "Completed").
     */

    private static void displayProjectsByCategory(List<Project> projects, String statusCategory) {
        System.out.println("\n📋===== " + statusCategory.toUpperCase() + " PROJECTS =====");

        boolean found = false;

        for (Project project : projects) {
            if (statusCategory.equalsIgnoreCase(project.getProjectStatus())) {
                found = true;
                System.out.println("------------------------------------------------------------");
                System.out.println("🆔 Project ID           : " + project.getProjectId());
                System.out.println("📌 Project Name         : " + project.getProjectName());
                System.out.println("📝 Description          : " + (project.getProjectDescription() != null ? project.getProjectDescription() : "-"));
                System.out.println("📅 Start Date           : " + project.getProjectStartDate());
                System.out.println("📅 Estimated Completion : " + (project.getProjectEstCompleteDate() != null ? project.getProjectEstCompleteDate() : "-"));
                System.out.println("📅 Actual Completion    : " + (project.getProjectActualCompleteDate() != null ? project.getProjectActualCompleteDate() : "-"));
                System.out.println("📊 Status               : " + project.getProjectStatus());
                System.out.println("👤 Manager ID           : " + project.getManagerId());
                System.out.println("🤝 Client ID            : " + project.getClientId());
                System.out.println("🏗️ Builder ID          : " + project.getBuilderId());
                System.out.println("💰 Estimated Cost       : ₹" + project.getEstimatedCost());
                System.out.println("------------------------------------------------------------\n");
            }
        }

        if (!found) {
            System.out.println("❌ No " + statusCategory.toLowerCase() + " projects found.\n");
        }
    }

    /**
     * Adds a payment for a specific project.
     * Prompts the user for Project ID, payment date, amount, and description.
     */

    public static void addPaymentForProject() {
        System.out.print("\nEnter Project ID to add payment: ");
        String projectId = sc.nextLine().trim();

        Date paymentDate = InputValidator.promptValidDate(sc, "Enter Payment Date (YYYY-MM-DD):");
        BigDecimal amountBigDecimal = InputValidator.promptValidAmount(sc, "Enter Payment Amount:");
        double amount = amountBigDecimal.doubleValue();

        String description = InputValidator.promptNonEmpty(sc, "Enter Payment Description:");

        ProjectExpense expense = new ProjectExpense(projectId, null, paymentDate, amount, description);

        boolean success = ProjectExpenseRepository.insertExpense(expense);

        if (success) {
            System.out.println("✅ Payment recorded successfully.");
        } else {
            System.out.println("❌ Failed to record payment.");
        }
    }

    /**
     * Views all payments made for a specific project.
     * Prompts the user for the Project ID and displays all associated payments.
     */
    public static void viewPaymentsForProject() {
        System.out.print("\n🔎 Enter Project ID to view payments: ");
        String projectId = sc.nextLine().trim();

        List<ProjectExpense> expenses = ProjectExpenseRepository.getExpensesByProjectId(projectId);

        if (expenses.isEmpty()) {
            System.out.println("❌ No payments found for this project.");
            return;
        }

        System.out.println("\n💰===== Payments for Project ID: " + projectId + " =====");

        for (ProjectExpense expense : expenses) {
            System.out.println("============================================");
            System.out.println("🏗️  Project ID     : " + expense.getProjectId());
            System.out.println("🆔 Payment ID      : " + (expense.getPaymentId() != null ? expense.getPaymentId() : "(Auto-Generated)"));
            System.out.println("📅 Payment Date    : " + expense.getPaymentDate());
            System.out.println("💵 Amount Paid     : ₹" + expense.getAmount());
            System.out.println("📝 Description     : " + expense.getPaymentDescription());
        }

        System.out.println("============================================");
    }


      /**
     * Displays the details of a project.
     *
     * @param project The project to display.
     */

    private static void showProjectDetails(Project project) {
        System.out.println("--------------------------------------------------");
        System.out.println("Project ID         : " + project.getProjectId());
        System.out.println("Name               : " + project.getProjectName());
        System.out.println("Description        : " + project.getProjectDescription());
        System.out.println("Start Date         : " + project.getProjectStartDate());
        System.out.println("Estimated Complete : " + project.getProjectEstCompleteDate());
        System.out.println("Actual Complete    : " + project.getProjectActualCompleteDate());
        System.out.println("Status             : " + project.getProjectStatus());
        System.out.println("Manager ID         : " + project.getManagerId());
        System.out.println("Client ID          : " + project.getClientId());
        System.out.println("Estimated Cost     : ₹" + project.getEstimatedCost());
        System.out.println("--------------------------------------------------");
    }
}
