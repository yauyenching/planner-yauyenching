package myplanner;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Project implements IProject {
    private final String NAME;
    private final String DESCRIPTION;
    private final LocalDateTime DEADLINE;
    private final ArrayList<Task> TASKS;


    public Project(String name, String description, LocalDateTime deadline, ArrayList<Task> tasks){
        NAME = name;
        DESCRIPTION = description;
        DEADLINE = deadline;
        TASKS = tasks;
    }

    public String getName() {
        return this.NAME;
    }

    public String getDescription() {
        return this.DESCRIPTION;
    }

    public LocalDateTime getDeadline() {
        return this.DEADLINE;
    }

    public Iterable<Task> getTasks() {
        return this.TASKS;
    }

    public void addTask(Task t) throws AlreadyExistsException {
        if (TASKS.contains(t)) {
            throw new AlreadyExistsException("Task " + t + " already exists");
        }
        else TASKS.add(t);
    }

    public void removeTask(Task t) throws NotFoundException {
        if (!TASKS.contains(t)) {
            throw new NotFoundException("Task " + t + " does not exist");
        }
        else TASKS.remove(t);
    }
}
