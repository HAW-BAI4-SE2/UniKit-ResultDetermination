package net.unikit.result_determination.models.interfaces;

import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.CourseRegistration;
import net.unikit.database.interfaces.entities.Team;
import net.unikit.result_determination.models.exceptions.CourseGroupDoesntExistException;
import net.unikit.result_determination.models.exceptions.NotEnoughCourseGroupsException;

import java.util.List;
import java.util.Map;

/**
 * Created by Jones on 15.11.2015.
 *
 * Note;
 * An Interface for AllocationPlan algorithms.
 */
public interface AllocationPlanAlgorithm {

    /**
     * Calculates the allocation plan.
     * @param courses the courses, for which the allocations will be created
     * @return the AllocationPlan
     */
    AllocationPlan calculateAllocationPlan(List<Course> courses) throws NotEnoughCourseGroupsException, CourseGroupDoesntExistException;

    Map<Course,List<CourseRegistration>> getNotMatchable();

    Map<Course,List<Team>> getNotMatchableTeams();

    Map<Course, Integer> getTeamPreservations();

    int getNumberOfTeamPreservations();
}
