package pms.model;

public class Student extends User {
    private final boolean groupLeader;

    public Student(String username, String password, String fullName, boolean groupLeader) {
        super(username, password, fullName, Role.STUDENT);
        this.groupLeader = groupLeader;
    }

    public boolean isGroupLeader() {
        return groupLeader;
    }
}
