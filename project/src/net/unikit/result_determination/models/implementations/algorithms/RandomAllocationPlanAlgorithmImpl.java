package net.unikit.result_determination.models.implementations.algorithms;

import net.unikit.database.external.interfaces.Course;
import net.unikit.result_determination.models.interfaces.AlgorithmSettings;
import net.unikit.result_determination.models.interfaces.AllocationPlan;
import net.unikit.result_determination.models.interfaces.AllocationPlanAlgorithm;

import java.util.List;

/**
 * Created by Jones on 15.11.2015.
 */
public class RandomAllocationPlanAlgorithmImpl implements AllocationPlanAlgorithm {

    AlgorithmSettings settings;

    public RandomAllocationPlanAlgorithmImpl(AlgorithmSettings settings){
        this.settings = settings;
    }

    @Override
    /**
     * TODO
     */
    public AllocationPlan calculateAllocationPlan(List<Course> courses) {
        AllocationPlan allocPlan = null;

        for(Course c : courses){

        }


        return allocPlan;
    }
}
