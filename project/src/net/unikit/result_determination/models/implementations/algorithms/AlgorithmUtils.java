package net.unikit.result_determination.models.implementations.algorithms;

import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.CourseGroup;
import net.unikit.database.interfaces.entities.Team;
import net.unikit.result_determination.models.exceptions.NotEnoughCourseGroupsException;

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
}
