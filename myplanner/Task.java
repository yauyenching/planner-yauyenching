package myplanner;

import java.time.Duration;
import java.util.ArrayList;

public class Task implements ITask, Comparable<Task> {
    private final String NAME;
    private final String DESCRIPTION;
    private final Duration ESTIMATED_DURATION;
    private final ArrayList<Task> SUBTASKS;
    private String section;

    public Task(String name, String description, Duration estimated_duration, ArrayList<Task> subtasks) {
        NAME = name;
        DESCRIPTION = description;
        ESTIMATED_DURATION = estimated_duration;
        SUBTASKS = subtasks;
    }

    public String getName() {
        return this.NAME;
    }

    public String getDescription() {
        return this.DESCRIPTION;
    }

    public Duration getExpectedDuration() {
        return this.ESTIMATED_DURATION;
    }

    public Iterable<Task> getSubTasks() {
        return this.SUBTASKS;
    }

    public void setSection(String name) {
        section = name;
    }

    public String getSection() {
        return section;
    }

    public void addSubTask(Task t) throws AlreadyExistsException {
        if (SUBTASKS.contains(t)) {
            throw new AlreadyExistsException("Subtask " + t + " already exists");
        }
        else SUBTASKS.add(t);
    }

    public void removeSubTask(Task t) throws NotFoundException {
        if (!SUBTASKS.contains(t)) {
            throw new NotFoundException("Subtask " + t + " does not exist");
        }
        else SUBTASKS.remove(t);
    }

    public int compareTo(Task t) {
        return NAME.compareTo(t.getName());
    }
}
