package pms.model;

public class Lecturer extends User {
    public Lecturer(String username, String password, String fullName) {
        super(username, password, fullName, Role.LECTURER);
    }
}
