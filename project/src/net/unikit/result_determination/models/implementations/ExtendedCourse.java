package net.unikit.result_determination.models.implementations;

import net.unikit.database.interfaces.entities.*;
import net.unikit.result_determination.models.exceptions.NoTeamRegistrationsFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jones on 03.01.2016.
 */
public class ExtendedCourse {

    private Course course;
    private int teamPreservation;
    private List<ExtendedCourseGroup> courseGroups;
    private List<ExtendedTeam> teams;

    private List<TeamRegistration> notMatchableTeamRegistrations;

    public ExtendedCourse(Course c) {
        this.course = c;
        this.teamPreservation = 0;
        notMatchableTeamRegistrations = new ArrayList<>();
        courseGroups = new ArrayList<>();
        for (CourseGroup group : c.getCourseGroups()) {
            courseGroups.add(new ExtendedCourseGroup(group, this));
        }

        teams = new ArrayList<>();
        for (Team t : c.getTeams()) {
            teams.add(new ExtendedTeam(t, this));
        }
    }

    public Course.ID getId() {
        return course.getId();
    }

    public String getName() {
        return course.getName();
    }

    public String getAbbreviation() {
        return course.getAbbreviation();
    }

    public Integer getSemester() {
        return course.getSemester();
    }

    public int getMinTeamSize() {
        return course.getMinTeamSize();
    }

    public int getMaxTeamSize() {
        return course.getMaxTeamSize();
    }

    public CourseLecture getCourseLecture() {
        return course.getCourseLecture();
    }

    public synchronized List<ExtendedCourseGroup> getCourseGroups() {

        return courseGroups;
    }

    public Course getCourse(){
        return course;
    }

    public List<FieldOfStudy> getFieldOfStudies() {
        return course.getFieldOfStudies();
    }

    public List<CourseRegistration> getSingleRegistrations() {
        return course.getSingleRegistrations();
    }

    public List<CourseRegistration> getAllCourseRegistrations() {
        return course.getAllCourseRegistrations();
    }

    public List<ExtendedTeam> getTeams() {
        return teams;
    }


    public List<TeamRegistration> getTeamRegistrations(){
        List<TeamRegistration> teamRegistrations = new ArrayList<>();

        for(ExtendedTeam t : teams){
               for(TeamRegistration teamRegistration : t.getTeamRegistrations()){
                   teamRegistrations.add(teamRegistration);
               }
        }
        return teamRegistrations;
    }

    public boolean hasConflictWith(ExtendedCourse course) {
        for (ExtendedCourseGroup c1Group : this.getCourseGroups()) {
            for (ExtendedCourseGroup c2Group : course.getCourseGroups()) {
                if (c1Group.hashConflictWith(c2Group)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void countTeamPreservation(){
        teamPreservation++;
    }

    public int getNumberOfTeamPreservations(){
        return teamPreservation;
    }

    public double getTeamPreservation(){
        return (((double)teamPreservation/course.getTeams().size()) *100);
    }

    public void addNotMatchableTeamRegistration(TeamRegistration teamRegistration) {
        notMatchableTeamRegistrations.add(teamRegistration);
    }

    public void removeNotMatchableTeamRegistration(TeamRegistration teamRegistration){
        if(notMatchableTeamRegistrations.contains(teamRegistration)){
            notMatchableTeamRegistrations.remove(teamRegistration);
        }
        else{
            try {
                throw new NoTeamRegistrationsFoundException();
            } catch (NoTeamRegistrationsFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized List<TeamRegistration> getNotMatchableTeamRegistrations(){
        return notMatchableTeamRegistrations;
    }

    public List<TeamRegistration> getAssignedTeamRegistrations(){
        List<TeamRegistration> teamRegistrations = new ArrayList<>();
        for(ExtendedCourseGroup group : courseGroups){
            for(TeamRegistration teamRegistration : group.getTeamRegistrations()){
                teamRegistrations.add(teamRegistration);
            }
        }
//        for(TeamRegistration teamRegistration : getTeamRegistrations()){
//            if(!notMatchableTeamRegistrations.contains(teamRegistration)){
//                teamRegistrations.add(teamRegistration);
//            }
//        }
        return teamRegistrations;
    }

    public double getAssignmentQuantity() {
//        return ((double) (getTeamRegistrations().size() - notMatchableTeamRegistrations.size()) / getTeamRegistrations().size()) * 100;
        return ((double) (getAssignedTeamRegistrations().size()) / getTeamRegistrations().size()) * 100;
    }

    @Override
    public String toString(){
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        return course.equals(o);
    }

    @Override
    public int hashCode() {
        return course.hashCode();
    }

    public void reset() {
        this.teamPreservation = 0;
        notMatchableTeamRegistrations = new ArrayList<>();
        courseGroups = new ArrayList<>();
        for (CourseGroup group : course.getCourseGroups()) {
            courseGroups.add(new ExtendedCourseGroup(group, this));
        }

        teams = new ArrayList<>();
        for (Team t : course.getTeams()) {
            teams.add(new ExtendedTeam(t, this));
        }
    }
}
