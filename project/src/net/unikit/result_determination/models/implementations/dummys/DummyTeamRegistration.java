package net.unikit.result_determination.models.implementations.dummys;

import net.unikit.database.interfaces.entities.Student;
import net.unikit.database.interfaces.entities.Team;
import net.unikit.database.interfaces.entities.TeamRegistration;

import java.util.Date;

/**
 * Created by abq307 on 30.11.2015.
 */
public class DummyTeamRegistration implements TeamRegistration {

    Team t;
    Student s;

    public DummyTeamRegistration(Team t, Student s){
        this.t = t;
        this.s = s;
    }

    @Override
    public String toString(){
        return s.toString();
    }

    @Override
    public ID getId() {
        return null;
    }

    @Override
    public Student getStudent() {
        return s;
    }

    @Override
    public void setStudent(Student student) {

    }

    @Override
    public Team getTeam() {
        return t;
    }

    @Override
    public void setTeam(Team team) {

    }

    @Override
    public Date getCreatedAt() {
        return null;
    }

    @Override
    public Date getUpdatedAt() {
        return null;
    }
}
