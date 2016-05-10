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
    private Map<Student,List<CourseGroup>> studentsCourseGroups;
    private final String CRLF = "\r\n";
    private String delimiter = ";";
    private String delimiterStudents = ",";

    /**
     * Initializes the AllocationPlan
     * @param courses
     */
    public AllocationPlanImpl(List<Course> courses){
        this.courses = courses;
        this.courseGroupSingleRegistrations = new HashMap<>();
        this.courseGroupTeamRegistrations = new HashMap<>();
        this.studentsCourseGroups = new HashMap<>();
        initCourseGroupRegistrations();
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
    public boolean conflict(Course c1, Course c2){
        for(CourseGroup c1Group : c1.getCourseGroups()){
            for(CourseGroup c2Group : c2.getCourseGroups()){
                if(conflict(c1Group, c2Group)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean conflict(Team team, CourseGroup courseGroup){
        List<TeamRegistration> teamRegistrations = team.getTeamRegistrations();
        for(TeamRegistration teamReg : teamRegistrations){
            List<CourseGroup> studentCourseGroups = studentsCourseGroups.get(teamReg.getStudent());

            if(studentCourseGroups != null){
                // hier wird für alle Praktikumsgruppen, in denen ein Student Mitglied ist überprüft,
                // ob es mit der zu überprüfenden Gruppe zu einem Konflikt käme
                for(CourseGroup group : studentCourseGroups){
                    if(conflict(group,courseGroup)){ // TODO Fachrichtung und Semester beachten! Andere Semester und Fachrichtungen sind egal.
                        return true;
                    }
                }
            }
        }
        return false; // kein konflikt ist aufgetreten
    }

    @Override
    public boolean conflict(CourseRegistration singleRegistration, CourseGroup courseGroup) {
        // alle praktikumsgruppen die einem Studenten derzeit zugewiesen wurden
        List<CourseGroup> studentCourseGroups = studentsCourseGroups.get(singleRegistration.getStudent());

        if(studentCourseGroups != null){
            // hier wird für alle Praktikumsgruppen, in denen ein Student Mitglied ist überprüft,
            // ob es mit der zu überprüfenden Gruppe zu einem Konflikt käme
            for(CourseGroup group : studentCourseGroups){
                if(conflict(group,courseGroup)){
                    return true;
                }
            }
        }
        return false; // kein konflikt ist aufgetreten
    }

    @Override
    public boolean conflict(CourseGroup course1, CourseGroup course2){
        for(Appointment c1App : course1.getAppointments()){
            for(Appointment c2App : course2.getAppointments()){
                if(c1App.equals(c2App)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void removeStudent(CourseRegistration courseRegistration, CourseGroup courseGroup){
        try {
            removeCourseGroupRegistration(courseRegistration, courseGroup);
        } catch (CourseGroupDoesntExistException e) {
            e.printStackTrace();
        }

        if(this.studentsCourseGroups.get(courseRegistration.getStudent()) != null){
            List<CourseGroup> studentsCourseGroups = this.studentsCourseGroups.get(courseRegistration.getStudent());
            studentsCourseGroups.remove(courseGroup);
            this.studentsCourseGroups.put(courseRegistration.getStudent(), studentsCourseGroups); // update
        }
    }

    @Override
    public void registerStudent(CourseRegistration courseRegistration, CourseGroup courseGroup) {
        try {
            addCourseRegistration(courseGroup, courseRegistration);
        } catch (CourseGroupFullException e) {
            e.printStackTrace();
        } catch (CourseGroupDoesntExistException e) {
            e.printStackTrace();
        }
        List<CourseGroup> studentsCourseGroups;

        // Wenn der Student noch keiner einzigen Gruppe zugewiesen wurde (dann hat er auch noch keinen Map Eintrag)
        if(this.studentsCourseGroups.get(courseRegistration.getStudent()) == null){
            studentsCourseGroups = new ArrayList<>();
        }
        else{
            studentsCourseGroups = this.studentsCourseGroups.get(courseRegistration.getStudent());
        }

        studentsCourseGroups.add(courseGroup);
//        System.out.println(singleRegistration.getStudent()+" Gruppen:"+studentsCourseGroups);
        this.studentsCourseGroups.put(courseRegistration.getStudent(), studentsCourseGroups); // update
    }

    @Override
    public void registerTeam(Team team, CourseGroup courseGroup){
        for(TeamRegistration teamReg : team.getTeamRegistrations() ){
            try {
                addTeamRegistration(courseGroup, teamReg);
            } catch (CourseGroupFullException e) {
                e.printStackTrace();
            } catch (CourseGroupDoesntExistException e) {
                e.printStackTrace();
            }
            List<CourseGroup> studentsCourseGroups;

            // Wenn der Student noch keiner einzigen Gruppe zugewiesen wurde (dann hat er auch noch keinen Map Eintrag)
            if(this.studentsCourseGroups.get(teamReg.getStudent()) == null){
                studentsCourseGroups = new ArrayList<>();
            }
            else{
                studentsCourseGroups = this.studentsCourseGroups.get(teamReg.getStudent());
            }

            studentsCourseGroups.add(courseGroup);
//            System.out.println(teamReg.getStudent()+" Gruppen:"+studentsCourseGroups);
            this.studentsCourseGroups.put(teamReg.getStudent(), studentsCourseGroups); // update
        }
    }

    @Override
    public Map<Student,List<CourseGroup>> getStudentsCourseGroups(){
        return studentsCourseGroups;
    }

    @Override
    public List<CourseRegistration> getCourseRegistrations(CourseGroup courseGroup) throws CourseGroupDoesntExistException {
        if(courseGroupSingleRegistrations.containsKey(courseGroup)){
            return courseGroupSingleRegistrations.get(courseGroup);
        }
        else throw new CourseGroupDoesntExistException();
    }

    @Override
    public List<TeamRegistration> getTeamRegistrations(CourseGroup courseGroup) throws CourseGroupDoesntExistException {
        if(courseGroupTeamRegistrations.containsKey(courseGroup)){
            return courseGroupTeamRegistrations.get(courseGroup);
        }
        else throw new CourseGroupDoesntExistException();
    }

    private void initCourseGroupRegistrations() {
        for(Course c : this.courses){
            for(CourseGroup g : c.getCourseGroups()){
                courseGroupSingleRegistrations.put(g, new ArrayList<>());
                courseGroupTeamRegistrations.put(g,new ArrayList<>());
            }
        }
    }

    private void removeCourseGroupRegistration(CourseRegistration changeableStudent, CourseGroup courseGroup) throws CourseGroupDoesntExistException, NoSuchElementException {
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

    private void removeCourseGroupRegistration(TeamRegistration changeableStudent, CourseGroup courseGroup) throws CourseGroupDoesntExistException, NoSuchElementException {
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

    private void addCourseRegistration(CourseGroup group, CourseRegistration registration) throws CourseGroupFullException, CourseGroupDoesntExistException {
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

    private void addTeamRegistration(CourseGroup group, TeamRegistration registration) throws CourseGroupFullException, CourseGroupDoesntExistException {
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

}
