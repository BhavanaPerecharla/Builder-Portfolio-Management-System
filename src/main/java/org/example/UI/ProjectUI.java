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

                case "0":
                    return;

                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }

    private static void displayProjects(String builderEmail) {
        List<Project> projects = ProjectRepository.getProjectsByBuilderEmail(builderEmail);

        if (projects == null || projects.isEmpty()) {
            System.out.println("❌ No projects found.");
            return;
        }

        System.out.println("\n📋===== Your Projects =====");
        System.out.printf("%-15s %-20s %-15s %-15s %-15s %-15s %-12s %-10s %-10s %-12s %-25s\n",
                "Project ID", "Name", "Start Date", "Est. Comp Date", "Actual Comp Date",
                "Status", "Manager", "Client", "Builder", "Est. Cost", "Description");

        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        for (Project project : projects) {
            System.out.printf("%-15s %-20s %-15s %-15s %-15s %-15s %-12s %-10s %-10s ₹%-11s %-25s\n",
                    project.getProjectId(),
                    project.getProjectName(),
                    String.valueOf(project.getProjectStartDate()),
                    project.getProjectEstCompleteDate() != null ? project.getProjectEstCompleteDate().toString() : "N/A",
                    project.getProjectActualCompleteDate() != null ? project.getProjectActualCompleteDate().toString() : "N/A",
                    project.getProjectStatus(),
                    project.getManagerId(),
                    project.getClientId(),
                    project.getBuilderId(),
                    project.getEstimatedCost(),
                    project.getProjectDescription() != null ? project.getProjectDescription() : "-");
        }

        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }


}
