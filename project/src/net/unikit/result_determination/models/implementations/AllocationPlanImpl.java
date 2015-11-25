package net.unikit.result_determination.models.implementations;

import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.CourseGroup;
import net.unikit.database.interfaces.entities.CourseRegistration;
import net.unikit.result_determination.models.exceptions.CourseGroupDoesntExistException;
import net.unikit.result_determination.models.exceptions.CourseGroupFullException;
import net.unikit.result_determination.models.interfaces.AllocationPlan;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jones on 15.11.2015.
 */
public class AllocationPlanImpl implements AllocationPlan {

    List<Course> courses;
    Map<CourseGroup,List<CourseRegistration>> courseGroupRegistrations;

    public AllocationPlanImpl(List<Course> courses){
        this.courses = courses;
        this.courseGroupRegistrations = new HashMap<>();
        initCourseGroupRegistrations();
    }

    private void initCourseGroupRegistrations() {
        for(Course c : this.courses){
            for(CourseGroup g : c.getCourseGroups()){
                courseGroupRegistrations.put(g,new ArrayList<>());
            }
        }
    }

    @Override
    public void exportAsCSV(File file) {
    // TODO Jana :D
    }

    @Override
    public void addCourseRegistration(CourseGroup group, CourseRegistration registration) throws CourseGroupFullException, CourseGroupDoesntExistException {
        if(courseGroupRegistrations.containsKey(group)){
            if(!isCourseGroupFull(group)){
                List<CourseRegistration> courseRegistrations = courseGroupRegistrations.get(group);
                courseRegistrations.add(registration);
            }
            else {
                throw new CourseGroupFullException();
            }
        }
        else throw new CourseGroupDoesntExistException();
    }

    @Override
    public boolean isCourseGroupFull(CourseGroup group) throws CourseGroupDoesntExistException{
        if(courseGroupRegistrations.containsKey(group)){
            int maxSize = group.getMaxGroupSize();
            if(courseGroupRegistrations.get(group).size()>= maxSize){ // Größer darf eigentlich nicht passieren TODO wurde schnell schnell gemacht und muss vllt nochmal überprüft werden
                return true;
            }
            else return false;
        }
        else throw new CourseGroupDoesntExistException();
    }

    @Override
    public List<CourseRegistration> getGroupMembers(CourseGroup group) throws CourseGroupDoesntExistException {
        if(courseGroupRegistrations.containsKey(group)){
            return courseGroupRegistrations.get(group);
        }
        else throw new CourseGroupDoesntExistException();
    }
}
