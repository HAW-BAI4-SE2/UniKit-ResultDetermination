package net.unikit.result_determination.controllers;

import net.unikit.database.external.interfaces.Course;
import net.unikit.result_determination.models.implementations.algorithms.RandomAllocationPlanAlgorithmImpl;
import net.unikit.result_determination.models.interfaces.AlgorithmSettings;
import net.unikit.result_determination.models.interfaces.AllocationPlan;
import net.unikit.result_determination.models.interfaces.AllocationPlanAlgorithm;

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

    private DatabaseController dbController; // Für die Datenbankzugriffe

    /**
     * Initializes the Object
     * @throws IOException
     */
    public ResultDeterminationController() throws IOException {
        dbController = new DatabaseController();
    }

    /**
     * Creates the AllocationPlan
     * @param algorithmSettings the settings which affect the algorithms behavier
     * @return the AllocationPlan
     * @ensure Every student will be assigned to a courseGroup. It could be possible that some teams have to be splitted to ensure that.
     */
    public AllocationPlan createAllocationPlan(AlgorithmSettings algorithmSettings){

        /*  All courses for which the allocations shall be created  */
        List<Course> courses = dbController.getCourseManager().getAllCourses();

        /* The Algorithm that does the work */
        AllocationPlanAlgorithm allocPlanAlgorithm = new RandomAllocationPlanAlgorithmImpl(algorithmSettings);

        // *************************** Idee 1 *************************************
        AllocationPlan allocPlan = allocPlanAlgorithm.calculateAllocationPlan(courses);

        return allocPlan;
    }
}
