package net.unikit.result_determination.controllers;

import net.unikit.database.external.interfaces.Course;
import net.unikit.database.unikit_.interfaces.TeamRegistration;
import net.unikit.result_determination.models.implementations.RandomAllocationPlanAlgorithmImpl;
import net.unikit.result_determination.models.interfaces.AlgorithmSettings;
import net.unikit.result_determination.models.interfaces.AllocationPlan;
import net.unikit.result_determination.models.interfaces.AllocationPlanAlgorithm;

import java.io.IOException;
import java.util.List;

/**
 * Created by Jones on 15.11.2015.
 */
public class ResultDeterminationController{

    private DatabaseController global; // Für die Datenbankzugriffe

    public ResultDeterminationController() throws IOException {
        global = new DatabaseController();
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

        List<TeamRegistration> teams = global.getTeamRegistrationManager().getAllTeamRegistrations();
        List<Course> courses = global.getCourseManager().getAllCourses();

        AllocationPlanAlgorithm allocPlanAlgorithm = new RandomAllocationPlanAlgorithmImpl(algorithmSettings);
        AllocationPlan allocPlan = allocPlanAlgorithm.calculateAllocationPlan(teams, courses);

        return allocPlan;
    }
}
