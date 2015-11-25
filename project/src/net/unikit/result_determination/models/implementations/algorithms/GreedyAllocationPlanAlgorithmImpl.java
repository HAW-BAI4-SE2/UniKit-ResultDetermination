package net.unikit.result_determination.models.implementations.algorithms;

import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.CourseGroup;
import net.unikit.database.interfaces.entities.CourseRegistration;
import net.unikit.database.interfaces.entities.Student;
import net.unikit.result_determination.models.exceptions.CourseGroupDoesntExistException;
import net.unikit.result_determination.models.exceptions.CourseGroupFullException;
import net.unikit.result_determination.models.exceptions.NotEnoughCourseGroupsException;
import net.unikit.result_determination.models.implementations.AlgorithmUtils;
import net.unikit.result_determination.models.implementations.AllocationPlanImpl;
import net.unikit.result_determination.models.interfaces.AlgorithmSettings;
import net.unikit.result_determination.models.interfaces.AllocationPlan;
import net.unikit.result_determination.models.interfaces.AllocationPlanAlgorithm;

import java.util.*;

/**
 * Created by Jones on 15.11.2015.
 */
public class GreedyAllocationPlanAlgorithmImpl implements AllocationPlanAlgorithm {

    AlgorithmSettings settings;
    Map<Student,List<CourseGroup>> studentsCourseGroups;
    List<CourseRegistration> notMatchable;

    /**
     * Initializes the object.
     * @param settings The AlgorithmSettings
     */
    public GreedyAllocationPlanAlgorithmImpl(AlgorithmSettings settings){
        this.settings = settings;
        notMatchable = new ArrayList<>();
        studentsCourseGroups = new HashMap<>();
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
    public AllocationPlan calculateAllocationPlan(List<Course> courses) throws NotEnoughCourseGroupsException {
        AllocationPlan allocPlan = new AllocationPlanImpl(courses);
        /*
         * Iterates over every Course
         */
        for(Course c : courses){

            // Check, if there are enough available slots to assign each Student to a courseGroup
            AlgorithmUtils.checkAvailableSlots(c);
            List<CourseRegistration> singleRegistrations = c.getSingleRegistrations();
            Collections.shuffle(singleRegistrations); // Keinen Studenten bevorzugen
            /*
             * At this Moment in the development process the algorithm just focuses on singleRegistrations and ignores the teams.
             */
            for(CourseRegistration singleRegistration :singleRegistrations){

                CourseGroup possibleCourseGroup = null;
                try {
                    possibleCourseGroup = AlgorithmUtils.findPossibileCourseGroupFor(singleRegistration, c, studentsCourseGroups, allocPlan);
                } catch (CourseGroupDoesntExistException e) {
                    //TODO !!!!!!!
                }

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
     * A List of all CourseRegistrations where no possibile Assignment existed without
     * accepting some conflicts between other CourseGroups.
     * @return all not matchable CourseRegistrations
     */
    public List<CourseRegistration> getNotMatchable(){
        return notMatchable;
    }
}
