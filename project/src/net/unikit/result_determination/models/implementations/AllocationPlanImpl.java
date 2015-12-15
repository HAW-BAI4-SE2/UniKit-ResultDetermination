package net.unikit.result_determination.models.implementations;

import net.unikit.database.interfaces.entities.*;
import net.unikit.result_determination.models.exceptions.CourseGroupDoesntExistException;
import net.unikit.result_determination.models.exceptions.CourseGroupFullException;
import net.unikit.result_determination.models.interfaces.AllocationPlan;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Jones on 15.11.2015.
 */
public class AllocationPlanImpl implements AllocationPlan {

    private List<Course> courses;
    private Map<CourseGroup,List<CourseRegistration>> courseGroupSingleRegistrations;
    private Map<CourseGroup,List<TeamRegistration>> courseGroupTeamRegistrations;


    private final String CRLF = "\r\n";

    private String delimiter = ";";
    private String delimiterStudents = ",";
    public AllocationPlanImpl(List<Course> courses){
        this.courses = courses;
        this.courseGroupSingleRegistrations = new HashMap<>();
        this.courseGroupTeamRegistrations = new HashMap<>();
        initCourseGroupRegistrations();
    }

    private void initCourseGroupRegistrations() {
        for(Course c : this.courses){
            for(CourseGroup g : c.getCourseGroups()){
                courseGroupSingleRegistrations.put(g, new ArrayList<>());
                courseGroupTeamRegistrations.put(g,new ArrayList<>());
            }
        }
    }

    @Override
    public void exportAsCSV( File file) {
        System.out.println("File: " + file);
        try {
            FileWriter writer = new FileWriter(file);
            //add date
            writer.append(LocalDateTime.now().toString());
            writer.append(delimiter);
            writer.append(CRLF);
            //Go through List of all Courses
            for (Course course : courses) {
                //Go through List of all Groups of one Course
                for (CourseGroup courseGroup : course.getCourseGroups()) {
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
                            courseGroupTeamRegistrations.get(courseGroup).toString()
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

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public void addCourseRegistration(CourseGroup group, CourseRegistration registration) throws CourseGroupFullException, CourseGroupDoesntExistException {
        if(courseGroupSingleRegistrations.containsKey(group)){
            if(!isCourseGroupFull(group)){
                List<CourseRegistration> courseRegistrations = courseGroupSingleRegistrations.get(group);
                courseRegistrations.add(registration);
            }
            else{
                throw new CourseGroupFullException();
            }
        }
        else throw new CourseGroupDoesntExistException();
    }

    @Override
    public void addTeamRegistration(CourseGroup group, TeamRegistration registration) throws CourseGroupFullException, CourseGroupDoesntExistException {
        if(courseGroupSingleRegistrations.containsKey(group)){
            if(!isCourseGroupFull(group)){
                List<TeamRegistration> courseRegistrations = courseGroupTeamRegistrations.get(group);
                courseRegistrations.add(registration);
            }
            else{
                throw new CourseGroupFullException();
            }
        }
        else throw new CourseGroupDoesntExistException();
    }

    @Override
    public boolean isCourseGroupFull(CourseGroup group) throws CourseGroupDoesntExistException {
        if(courseGroupSingleRegistrations.containsKey(group)){
            int maxSize = group.getMaxGroupSize();
            if(courseGroupSingleRegistrations.get(group).size() + courseGroupTeamRegistrations.get(group).size() >= maxSize){ // TODO wurde schnell schnell gemacht und muss vllt nochmal überprüft werden
                return true;
            }
            else return false;
        }
        else throw new CourseGroupDoesntExistException();
    }

    @Override
    public List<CourseRegistration> getCourseRegistrations(CourseGroup group) throws CourseGroupDoesntExistException {
        if(courseGroupSingleRegistrations.containsKey(group)){
            return courseGroupSingleRegistrations.get(group);
        }
        else throw new CourseGroupDoesntExistException();
    }

    @Override
    public List<TeamRegistration> getTeamRegistrations(CourseGroup group) throws CourseGroupDoesntExistException {
        if(courseGroupTeamRegistrations.containsKey(group)){
            return courseGroupTeamRegistrations.get(group);
        }
        else throw new CourseGroupDoesntExistException();
    }

    @Override
    public void removeCourseGroupRegistration(CourseRegistration changeableStudent, CourseGroup courseGroup) throws CourseGroupDoesntExistException, NoSuchElementException {
        if(courseGroupSingleRegistrations.containsKey(courseGroup)){
            List<CourseRegistration> registrations = courseGroupSingleRegistrations.get(courseGroup);
            if(registrations.contains(changeableStudent)){
                registrations.remove(changeableStudent);
            }
            else{
                throw new NoSuchElementException(changeableStudent.getStudent()+" isn't part of this CourseGoup.");
            }
        }
        else throw new CourseGroupDoesntExistException();
    }

    @Override
    public void removeCourseGroupRegistration(TeamRegistration changeableStudent, CourseGroup courseGroup) throws CourseGroupDoesntExistException, NoSuchElementException {
        if(courseGroupTeamRegistrations.containsKey(courseGroup)){
            List<TeamRegistration> registrations = courseGroupTeamRegistrations.get(courseGroup);
            if(registrations.contains(changeableStudent)){
                registrations.remove(changeableStudent);
            }
            else{
                throw new NoSuchElementException(changeableStudent.getStudent()+" isn't part of this CourseGoup.");
            }
        }
        else throw new CourseGroupDoesntExistException();
    }

}
