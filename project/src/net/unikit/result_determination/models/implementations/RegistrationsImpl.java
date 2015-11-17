package net.unikit.result_determination.models.implementations;

import net.unikit.database.external.interfaces.Course;
import net.unikit.database.external.interfaces.Student;
import net.unikit.database.unikit_.interfaces.Team;
import net.unikit.result_determination.models.interfaces.Registrations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreas on 13.11.2015.
 */
final class RegistrationsImpl implements Registrations {

    private List<Team> teams;
    private List<Student> teamless;

    private RegistrationsImpl(List<Team> teams, List<Student> teamless) {
        this.teams = teams;
        this.teamless = teamless;
    }

    public static Registrations create(List<Team> teams, List<Student> teamless) {
        return new RegistrationsImpl(teams, teamless);
    }

    @Override
    public List<Team> getTeams() {
        return teams;
    }

    @Override
    public List<Student> getStudentsWithoutTeam() {
        return teamless;
    }

    @Override
    public List<Team> getTeamFor(Course c) {
        List<Team> teams = new ArrayList<>();
        for(Team t : this.teams){
            if(t.getCourseId() == c.getId()){
                teams.add(t);
            }
        }
        return teams;
    }

    @Override
    public List<Student> getTeamlessFor(Course c) {
        List<Student> teamlessInCourse = new ArrayList<>();
        for(Student s : this.teamless){
            if(){
                teamlessInCourse.add(s);
            }
        }
        return teamlessInCourse;
    }
}
