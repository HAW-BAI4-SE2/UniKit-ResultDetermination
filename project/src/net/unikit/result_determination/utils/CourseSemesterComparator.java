package net.unikit.result_determination.utils;

import net.unikit.database.interfaces.entities.Course;

import java.util.Comparator;

/**
 * Created by Jones on 10.12.2015.
 */
public class CourseSemesterComparator implements Comparator<Course> {

    @Override
    public int compare(Course o1, Course o2) {
        return o1.getSemester().compareTo(o2.getSemester());
    }
}
