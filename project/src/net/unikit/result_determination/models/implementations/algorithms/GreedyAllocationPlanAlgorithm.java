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
     *          destroy Team and mark the TeamRegistrations as unfinished
     *    3. For every unfinished TeamRegistration
     *       Group g = findGroup(teamReg,c); //
     *       if (g not null)
     *          g.addRegistration(teamReg);
     *       else
     *          mark teamReg as unMatchable
     *
     */
    public NewAllocationPlan calculateAllocationPlan(List<ExtendedCourse> courses) throws NotEnoughCourseGroupsException, CourseGroupDoesntExistException, CourseGroupFullException {
//        // Check, if there are enough available slots to assign each Student to a courseGroup
        long start = System.currentTimeMillis();
        int notMatchable=0;
        int globalTeamPreservation=0;


        AlgorithmUtils.checkAvailableSlotsStatus(courses);

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
                    for (TeamRegistration s : t.getTeamRegistrations()) {
                        unAssignedTeamRegistrations.add(s);
                    }
                }
            }

            System.out.println(course.getName() + " Team Preservation: " +course.getTeamPreservation());
            globalTeamPreservation += course.getNumberOfTeamPreservations();

            for(TeamRegistration teamRegistration : unAssignedTeamRegistrations){
                ExtendedCourseGroup possibleCourseGroup = findPossibileCourseGroupFor(teamRegistration, course);
                if(possibleCourseGroup != null){
                    registerTeamRegistration(teamRegistration,possibleCourseGroup);
                } else{
                    course.addNotMatchableTeamRegistration(teamRegistration);
                }
            }
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

    private void registerTeamRegistration(TeamRegistration teamRegistration, ExtendedCourseGroup possibleCourseGroup) {
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

    public void fixNotMatchables(List<ExtendedCourse> courses,NewAllocationPlan allocationPlan) throws CourseGroupDoesntExistException, CourseGroupFullException {
        while(allocationPlan.getNumberOfNotMatchableRegistrations() != 0){
            int notMatchable=0;
            for(ExtendedCourse course : courses){
                for(int j=0; j< course.getNotMatchableTeamRegistrations().size();j++){
                    TeamRegistration teamRegistration = course.getNotMatchableTeamRegistrations().get(j);
                    for(int i=0; i<course.getCourseGroups().size();i++){
                        ExtendedCourseGroup group = course.getCourseGroups().get(i);
                        int size = group.getMaxGroupSize()+1;
                        group.setMaxGroupSize(size);
                    }

//                for(ExtendedCourseGroup group : course.getCourseGroups()){
//                    int size = group.getMaxGroupSize()+1;
//                    group.setMaxGroupSize(size);
//                }
                    ExtendedCourseGroup possibleCourseGroup = findPossibileCourseGroupFor(teamRegistration, course);
                    if(possibleCourseGroup != null){
                        registerTeamRegistration(teamRegistration,possibleCourseGroup);
                        course.removeNotMatchableTeamRegistration(teamRegistration);
                    }
                    notMatchable+=course.getNotMatchableTeamRegistrations().size();
                }
            }
            allocationPlan.setNumberOfNotMatchableRegistrations(notMatchable);
        }
        System.out.println("*** New AllocationPlan ***");
        System.out.println(allocationPlan);

    }

    public void fixNotMatchables(ExtendedCourse course, NewAllocationPlan allocationPlan) throws CourseGroupFullException, CourseGroupDoesntExistException {
        int notMatchableOld = course.getNotMatchableTeamRegistrations().size();
        int notMatchable = 0;
        while(notMatchable < notMatchableOld){
            for(int j=0; j< course.getNotMatchableTeamRegistrations().size();j++){
                TeamRegistration teamRegistration = course.getNotMatchableTeamRegistrations().get(j);
                for(int i=0; i<course.getCourseGroups().size();i++){
                    ExtendedCourseGroup group = course.getCourseGroups().get(i);
                    int size = group.getMaxGroupSize()+1;
                    group.setMaxGroupSize(size);
                }

//                for(ExtendedCourseGroup group : course.getCourseGroups()){
//                    int size = group.getMaxGroupSize()+1;
//                    group.setMaxGroupSize(size);
//                }
                ExtendedCourseGroup possibleCourseGroup = findPossibileCourseGroupFor(teamRegistration, course);
                if(possibleCourseGroup != null){
                    registerTeamRegistration(teamRegistration,possibleCourseGroup);
                    course.removeNotMatchableTeamRegistration(teamRegistration);
                }
                notMatchable++;
            }
        }
        allocationPlan.setNumberOfNotMatchableRegistrations((allocationPlan.getNumberOfNotMatchableRegistrations()-notMatchableOld));
        System.out.println("*** New AllocationPlan ***!");
        System.out.println(allocationPlan);
    }



    public Map<Course, Integer> getTeamPreservations() {
        return teamPreservations;
    }


    public Map<Student, List<ExtendedCourseGroup>> getStudentsCourseGroups() {
        return studentsCourseGroups;
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
     * Tries to find a possibile CourseGroup for a TeamRegistration
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

    /*
     * checks if there is a conflict between a Teamregistration and a CourseGroup
     */
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
}
