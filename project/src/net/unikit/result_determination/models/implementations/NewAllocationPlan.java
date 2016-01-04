package net.unikit.result_determination.models.implementations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Jones on 03.01.2016.
 */
public class NewAllocationPlan {

    private final String CRLF = "\r\n";
    private String delimiter = ";";
    private String delimiterStudents = ",";

    private List<ExtendedCourse> courses;

    //    private List<ExtendedTeam> not
    private int numberOfStudents;
    private int numberOfTeams;
    private int numberOfRegistrations;
    private long algorithmRuntime;
    private int numberOfTeamPreservation;
    private int numberOfNotMatchableRegistrations;
    /**
     * Initializes the AllocationPlan
     */
    public NewAllocationPlan(List<ExtendedCourse> courses){
        this.courses = courses;
        this.numberOfStudents = 0;
        this.numberOfTeams = 0;
        this.numberOfRegistrations = 0;
        this.algorithmRuntime = 0;
        this.numberOfTeamPreservation = 0;
        this.numberOfNotMatchableRegistrations = 0;
    }

    public List<ExtendedCourse> getCourses() {
        return courses;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public int getNumberOfTeams() {
        return numberOfTeams;
    }

    public int getNumberOfRegistrations() {
        return numberOfRegistrations;
    }

    public long getAlgorithmRuntime() {
        return algorithmRuntime;
    }

    public int getNumberOfTeamPreservation(){
        return numberOfTeamPreservation;
    }

    public double getGlobalTeamPreservation() {
        return (((double) numberOfTeamPreservation / numberOfTeams) * 100);
    }

    public int getNumberOfNotMatchableRegistrations() {
        return numberOfNotMatchableRegistrations;
    }

    public int getNumberOfMatchableRegistrations(){
        return numberOfRegistrations-numberOfNotMatchableRegistrations;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public void setCourses(List<ExtendedCourse> courses) {
        this.courses = courses;
    }

    public void setNumberOfTeams(int numberOfTeams) {
        this.numberOfTeams = numberOfTeams;
    }

    public void setNumberOfRegistrations(int numberOfRegistrations) {
        this.numberOfRegistrations = numberOfRegistrations;
    }

    public void setAlgorithmRuntime(long algorithmRuntime) {
        this.algorithmRuntime = algorithmRuntime;
    }

    public void setNumberOfTeamPreservation(int numberOfTeamPreservation) {
        this.numberOfTeamPreservation = numberOfTeamPreservation;
    }

    public void setNumberOfNotMatchableRegistrations(int unassignedRegistrations) {
        this.numberOfNotMatchableRegistrations = unassignedRegistrations;
    }


    public void exportAsCSV( File file) {
        System.out.println("File: " + file);
        try {
            FileWriter writer = new FileWriter(file);
            //add date
            writer.append(LocalDateTime.now().toString());
            writer.append(delimiter);
            writer.append(CRLF);
            //TODO
            //Go through List of all Courses
            for (ExtendedCourse course : courses) {
                //Go through List of all Groups of one Course
                for (ExtendedCourseGroup courseGroup : course.getCourseGroups()) {
                    writer.append(course.getName());
                    //add delimiter
                    writer.append(
                            delimiter
                    );
                    //add groupNumber to the List
                    writer.append(
                            String.valueOf(courseGroup.getGroupNumber())
                    );
                    //add delimiter
                    writer.append(
                            delimiter
                    );
                    writer.append(
                            courseGroup.getTeamRegistrations().toString()
//                                // add all students of the Group
//                                 // TO DO List<Student> getStudentsOfTheGroup
//                                course.getCourseGroups().get(courseGroup).get(student).getStudentNumber().toString()
                    );
                }
            }
            //makes buffer empty
            writer.flush();
            //close the stream
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        String result = "\n***** Globales Ergebnis *****" + "\nAlgorithm Runtime: " + algorithmRuntime +
                "\nStudenten: " + numberOfStudents +
                "\nVeranstaltungen: " + courses.size() +
                "\nAnmeldungen: " + numberOfRegistrations +
                "\nErhaltenen Teams: " + numberOfTeamPreservation + " von " + numberOfTeams +
                "\nNotMatchable global: " + numberOfNotMatchableRegistrations + " von " + numberOfRegistrations +
                "\n****************************** END ALGORITHM ******************************";
        return result;
    }
}
