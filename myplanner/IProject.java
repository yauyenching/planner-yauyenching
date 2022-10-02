package myplanner;

public interface IProject {
    String getName();
    Iterable<Task> getTasks() ;
    void addTask(Task t)  throws AlreadyExistsException;
    void removeTask(Task t) throws NotFoundException;
}
