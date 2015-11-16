package net.unikit.result_determination.models.interfaces;

import net.unikit.database.external.interfaces.Course;
import net.unikit.database.unikit_.interfaces.TeamRegistration;

import java.util.List;

/**
 * Created by Jones on 15.11.2015.
 *
 * Note;
 * An Interface for AllocationPlan algorithms.
 */
public interface AllocationPlanAlgorithm {

    /**
     * Calculates the allocation plan.
     * @param teams all registrated teams
     * @param courses the courses, for which the allocations will be created
     * @return the AllocationPlan
     */
    public AllocationPlan calculateAllocationPlan(List<TeamRegistration> teams, List<Course> courses);
}
