package pms;

import pms.model.CourseProject;
import pms.model.DifficultyLevel;
import pms.model.Lecturer;
import pms.model.Milestone;
import pms.model.ProjectProfile;
import pms.model.ProjectType;
import pms.model.Student;
import pms.model.StudentGroup;
import pms.model.User;
import pms.service.AuthService;
import pms.service.ProjectManagementService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    private final Scanner scanner = new Scanner(System.in);
    private final ProjectManagementService projectService = new ProjectManagementService();
    private final AuthService authService = new AuthService(projectService.getUsers());

    public static void main(String[] args) {
        new App().start();
    }

    private void start() {
        System.out.println("=== STUDENT-LECTURER PROJECT MANAGEMENT SYSTEM ===");
        System.out.println("Demo accounts: lect1/pass123, leader1/pass123, stud1/pass123");

        boolean running = true;
        while (running) {
            System.out.println("\n1. Login");
            System.out.println("2. Exit");
            System.out.print("Choose option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> loginFlow();
                case "2" -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }

        System.out.println("System closed.");
    }

    private void loginFlow() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        User user = authService.login(username, password);
        if (user == null) {
            System.out.println("Login failed. Check username or password.");
            return;
        }

        System.out.println("Welcome, " + user.getFullName() + " (" + user.getRole() + ")");
        if (user instanceof Lecturer lecturer) {
            lecturerMenu(lecturer);
        } else if (user instanceof Student student) {
            studentMenu(student);
        }
    }

    private void lecturerMenu(Lecturer lecturer) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- Lecturer Menu ---");
            System.out.println("1. Create project");
            System.out.println("2. Add milestone to project");
            System.out.println("3. View all projects for a course");
            System.out.println("4. View audit log");
            System.out.println("5. Logout");
            System.out.print("Choose option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> createProjectFlow(lecturer);
                case "2" -> addMilestoneFlow(lecturer);
                case "3" -> viewProjectsByCourseFlow();
                case "4" -> viewAuditLog();
                case "5" -> loggedIn = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void studentMenu(Student student) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- Student Menu ---");
            System.out.println("1. View my projects");
            System.out.println("2. Register group for a project");
            System.out.println("3. Logout");
            System.out.print("Choose option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> viewStudentProjects(student);
                case "2" -> registerGroupFlow(student);
                case "3" -> loggedIn = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void createProjectFlow(Lecturer lecturer) {
        System.out.println("\nCreate New Project");
        System.out.print("Course/Unit: ");
        String courseUnit = scanner.nextLine().trim();
        System.out.print("Project Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Description: ");
        String description = scanner.nextLine().trim();
        LocalDate startDate = readDate("Start Date (YYYY-MM-DD): ");
        LocalDate endDate = readDate("End Date (YYYY-MM-DD): ");
        ProjectType projectType = readProjectType();
        System.out.print("Expected Group Size: ");
        int expectedGroupSize = Integer.parseInt(scanner.nextLine().trim());
        DifficultyLevel difficultyLevel = readDifficultyLevel();

        CourseProject project = projectService.createProject(
                lecturer,
                courseUnit,
                title,
                description,
                startDate,
                endDate,
                new ProjectProfile(projectType, expectedGroupSize, difficultyLevel)
        );

        System.out.print("How many milestones do you want to add now? ");
        int milestoneCount = Integer.parseInt(scanner.nextLine().trim());
        for (int i = 0; i < milestoneCount; i++) {
            System.out.print("Milestone name: ");
            String name = scanner.nextLine().trim();
            LocalDate targetDate = readDate("Milestone target date (YYYY-MM-DD): ");
            projectService.addMilestone(lecturer, project.getId(), new Milestone(name, targetDate));
        }

        System.out.println("Project created successfully with ID: " + project.getId());
    }

    private void addMilestoneFlow(Lecturer lecturer) {
        showAllProjects();
        System.out.print("Enter project ID: ");
        int projectId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Milestone name: ");
        String name = scanner.nextLine().trim();
        LocalDate targetDate = readDate("Milestone target date (YYYY-MM-DD): ");
        projectService.addMilestone(lecturer, projectId, new Milestone(name, targetDate));
        System.out.println("Milestone added.");
    }

    private void viewProjectsByCourseFlow() {
        System.out.print("Enter course/unit code: ");
        String courseUnit = scanner.nextLine().trim();
        List<CourseProject> projects = projectService.getProjectsForCourse(courseUnit);

        if (projects.isEmpty()) {
            System.out.println("No projects found for that course.");
            return;
        }

        for (CourseProject project : projects) {
            System.out.println("\nProject ID: " + project.getId());
            System.out.println("Title: " + project.getTitle());
            System.out.println("Description: " + project.getDescription());
            System.out.println("Duration: " + project.getStartDate() + " to " + project.getEndDate());
            System.out.println("Groups registered: " + project.getGroups().size());
            if (project.getGroups().isEmpty()) {
                System.out.println("No groups registered yet.");
            } else {
                for (StudentGroup group : project.getGroups()) {
                    System.out.println("Group: " + group.getGroupName()
                            + " | Leader: " + group.getLeaderUsername()
                            + " | Status: " + group.getStatus());
                }
            }
        }
    }

    private void registerGroupFlow(Student leader) {
        if (!leader.isGroupLeader()) {
            System.out.println("Only registered group leaders can create group registrations.");
            return;
        }

        showAllProjects();
        System.out.print("Enter project ID: ");
        int projectId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Group name: ");
        String groupName = scanner.nextLine().trim();
        System.out.print("Enter member usernames separated by commas: ");
        String input = scanner.nextLine().trim();

        List<String> members = new ArrayList<>();
        if (!input.isBlank()) {
            String[] usernames = input.split(",");
            for (String username : usernames) {
                members.add(username.trim());
            }
        }

        boolean registered = projectService.registerGroup(leader, projectId, groupName, members);
        if (registered) {
            System.out.println("Group registered successfully.");
        } else {
            System.out.println("Group registration failed. Check project ID, members, or leader status.");
        }
    }

    private void viewStudentProjects(Student student) {
        List<CourseProject> projects = projectService.getProjectsForStudent(student.getUsername());
        if (projects.isEmpty()) {
            System.out.println("You are not yet assigned to any project.");
            return;
        }

        for (CourseProject project : projects) {
            System.out.println("\nProject ID: " + project.getId());
            System.out.println("Course/Unit: " + project.getCourseUnit());
            System.out.println("Title: " + project.getTitle());
            System.out.println("Description: " + project.getDescription());
            System.out.println("Deadlines: " + project.getStartDate() + " to " + project.getEndDate());
            System.out.println("Milestones:");
            for (Milestone milestone : project.getMilestones()) {
                System.out.println("- " + milestone);
            }
        }
    }

    private void viewAuditLog() {
        System.out.println("\nAudit Log");
        projectService.getAuditLogs().forEach(System.out::println);
    }

    private void showAllProjects() {
        System.out.println("\nAvailable Projects");
        for (CourseProject project : projectService.getProjects()) {
            System.out.println(project.getId() + ". " + project.getCourseUnit() + " - " + project.getTitle());
        }
    }

    private LocalDate readDate(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return LocalDate.parse(scanner.nextLine().trim());
            } catch (DateTimeParseException ex) {
                System.out.println("Invalid date format. Use YYYY-MM-DD.");
            }
        }
    }

    private ProjectType readProjectType() {
        while (true) {
            System.out.println("Project Type: 1.RESEARCH 2.SOFTWARE 3.CASE_STUDY");
            System.out.print("Choose option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    return ProjectType.RESEARCH;
                }
                case "2" -> {
                    return ProjectType.SOFTWARE;
                }
                case "3" -> {
                    return ProjectType.CASE_STUDY;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private DifficultyLevel readDifficultyLevel() {
        while (true) {
            System.out.println("Difficulty Level: 1.LOW 2.MEDIUM 3.HIGH");
            System.out.print("Choose option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    return DifficultyLevel.LOW;
                }
                case "2" -> {
                    return DifficultyLevel.MEDIUM;
                }
                case "3" -> {
                    return DifficultyLevel.HIGH;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }
}
