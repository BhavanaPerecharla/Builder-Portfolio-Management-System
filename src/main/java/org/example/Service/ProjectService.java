package org.example.Service;

import org.example.Model.Manager;
import org.example.Model.Project;
import org.example.Repository.*;
import org.example.Util.InputValidator;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Scanner;

import static org.example.Util.InputValidator.promptValidProjectStatus;

public class ProjectService {

    private static final Scanner sc = new Scanner(System.in);

    public static void addProject(String builderEmail) {
        String builderId = BuilderRepository.getBuilderIdByEmail(builderEmail);

        if (builderId == null) {
            System.out.println("❌ Builder not found. Cannot create project.");
            return;
        }

        System.out.println("\n📋===== Add New Project =====");

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
                default -> {
                    System.out.println("❌ Unknown table specified.");
                }
            }

            if (exists) {
                return id;
            }
        }
    }


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
