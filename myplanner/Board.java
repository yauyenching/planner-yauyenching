package myplanner;


import java.util.ArrayList;

public class Board implements IBoard{
    private final String NAME;
    private final ArrayList<Section> SECTIONS;

    public Board(String name, ArrayList<Section> sections) {
        NAME = name;
        SECTIONS = sections;
    }


    public String getName() {
        return this.NAME;
    }

    public Iterable<Section> getSections() {
        return this.SECTIONS;
    }

    public void addSection(Section t) throws AlreadyExistsException {
        if (SECTIONS.contains(t)) {
            throw new AlreadyExistsException("Section " + t + " already exists");
        }
        else SECTIONS.add(t);
    }

    public void removeSection(Section t) throws NotFoundException {
        if (!SECTIONS.contains(t)) {
            throw new NotFoundException("Section " + t + " does not exist");
        }
        else SECTIONS.remove(t);
    }

    public Section getSection(String sectionName) throws NotFoundException {
        for (Section section : getSections()){
            if (section.getName().equals(sectionName)) return section;
        }
        throw new NotFoundException("Section " + sectionName + " does not exist");
    }
}
