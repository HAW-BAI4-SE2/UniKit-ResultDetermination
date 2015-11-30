package net.unikit.result_determination.models.interfaces;

import net.unikit.database.interfaces.entities.CourseGroup;
import net.unikit.database.interfaces.entities.CourseRegistration;
import net.unikit.result_determination.models.exceptions.CourseGroupDoesntExistException;
import net.unikit.result_determination.models.exceptions.CourseGroupFullException;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by abq307 on 16.11.2015.
 */
public interface AllocationPlan {

    public void exportAsCSV(File file);

    public void addCourseRegistration(CourseGroup group, CourseRegistration registration) throws CourseGroupFullException, CourseGroupDoesntExistException;

    public boolean isCourseGroupFull(CourseGroup g) throws CourseGroupDoesntExistException;

    public List<CourseRegistration> getGroupMembers(CourseGroup group) throws CourseGroupDoesntExistException;

    void removeCourseGroupRegistration(CourseRegistration changeableStudent, CourseGroup courseGroup) throws CourseGroupDoesntExistException,NoSuchElementException;
}
