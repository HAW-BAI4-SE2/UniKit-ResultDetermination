package net.unikit.result_determination.models.implementations.algorithms;

import net.unikit.database.interfaces.entities.*;
import net.unikit.result_determination.models.exceptions.CourseGroupDoesntExistException;
import net.unikit.result_determination.models.exceptions.NotEnoughCourseGroupsException;
import net.unikit.result_determination.models.implementations.AllocationPlanImpl;
import net.unikit.result_determination.models.interfaces.AlgorithmSettings;
import net.unikit.result_determination.models.interfaces.AllocationPlan;

import java.util.*;

/**
 * Created by Jones on 15.11.2015.
 * An Implementation for creating allocation plans inspired by Greedy-Algorithms.
 */
public class SimpleGreedyAllocationPlanAlgorithmImpl extends AbstractAllocationPlanAlgorithm {

    AlgorithmSettings settings;
    List<CourseRegistration> notMatchable;
    List<Team> notMatchableTeams;
    Map<CourseRegistration,Integer> dangerValues;


    /**
     * Initializes the object.
     * @param settings The AlgorithmSettings
     */
    public SimpleGreedyAllocationPlanAlgorithmImpl(AlgorithmSettings settings){
        this.settings = settings;
        notMatchable = new ArrayList<>();
        notMatchableTeams = new ArrayList<>();
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
        checkAvailableSlots(courses);
        /*
         * Iterates over every Course
         */
        for(Course course : courses){
            calculateSingleRegistrations(courses, course, allocPlan);
            calculateTeamRegistrations(courses, course, allocPlan);
        }
        return allocPlan;
    }

    private void calculateTeamRegistrations(List<Course> courses, Course course, AllocationPlan allocPlan) throws CourseGroupDoesntExistException {
        List<Team> teams = course.getTeams();
        Collections.shuffle(teams);

        for(Team t : teams){

            CourseGroup possibleCourseGroup = findPossibileCourseGroupFor(t, course, allocPlan); // throws CourseGroupDoesntExistException
//                System.out.println("Mögliche CourseGroup:" + possibleCourseGroup);

            // a possibile courseGroup was found
            if(possibleCourseGroup != null){
                registerTeam(t, possibleCourseGroup, allocPlan);
            }
            else{
//                    hier wäre auch ein tryToKickOtherRegistration oder sowas denkbar um einen Platz frei zu bekommen
                notMatchableTeams.add(t);
            }
        }
    }

    private void calculateSingleRegistrations(List<Course> courses, Course course, AllocationPlan allocPlan) throws CourseGroupDoesntExistException {
        List<CourseRegistration> singleRegistrations = course.getSingleRegistrations();
        Collections.shuffle(singleRegistrations); // Keinen Studenten bevorzugen
        /*
         * At this Moment in the development process the algorithm just focuses on singleRegistrations and ignores the teams.
         */
        for(CourseRegistration singleRegistration :singleRegistrations){

            CourseGroup possibleCourseGroup = findPossibileCourseGroupFor(singleRegistration, course, allocPlan); // throws CourseGroupDoesntExistException
//                System.out.println("Mögliche CourseGroup:" + possibleCourseGroup);

            // a possibile courseGroup was found
            if(possibleCourseGroup != null){
                registerStudent(singleRegistration, possibleCourseGroup, allocPlan);
            }
            else{
//                    hier wäre auch ein tryToKickOtherRegistration oder sowas denkbar um einen Platz frei zu bekommen
                notMatchable.add(singleRegistration);
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
            if(!allocPlan.isCourseGroupFull(courseGroup) && !conflict(singleRegistration,courseGroup)){
                return courseGroup;
            }
        }
        System.out.println("Für " + singleRegistration.getStudent() + " und Kurs " + course.getName() + " konnte keine freie Gruppe mehr gefunden werden.");
        return null;
    }

    /**
     * Tries to find a possibile CourseGroup for one CourseRegistration
     * @param team the Team
     * @param course the Course for which the CourseGroup shall be found
     */
    private CourseGroup findPossibileCourseGroupFor(Team team, Course course, AllocationPlan allocPlan) throws CourseGroupDoesntExistException {
        for(CourseGroup courseGroup : course.getCourseGroups()){
            if(!allocPlan.isCourseGroupFull(courseGroup) && !conflict(team,courseGroup)){
                return courseGroup;
            }
        }
        System.out.println("Für " + team + " und Kurs " + course.getName() + " konnte keine freie Gruppe mehr gefunden werden.");
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
    private List<CourseGroup> findPossibileCourseGroups(CourseRegistration singleRegistration, Course course){
        List<CourseGroup> groups = new ArrayList<>();
        for(CourseGroup g : course.getCourseGroups()){
            if(!conflict(singleRegistration,g)){
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
    public List<CourseRegistration> getNotMatchable(){
        return notMatchable;
    }

    @Override
    public List<Team> getNotMatchableTeams() {
        return this.notMatchableTeams;
    }

}
