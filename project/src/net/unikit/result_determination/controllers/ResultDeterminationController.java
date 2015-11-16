package net.unikit.result_determination.controllers;

import net.unikit.database.external.interfaces.Course;
import net.unikit.database.unikit_.interfaces.CourseRegistration;
import net.unikit.database.unikit_.interfaces.TeamRegistration;
import net.unikit.result_determination.models.implementations.RandomAllocationPlanAlgorithmImpl;
import net.unikit.result_determination.models.implementations.RegistrationReaderImpl;
import net.unikit.result_determination.models.interfaces.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by Jones on 15.11.2015.
 *
 * Note:
 *
 * This is the main Controller.
 * Every interaction will be noticed here and will be delegated to the individual models or
 * to other controllers, that handle for example the database.
 *
 * To create an AllocationPlan just use the controllers method.
 */
public class ResultDeterminationController{

    private DatabaseController global; // Für die Datenbankzugriffe
    private RegistrationReader registrationReader;

    public ResultDeterminationController() throws IOException {
        global = new DatabaseController();
        registrationReader = new RegistrationReaderImpl(global.getCourseRegistrationManager(),global.getTeamRegistrationManager());
    }

    /**
     * Creates the AllocationPlan
     * @param algorithmSettings the settings which affect the algorithms behavier
     * @return the AllocationPlan
     * @ensure Every student will be assigned to a courseGroup. It could be possible that some teams have to be splitted to ensure that.
     */
    public AllocationPlan createAllocationPlan(AlgorithmSettings algorithmSettings){

//      // TODO Wie wurde es jetzt nochmal gelöst? Sind jetzt alle Studenten, die in einem Team Mitglied sind auch noch in der CourseRegistration-Tabelle?
        // Für den Algorithmus wäre es glaube ich doch schöner, wenn einzelne Personen dann in 1-mann-Teams kommen.

        /*  All courses for which the allocations shall be created  */
        List<Course> courses = global.getCourseManager().getAllCourses();

        /* The Algorithm that does the work */
        AllocationPlanAlgorithm allocPlanAlgorithm = new RandomAllocationPlanAlgorithmImpl(algorithmSettings);

        // *************************** Idee 1 *************************************

        // entweder jeder student ist in einem Team registriert (wenn einer in keinem war, haben wir ihn manuell in ein 1-Mann Team eingefügt)
        List<CourseRegistration> teamless = global.getCourseRegistrationManager().getAllCourseRegistrations();
        List<TeamRegistration> teams = global.getTeamRegistrationManager().getAllTeamRegistrations();
        //AllocationPlan allocPlan = allocPlanAlgorithm.calculateAllocationPlan(teams, teamless, courses);


        // *************************** Idee 2 **************************************
        Registrations registrations = registrationReader.read();
        // oder wir schauen uns alle Registrations an
        AllocationPlan allocPlan = allocPlanAlgorithm.calculateAllocationPlan(registrations,courses);

        return allocPlan;
    }
}
