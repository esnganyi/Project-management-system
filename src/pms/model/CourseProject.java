package pms.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CourseProject {
    private final int id;
    private final String courseUnit;
    private final String title;
    private final String description;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String lecturerUsername;
    private ProjectProfile profile;
    private final List<Milestone> milestones;
    private final List<StudentGroup> groups;

    public CourseProject(
            int id,
            String courseUnit,
            String title,
            String description,
            LocalDate startDate,
            LocalDate endDate,
            String lecturerUsername,
            ProjectProfile profile
    ) {
        this.id = id;
        this.courseUnit = courseUnit;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.lecturerUsername = lecturerUsername;
        this.profile = profile;
        this.milestones = new ArrayList<>();
        this.groups = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getCourseUnit() {
        return courseUnit;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getLecturerUsername() {
        return lecturerUsername;
    }

    public ProjectProfile getProfile() {
        return profile;
    }

    public void setProfile(ProjectProfile profile) {
        this.profile = profile;
    }

    public void addMilestone(Milestone milestone) {
        milestones.add(milestone);
    }

    public List<Milestone> getMilestones() {
        return Collections.unmodifiableList(milestones);
    }

    public void addGroup(StudentGroup group) {
        groups.add(group);
    }

    public List<StudentGroup> getGroups() {
        return Collections.unmodifiableList(groups);
    }
}
