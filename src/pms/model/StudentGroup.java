package pms.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentGroup {
    private final String groupName;
    private final String leaderUsername;
    private final List<String> memberUsernames;
    private ProjectStatus status;

    public StudentGroup(String groupName, String leaderUsername, List<String> memberUsernames) {
        this.groupName = groupName;
        this.leaderUsername = leaderUsername;
        this.memberUsernames = new ArrayList<>(memberUsernames);
        this.status = ProjectStatus.NOT_STARTED;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getLeaderUsername() {
        return leaderUsername;
    }

    public List<String> getMemberUsernames() {
        return Collections.unmodifiableList(memberUsernames);
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }
}
