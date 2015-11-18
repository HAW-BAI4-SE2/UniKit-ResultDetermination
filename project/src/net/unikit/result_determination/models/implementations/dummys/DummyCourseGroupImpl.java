package net.unikit.result_determination.models.implementations.dummys;

import net.unikit.database.interfaces.entities.Appointment;
import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.CourseGroup;
import net.unikit.database.interfaces.entities.Student;
import net.unikit.database.interfaces.ids.CourseGroupId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abq307 on 18.11.2015.
 */
public class DummyCourseGroupImpl implements CourseGroup {

    Course course;
    List<Appointment> appointments;
    List<Student> groupMembers;
    int groupNumber;
    int maxGroupSize;

    public DummyCourseGroupImpl(Course c, List<Appointment> appointments, int number, int maxSize){
        this.course = c;
        this.appointments = appointments;
        this.groupNumber = number;
        this.maxGroupSize = maxSize;
        groupMembers = new ArrayList<>();
    }

    public void addStudent(Student s){
        groupMembers.add(s);
    }

    public List<Student> getGroupMembers(){
        return groupMembers;
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
