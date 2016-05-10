package net.unikit.result_determination.utils;

import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.CourseGroup;
import net.unikit.database.interfaces.entities.Team;
import net.unikit.result_determination.models.exceptions.NotEnoughCourseGroupsException;
import net.unikit.result_determination.models.implementations.ExtendedCourse;
import net.unikit.result_determination.models.implementations.ExtendedCourseGroup;
import net.unikit.result_determination.models.implementations.ExtendedTeam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jones on 30.12.2015.
 */
public class AlgorithmUtils {

    /**
     * Checks, if there are enough available slots to assign each Student to a courseGroup
     * @param courses the courses that shall be checked
     */
    public static void checkAvailableSlots(List<Course> courses) throws NotEnoughCourseGroupsException {
        System.out.println("\n*** Check available slots ***");
        for(Course c : courses){
            int availableSlots = 0;
            int numberRegistrations = c.getSingleRegistrations().size();
            for(Team t : c.getTeams()){
                numberRegistrations+= t.getTeamRegistrations().size();
            }

            for(CourseGroup group : c.getCourseGroups()){
                availableSlots+=group.getMaxGroupSize();
            }

            System.out.println(c.getName()+ " Registrations: " + numberRegistrations + " Plätze: " + availableSlots);
            if(availableSlots < numberRegistrations){
                throw new NotEnoughCourseGroupsException();
            }
        }
        System.out.println("");
    }

    public static void checkAvailableSlotsStatus(List<ExtendedCourse> courses) throws NotEnoughCourseGroupsException {
        System.out.println("\n*** Check available slots ***");
        for(ExtendedCourse c : courses){
            int availableSlots = 0;
            int numberRegistrations = c.getSingleRegistrations().size();
            for(ExtendedTeam t : c.getTeams()){
                numberRegistrations+= t.getSize();
            }

            for(ExtendedCourseGroup group : c.getCourseGroups()){
                availableSlots+=group.getMaxGroupSize();
            }

            System.out.println(c.getName()+ " Registrations: " + numberRegistrations + " Plätze: " + availableSlots);
            if(availableSlots < numberRegistrations){
                throw new NotEnoughCourseGroupsException();
            }
        }
        System.out.println("");
    }

    public static List<ExtendedCourse> wrapCourses(List<Course> c) {
        List<ExtendedCourse> courses = new ArrayList<>();

        for(Course course : c){
            courses.add(new ExtendedCourse(course));
        }
        return courses;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
