package net.unikit.result_determination.tests;

import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.CourseGroup;
import net.unikit.database.interfaces.entities.CourseRegistration;
import net.unikit.result_determination.models.exceptions.NotEnoughCourseGroupsException;
import net.unikit.result_determination.models.implementations.AlgorithmSettingsImpl;
import net.unikit.result_determination.models.implementations.algorithms.GreedyAllocationPlanAlgorithmImpl;
import net.unikit.result_determination.models.implementations.dummys.DummyCourseGroupImpl;
import net.unikit.result_determination.models.implementations.dummys.DummyDataGenerator;
import net.unikit.result_determination.models.interfaces.AlgorithmSettings;
import net.unikit.result_determination.models.interfaces.AllocationPlan;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.*;

/**
 * Created by abq307 on 18.11.2015.
 */
public class GreedyAllocationPlanAlgorithmImplTest {


    DummyDataGenerator dummyDataGenerator;

    @Before
    public void setUp() throws Exception {
        dummyDataGenerator = new DummyDataGenerator(48,16);
    }

    @Test
    public void testCalculateAllocationPlan(){
        AlgorithmSettings settings = new AlgorithmSettingsImpl();

        /*  All courses for which the allocations shall be created  */
        //List<Course> courses = dbmanager.getCourseManager().getAllEntities(); --> wird später verwendet!!! Erstmal nur mit Dummys arbeiten!
        List<Course> courses = (List<Course>)((List<?>) dummyDataGenerator.getDummyCourses());

        /* The Algorithm that does the work */
        GreedyAllocationPlanAlgorithmImpl allocPlanAlgorithm = new GreedyAllocationPlanAlgorithmImpl(settings);

        // *************************** start *************************************
        try {
            AllocationPlan allocPlan = allocPlanAlgorithm.calculateAllocationPlan(courses);
        } catch (NotEnoughCourseGroupsException e) {
            System.err.println("NotEnoughCourseGroups");
        }


        System.out.println("Liste aller Studenten, die nicht verarbeitet werden konnten: "+allocPlanAlgorithm.getNotMatchable());

        for(Course course : courses){
            for(CourseGroup courseGroup : course.getCourseGroups()){
                System.out.println(courseGroup + " Teilnehmerzahl: " + ((DummyCourseGroupImpl)courseGroup).getGroupMembers().size());
            }
        }
        /*
         * Wir müssen für jeden AllocationPlan folgendes garantieren:
         *
         * 1. Jeder Student, der sich angemeldet hat für eine Veranstaltung muss auch einer Praktikumsgruppe zugeordnet sein.
         * 2. Ein Student darf nicht in mehreren Praktikumsgruppen der selben Veranstaltung sein
         * 3. Es gibt keine Überschneidungen für Gruppen, die im selben Semester stattfinden
         *
         */
        for(Course course : courses){
            for(CourseRegistration singleReg : course.getSingleRegistrations()){
                assertTrue(isPartOfCourseGroup(course, singleReg));
            }
        }
//        assertTrue(!allocPlanAlgorithm.getNotMatchable().isEmpty());
    }

    /*
     * Prüft nur, ob ein Student irgendeiner Gruppe zugeordnet wurde.
     * Ob er in mehreren Gruppen derselben Veranstaltung ist wird hier nicht geprüft.
     */
    private boolean isPartOfCourseGroup(Course c,CourseRegistration cReg){
        boolean isPartOfOneCourseGroup=false;
        List<CourseGroup> groups = c.getCourseGroups();
        for(CourseGroup group : groups){
            // Wenn die CourseRegistration noch nicht gefunden wurde und die CourseRegistration in der aktuellen Gruppe registriert wurde
            if(((DummyCourseGroupImpl)group).getGroupMembers().contains(cReg)){
                isPartOfOneCourseGroup=true;
                break;
            }
        }


        return isPartOfOneCourseGroup;
    }
}