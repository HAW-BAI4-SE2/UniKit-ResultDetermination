package net.unikit.result_determination.models.interfaces;

import net.unikit.database.external.interfaces.Course;
import net.unikit.database.external.interfaces.Student;
import net.unikit.database.unikit_.interfaces.Team;

import java.util.List;

/**
 * Created by Andreas on 13.11.2015.
 */
public interface Registrations {
    List<Team> getTeams();
    List<Student> getStudentsWithoutTeam();
    List<Team> getTeamFor(Course c);
    List<Student> getTeamlessFor(Course c);
}
