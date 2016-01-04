package net.unikit.result_determination.models.implementations;

import net.unikit.database.interfaces.entities.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Jones on 03.01.2016.
 */
public class ExtendedTeam {

    private ExtendedCourse course;
    private Team team;
    private List<TeamRegistration> registrations;

    public ExtendedTeam(Team t, ExtendedCourse course) {
        this.course = course;
        this.team = t;
        registrations = team.getTeamRegistrations();
    }

    public Team.ID getId() {
        return team.getId();
    }

    public ExtendedCourse getCourse() {
        return course;
    }

    public List<MembershipRequest> getMembershipRequests() {
        return team.getMembershipRequests();
    }

    public Student getCreatedBy() {
        return team.getCreatedBy();
    }

    public List<TeamInvitation> getTeamInvitations() {
        return team.getTeamInvitations();
    }

    public Date getCreatedAt() {
        return team.getCreatedAt();
    }

    public List<TeamRegistration> getTeamRegistrations() {
        return registrations;
    }

    public Date getUpdatedAt() {
        return team.getUpdatedAt();
    }

    public int getSize() {
        return team.getTeamRegistrations().size();
    }

    public void addTeamRegistration(TeamRegistration teamRegistration){
        registrations.add(teamRegistration);
    }

    public boolean hasConflictWith(ExtendedCourseGroup courseGroup, Map<Student, List<ExtendedCourseGroup>> studentsCourseGroups) {
        for (TeamRegistration teamReg : team.getTeamRegistrations()) {
            List<ExtendedCourseGroup> studentCourseGroups = studentsCourseGroups.get(teamReg.getStudent());

            if (studentCourseGroups != null) {
                // hier wird für alle Praktikumsgruppen, in denen ein Student Mitglied ist überprüft,
                // ob es mit der zu überprüfenden Gruppe zu einem Konflikt käme
                for (ExtendedCourseGroup group : studentCourseGroups) {
                    if (group.hashConflictWith(courseGroup)) { // TODO Fachrichtung und Semester beachten! Andere Semester und Fachrichtungen sind egal.
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String toString(){
        return team.toString();
    }
}
