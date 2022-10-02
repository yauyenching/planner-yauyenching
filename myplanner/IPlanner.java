package myplanner;

public interface IPlanner {
    void addBoard(Board b) throws AlreadyExistsException;
    void addProject(Project p) throws AlreadyExistsException;

    Iterable<Board> getBoards();
    Iterable<Project> getProjects();

    public String writeXMLData();
    public void readXMLData(String data);
}
