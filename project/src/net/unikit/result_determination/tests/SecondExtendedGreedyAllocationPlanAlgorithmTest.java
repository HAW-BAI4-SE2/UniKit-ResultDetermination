package net.unikit.result_determination.tests;

import junit.framework.TestCase;
import net.unikit.database.interfaces.entities.*;
import net.unikit.result_determination.models.exceptions.CourseGroupDoesntExistException;
import net.unikit.result_determination.models.exceptions.NotEnoughCourseGroupsException;
import net.unikit.result_determination.models.implementations.AlgorithmSettingsImpl;
import net.unikit.result_determination.models.implementations.algorithms.SecondExtendedGreedyAllocationPlanAlgorithm;
import net.unikit.result_determination.models.implementations.dummys.DummyDataGenerator;
import net.unikit.result_determination.models.interfaces.AlgorithmSettings;
import net.unikit.result_determination.models.interfaces.AllocationPlan;
import net.unikit.result_determination.models.interfaces.AllocationPlanAlgorithm;
import org.junit.Test;

import java.util.List;

/**
 * Created by Jones on 13.12.2015.
 */
public class SecondExtendedGreedyAllocationPlanAlgorithmTest extends TestCase {
    DummyDataGenerator dummyDataGenerator;
    int numberOfStudents;

    public void setUp() throws Exception {
        numberOfStudents = 48;
        dummyDataGenerator = new DummyDataGenerator(numberOfStudents,numberOfStudents/3);
        dummyDataGenerator.buildTeamAndRegister();
    }

    @Test
    public void testCalculateAllocationPlan() throws CourseGroupDoesntExistException, NotEnoughCourseGroupsException {
        AlgorithmSettings settings = new AlgorithmSettingsImpl();

        /*  All courses for which the allocations shall be created  */
        //List<Course> courses = dbmanager.getCourseManager().getAllEntities(); --> wird später verwendet!!! Erstmal nur mit Dummys arbeiten!
        List<Course> courses = dummyDataGenerator.getDummyCourses();

        /* The Algorithm that does the work */
        AllocationPlanAlgorithm allocPlanAlgorithm = new SecondExtendedGreedyAllocationPlanAlgorithm();
        AllocationPlan allocPlan = null;
        // *************************** start *************************************

//        allocplan = allocplanalgorithm.calculateallocationplan(courses);
//        allocPlan.exportAsCSV(new File("C:"+File.separator+"Users"+ File.separator + "abq307.INFORMATIK"+File.separator+"Desktop"+File.separator+"AllocPlanTest.txt"));

        for(int i=0; i<100; i++){
            System.out.println("****************************** START ALGORITHM ******************************");
            int notMatchable=0;
            int numberOfRegistrations=0;
            int numberOfTeamRegistrations = 0;

            allocPlanAlgorithm = new SecondExtendedGreedyAllocationPlanAlgorithm();
            allocPlan = allocPlanAlgorithm.calculateAllocationPlan(courses);

            System.out.println("\n***** Kurs-Ergebnisse *****");
            for(Course course : courses){
                System.out.println("\nKurs: "+course.getName());
                System.out.println("NotMatchable: "+allocPlanAlgorithm.getNotMatchableTeams().get(course));

                for(Team t: course.getTeams()){
                    numberOfRegistrations+=t.getTeamRegistrations().size();
                }

                numberOfTeamRegistrations += course.getTeams().size();
                notMatchable += allocPlanAlgorithm.getNotMatchableTeams().get(course).size();
                for(CourseGroup courseGroup : course.getCourseGroups()){
                    System.out.println(courseGroup + " Teilnehmer: " + (allocPlan.getCourseRegistrations(courseGroup).size() + allocPlan.getTeamRegistrations(courseGroup).size()));
                }
            }

            System.out.println("\n***** Globales Ergebnis *****");
            System.out.println("Studenten: " + numberOfStudents +
                    "\nVeranstaltungen: " + courses.size() +
                    "\nAnmeldungen: " + numberOfRegistrations +
                    "\nErhaltenen Teams: " + allocPlanAlgorithm.getNumberOfTeamPreservations() + " von " + numberOfTeamRegistrations +
                    "\nNotMatchable global: " + notMatchable + " von " + numberOfRegistrations);
            System.out.println("****************************** END ALGORITHM ******************************");
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

    private boolean isPartOfOnlyOneCourseGroup(Course course, TeamRegistration teamReg, AllocationPlan allocPlan) throws CourseGroupDoesntExistException {
        int numberOfGroups=0;
        for(CourseGroup g : course.getCourseGroups()){
            if(allocPlan.getTeamRegistrations(g).contains(teamReg)){
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