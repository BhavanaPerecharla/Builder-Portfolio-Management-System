package org.example.UI;

import org.example.Model.Project;
import org.example.Model.ProjectDocument;
import org.example.Repository.ProjectRepository;
import org.example.Service.ClientService;
import org.example.Service.ProjectService;
import org.example.Repository.ProjectDocumentRepository;

import java.util.List;
import java.util.Scanner;

public class ClientDashboard {

    public static void showDashboard(String clientEmail) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n🛠️===== Client Dashboard =====");
            System.out.println("[1] View Profile");
            System.out.println("[2] Edit Profile");
            System.out.println("[3] Change Password");
            System.out.println("[4] View My Projects");
            System.out.println("[5] View Payments for My Projects");
            System.out.println("[6] View Project Documents");
            System.out.println("[0] Logout");
            System.out.print("👉 Enter your choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    ClientService.viewProfile(clientEmail);
                    break;
                case "2":
                    ClientService.editProfile(clientEmail);
                    break;
                case "3":
                    ClientService.changePassword(clientEmail);
                    break;
                case "4":
                    ProjectService.viewProjectsByClientEmail(clientEmail);  // NEW METHOD CALL
                    break;
                case "5":
                    ProjectService.viewPaymentsForClientProjects(clientEmail);
                    break;
                case "6":
                    viewProjectDocuments(clientEmail);   // 📄 New Call
                    break;

                case "0":
                    System.out.println("👋 Logging out...");
                    return;
                default:
                    System.out.println("❌ Invalid choice.");
            }
        }
    }
    private static void viewProjectDocuments(String clientEmail) {
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

    }

