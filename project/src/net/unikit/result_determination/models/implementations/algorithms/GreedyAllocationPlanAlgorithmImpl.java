package net.unikit.result_determination.models.implementations.algorithms;

import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.CourseGroup;
import net.unikit.database.interfaces.entities.CourseRegistration;
import net.unikit.result_determination.models.exceptions.CourseGroupDoesntExistException;
import net.unikit.result_determination.models.exceptions.CourseGroupFullException;
import net.unikit.result_determination.models.exceptions.NotEnoughCourseGroupsException;
import net.unikit.result_determination.models.implementations.AllocationPlanImpl;
import net.unikit.result_determination.models.interfaces.AlgorithmSettings;
import net.unikit.result_determination.models.interfaces.AllocationPlan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jones on 15.11.2015.
 * An Implementation for creating allocation plans inspired by Greedy-Algorithms.
 */
public class GreedyAllocationPlanAlgorithmImpl extends AbstractAllocationPlanAlgorithm {

    AlgorithmSettings settings;
    List<CourseRegistration> notMatchable;

    /**
     * Initializes the object.
     * @param settings The AlgorithmSettings
     */
    public GreedyAllocationPlanAlgorithmImpl(AlgorithmSettings settings){
        this.settings = settings;
        notMatchable = new ArrayList<>();
    }

    @Override
    /**
     * TODO the algorithm ignores TeamRegistrations at this moment
     * TODO Zurzeit ist der Algorithmus auch noch nicht Random -> es wird noch nichts geshufflet (könnte man ganz am Anfang einmal machen)
     * Algorithm Idea:
     *
     * For every Course
     *      For Every Registration
     *          possibileCourseGroup = findPossibileCourseGroup(the Registration, the Course);
     *
     *          either there was a possibile Group
     *                 add the Registration to the possibile Group
     *          else mark the Registration as notMatchable
     */
    public AllocationPlan calculateAllocationPlan(List<Course> courses) throws NotEnoughCourseGroupsException, CourseGroupDoesntExistException {
        AllocationPlan allocPlan = new AllocationPlanImpl(courses);
        /*
         * Iterates over every Course
         */
        for(Course course : courses){

            // Check, if there are enough available slots to assign each Student to a courseGroup
            checkAvailableSlots(course);
            List<CourseRegistration> singleRegistrations = course.getSingleRegistrations();
            Collections.shuffle(singleRegistrations); // Keinen Studenten bevorzugen
            /*
             * At this Moment in the development process the algorithm just focuses on singleRegistrations and ignores the teams.
             */
            for(CourseRegistration singleRegistration :singleRegistrations){

                CourseGroup possibleCourseGroup = findPossibileCourseGroupFor(singleRegistration, course, allocPlan); // throws CourseGroupDoesntExistException
//                System.out.println("Mögliche CourseGroup:" + possibleCourseGroup);

                // a possibile courseGroup was found
                if(possibleCourseGroup != null){
                    try {
                        allocPlan.addCourseRegistration(possibleCourseGroup, singleRegistration);
                    } catch (CourseGroupFullException e) {
                        e.printStackTrace();
                    } catch (CourseGroupDoesntExistException e) {
                        e.printStackTrace();
                    }
                    List<CourseGroup> studentsCourseGroups;

                    // Wenn der Student noch keiner einzigen Gruppe zugewiesen wurde (dann hat er auch noch keinen Map Eintrag)
                    if(this.studentsCourseGroups.get(singleRegistration.getStudent()) == null){
                        studentsCourseGroups = new ArrayList<>();
                    }
                    else{
                        studentsCourseGroups = this.studentsCourseGroups.get(singleRegistration.getStudent());
                    }

                    studentsCourseGroups.add(possibleCourseGroup);
                    System.out.println(singleRegistration.getStudent()+" Gruppen:"+studentsCourseGroups);
                    this.studentsCourseGroups.put(singleRegistration.getStudent(), studentsCourseGroups); // update
                }
                else{
//                    hier wäre auch ein tryToKickOtherRegistration oder sowas denkbar um einen Platz frei zu bekommen
                    notMatchable.add(singleRegistration);
                }
            }
        }
        return allocPlan;
    }

    /**
     * Tries to find a possibile CourseGroup for one CourseRegistration
     * @param singleRegistration the Registration for which an available CourseGroup shall be found
     * @param course the Course for which the CourseGroup shall be found
     */
    public CourseGroup findPossibileCourseGroupFor(CourseRegistration singleRegistration, Course course, AllocationPlan allocPlan) throws CourseGroupDoesntExistException {
        for(CourseGroup courseGroup : course.getCourseGroups()){
            if(!allocPlan.isCourseGroupFull(courseGroup) && !conflict(singleRegistration,courseGroup)){
                return courseGroup;
            }
        }
        System.out.println("Für " + singleRegistration.getStudent() + " und Kurs " + course.getName() + " konnte keine freie Gruppe mehr gefunden werden.");
        return null;
    }

    /**
     * A List of all CourseRegistrations where no possibile Assignment existed without
     * accepting some conflicts between other CourseGroups.
     * @return all not matchable CourseRegistrations
     */
    public List<CourseRegistration> getNotMatchable(){
        return notMatchable;
    }
}
