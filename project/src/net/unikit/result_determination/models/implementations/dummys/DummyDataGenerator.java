package net.unikit.result_determination.models.implementations.dummys;

import net.unikit.result_determination.models.interfaces.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abq307 on 17.11.2015.
 */
public class DummyDataGenerator {

    List<Course> courses;

    public DummyDataGenerator(){
        generateTestData();
    }

    private void generateTestData(){
        courses = new ArrayList<>();
    }


}
