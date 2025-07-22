package org.example.UI;


import org.example.Model.Project;
import org.example.Repository.ProjectRepository;
import org.example.Service.ProjectService;

import java.util.List;
import java.util.Scanner;

/**
 * UI Layer for managing project-related operations for builders.
 * Acts as an interface between the user and service/repository layers.
 */
public class ProjectUI {

    /**
     * Displays the project management menu for the logged-in builder.
     *
     * @param builderEmail Email of the currently logged-in builder.
     */
    public static void manageProjects(String builderEmail) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            displayProjectMenu();  // Modularized menu display
            System.out.print("👉 Enter your choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> displayProjects(builderEmail); // View projects
                case "2" -> ProjectService.addProject(builderEmail); // Add project
                case "3" -> {
                    displayProjects(builderEmail); // Show list before editing
                    ProjectService.viewAndEditProject(); // Edit project
                }
                case "4" -> {
                    displayProjects(builderEmail); // Show list before deleting
                    ProjectService.deleteProjectByMenu(); // Delete project
                }
                case "5" -> {
                    displayProjectSummary(builderEmail); // Show IDs before adding payment
                    ProjectService.addPaymentForProject(); // Add payment
                }
                case "6" -> {
                    displayProjectSummary(builderEmail); // Show IDs before viewing payments
                    ProjectService.viewPaymentsForProject(); // View payments
                }
                case "7" -> handleDocumentManagement(builderEmail); // Manage project documents
                case "0" -> {
                    System.out.println("👋 Returning to dashboard...");
                    return;
                }
                default -> System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the main project menu for builder operations.
     */
    private static void displayProjectMenu() {
        System.out.println("\n📋===== Project Menu =====");
        System.out.println("[1] View My Projects");
        System.out.println("[2] Add New Project");
        System.out.println("[3] Edit Project");
        System.out.println("[4] Delete Project");
        System.out.println("[5] Add Payment for Project");
        System.out.println("[6] View Payments For Project");
        System.out.println("[7] Manage Documents for Project");
        System.out.println("[0] Back to Dashboard");
    }

    /**
     * Retrieves and displays a detailed list of projects for the given builder.
     *
     * @param builderEmail Builder's email to fetch associated projects.
     */
    private static void displayProjects(String builderEmail) {
        List<Project> projects = ProjectRepository.getProjectsByBuilderEmail(builderEmail);

        if (projects.isEmpty()) {
            System.out.println("❌ No projects found.");
            return;
        }

        System.out.println("\n📋===== Your Projects =====");
        for (Project project : projects) {
            System.out.println("------------------------------------------------------------");
            System.out.println("🆔 Project ID           : " + project.getProjectId());
            System.out.println("📛 Name                 : " + project.getProjectName());
            System.out.println("📝 Description          : " + (project.getProjectDescription() != null ? project.getProjectDescription() : "-"));
            System.out.println("📅 Start Date           : " + project.getProjectStartDate());
            System.out.println("📆 Est. Completion Date : " +
                    (project.getProjectEstCompleteDate() != null ? project.getProjectEstCompleteDate() : "N/A"));
            System.out.println("✅ Actual Completion    : " +
                    (project.getProjectActualCompleteDate() != null ? project.getProjectActualCompleteDate() : "N/A"));
            System.out.println("📊 Status               : " + project.getProjectStatus());
            System.out.println("👨‍💼 Manager ID          : " + project.getManagerId());
            System.out.println("👤 Client ID           : " + project.getClientId());
            System.out.println("🏗️  Builder ID         : " + project.getBuilderId());
            System.out.println("💰 Estimated Cost       : ₹" + project.getEstimatedCost());
            System.out.println("------------------------------------------------------------\n");
        }
    }

    /**
     * Displays only summary (Project ID and Name) for quick selection menus.
     *
     * @param builderEmail Builder's email.
     */
    public static void displayProjectSummary(String builderEmail) {
        List<Project> projects = ProjectRepository.getProjectsByBuilderEmail(builderEmail);

        if (projects.isEmpty()) {
            System.out.println("⚠️  No projects found.");
            return;
        }

        System.out.println("\n📋 Project List (ID - Name):");
        for (Project project : projects) {
            System.out.println(project.getProjectId() + " - " + project.getProjectName());
        }
    }

    /**
     * Handles the logic for document management including validation of project ID.
     *
     * @param builderEmail Builder's email to fetch their project list.
     */
    private static void handleDocumentManagement(String builderEmail) {
        Scanner sc = new Scanner(System.in);

        displayProjectSummary(builderEmail);
        System.out.print("Enter Project ID to manage documents: ");
        String projectId = sc.nextLine().trim();

        // Validate project existence
        if (!ProjectRepository.projectExists(projectId)) {
            System.out.println("❌ Invalid Project ID.");
        } else {
            ProjectDocumentController docController = new ProjectDocumentController(projectId);
            docController.manageDocumentsForProject();
        }
    }
}
