package net.unikit.result_determination.models.implementations;

import net.unikit.database.interfaces.entities.*;
import net.unikit.result_determination.models.exceptions.CourseGroupFullException;
import net.unikit.result_determination.models.exceptions.NoTeamRegistrationsFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jones on 03.01.2016.
 */
public class ExtendedCourseGroup {

    private ExtendedCourse course;
    private CourseGroup courseGroup;

    private List<TeamRegistration> teamRegistrations;

    public ExtendedCourseGroup(CourseGroup courseGroup, ExtendedCourse course){
        this.course = course;
        this.courseGroup = courseGroup;
        teamRegistrations = new ArrayList<>();
    }

    public void addTeamRegistration(TeamRegistration teamRegistration) throws CourseGroupFullException {
        if(!isFull()){
            teamRegistrations.add(teamRegistration);
        }
        else{
            throw new CourseGroupFullException();
        }
    }

    public void removeTeamRegistration(TeamRegistration teamRegistration) throws NoTeamRegistrationsFoundException {
        if(teamRegistrations.contains(teamRegistration)){
            teamRegistrations.remove(teamRegistration);
        }
        else{
            throw new NoTeamRegistrationsFoundException();
        }
    }

    public boolean isFull(){
        return teamRegistrations.size() >= courseGroup.getMaxGroupSize();
    }

    public CourseGroup.ID getId() {
        return courseGroup.getId();
    }

    public void setCourse(Course course) {
        courseGroup.setCourse(course);
    }

    public ExtendedCourse getCourse() {
        return course;
    }

    public List<CourseGroupAppointment> getAppointments() {
        return courseGroup.getAppointments();
    }

    public void setGroupNumber(int i) {
        courseGroup.setGroupNumber(i);
    }

    public List<TeamRegistration> getTeamRegistrations() {
        return teamRegistrations;
    }

    public int getNumberOfEmptyPlaces(){
        int assignedPlaces = teamRegistrations.size();
        return getMaxGroupSize()-assignedPlaces;
    }

    public int getMaxGroupSize() {
        return courseGroup.getMaxGroupSize();
    }

    public int getGroupNumber() {
        return courseGroup.getGroupNumber();
    }

    public void setMaxGroupSize(int i) {
        courseGroup.setMaxGroupSize(i);
    }

    public boolean hashConflictWith(ExtendedCourseGroup courseGroup) {
        for(Appointment c1App : this.getAppointments()){
            for(Appointment c2App : courseGroup.getAppointments()){
                if(c1App.equals(c2App)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString(){
        return courseGroup.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExtendedCourseGroup that = (ExtendedCourseGroup) o;

        if (getCourse() != null ? !getCourse().equals(that.getCourse()) : that.getCourse() != null) return false;
        if (courseGroup != null ? !courseGroup.equals(that.courseGroup) : that.courseGroup != null) return false;
        return !(getTeamRegistrations() != null ? !getTeamRegistrations().equals(that.getTeamRegistrations()) : that.getTeamRegistrations() != null);

    }

    @Override
    public int hashCode() {
        int result = getCourse() != null ? getCourse().hashCode() : 0;
        result = 31 * result + (courseGroup != null ? courseGroup.hashCode() : 0);
        result = 31 * result + (getTeamRegistrations() != null ? getTeamRegistrations().hashCode() : 0);
        return result;
    }
}
