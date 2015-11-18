package net.unikit.result_determination.models.implementations.dummys;


import net.unikit.database.interfaces.entities.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abq307 on 17.11.2015.
 */
public class DummyDataGenerator {

    List<Course> courses;

    public DummyDataGenerator(){

    }

    public void generateTestData(){
        courses = new ArrayList<>();
    }

    public List<Course> getDummyCourses(){
        return courses;
    }

}
