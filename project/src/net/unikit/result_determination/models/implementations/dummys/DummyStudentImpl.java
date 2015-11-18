package net.unikit.result_determination.models.implementations.dummys;

import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.FieldOfStudy;
import net.unikit.database.interfaces.entities.Student;
import net.unikit.database.interfaces.entities.Team;
import net.unikit.database.interfaces.ids.StudentNumber;

import java.util.List;

/**
 * Created by abq307 on 18.11.2015.
 */
public class DummyStudentImpl implements Student {

    StudentNumber s;

    public DummyStudentImpl(StudentNumber s){
        this.s = s;
    }

    @Override
    public StudentNumber getStudentNumber() {
        return s;
    }

    @Override
    public void setStudentNumber(StudentNumber studentNumber) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getFirstName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFirstName(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getLastName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLastName(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getEmail() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setEmail(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FieldOfStudy getFieldOfStudy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFieldOfStudy(FieldOfStudy fieldOfStudy) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getSemester() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSemester(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Course> getCompletedCourses() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCompletedCourses(List<Course> list) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Course> getRegisteredCourses() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRegisteredCourses(List<Course> list) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Team> getRegisteredTeams() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRegisteredTeams(List<Team> list) {
        throw new UnsupportedOperationException();
    }
}
