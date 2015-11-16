package net.unikit.result_determination.models.implementations;

import net.unikit.database.external.interfaces.Student;
import net.unikit.database.unikit_.interfaces.*;
import net.unikit.result_determination.models.interfaces.RegistrationReader;
import net.unikit.result_determination.models.interfaces.Registrations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abq307 on 16.11.2015.
 */
public class RegistrationReaderImpl implements RegistrationReader {

    private CourseRegistrationManager courseRegistrationManager;
    private TeamRegistrationManager teamRegistrationManager;

    public RegistrationReaderImpl(CourseRegistrationManager courseRegistrationManager,TeamRegistrationManager teamRegistrationManager ){
        this.courseRegistrationManager = courseRegistrationManager;
        this.teamRegistrationManager = teamRegistrationManager;
    }

    @Override
    public Registrations read() {

        List<TeamRegistration> teamRegistrations = teamRegistrationManager.getAllTeamRegistrations();
        List<Team> teams = new ArrayList<>();
        for(TeamRegistration t : teamRegistrations){
            teams.add(t.getTeam());
        }

        List<CourseRegistration> courseRegistrations = courseRegistrationManager.getAllCourseRegistrations();
        List<Student> teamless = new ArrayList<>();
        for(CourseRegistration t : courseRegistrations){
            teamless.add(t.getStudentNumber());  // TODO t.getStudent() Hier ist es nicht einheitlich und nicht objektorientiert.
        }

        return RegistrationsImpl.create(teams, teamless);
    }
}
