package pms.service;

import pms.model.AuditLogEntry;
import pms.model.CourseProject;
import pms.model.Lecturer;
import pms.model.Milestone;
import pms.model.ProjectProfile;
import pms.model.Student;
import pms.model.StudentGroup;
import pms.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectManagementService {
    private final List<User> users = new ArrayList<>();
    private final List<CourseProject> projects = new ArrayList<>();
    private final List<AuditLogEntry> auditLogs = new ArrayList<>();
    private int projectCounter = 1;

    public ProjectManagementService() {
        seedUsers();
        seedSampleProject();
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    public List<CourseProject> getProjects() {
        return Collections.unmodifiableList(projects);
    }

    public List<AuditLogEntry> getAuditLogs() {
        return Collections.unmodifiableList(auditLogs);
    }

    public CourseProject createProject(
            Lecturer lecturer,
            String courseUnit,
            String title,
            String description,
            LocalDate startDate,
            LocalDate endDate,
            ProjectProfile profile
    ) {
        CourseProject project = new CourseProject(
                projectCounter++,
                courseUnit,
                title,
                description,
                startDate,
                endDate,
                lecturer.getUsername(),
                profile
        );
        projects.add(project);
        log(lecturer.getUsername(), "Created project: " + title + " for " + courseUnit);
        log(lecturer.getUsername(), "Updated profile for project: " + title);
        return project;
    }

    public void addMilestone(Lecturer lecturer, int projectId, Milestone milestone) {
        CourseProject project = findProjectById(projectId);
        if (project != null) {
            project.addMilestone(milestone);
            log(lecturer.getUsername(), "Added milestone '" + milestone.getName() + "' to project: " + project.getTitle());
        }
    }

    public boolean registerGroup(Student leader, int projectId, String groupName, List<String> memberUsernames) {
        CourseProject project = findProjectById(projectId);
        if (project == null || !leader.isGroupLeader()) {
            return false;
        }

        for (String username : memberUsernames) {
            User member = findUserByUsername(username);
            if (!(member instanceof Student)) {
                return false;
            }
        }

        if (!memberUsernames.contains(leader.getUsername())) {
            memberUsernames.add(leader.getUsername());
        }

        StudentGroup group = new StudentGroup(groupName, leader.getUsername(), memberUsernames);
        project.addGroup(group);
        log(leader.getUsername(), "Registered group '" + groupName + "' for project: " + project.getTitle());
        return true;
    }

    public List<CourseProject> getProjectsForStudent(String username) {
        List<CourseProject> result = new ArrayList<>();
        for (CourseProject project : projects) {
            for (StudentGroup group : project.getGroups()) {
                if (group.getMemberUsernames().contains(username)) {
                    result.add(project);
                    break;
                }
            }
        }
        return result;
    }

    public List<CourseProject> getProjectsForCourse(String courseUnit) {
        List<CourseProject> result = new ArrayList<>();
        for (CourseProject project : projects) {
            if (project.getCourseUnit().equalsIgnoreCase(courseUnit)) {
                result.add(project);
            }
        }
        return result;
    }

    public CourseProject findProjectById(int id) {
        for (CourseProject project : projects) {
            if (project.getId() == id) {
                return project;
            }
        }
        return null;
    }

    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    private void log(String username, String action) {
        auditLogs.add(new AuditLogEntry(username, action, LocalDateTime.now()));
    }

    private void seedUsers() {
        users.add(new Lecturer("lect1", "pass123", "Dr. Mercy Njeri"));
        users.add(new Lecturer("lect2", "pass123", "Mr. Daniel Mwangi"));
        users.add(new Student("leader1", "pass123", "Alice Wanjiru", true));
        users.add(new Student("stud1", "pass123", "Brian Otieno", false));
        users.add(new Student("stud2", "pass123", "Faith Akinyi", false));
        users.add(new Student("leader2", "pass123", "Kevin Kiptoo", true));
    }

    private void seedSampleProject() {
        Lecturer lecturer = (Lecturer) findUserByUsername("lect1");
        CourseProject project = createProject(
                lecturer,
                "SAD 2201",
                "Project Management System",
                "Develop a console-based system for managing student course projects.",
                LocalDate.now(),
                LocalDate.now().plusWeeks(8),
                new ProjectProfile(pms.model.ProjectType.SOFTWARE, 3, pms.model.DifficultyLevel.MEDIUM)
        );
        project.addMilestone(new Milestone("Proposal", LocalDate.now().plusWeeks(2)));
        project.addMilestone(new Milestone("Draft", LocalDate.now().plusWeeks(5)));
        project.addMilestone(new Milestone("Final Submission", LocalDate.now().plusWeeks(8)));
    }
}
