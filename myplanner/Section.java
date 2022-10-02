package myplanner;

import java.util.ArrayList;

public class Section implements ISection {
    private final String NAME;
    private final ArrayList<Task> TASKS;

    public Section(String name, ArrayList<Task> tasks){
        NAME = name;
        TASKS = tasks;
    }

    public String getName() {
        return this.NAME;
    }

    public Iterable<Task> getTasks() {
        return this.TASKS;
    }

    public void addTask(Task t) throws AlreadyExistsException {
        if (TASKS.contains(t)) {
            throw new AlreadyExistsException("Task " + t + " already exists");
        }
        else {
            TASKS.add(t);
            t.setSection(NAME);
        }
    }

    public void removeTask(Task t) throws NotFoundException {
        if (!TASKS.contains(t)) {
            throw new NotFoundException("Task " + t + " does not exist");
        }
        else TASKS.remove(t);
    }
}
