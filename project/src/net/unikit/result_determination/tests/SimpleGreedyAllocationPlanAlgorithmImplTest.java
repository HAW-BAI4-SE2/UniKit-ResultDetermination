package net.unikit.result_determination.tests;

import net.unikit.database.interfaces.entities.*;
import net.unikit.result_determination.models.exceptions.CourseGroupDoesntExistException;
import net.unikit.result_determination.models.exceptions.NotEnoughCourseGroupsException;
import net.unikit.result_determination.models.implementations.AlgorithmSettingsImpl;
import net.unikit.result_determination.models.implementations.algorithms.SimpleGreedyAllocationPlanAlgorithmImpl;
import net.unikit.result_determination.models.implementations.dummys.DummyDataGenerator;
import net.unikit.result_determination.models.interfaces.AlgorithmSettings;
import net.unikit.result_determination.models.interfaces.AllocationPlan;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertTrue;

/**
 * Created by abq307 on 18.11.2015.
 */
public class SimpleGreedyAllocationPlanAlgorithmImplTest {


    DummyDataGenerator dummyDataGenerator;

    @Before
    public void setUp() throws Exception {
        dummyDataGenerator = new DummyDataGenerator(18,6);
        dummyDataGenerator.buildTeamAndRegister();
    }

    @Test
    public void testCalculateAllocationPlan() throws CourseGroupDoesntExistException, NotEnoughCourseGroupsException {
        AlgorithmSettings settings = new AlgorithmSettingsImpl();

        /*  All courses for which the allocations shall be created  */
        //List<Course> courses = dbmanager.getCourseManager().getAllEntities(); --> wird später verwendet!!! Erstmal nur mit Dummys arbeiten!
        List<Course> courses = dummyDataGenerator.getDummyCourses();

        /* The Algorithm that does the work */
        SimpleGreedyAllocationPlanAlgorithmImpl allocPlanAlgorithm = new SimpleGreedyAllocationPlanAlgorithmImpl(settings);
        AllocationPlan allocPlan = null;
        // *************************** start *************************************

        allocPlan = allocPlanAlgorithm.calculateAllocationPlan(courses);




        for(Course course : courses){
            System.out.println("\n*** "+course.getName()+" ***");
            System.out.println("NOT MATCHABLE : "+allocPlanAlgorithm.getNotMatchable().get(course));
            System.out.println("NOT MATCHABLE TEAMS : "+allocPlanAlgorithm.getNotMatchableTeams().get(course));
            for(CourseGroup courseGroup : course.getCourseGroups()){
                System.out.println(courseGroup + " Teilnehmer: " + allocPlan.getCourseRegistrations(courseGroup) + allocPlan.getTeamRegistrations(courseGroup));
            }
        }
        /*
         * Wir müssen für jeden AllocationPlan folgendes garantieren:
         *
         * 1. Jeder Student, der sich angemeldet hat für eine Veranstaltung muss auch einer Praktikumsgruppe zugeordnet sein.
         * 2. Ein Student darf nicht in mehreren Praktikumsgruppen derselben Veranstaltung sein
         * 3. Es gibt keine Überschneidungen für Gruppen, die im selben Semester stattfinden
         *
         */
        for(Course course : courses){
            for(CourseRegistration singleReg : course.getSingleRegistrations()){
                assertTrue(isPartOfOnlyOneCourseGroup(course, singleReg, allocPlan));
            }
            for(Team team : course.getTeams()){
                for(TeamRegistration teamRegistration : team.getTeamRegistrations()){
                    assertTrue(isPartOfOnlyOneCourseGroup(course, teamRegistration, allocPlan));
                }
            }
        }


//        assertTrue(!allocPlanAlgorithm.getNotMatchable().isEmpty());
    }

    private boolean isPartOfOnlyOneCourseGroup(Course course, TeamRegistration singleReg, AllocationPlan allocPlan) throws CourseGroupDoesntExistException {
        int numberOfGroups=0;
        for(CourseGroup g : course.getCourseGroups()){
            if(allocPlan.getTeamRegistrations(g).contains(singleReg)){
                numberOfGroups++;
            }
        }
        if(numberOfGroups == 1){
            return true;
        }
        return false;
    }

    private boolean isPartOfOnlyOneCourseGroup(Course course, CourseRegistration singleReg, AllocationPlan allocPlan) throws CourseGroupDoesntExistException {
        int numberOfGroups=0;
        for(CourseGroup g : course.getCourseGroups()){
            if(allocPlan.getCourseRegistrations(g).contains(singleReg)){
                numberOfGroups++;
            }
        }
        if(numberOfGroups == 1){
            return true;
        }
        return false;
    }

    /*
     * Prüft nur, ob ein Student irgendeiner Gruppe zugeordnet wurde.
     * Ob er in mehreren Gruppen derselben Veranstaltung ist wird hier nicht geprüft.
     */
    private boolean isPartOfCourseGroup(Course c,CourseRegistration cReg, AllocationPlan allocPlan) throws CourseGroupDoesntExistException {
        boolean isPartOfOneCourseGroup=false;
        List<CourseGroup> groups = c.getCourseGroups();
        for(CourseGroup group : groups){
            // Wenn die CourseRegistration noch nicht gefunden wurde und die CourseRegistration in der aktuellen Gruppe registriert wurde
            if(allocPlan.getCourseRegistrations(group).contains(cReg)){
                isPartOfOneCourseGroup=true;
                break;
            }
        }


        return isPartOfOneCourseGroup;
    }
}