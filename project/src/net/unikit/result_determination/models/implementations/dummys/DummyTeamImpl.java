package net.unikit.result_determination.models.implementations.dummys;

import net.unikit.database.interfaces.entities.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by abq307 on 30.11.2015.
 */
public class DummyTeamImpl implements net.unikit.database.interfaces.entities.Team {
    Course c;
    List<TeamRegistration> registrations;

    public DummyTeamImpl(Course c){
        this.c = c;
        this.registrations = new ArrayList<>();
    }

    public void addTeamRegistration(TeamRegistration t){
        registrations.add(t);
    }

    public void removeTeamRegistration(TeamRegistration t){
        registrations.remove(t);
    }

    @Override
    public String toString(){
        String s = "*********** Team ***********\n";
        s+=registrations;
        return s;
    }
    @Override
    public ID getId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Course getCourse() {
        return c;
    }

    @Override
    public void setCourse(Course course) {
        this.c=course;
    }

    @Override
    public Student getCreatedBy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCreatedBy(Student student) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<MembershipRequest> getMembershipRequests() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TeamInvitation> getTeamInvitations() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TeamRegistration> getTeamRegistrations() {
        return registrations;
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
