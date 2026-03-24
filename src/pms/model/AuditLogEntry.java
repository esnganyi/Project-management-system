package pms.model;

import java.time.LocalDateTime;

public class AuditLogEntry {
    private final String username;
    private final String action;
    private final LocalDateTime timestamp;

    public AuditLogEntry(String username, String action, LocalDateTime timestamp) {
        this.username = username;
        this.action = action;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public String getAction() {
        return action;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return timestamp + " | " + username + " | " + action;
    }
}
