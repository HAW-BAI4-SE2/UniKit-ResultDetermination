package net.unikit.result_determination.models.implementations.dummys;

import net.unikit.database.interfaces.entities.Appointment;
import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.CourseGroup;
import net.unikit.database.interfaces.entities.CourseRegistration;
import net.unikit.database.interfaces.ids.CourseGroupId;
import net.unikit.result_determination.models.exceptions.CourseGroupFullException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abq307 on 18.11.2015.
 */
public class DummyCourseGroupImpl implements CourseGroup {

    Course course;
    List<Appointment> appointments;
    List<CourseRegistration> groupMembers;
    int groupNumber;
    int maxGroupSize;

    public DummyCourseGroupImpl(Course c, List<Appointment> appointments, int number, int maxSize){
        this.course = c;
        this.appointments = appointments;
        this.groupNumber = number;
        this.maxGroupSize = maxSize;
        groupMembers = new ArrayList<>();
    }

    public String toString(){
        return course.getName()+" Gruppe:"+groupNumber;
    }
    public void addCourseRegistration(CourseRegistration courseReg) throws CourseGroupFullException {
        if(!isFull()){
            groupMembers.add(courseReg);
        }
        else {
            throw new CourseGroupFullException();
        }
    }

    public List<CourseRegistration> getGroupMembers(){
        return groupMembers;
    }

    public boolean isFull(){
        return (groupMembers.size() >= maxGroupSize);
    }

    @Override
    public CourseGroupId getId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Course getCourse() {
        return course;
    }

    @Override
    public void setCourse(Course course) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Appointment> getAppointments() {
        return appointments;
    }

    @Override
    public void setAppointments(List<Appointment> list) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getGroupNumber() {
        return groupNumber;
    }

    @Override
    public void setGroupNumber(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMaxGroupSize() {
        return maxGroupSize;
    }

    @Override
    public void setMaxGroupSize(int i) {
        throw new UnsupportedOperationException();
    }
}
