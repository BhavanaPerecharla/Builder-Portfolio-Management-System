package org.example.UI;

import org.example.Model.Project;
import org.example.Repository.ProjectRepository;
import org.example.Service.ProjectService;

import java.util.List;
import java.util.Scanner;

public class ProjectUI {

    public static void manageProjects(String builderEmail) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n📋===== Project Menu =====");
            System.out.println("[1] View My Projects");
            System.out.println("[2] Add New Project");
            System.out.println("[3] Edit Project");
            System.out.println("[4] Delete Project");
            System.out.println("[5] Add Payment for Project");
            System.out.println("[6] View Payments For Project");
            System.out.println("[0] Back to Dashboard");
            System.out.print("👉 Enter your choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    displayProjects(builderEmail);
                    break;

                case "2":
                    ProjectService.addProject(builderEmail);
                    break;

                case "3":
                    displayProjects(builderEmail);
                    ProjectService.viewAndEditProject();
                    break;

                case "4":
                    displayProjects(builderEmail);
                    ProjectService.deleteProjectByMenu();
                    break;
                case "5" :
                    displayProjectSummary(builderEmail);
                    ProjectService.addPaymentForProject();
                    break;
                case "6" :
                    displayProjectSummary(builderEmail);
                    ProjectService.viewPaymentsForProject();
                    break;
                case "0":
                    return;

                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }

    // Method to display all projects for the builder
    private static void displayProjects(String builderEmail) {
        List<Project> projects = ProjectRepository.getProjectsByBuilderEmail(builderEmail);

        if (projects.isEmpty()) {
            System.out.println("❌ No projects found.");
            return;
        }

        // Displaying the list of projects
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

    // In ProjectUI.java
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


}
