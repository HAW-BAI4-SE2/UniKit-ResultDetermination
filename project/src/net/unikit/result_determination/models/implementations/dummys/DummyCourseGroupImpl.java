package net.unikit.result_determination.models.implementations.dummys;

import net.unikit.database.interfaces.entities.Appointment;
import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.CourseGroup;
import net.unikit.database.interfaces.entities.CourseGroupAppointment;

import java.util.List;

/**
 * Created by abq307 on 18.11.2015.
 */
public class DummyCourseGroupImpl implements CourseGroup {

    Course course;
    List<CourseGroupAppointment> appointments;
    int groupNumber;
    int maxGroupSize;

    public DummyCourseGroupImpl(Course c, List<CourseGroupAppointment> appointments, int number, int maxSize){
        this.course = c;
        this.appointments = appointments;
        this.groupNumber = number;
        this.maxGroupSize = maxSize;
    }

    public String toString(){
        return course.getName()+" Gruppe:"+groupNumber;
    }

    @Override
    public CourseGroup.ID getId() {
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
    public List<CourseGroupAppointment> getAppointments() {
        return appointments;
    }

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
