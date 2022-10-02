package myplanner;

public interface IBoard {
    String getName();

    Iterable<Section> getSections() ;
    void addSection(Section t) throws AlreadyExistsException;
    void removeSection(Section t) throws NotFoundException;
    Section getSection(String sectionName) throws NotFoundException;
}
