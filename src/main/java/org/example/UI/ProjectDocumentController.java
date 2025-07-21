package org.example.UI;

import org.example.Model.ProjectDocument;
import org.example.Service.ProjectDocumentService;

import java.awt.datatransfer.FlavorEvent;
import java.io.*;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProjectDocumentController {

    private final ProjectDocumentService documentService;
    private final Scanner scanner;
    private static final Logger logger = Logger.getLogger(ProjectDocumentController.class.getName());
    private final String projectId;

    public ProjectDocumentController(String projectId) {
        this.documentService = new ProjectDocumentService();
        this.scanner = new Scanner(System.in);
        this.projectId = projectId;
    }



    public void start() {
        while (true) {
            System.out.println("\n===== Document Management Menu =====");
            System.out.println("1. Upload Document");
            System.out.println("2. Download Document");
            System.out.println("3. List Documents by Project");
            System.out.println("4. Delete Document");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    handleUpload();
                    break;
                case 2:
                    handleDownload();
                    break;
                case 3:
                    handleListDocuments();
                    break;
                case 4:
                    handleDelete();
                    break;
                case 5:
                    boolean confirmExit = promptYesNo("Are you sure you want to exit?");
                    if (confirmExit) {
                        System.out.println("Exiting Document Manager.");
                        return;
                    } else {
                        System.out.println("Exit cancelled.");
                    }
                    break;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }

    public void manageDocumentsForProject() {
        while (true) {
            System.out.println("\n===== Document Management for Project ID: " + projectId + " =====");
            System.out.println("1. Upload Document");
            System.out.println("2. Download Document");
            System.out.println("3. List Documents");
            System.out.println("4. Delete Document");
            System.out.println("0. Back to Project Menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleUpload();
                    break;
                case "2":
                    handleDownload();
                    break;
                case "3":
                    handleListDocuments();
                    break;
                case "4":
                    handleDelete();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }


    private void handleUpload() {
        try {
            String projectId = promptValidProjectId();
            if (projectId == null) {
                System.out.println("⚠️ Upload cancelled due to invalid project ID.");
                return;
            }

            File file = promptValidFile("Enter full file path to upload");
            if (file == null) {
                System.out.println("⚠️ Upload cancelled due to invalid file path.");
                return;
            }

            String fileName = file.getName();
            String fileType = getFileExtension(file);
            byte[] fileData = readFileBytes(file);

            System.out.println("\nYou're about to upload:");
            System.out.println("📄 File Name : " + fileName);
            System.out.println("📂 File Type : " + fileType);
            System.out.println("📁 File Size : " + fileData.length + " bytes");
            System.out.println("🔗 Project ID: " + projectId);

            boolean confirm = promptYesNo("Do you want to proceed with the upload?");
            if (!confirm) {
                System.out.println("❌ Upload cancelled by user.");
                return;
            }

            documentService.uploadDocument(projectId, fileName, fileType, fileData);
            System.out.println("✅ File uploaded successfully.");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "❌ Error during file upload.", e);
            System.out.println("❌ An unexpected error occurred during file upload.");
        }
    }


    private void handleDownload() {
        try {
            String projectId = promptValidProjectId();
            if (projectId == null) {
                System.out.println("⚠️ Download cancelled due to invalid Project ID.");
                return;
            }

            listDocumentsForProject(projectId);

            List<ProjectDocument> documents = documentService.getDocumentsByProjectId(projectId);

            if (documents.isEmpty()) {
                return;  // Nothing to download
            }

            String documentId = promptNonEmpty("Enter Document ID to download");

            ProjectDocument selectedDoc = documentService.getDocumentById(documentId);

            if (selectedDoc == null || !selectedDoc.getProjectId().equals(projectId)) {
                System.out.println("❌ Document ID does not belong to this project.");
                return;
            }

            String destPath = promptNonEmpty("Enter destination file path (including filename)");
            writeFileBytes(destPath, selectedDoc.getFileData());

            System.out.println("✅ File downloaded to: " + destPath);

        } catch (Exception e) {
            System.out.println("❌ Error during file download.");
            logger.log(Level.SEVERE, "Error during file download.", e);
        }
    }

    // Utility method
    private void listDocumentsForProject(String projectId) {
        List<ProjectDocument> documents = documentService.getDocumentsByProjectId(projectId);

        if (documents.isEmpty()) {
            System.out.println("⚠️ No documents found for this project.");
            return;
        }

        System.out.println("\n📄 Documents for Project ID: " + projectId);
        for (ProjectDocument doc : documents) {
            System.out.println("Document ID: " + doc.getDocumentId()
                    + " | Name: " + doc.getFileName()
                    + " | Type: " + doc.getFileType()
                    + " | Uploaded On: " + doc.getUploadedOn());
        }
    }


    private void handleListDocuments() {
        try {
            String projectId = promptValidProjectId();  // using loop until valid ID entered

            List<ProjectDocument> documents = documentService.getDocumentsByProjectId(projectId);

            if (documents.isEmpty()) {
                System.out.println("⚠️ No documents found for this project.");
            } else {
                System.out.println("\nDocuments for Project ID: " + projectId);
                for (ProjectDocument doc : documents) {
                    System.out.println("Document ID: " + doc.getDocumentId()
                            + " | Name: " + doc.getFileName()
                            + " | Type: " + doc.getFileType()
                            + " | Uploaded On: " + doc.getUploadedOn());
                }
            }

        } catch (Exception e) {
            System.out.println("❌ Error while listing documents.");
            logger.log(Level.SEVERE, "Error while listing documents.", e);
        }
    }

    private void handleDelete() {
        try {
            String projectId = promptValidProjectId();
            if (projectId == null) {
                System.out.println("⚠️ Deletion cancelled due to invalid Project ID.");
                return;
            }

            List<ProjectDocument> documents = documentService.getDocumentsByProjectId(projectId);

            if (documents.isEmpty()) {
                System.out.println("⚠️ No documents found for this project.");
                return;
            }

            System.out.println("\nDocuments for Project ID: " + projectId);
            for (ProjectDocument doc : documents) {
                System.out.println("Document ID: " + doc.getDocumentId() + " | Name: " + doc.getFileName());
            }

            String documentId = promptNonEmpty("Enter Document ID to delete");

            // Confirm document belongs to project
            ProjectDocument doc = documentService.getDocumentById(documentId);
            if (doc == null || !doc.getProjectId().equals(projectId)) {
                System.out.println("❌ This Document ID does not belong to Project ID: " + projectId);
                return;
            }

            boolean confirm = promptYesNo("Are you sure you want to delete this document?");
            if (!confirm) {
                System.out.println("Deletion cancelled.");
                return;
            }

            boolean deleted = documentService.deleteDocument(documentId);

            if (deleted) {
                System.out.println("✅ Document deleted successfully.");
            } else {
                System.out.println("⚠️ Document deletion failed.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error during document deletion.");
            logger.log(Level.SEVERE, "Error during document deletion.", e);
        }
    }


    // ==== Utility Methods ====


    private byte[] readFileBytes(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return fis.readAllBytes();
        }
    }

    private void writeFileBytes(String destinationPath, byte[] fileData) throws IOException {
        File file = new File(destinationPath);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(fileData);
        }
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex >= 0) {
            return fileName.substring(dotIndex + 1);
        } else {
            return "unknown";
        }
    }

    private String promptNonEmpty(String message) {
        String input;
        while (true) {
            System.out.print(message + ": ");
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("❌ Input cannot be empty. Please try again.");
            }
        }
    }
    private String promptValidProjectId() {
        int attempts = 0;
        int maxAttempts = 4;

        while (attempts < maxAttempts) {
            String projectId = promptNonEmpty("Enter Project ID");

            if (documentService.projectExists(projectId)) {
                return projectId;  // Valid ID
            } else {
                attempts++;
                System.out.println("❌ Invalid Project ID! No such project exists. Please try again.");
            }
        }

        System.out.println("⚠️ Maximum attempts reached. Operation cancelled.");
        return null;
    }

    private File promptValidFile(String message) {
        File file;
        int attempts = 0;
        int maxAttempts = 4;

        while (attempts < maxAttempts) {
            System.out.print(message + ": ");
            String filePath = scanner.nextLine().trim();
            file = new File(filePath);

            if (file.exists() && file.isFile()) {
                return file;
            } else {
                attempts++;
                System.out.println("❌ File does not exist. Please enter a valid file path.");
            }
        }

        System.out.println("⚠️ Maximum attempts reached. Upload cancelled.");
        return null;
    }


    private boolean promptYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (Y/N): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.out.println("❌ Invalid input. Please enter Y or N.");
            }
        }
    }
}
