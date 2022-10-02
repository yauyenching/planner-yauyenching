package myplanner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Main {
    /*static String xmlString = "<planner>\n" +
            "\t<board name=\"GTD\">\n" +
            "\t\t<section name=\"Plan\"></section>\n" +
            "\t\t<section name=\"Review\"></section>\n" +
            "\t</board>\n" +
            "\t<project name=\"YSC3000\" description=\"null\" deadline=\"Tuesday, Oct 13, 2020 12:10:56 PM\">\n" +
            "\t\t<task name=\"Assignment 02\" description=\"programming\" duration=\"PT11H\" section=\"Plan\">\n" +
            "\t\t\t<task name=\"Fast fib\" description=\"null\" duration=\"null\" section=\"Review\"></task></task>\n" +
            "\t\t<task name=\"Assignment 03\" description=\"theory\" duration=\"PT1D\" section=\"Review\"></task>\n" +
            "\t</project>\n" +
            "\t<project name=\"YSC2808\" description=\"null\" deadline=\"Monday, Nov 2, 2020 11:59:59 PM\">\n" +
            "\t\t<task name=\"Group project\" description=\"Team members: Paula, Jia Tang, Yen Ching, Kosuke\" duration=\"PT14DT3H20M\" section=\"Plan\"></task>\n" +
            "\t</project>\n" +
            "</planner>";*/
    static String xmlString = 
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
            "<planner>" +
                    "<board name=\"GTD\">" +
                            "<section name=\"Plan\"/>" +
                            "<section name=\"Review\"/>" +
                    "</board>" +
                    "<board name=\"Important\">" +
                            "<section name=\"Urgent\"/>" +
                    "</board>" +
            "<project name=\"YSC3000\">" +
                    "<description>Computational Theory</description>" +
                    "<deadline>2020-10-10 11:59 PM</deadline>" +
                    "<task name=\"Assignment 02\" section=\"Plan\">" +
                            "<description>programming</description>" +
                            "<duration>PT11H</duration>" +
                            "<subtask name=\"Assignment 01\" section=\"Review\">" +
                                    "<description>theory</description>" +
                                    "<duration/>" +
                            "</subtask>" +
                    "</task>" +
            "</project>" +
            "<project name=\"CS3204\">" +
                    "<description>Interactive Design</description>" +
                    "<deadline/>" +
                    "<task name=\"Group Project Draft\" section=\"Plan\">" +
                            "<description>Team members: Paula, Jia Tang, Yen Ching, Kosuke</description>" +
                            "<duration>PT171H4M</duration>" +
                    "</task>" +
                    "<task name=\"User Study\" section=\"Urgent\">" +
                            "<description/>" +
                            "<duration>PT45M</duration>" +
                    "</task>" +
            "</project>" +
            "</planner>";

    // This main function carries out a series of tasks and tests for my own debugging purposes
    public static void main(String[] args)  {
        // Read out structure of existing planner from XMLString
        Planner planner = new Planner();
        planner.readXMLData(xmlString);

        // Write existing planner to XML string and assert that it's the same XML string
        String writtenXML = planner.writeXMLData();
        System.out.println(writtenXML);
        assert(writtenXML.equals(xmlString));

        // Make sure that written planner can still be read and that no exceptions are produced
        Planner testPlanner = new Planner();
        testPlanner.readXMLData(writtenXML);

        // Print out structure of existing planner
        System.out.println("\n");
        System.out.println("BEFORE:");
        planner.printPlanner();

        // Carry out functions in use-case
        // Add "YSC3232" Board
        Board board = new Board("YSC3232", new ArrayList<>());
        Section todo = new Section("TODO", new ArrayList<>());
        Section done = new Section("Done", new ArrayList<>());
        try {
            board.addSection(todo);
            board.addSection(done);
            planner.addBoard(board);
        } catch(Exception e) {}

        // Add "YSC3232" Project
        Project project = new Project("YSC3232", "", null, new ArrayList<>());
        Task T1 = new Task("T1", "", null, new ArrayList<>());
        Task T2 = new Task("T2", "", null, new ArrayList<>());
        Task T3 = new Task("T3", "", null, new ArrayList<>());
        Task T4 = new Task("T4", "", null, new ArrayList<>());
        try {
            // Add T1, T2, T3 tasks into project
            project.addTask(T1);
            project.addTask(T2);
            project.addTask(T3);
            // Orphaned task without section test - commented out because not part of use case
            // project.addTask(T4);
            planner.addProject(project);
            // Add T1, T2, T3 tasks into corresponding sections
            todo.addTask(T1);
            todo.addTask(T2);
            done.addTask(T3);
        } catch(Exception e) {}

        // Print out structure of modified planner
        System.out.println("\n");
        System.out.println("AFTER:");
        planner.printPlanner();

        // Write XML string of modified planner and print it out
        String modifiedXML = planner.writeXMLData();
        System.out.println("\n");
        System.out.println(modifiedXML);

        // Make sure written XML string of modified planner can still be read
        Planner modifiedPlanner = new Planner();
        modifiedPlanner.readXMLData(modifiedXML);
    }
}
