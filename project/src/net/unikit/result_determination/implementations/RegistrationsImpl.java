package net.unikit.result_determination.implementations;

import net.unikit.database.external.interfaces.Student;
import net.unikit.database.unikit_.interfaces.Team;
import net.unikit.result_determination.interfaces.Registrations;

import java.util.List;

/**
 * Created by Andreas on 13.11.2015.
 */
final class RegistrationsImpl implements Registrations {
    private RegistrationsImpl() {
        throw new UnsupportedOperationException();
    }

    public static Registrations create() {
        return new RegistrationsImpl();
    }

    @Override
    public List<Team> getTeams() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Student> getStudentsWithoutTeam() {
        throw new UnsupportedOperationException();
    }
}
