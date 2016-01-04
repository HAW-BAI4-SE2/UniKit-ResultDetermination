package net.unikit.result_determination.models.implementations.algorithms;

import net.unikit.database.interfaces.entities.*;
import net.unikit.result_determination.models.exceptions.CourseGroupDoesntExistException;
import net.unikit.result_determination.models.exceptions.CourseGroupFullException;
import net.unikit.result_determination.models.exceptions.NotEnoughCourseGroupsException;
import net.unikit.result_determination.models.implementations.*;
import net.unikit.result_determination.models.implementations.dummys.DummyTeamImpl;
import net.unikit.result_determination.utils.AlgorithmUtils;
import net.unikit.result_determination.utils.NewTeamSizeComparator;

import java.util.*;

/**
 * Created by Jones on 03.01.2016.
 */
public class GreedyAllocationPlanAlgorithm {

    private Map<Course, Integer> teamPreservations;
    private Map<Student,List<ExtendedCourseGroup>> studentsCourseGroups;

    public GreedyAllocationPlanAlgorithm() {
        teamPreservations = new HashMap<>();
        this.studentsCourseGroups = new HashMap<>();
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
    public NewAllocationPlan calculateAllocationPlan(List<ExtendedCourse> courses) throws NotEnoughCourseGroupsException, CourseGroupDoesntExistException, CourseGroupFullException {
//        // Check, if there are enough available slots to assign each Student to a courseGroup
        long start = System.currentTimeMillis();
        int notMatchable=0;
        int globalTeamPreservation=0;


        AlgorithmUtils.checkAvailableSlotsStatus(courses);
//        calculateConflictsBetweenCourses(courses, allocPlan);

        for (ExtendedCourse course : courses) {

            List<TeamRegistration> unAssignedTeamRegistrations = new ArrayList<>();
            List<ExtendedTeam> teams = course.getTeams();
            Collections.shuffle(teams);
            Collections.sort(teams, new NewTeamSizeComparator()); // Innerhalb der Größe wird es trotzdem ein anderes geben

            for (ExtendedTeam t : teams) {

                ExtendedCourseGroup possibleCourseGroup = findPossibileCourseGroupFor(t, course); // throws CourseGroupDoesntExistException

                if (possibleCourseGroup != null) {
                    registerTeam(t,possibleCourseGroup);
                    course.countTeamPreservation();
                } else {
//                    System.out.println("@@@@ FEHLER: " + t + " -> " + course.getName());
                    for (TeamRegistration s : t.getTeamRegistrations()) {
//                        ExtendedTeam singleStudentTeam = new ExtendedTeam(new DummyTeamImpl(course.getCourse()),course);
//                        singleStudentTeam.addTeamRegistration(s);
//                        course.addUnassignedTeam(singleStudentTeam);
                        unAssignedTeamRegistrations.add(s);
                    }
                }
            }

            System.out.println(course.getName() + " Team Preservation: " +course.getTeamPreservation());
            globalTeamPreservation += course.getNumberOfTeamPreservations();

//            System.out.println("**** Check Unfinished Teams ***");
//            for (ExtendedTeam t : course.getUnAssignedTeams()) {
//
//                ExtendedCourseGroup possibleCourseGroup = findPossibileCourseGroupFor(t, course); // throws CourseGroupDoesntExistException
//
//                if (possibleCourseGroup != null) {
//                    registerTeam(t,possibleCourseGroup);
//                } else {
//                    course.addNotMatchableTeam(t);
//                }
//            }
            for(TeamRegistration teamRegistration : unAssignedTeamRegistrations){
                ExtendedCourseGroup possibleCourseGroup = findPossibileCourseGroupFor(teamRegistration, course);
                if(possibleCourseGroup != null){
                    possibleCourseGroup.addTeamRegistration(teamRegistration);
                } else{
                    course.addNotMatchableTeamRegistration(teamRegistration);
                }
            }
//            notMatchable+=course.getNotMatchable().size();
            notMatchable+=course.getNotMatchableTeamRegistrations().size();
        }
        long end = System.currentTimeMillis();
        long algorithmRuntime = end-start;

        System.out.println("Global Team Preservation: " +globalTeamPreservation);

        NewAllocationPlan allocationPlan = new NewAllocationPlan(courses);
        allocationPlan.setAlgorithmRuntime(algorithmRuntime);
        allocationPlan.setNumberOfTeamPreservation(globalTeamPreservation);
        allocationPlan.setNumberOfNotMatchableRegistrations(notMatchable);
        return allocationPlan;
    }

    private void registerTeam(ExtendedTeam t, ExtendedCourseGroup possibleCourseGroup){
        for(TeamRegistration teamRegistration : t.getTeamRegistrations()){
            try {
                possibleCourseGroup.addTeamRegistration(teamRegistration);

                // Save the courseGroups that are assigned to a student
                List<ExtendedCourseGroup> studentsCourseGroups;

                // Wenn der Student noch keiner einzigen Gruppe zugewiesen wurde (dann hat er auch noch keinen Map Eintrag)
                if (this.studentsCourseGroups.get(teamRegistration.getStudent()) == null) {
                    studentsCourseGroups = new ArrayList<>();
                } else {
                    studentsCourseGroups = this.studentsCourseGroups.get(teamRegistration.getStudent());
                }
                studentsCourseGroups.add(possibleCourseGroup);
                this.studentsCourseGroups.put(teamRegistration.getStudent(), studentsCourseGroups); // update


            } catch (CourseGroupFullException e) {
                e.printStackTrace();
            }
        }
    }



    public Map<Course, Integer> getTeamPreservations() {
        return teamPreservations;
    }

    /**
     * Tries to find a possibile CourseGroup for a team
     *
     * @param team   the Team
     * @param course the course
     */
    private ExtendedCourseGroup findPossibileCourseGroupFor(ExtendedTeam team, ExtendedCourse course) throws CourseGroupDoesntExistException {
        for (ExtendedCourseGroup courseGroup : course.getCourseGroups()) {
            int emptyPlaces = courseGroup.getNumberOfEmptyPlaces();
            if (emptyPlaces >= team.getSize() && !team.hasConflictWith(courseGroup,studentsCourseGroups)) {
                return courseGroup;
            }
        }
        return null;
    }

    /**
     * Tries to find a possibile CourseGroup for a team
     *
     * @param teamRegistration the TeamRegistration
     * @param course the course
     */
    private ExtendedCourseGroup findPossibileCourseGroupFor(TeamRegistration teamRegistration, ExtendedCourse course) throws CourseGroupDoesntExistException {
        for (ExtendedCourseGroup courseGroup : course.getCourseGroups()) {
            int emptyPlaces = courseGroup.getNumberOfEmptyPlaces();
            if (emptyPlaces >= 1 && !conflict(teamRegistration, courseGroup, studentsCourseGroups)) {
                return courseGroup;
            }
        }
        return null;
    }

    private boolean conflict(TeamRegistration teamRegistration, ExtendedCourseGroup courseGroup, Map<Student, List<ExtendedCourseGroup>> studentsCourseGroups) {
        List<ExtendedCourseGroup> studentCourseGroups = studentsCourseGroups.get(teamRegistration.getStudent());
        if (studentCourseGroups != null) {
            // hier wird für alle Praktikumsgruppen, in denen ein Student Mitglied ist überprüft,
            // ob es mit der zu überprüfenden Gruppe zu einem Konflikt käme
            for (ExtendedCourseGroup group : studentCourseGroups) {
                if (group.hashConflictWith(courseGroup)) {
                    return true;
                }
            }
        }
        return false;
    }

//    private boolean conflict(TeamRegistration teamRegistration, )

}
