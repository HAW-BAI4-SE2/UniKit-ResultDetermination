package net.unikit.result_determination.tests;

import junit.framework.TestCase;
import net.unikit.database.interfaces.entities.*;
import net.unikit.result_determination.models.exceptions.CourseGroupDoesntExistException;
import net.unikit.result_determination.models.exceptions.CourseGroupFullException;
import net.unikit.result_determination.models.exceptions.NotEnoughCourseGroupsException;
import net.unikit.result_determination.models.implementations.*;
import net.unikit.result_determination.models.implementations.algorithms.GreedyAllocationPlanAlgorithm;
import net.unikit.result_determination.models.implementations.dummys.DummyDataGenerator;
import net.unikit.result_determination.models.interfaces.AlgorithmSettings;
import net.unikit.result_determination.models.interfaces.AllocationPlan;
import net.unikit.result_determination.models.interfaces.AllocationPlanAlgorithm;
import net.unikit.result_determination.utils.AlgorithmUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jones on 03.01.2016.
 */
public class GreedyAllocationPlanAlgorithmTest extends TestCase {
    DummyDataGenerator dummyDataGenerator;
    int numberOfStudents;

    public void setUp() throws Exception {
        numberOfStudents = 48;
        dummyDataGenerator = new DummyDataGenerator(numberOfStudents,numberOfStudents/3);
        dummyDataGenerator.buildTeamAndRegister();
    }

    @Test
    public void testCalculateAllocationPlan() throws CourseGroupDoesntExistException, NotEnoughCourseGroupsException, CourseGroupFullException {
        // *************************** start *************************************

        for(int i=0; i<100; i++){
            System.out.println("****************************** START ALGORITHM ******************************");
            int notMatchable=0;
            int numberOfRegistrations=0;
            int numberOfTeamRegistrations = 0;

            List<Course> c = dummyDataGenerator.getDummyCourses();
            List<ExtendedCourse> courses = AlgorithmUtils.wrapCourses(c);
            GreedyAllocationPlanAlgorithm allocPlanAlgorithm = new GreedyAllocationPlanAlgorithm();
            NewAllocationPlan allocPlan = allocPlanAlgorithm.calculateAllocationPlan(courses);

            System.out.println(allocPlan);

        }

        /*
         * Wir müssen für jeden AllocationPlan folgendes garantieren:
         *
         * Nicht möglich, die folgenden Punkte alle zu garantieren.
         *
         * 1. Jeder Student, der sich angemeldet hat für eine Veranstaltung muss auch einer Praktikumsgruppe zugeordnet sein.
         * 2. Ein Student darf nicht in mehreren Praktikumsgruppen derselben Veranstaltung sein
         * 3. Es gibt keine Überschneidungen für Gruppen, die im selben Semester stattfinden
         *
         */
//        for(Course course : courses){
//            for(CourseRegistration singleReg : course.getSingleRegistrations()){
//                assertTrue(isPartOfOnlyOneCourseGroup(course, singleReg, allocPlan));
//            }
//            for(Team team : course.getTeams()){
//                for(TeamRegistration teamRegistration : team.getTeamRegistrations()){
//                    assertTrue(isPartOfOnlyOneCourseGroup(course, teamRegistration, allocPlan));
//                }
//            }
//        }


//        assertTrue(!allocPlanAlgorithm.getNotMatchable().isEmpty());
    }

    private boolean isPartOfOnlyOneCourseGroup(ExtendedCourse course, TeamRegistration teamReg) throws CourseGroupDoesntExistException {
        int numberOfGroups=0;
        for(ExtendedCourseGroup g : course.getCourseGroups()){
            if(g.getTeamRegistrations().contains(teamReg)){
                numberOfGroups++;
            }
        }
        if(numberOfGroups == 1){
            return true;
        }
        return false;
    }
}