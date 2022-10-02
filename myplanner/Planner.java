package myplanner;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Planner implements IPlanner {
    // In our planner, input of "null" means undefined
    public ArrayList<Board> boards = new ArrayList<>();
    public ArrayList<Project> projects = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");

    public void addBoard(Board b) throws AlreadyExistsException {
        if (boards.contains(b)) {
            throw new AlreadyExistsException("Board " + b + " already exists");
        } else boards.add(b);
    }

    public void addProject(Project p) throws AlreadyExistsException {
        if (projects.contains(p)) {
            throw new AlreadyExistsException("Project " + p + " already exists");
        } else projects.add(p);
    }

    public Iterable<Board> getBoards() {
        return this.boards;
    }

    public HashMap<String, Board> getSections() {
        HashMap<String, Board> dict = new HashMap<>();
        for (Board board : getBoards()){
            for (Section section : board.getSections()){
                String sectionName = section.getName();
                dict.put(sectionName, board);
            }
        }
        return dict;
    }

    public Iterable<Project> getProjects() {
        return this.projects;
    }

    public void printPlanner() {
        for(Board board : getBoards()){
            System.out.println("Board: " + board.getName());
            for (Section section : board.getSections()) {
                System.out.println("\t" + section.getName());
                for (Task task : section.getTasks()) {
                    System.out.println("\t" + "\t" + task.getName());
                }
            }
        }
        for(Project project : getProjects()){
            System.out.println("Project: " + project.getName());
            for (Task task : project.getTasks()) {
                System.out.println("\t" + task.getName());
                // System.out.println("\t" + task.getDescription());
                for (Task subtask : task.getSubTasks()) {
                    System.out.println("\t" + "\t" + subtask.getName());
                }
            }
        }
    }

    public String writeXMLData() {
        DocumentBuilderFactory dbFactory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        assert dBuilder != null;
        Document doc = dBuilder.newDocument();

        // planner element
        Element plannerElement = doc.createElement("planner");
        doc.appendChild(plannerElement);

        // board elements
        for (Board board : getBoards()){
            Element boardElement = doc.createElement("board");
            plannerElement.appendChild(boardElement);

            // setting name attribute to board
            String boardName = board.getName();
            Attr bNameAttr = doc.createAttribute("name");
            bNameAttr.setValue(boardName);
            boardElement.setAttributeNode(bNameAttr);

            // section elements
            for (Section section : board.getSections()){
                Element sectionElement = doc.createElement("section");
                boardElement.appendChild(sectionElement);

                // setting name attribute to section
                String sectionName = section.getName();
                Attr sNameAttr = doc.createAttribute("name");
                sNameAttr.setValue(sectionName);
                sectionElement.setAttributeNode(sNameAttr);
            }
        }

        // project elements
        for (Project project : getProjects()) {
            Element projectElement = doc.createElement("project");
            plannerElement.appendChild(projectElement);

            // setting name attribute to project
            String projectName = project.getName();
            Attr pNameAttr = doc.createAttribute("name");
            pNameAttr.setValue(projectName);
            projectElement.setAttributeNode(pNameAttr);

            // creating description element under project
            String projectDescription = project.getDescription();
            Element pDesElem = doc.createElement("description");
            pDesElem.appendChild(doc.createTextNode(projectDescription));
            projectElement.appendChild(pDesElem);

            // creating deadline element under project
            LocalDateTime deadline = project.getDeadline();
            String projectDeadline;
            if (deadline == null) projectDeadline = "";
            else projectDeadline = formatter.format(deadline);
            Element pDeadlineElem = doc.createElement("deadline");
            pDeadlineElem.appendChild(doc.createTextNode(projectDeadline));
            projectElement.appendChild(pDeadlineElem);

            // creating task elements
            for (Task task : project.getTasks()) {
                Element taskElement = doc.createElement("task");
                projectElement.appendChild(taskElement);

                // setting name attribute to task
                String taskName = task.getName();
                Attr tNameAttr = doc.createAttribute("name");
                tNameAttr.setValue(taskName);
                taskElement.setAttributeNode(tNameAttr);

                // creating description element under task
                String taskDescription = task.getDescription();
                Element tDesElem = doc.createElement("description");
                tDesElem.appendChild(doc.createTextNode(taskDescription));
                taskElement.appendChild(tDesElem);

                // creating duration element under task
                Duration duration = task.getExpectedDuration();
                String taskDuration;
                if (duration == null) taskDuration = "";
                else taskDuration = duration.toString();
                Element tDurElem = doc.createElement("duration");
                tDurElem.appendChild(doc.createTextNode(taskDuration));
                taskElement.appendChild(tDurElem);

                // setting section attribute to task
                String taskSection = task.getSection();
                Attr tSecAttr = doc.createAttribute("section");
                tSecAttr.setValue(taskSection);
                taskElement.setAttributeNode(tSecAttr);

                for (Task subtask : task.getSubTasks()) {
                    Element subtaskElement = doc.createElement("subtask");
                    taskElement.appendChild(subtaskElement);

                    // setting name attribute to subtask
                    String subtaskName = subtask.getName();
                    Attr stNameAttr = doc.createAttribute("name");
                    stNameAttr.setValue(subtaskName);
                    subtaskElement.setAttributeNode(stNameAttr);

                    // creating description element under subtask
                    String subtaskDescription = subtask.getDescription();
                    Element stDesElem = doc.createElement("description");
                    stDesElem.appendChild(doc.createTextNode(subtaskDescription));
                    subtaskElement.appendChild(stDesElem);

                    // creating duration element under subtask
                    Duration subDuration = subtask.getExpectedDuration();
                    String subtaskDuration;
                    if (subDuration == null) subtaskDuration = "";
                    else subtaskDuration = subDuration.toString();
                    Element stDurElem = doc.createElement("duration");
                    stDurElem.appendChild(doc.createTextNode(subtaskDuration));
                    subtaskElement.appendChild(stDurElem);

                    // setting section attribute to task
                    String subtaskSection = subtask.getSection();
                    Attr stSecAttr = doc.createAttribute("section");
                    stSecAttr.setValue(subtaskSection);
                    subtaskElement.setAttributeNode(stSecAttr);
                }
            }
        }

        // write the content into xml file
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        String xmlString = "";
        try {
            transformer = tf.newTransformer();

            //A character stream that collects its output in a string buffer,
            //which can then be used to construct a string.
            StringWriter writer = new StringWriter();

            //transform document to string
            transformer.transform(new DOMSource(doc), new StreamResult(writer));

            xmlString = writer.getBuffer().toString();
        } catch (TransformerException e)
        {
            e.printStackTrace();
        }
        return xmlString;
    }

    public void readXMLData(String data) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println("Error: not a valid XML file");
            e.printStackTrace();
        }
        Document doc = null;
        try {
            assert builder != null;
            doc = builder.parse(new InputSource(new StringReader(data)));
        } catch (SAXException | IOException e) {
            System.out.println("Error: not a valid XML file");
            e.printStackTrace();
        }
        assert doc != null;
        doc.getDocumentElement().normalize();

        // From here, the entire XML file is in memory, and structured as a Tree.

        NodeList projectsList = doc.getElementsByTagName("project");
        NodeList boardsList = doc.getElementsByTagName("board");

        for (int i = 0; i < boardsList.getLength(); i++) {
            Node boardNode = boardsList.item(i);

            if (boardNode.getNodeType() == Node.ELEMENT_NODE) {
                Element currentBoard = (Element) boardsList.item(i);

                NodeList sectionNodes = currentBoard.getElementsByTagName("section");
                ArrayList<Section> sectionList = new ArrayList<>();
                Board board = new Board(currentBoard.getAttribute("name"), sectionList);

                for (int j = 0; j < sectionNodes.getLength(); j++) {
                    Node sectionNode = sectionNodes.item(j);

                    if (sectionNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element currentSection = (Element) sectionNodes.item(j);
                        String sectionName = currentSection.getAttribute("name");
                        ArrayList<Task> taskList = new ArrayList<>();
                        Section section = new Section(sectionName, taskList);
                        try {
                            board.addSection(section);
                        } catch (AlreadyExistsException e) {
                            System.out.println("Error: " + e);
                        }
                    }
                }

                try {
                    // Add board to planner
                    addBoard(board);
                } catch (AlreadyExistsException e) {
                    System.out.println("Error: " + e);
                }
            }
        }

        for (int i = 0; i < projectsList.getLength(); i++) {
            Node projectNode = projectsList.item(i);

            if (projectNode.getNodeType() == Node.ELEMENT_NODE) {
                Element currentProject = (Element) projectsList.item(i);

                String projectName = currentProject.getAttribute("name");
                String projectDescription = currentProject.getElementsByTagName("description")
                        .item(0).getTextContent();
                String deadline = currentProject.getElementsByTagName("deadline").item(0).getTextContent();
                LocalDateTime projectDeadline;
                if (deadline.equals("")) {
                    projectDeadline = null;
                }
                else projectDeadline = LocalDateTime.parse(deadline, formatter);

                ArrayList<Task> taskList = new ArrayList<>();
                Project project = new Project(projectName, projectDescription, projectDeadline, taskList);
                NodeList tasksNodes = currentProject.getElementsByTagName("task");

                for (int j = 0; j < tasksNodes.getLength(); j++) {
                    Node taskNode = tasksNodes.item(i);

                    if (taskNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element currentTask = (Element) tasksNodes.item(j);

                        String taskName = currentTask.getAttribute("name");
                        String taskDescription = currentTask.getElementsByTagName("description")
                                .item(0).getTextContent();
                        String duration = currentTask.getElementsByTagName("duration")
                                .item(0).getTextContent();
                        Duration taskDuration = null;
                        if (!duration.equals("")){
                            taskDuration = Duration.parse(duration);
                        }
                        String taskSection = currentTask.getAttribute("section");

                        Task task = new Task(taskName, taskDescription, taskDuration, new ArrayList<>());

                        NodeList subtasksNodes = currentTask.getElementsByTagName("subtask");
                        HashMap<String, Board> sections = getSections();
                        for (int z = 0; z < subtasksNodes.getLength(); z++) {
                            Element currentSubtask = (Element) subtasksNodes.item(z);
                            String subtaskName = currentSubtask.getAttribute("name");
                            String subtaskDescription = currentSubtask.getElementsByTagName("description")
                                    .item(0).getTextContent();
                            String subDuration = currentSubtask.getElementsByTagName("duration")
                                    .item(0).getTextContent();
                            Duration subtaskDuration = null;
                            if (!subDuration.equals("")) {
                                subtaskDuration = Duration.parse(subDuration);
                            }
                            String subtaskSection = currentSubtask.getAttribute("section");

                            Task subtask = new Task(subtaskName, subtaskDescription, subtaskDuration, new ArrayList<>());
                            try {
                                // Add subtask to task
                                task.addSubTask(subtask);
                                // Add subtask to section if applicable
                                if (!subtaskSection.equals("")) {
                                    Board board = sections.get(taskSection);
                                    Section section = board.getSection(subtaskSection);
                                    section.addTask(subtask);
                                }
                            } catch (AlreadyExistsException | NotFoundException e) {
                                System.out.println("Error: " + e);
                            }
                        }
                        try {
                            // Add task to project
                            project.addTask(task);
                        } catch (AlreadyExistsException e) {
                            System.out.println("Error: " + e);
                        }
                        try {
                            // Add task to section if applicable
                            if (!taskSection.equals("")) {
                                Board board = sections.get(taskSection);
                                Section section = board.getSection(taskSection);
                                section.addTask(task);
                            }
                        } catch (AlreadyExistsException | NotFoundException e) {
                            System.out.println("Error: " + e);
                        }
                    }
                }
                try {
                    // Add project to planner
                    addProject(project);
                } catch (AlreadyExistsException e) {
                    System.out.println("Error: " + e);
                }
            }
        }
    }
}

