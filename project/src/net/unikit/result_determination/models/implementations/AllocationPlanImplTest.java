package net.unikit.result_determination.models.implementations;

import junit.framework.TestCase;
import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.CourseGroup;
import net.unikit.result_determination.models.implementations.dummys.DummyCourseRegistrationImpl;
import net.unikit.result_determination.models.implementations.dummys.DummyDataGenerator;
import net.unikit.result_determination.models.implementations.dummys.DummyStudentImpl;
import net.unikit.result_determination.models.implementations.dummys.DummyStudentNumberImpl;
import net.unikit.result_determination.models.interfaces.AllocationPlan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jones on 01.01.2016.
 */
public class AllocationPlanImplTest extends TestCase {

    DummyDataGenerator dummyDataGenerator;
    int numberOfStudents;

    public void setUp() throws Exception {
        numberOfStudents = 48;
        dummyDataGenerator = new DummyDataGenerator(numberOfStudents,numberOfStudents/3);
        dummyDataGenerator.buildTeamAndRegister();
    }

    public void testIsCourseGroupFull() throws Exception {
        List<Course> courses = dummyDataGenerator.getDummyCourses();
        AllocationPlan allocationPlan = new AllocationPlanImpl(courses);

        CourseGroup group = courses.get(0).getCourseGroups().get(0);
        assertFalse(allocationPlan.isCourseGroupFull(group));

        for(int i=0; i<group.getMaxGroupSize();i++){
            allocationPlan.registerStudent(new DummyCourseRegistrationImpl(new DummyStudentImpl(new DummyStudentNumberImpl("" + i)), courses.get(0)), group);
        }

        assertTrue(allocationPlan.isCourseGroupFull(group));
    }

}