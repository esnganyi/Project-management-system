package pms.model;

public class ProjectProfile {
    private final ProjectType projectType;
    private final int expectedGroupSize;
    private final DifficultyLevel difficultyLevel;

    public ProjectProfile(ProjectType projectType, int expectedGroupSize, DifficultyLevel difficultyLevel) {
        this.projectType = projectType;
        this.expectedGroupSize = expectedGroupSize;
        this.difficultyLevel = difficultyLevel;
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public int getExpectedGroupSize() {
        return expectedGroupSize;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }
}
