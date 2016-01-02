package net.unikit.result_determination.models.implementations.algorithms;

import net.unikit.database.interfaces.entities.*;
import net.unikit.result_determination.models.exceptions.CourseGroupDoesntExistException;
import net.unikit.result_determination.models.exceptions.NotEnoughCourseGroupsException;
import net.unikit.result_determination.models.implementations.AllocationPlanImpl;
import net.unikit.result_determination.models.interfaces.AlgorithmSettings;
import net.unikit.result_determination.models.interfaces.AllocationPlan;
import net.unikit.result_determination.models.interfaces.AllocationPlanAlgorithm;
import net.unikit.result_determination.utils.AlgorithmUtils;

import java.util.*;

/**
 * Created by Jones on 15.11.2015.
 * An Implementation for creating allocation plans inspired by Greedy-Algorithms.
 */
public class SimpleGreedyAllocationPlanAlgorithmImpl implements AllocationPlanAlgorithm{

    AlgorithmSettings settings;
    Map<Course,List<CourseRegistration>> notMatchable;
    Map<Course,List<Team>> notMatchableTeams;
    Map<CourseRegistration,Integer> dangerValues;


    /**
     * Initializes the object.
     * @param settings The AlgorithmSettings
     */
    public SimpleGreedyAllocationPlanAlgorithmImpl(AlgorithmSettings settings){
        this.settings = settings;
        notMatchable = new HashMap<>();
        notMatchableTeams = new HashMap<>();
        dangerValues = new HashMap<>();
    }

    @Override
    /**
     * TODO the algorithm ignores TeamRegistrations at this moment
     * TODO Zurzeit ist der Algorithmus auch noch nicht Random -> es wird noch nichts geshufflet (könnte man ganz am Anfang einmal machen)
     * Algorithm Idea:
     *
     * For every Course
     *      For Every Registration
     *          possibileCourseGroup = findPossibileCourseGroup(the Registration, the Course);
     *
     *          either there was a possibile Group
     *                 add the Registration to the possibile Group
     *          else mark the Registration as notMatchable
     */
    public AllocationPlan calculateAllocationPlan(List<Course> courses) throws NotEnoughCourseGroupsException, CourseGroupDoesntExistException {
        AllocationPlan allocPlan = new AllocationPlanImpl(courses);
        // Check, if there are enough available slots to assign each Student to a courseGroup
        AlgorithmUtils.checkAvailableSlots(courses);

        System.out.println("***** START ALGORITHM *****");

        /*
         * Iterates over every Course
         */
        for(Course course : courses){
            // Init "Auffangbehälter"
            notMatchable.put(course, new ArrayList<>());
            notMatchableTeams.put(course, new ArrayList<>());

            calculateTeamRegistrations(courses, course, allocPlan);
            calculateSingleRegistrations(courses, course, allocPlan);
        }

        System.out.println("***** STOP ALGORITHM *****");
        return allocPlan;
    }

    private void calculateTeamRegistrations(List<Course> courses, Course course, AllocationPlan allocPlan) throws CourseGroupDoesntExistException {
        List<Team> teams = course.getTeams();
        Collections.shuffle(teams);

        for(Team t : teams){

            CourseGroup possibleCourseGroup = findPossibileCourseGroupFor(t, course, allocPlan); // throws CourseGroupDoesntExistException

            // a possibile courseGroup was found
            if(possibleCourseGroup != null){
                allocPlan.registerTeam(t, possibleCourseGroup);
            }
            else{
                System.out.println("@@@@ FEHLER: " + t + " -> " + course.getName());
                List<Team> notMatchable = notMatchableTeams.get(course);
                notMatchable.add(t);
                notMatchableTeams.put(course,notMatchable);
            }
        }
    }

    private void calculateSingleRegistrations(List<Course> courses, Course course, AllocationPlan allocPlan) throws CourseGroupDoesntExistException {
        List<CourseRegistration> singleRegistrations = course.getSingleRegistrations();
        Collections.shuffle(singleRegistrations); // Keinen Studenten bevorzugen

        for(CourseRegistration singleRegistration :singleRegistrations){

            CourseGroup possibleCourseGroup = findPossibileCourseGroupFor(singleRegistration, course, allocPlan); // throws CourseGroupDoesntExistException

            // a possibile courseGroup was found
            if(possibleCourseGroup != null){
                allocPlan.registerStudent(singleRegistration, possibleCourseGroup);
            }
            else{
                System.out.println("@@@@ FEHLER: " + singleRegistration.getStudent() + " -> " + course.getName());
                List<CourseRegistration> nMatchable = notMatchable.get(course);
                nMatchable.add(singleRegistration);
                notMatchable.put(course,nMatchable);
            }
        }
    }

    /**
     * Tries to find a possibile CourseGroup for one CourseRegistration
     * @param singleRegistration the Registration for which an available CourseGroup shall be found
     * @param course the Course for which the CourseGroup shall be found
     */
    private CourseGroup findPossibileCourseGroupFor(CourseRegistration singleRegistration, Course course, AllocationPlan allocPlan) throws CourseGroupDoesntExistException {
        for(CourseGroup courseGroup : course.getCourseGroups()){
            if(!allocPlan.isCourseGroupFull(courseGroup) && !allocPlan.conflict(singleRegistration, courseGroup)){
                return courseGroup;
            }
        }
        return null;
    }

    /**
     * Tries to find a possibile CourseGroup for one CourseRegistration
     * @param team the Team
     * @param course the Course for which the CourseGroup shall be found
     */
    private CourseGroup findPossibileCourseGroupFor(Team team, Course course, AllocationPlan allocPlan) throws CourseGroupDoesntExistException {
        for(CourseGroup courseGroup : course.getCourseGroups()){
            if(!allocPlan.isCourseGroupFull(courseGroup) && !allocPlan.conflict(team, courseGroup)){
                return courseGroup;
            }
        }

        return null;
    }


    /**
     * Tries to find every potential CourseGroup.
     * A potential CourseGroup is a CourseGroup that doesn't conflict with other CourseGroups of the Student
     * Warning! The CourseGroups might be full.
     * @param singleRegistration
     * @param course
     * @return
     */
    private List<CourseGroup> findPossibileCourseGroups(CourseRegistration singleRegistration, Course course, AllocationPlan allocPlan){
        List<CourseGroup> groups = new ArrayList<>();
        for(CourseGroup g : course.getCourseGroups()){
            if(!allocPlan.conflict(singleRegistration, g)){
                groups.add(g);
            }
        }
        return groups;
    }

    /**
     * A List of all CourseRegistrations where no possibile Assignment existed without
     * accepting some conflicts between other CourseGroups.
     * @return all not matchable CourseRegistrations
     */
    public Map<Course,List<CourseRegistration>> getNotMatchable(){
        return notMatchable;
    }

    @Override
    public Map<Course,List<Team>> getNotMatchableTeams() {
        return this.notMatchableTeams;
    }

    @Override
    public Map<Course, Integer> getTeamPreservations() {
        return null;
    }

    @Override
    public int getNumberOfTeamPreservations() {
        return 0;
    }

}
