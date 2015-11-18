package net.unikit.result_determination.models.implementations.dummys;

import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.CourseRegistration;
import net.unikit.database.interfaces.entities.Student;
import net.unikit.database.interfaces.ids.CourseRegistrationId;

import java.util.Date;

/**
 * Created by abq307 on 18.11.2015.
 */
public class DummyCourseRegistrationImpl implements CourseRegistration {

    Student s;
    Course c;

    public DummyCourseRegistrationImpl(Student s, Course c){
        this.s = s;
        this.c = c;
    }

    @Override
    public CourseRegistrationId getId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Student getStudent() {
        return this.s;
    }

    @Override
    public void setStudent(Student student) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Course getCourse() {
        return c;
    }

    @Override
    public void setCourse(Course course) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCurrentlyAssignedToTeam() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCurrentlyAssignedToTeam(boolean b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getCreatedAt() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getUpdatedAt() {
        throw new UnsupportedOperationException();
    }
}
