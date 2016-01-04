package net.unikit.result_determination.models.implementations.algorithms;

import net.unikit.database.interfaces.entities.*;
import net.unikit.result_determination.models.exceptions.CourseGroupDoesntExistException;
import net.unikit.result_determination.models.exceptions.NotEnoughCourseGroupsException;
import net.unikit.result_determination.models.implementations.AllocationPlanImpl;
import net.unikit.result_determination.models.implementations.dummys.DummyTeamImpl;
import net.unikit.result_determination.models.interfaces.AllocationPlan;
import net.unikit.result_determination.models.interfaces.AllocationPlanAlgorithm;
import net.unikit.result_determination.utils.AlgorithmUtils;
import net.unikit.result_determination.utils.TeamSizeComparator;

import java.util.*;

/**
 * Created by Jones on 10.12.2015.
 */
public class SecondExtendedGreedyAllocationPlanAlgorithm implements AllocationPlanAlgorithm {

    private Map<Course,List<Team>> notMatchable;
    private Map<Course, Map<Team,Integer>> dangerValues;
    private Map<Course, List<Course>> possibileCourseConflicts;
    private Map<Course,Integer> teamPreservations;
    private int globalTeamPreservation;

    public SecondExtendedGreedyAllocationPlanAlgorithm(){
        notMatchable = new HashMap<>();
        dangerValues = new HashMap<>();
        teamPreservations = new HashMap<>();
        possibileCourseConflicts = new HashMap<>();
        globalTeamPreservation=0;
    }

    /*
     * The Algorithm:
     *
     * 1. For every Course c
     *    2. For every registrated Team
     *       Group g = findGroup(team,c); //
     *       if (g not null)
     *          g.addRegistration(team);
     *       else
     *          split Team into separated one-person-teams
     *    3. For every unfinished Team
     *       Group g = findGroup(team,c); //
     *       if (g not null)
     *          g.addRegistration(team);
     *       else
     *          split Team into separated one-person-teams
     *
     */
    @Override
    public AllocationPlan calculateAllocationPlan(List<Course> courses) throws NotEnoughCourseGroupsException, CourseGroupDoesntExistException {
        AllocationPlan allocPlan = new AllocationPlanImpl(courses);
        // Check, if there are enough available slots to assign each Student to a courseGroup
        AlgorithmUtils.checkAvailableSlots(courses);
//        calculateConflictsBetweenCourses(courses, allocPlan);

        for(Course course : courses){
            int courseTeamPreservation=0;
            notMatchable.put(course,new ArrayList<>());
            List<Team> unfinishedTeams = new ArrayList<>();

            List<Team> teams = course.getTeams();
            Collections.shuffle(teams);
            Collections.sort(teams, new TeamSizeComparator()); // Innerhalb der Größe wird es trotzdem ein anderes geben

            for(Team t : teams){

                CourseGroup possibleCourseGroup = findPossibileCourseGroupFor(t, course, allocPlan); // throws CourseGroupDoesntExistException

                if(possibleCourseGroup != null){
                    allocPlan.registerTeam(t, possibleCourseGroup);
                    courseTeamPreservation++;
                }
                else{
//                    System.out.println("@@@@ FEHLER: " + t + " -> " + course.getName());
                    for(TeamRegistration s : t.getTeamRegistrations()){
                        DummyTeamImpl newTeam = new DummyTeamImpl(course);
                        newTeam.addTeamRegistration(s);
                        unfinishedTeams.add(newTeam);
                    }
                }
            }
            teamPreservations.put(course,courseTeamPreservation);
            globalTeamPreservation+=courseTeamPreservation;

//            System.out.println("**** Check Unfinished Teams ***");
            for(Team t : unfinishedTeams){

                CourseGroup possibleCourseGroup = findPossibileCourseGroupFor(t, course, allocPlan); // throws CourseGroupDoesntExistException

                if(possibleCourseGroup != null){
                    allocPlan.registerTeam(t, possibleCourseGroup);
                }
                else{
//                    System.out.println("@@@@ FEHLER: " + t + " -> " + course.getName());
                    List<Team> notMatchableTeams = notMatchable.get(course);
                    notMatchableTeams.add(t);
                    notMatchable.put(course,notMatchableTeams);
                }
            }
        }
        return allocPlan;
    }

    @Override
    public Map<Course, List<CourseRegistration>> getNotMatchable() {
        return null;
    }

    @Override
    public Map<Course, List<Team>> getNotMatchableTeams() {
        return notMatchable;
    }

    @Override
    public Map<Course, Integer> getTeamPreservations() {
        return teamPreservations;
    }

    @Override
    public int getNumberOfTeamPreservations() {
        return globalTeamPreservation;
    }

    /**
     * Tries to find a possibile CourseGroup for a team
     * @param team the Team
     * @param course the course
     */
    private CourseGroup findPossibileCourseGroupFor(Team team, Course course, AllocationPlan allocPlan) throws CourseGroupDoesntExistException {
        for(CourseGroup courseGroup : course.getCourseGroups()){
            if(!allocPlan.isCourseGroupFull(courseGroup) && !allocPlan.conflict(team, courseGroup)){
                return courseGroup;
            }
        }
        return null;
    }

    /*
    TODO Man könnte die Laufzeit hier wahrscheinlich noch ein wenig verkürzen
    */
    private void calculateConflictsBetweenCourses(List<Course> courses, AllocationPlan allocPlan) {
        for(Course course : courses){
            List<Course> possibileConflicts = new ArrayList<>();

            for(Course otherCourse : courses){
                if(!course.equals(otherCourse) && allocPlan.conflict(course, otherCourse)){
                    possibileConflicts.add(otherCourse);
                }
            }
            possibileCourseConflicts.put(course,possibileConflicts);
        }
    }

}
