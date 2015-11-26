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

import java.util.*;

/**
 * Created by abq307 on 26.11.2015.
 */
public class ExtendedGreedyAllocationPlanAlgorithmImpl extends AbstractAllocationPlanAlgorithm {

    AlgorithmSettings settings;
    List<CourseRegistration> notMatchable;
    Map<CourseRegistration,Integer> dangerValues;


    /**
     * Initializes the object.
     * @param settings The AlgorithmSettings
     */
    public ExtendedGreedyAllocationPlanAlgorithmImpl(AlgorithmSettings settings){
        this.settings = settings;
        notMatchable = new ArrayList<>();
        dangerValues = new HashMap<>();
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
        // Check, if there are enough available slots to assign each Student to a courseGroup
        checkAvailableSlots(courses);
        /*
         * Iterates over every Course
         */
        for(Course course : courses){


            List<CourseRegistration> singleRegistrations = course.getSingleRegistrations();
            calculateDangerValues(singleRegistrations);

            // TODO Möglicherweise je nach Danger-Wert des Studenten sortieren
            Collections.shuffle(singleRegistrations); // Keinen Studenten bevorzugen
            /*
             * At this Moment in the development process the algorithm just focuses on singleRegistrations and ignores the teams.
             */
            for(CourseRegistration singleRegistration :singleRegistrations){


                // Irgendwie möchte ich bei findPossibileCourseGroupFor in jedem Fall etwas zurück bekommen, außer sie ist ungültig.
                // ich möchte aber nur eine Gruppe zurückbekommen, wenn es keine andere Möglichkeit mehr gibt. Also die letzte gültige Gruppe
                // a) die Gruppe ist gültig und noch nicht voll
                // b) die Gruppe ist gültig und voll






                CourseGroup possibleCourseGroup = findPossibileCourseGroupFor(singleRegistration, course, allocPlan); // throws CourseGroupDoesntExistException
//                System.out.println("Mögliche CourseGroup:" + possibleCourseGroup);

                // a possibile courseGroup was found

                List<CourseGroup> possibileCourseGroups = findPossibileCourseGroups(singleRegistration, course);





                if(!possibileCourseGroups.isEmpty()){
                    for(CourseGroup group : possibileCourseGroups){
//                            CourseRegistration changePartner = findChangePartner()
                        if(!allocPlan.isCourseGroupFull(group)){
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
                    }
                }
                else{
                    notMatchable.add(singleRegistration);
                }
            }
        }
        return allocPlan;
    }

    /**
     * Tries to find a possibile CourseGroup for one CourseRegistration
     * A possibile Group is a group that isn't full and doesn't lead to a conflict with other courseGroups
     * @param singleRegistration the Registration for which an available CourseGroup shall be found
     * @param course the Course for which the CourseGroup shall be found
     * @param allocPlan the allocation plan, that has the knowledge about the assigned students
     */
    private CourseGroup findPossibileCourseGroupFor(CourseRegistration singleRegistration, Course course, AllocationPlan allocPlan) throws CourseGroupDoesntExistException {
        for(CourseGroup courseGroup : course.getCourseGroups()){
            if(!allocPlan.isCourseGroupFull(courseGroup) && !conflict(singleRegistration,courseGroup)){
                return courseGroup;
            }
        }
        System.out.println("Für " + singleRegistration.getStudent() + " und Kurs " + course.getName() + " konnte keine freie Gruppe mehr gefunden werden.");
        return null;
    }

    /**
     * Tries to find every potential CourseGroup.
     * A potential CourseGroup is a CourseGroup that doesn't conflict with other CourseGroups of the Student
     * Warning! The CourseGroups might be full.
     * @param singleRegistration
     * @param course
     * @return
     */
    private List<CourseGroup> findPossibileCourseGroups(CourseRegistration singleRegistration, Course course){
        List<CourseGroup> groups = new ArrayList<>();
        for(CourseGroup g : course.getCourseGroups()){
            if(!conflict(singleRegistration,g)){
                groups.add(g);
            }
        }
        return groups;
    }

    /**
     * A List of all CourseRegistrations where no possibile Assignment existed without
     * accepting some conflicts between other CourseGroups.
     * @return all not matchable CourseRegistrations
     */
    public List<CourseRegistration> getNotMatchable(){
        return notMatchable;
    }

    /*
     * Calculates the Danger-Value of alle registrations
     * TODO Not Finished!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    private void calculateDangerValues(List<CourseRegistration> singleRegistrations) {
        /*
         * Für alle Registrierungen die im aktuellen Kurs gemacht wurden
         */
        for(CourseRegistration courseRegistration : singleRegistrations){
            int numberOfPotentialConflictingCourses = 0;
            for(CourseRegistration creg1 : courseRegistration.getStudent().getCourseRegistrations()){ // TODO Was passiert mit CourseRegistrations, die abgeschlossen sind? An dieser Stelle interessiert mich nur, für welche Kurse sich der Student aktuell angemeldet hat
                for(CourseRegistration creg2 : courseRegistration.getStudent().getCourseRegistrations()){
                    if(conflict(creg1.getCourse(),creg2.getCourse())){
                        numberOfPotentialConflictingCourses++;
                    }
                }
            }
        }
    }

}
