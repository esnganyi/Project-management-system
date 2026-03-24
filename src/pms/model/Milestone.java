package pms.model;

import java.time.LocalDate;

public class Milestone {
    private final String name;
    private final LocalDate targetDate;

    public Milestone(String name, LocalDate targetDate) {
        this.name = name;
        this.targetDate = targetDate;
    }

    public String getName() {
        return name;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    @Override
    public String toString() {
        return name + " - " + targetDate;
    }
}
