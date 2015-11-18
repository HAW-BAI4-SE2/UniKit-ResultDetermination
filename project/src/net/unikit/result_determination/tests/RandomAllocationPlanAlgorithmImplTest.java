package net.unikit.result_determination.tests;

import net.unikit.database.interfaces.entities.Course;
import net.unikit.result_determination.models.implementations.AlgorithmSettingsImpl;
import net.unikit.result_determination.models.implementations.algorithms.RandomAllocationPlanAlgorithmImpl;
import net.unikit.result_determination.models.implementations.dummys.DummyDataGenerator;
import net.unikit.result_determination.models.interfaces.AlgorithmSettings;
import net.unikit.result_determination.models.interfaces.AllocationPlan;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by abq307 on 18.11.2015.
 */
public class RandomAllocationPlanAlgorithmImplTest {


    DummyDataGenerator dummyDataGenerator;

    @Before
    public void setUp() throws Exception {
        dummyDataGenerator = new DummyDataGenerator(48);
    }

    @Test
    public void testCalculateAllocationPlan() throws Exception {
        AlgorithmSettings settings = new AlgorithmSettingsImpl();

        /*  All courses for which the allocations shall be created  */
        //List<Course> courses = dbmanager.getCourseManager().getAllEntities(); --> wird später verwendet!!! Erstmal nur mit Dummys arbeiten!
        List<Course> courses = dummyDataGenerator.getDummyCourses();

        /* The Algorithm that does the work */
        RandomAllocationPlanAlgorithmImpl allocPlanAlgorithm = new RandomAllocationPlanAlgorithmImpl(settings);

        // *************************** start *************************************
        AllocationPlan allocPlan = allocPlanAlgorithm.calculateAllocationPlan(courses);

        /*
         * Wir müssen für jeden AllocationPlan folgendes garantieren:
         *
         * 1. Jeder Student, der sich angemeldet hat für eine Veranstaltung muss auch einer Praktikumsgruppe zugeordnet sein.
         * 2. Ein Student darf nicht in mehreren Praktikumsgruppen der selben Veranstaltung sein
         *
         */
//        for(Course course :)
//        assertTrue(!allocPlanAlgorithm.getNotMatchable().isEmpty());
    }
}