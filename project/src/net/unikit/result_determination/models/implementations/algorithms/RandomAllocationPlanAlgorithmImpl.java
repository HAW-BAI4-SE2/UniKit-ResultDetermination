package net.unikit.result_determination.models.implementations.algorithms;

import net.unikit.database.interfaces.entities.*;
import net.unikit.result_determination.models.exceptions.CourseGroupFullException;
import net.unikit.result_determination.models.exceptions.NotEnoughCourseGroupsException;
import net.unikit.result_determination.models.implementations.AllocationPlanImpl;
import net.unikit.result_determination.models.implementations.dummys.DummyCourseGroupImpl;
import net.unikit.result_determination.models.interfaces.AlgorithmSettings;
import net.unikit.result_determination.models.interfaces.AllocationPlan;
import net.unikit.result_determination.models.interfaces.AllocationPlanAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jones on 15.11.2015.
 */
public class RandomAllocationPlanAlgorithmImpl implements AllocationPlanAlgorithm {

    AlgorithmSettings settings;
    Map<Student,List<CourseGroup>> studentsCourseGroups;
    List<CourseRegistration> notMatchable;

    /**
     * Initializes the object.
     * @param settings The AlgorithmSettings
     */
    public RandomAllocationPlanAlgorithmImpl(AlgorithmSettings settings){
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
        AllocationPlan allocPlan = null;
        /*
         * Iterates over every Course
         */
        for(Course c : courses){

            // Check, if there are enough available slots to assign each Student to a courseGroup
            checkAvailableSlots(c);

            /*
             * At this Moment in the development process the algorithm just focuses on singleRegistrations and ignores the teams.
             */
            for(CourseRegistration singleRegistration : c.getSingleRegistrations()){

                CourseGroup possibleCourseGroup = findPossibileCourseGroupFor(singleRegistration,c);

//                System.out.println("Mögliche CourseGroup:" + possibleCourseGroup);

                // a possibile courseGroup was found
                if(possibleCourseGroup != null){
                    try {
                        ((DummyCourseGroupImpl)possibleCourseGroup).addCourseRegistration(singleRegistration);
                    } catch (CourseGroupFullException e) {
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
                    System.out.println("Student:"+singleRegistration.getStudent()+" Gruppen:"+studentsCourseGroups);
                    this.studentsCourseGroups.put(singleRegistration.getStudent(), studentsCourseGroups); // update
                }
                else{
//                    hier wäre auch ein tryToKickOtherRegistration oder sowas denkbar um einen Platz frei zu bekommen
                    notMatchable.add(singleRegistration);
                }
            }
        }

        allocPlan = new AllocationPlanImpl(courses);
        return allocPlan;
    }

    /**
     * Checks, if there are enough available slots to assign each Student to a courseGroup
     * @param c the Course for which shall be checked if there are enough groups and slots to assign each student
     */
    public void checkAvailableSlots(Course c) throws NotEnoughCourseGroupsException {
        int availableSlots = 0;
        int numberRegistrations = c.getSingleRegistrations().size()+c.getTeams().size();
        for(CourseGroup group : c.getCourseGroups()){
            availableSlots+=group.getMaxGroupSize();
        }

        if(availableSlots < numberRegistrations){
            throw new NotEnoughCourseGroupsException();
        }
    }

    /**
     * Tries to find a possibile CourseGroup for one CourseRegistration
     * @param singleRegistration the Registration for which an available CourseGroup shall be found
     * @param course the Course for which the CourseGroup shall be found
     */
    public CourseGroup findPossibileCourseGroupFor(CourseRegistration singleRegistration, Course course) {
        for(CourseGroup courseGroup : course.getCourseGroups()){
            if((!((DummyCourseGroupImpl)courseGroup).isFull()) && !conflict(singleRegistration,courseGroup)){
                return courseGroup;
            }
        }
        return null;
    }

    /*
     * Prüft, ob eine Einzelanmeldung mit einer Gruppe im Konflikt steht.
     * Ein konflikt trifft auf, falls der Student in der Einzelanmeldung bereits für eine Praktikumsgruppe registriert ist,
     * die an einem gleichen Termin stattfindet, wie die zu überprüfende Gruppe.
     */
    private boolean conflict(CourseRegistration singleRegistration, CourseGroup courseGroup) {
        // alle praktikumsgruppen die einem Studenten derzeit zugewiesen wurden
        List<CourseGroup> studentCourseGroups = studentsCourseGroups.get(singleRegistration.getStudent());

        if(studentCourseGroups != null){
            // hier wird für alle Praktikumsgruppen, in denen ein Student Mitglied ist überprüft,
            // ob es mit der zu überprüfenden Gruppe zu einem Konflikt käme
            for(CourseGroup group : studentCourseGroups){
                if(conflict(group,courseGroup)){
                    return true;
                }
            }
        }
        return false; // kein konflikt ist aufgetreten
    }

    /**
     * Checks if there is a conflict between two CourseGroups. A conflict exists if there is at least one similar
     * appointment that each CourseGroup has.
     * @param c1 the first CourseGroup
     * @param c2 the second CourseGroup
     * @returns true, if there is a conflict between those two CourseGroups
     */
    public boolean conflict(CourseGroup c1, CourseGroup c2){
        for(Appointment c1App : c1.getAppointments()){
            for(Appointment c2App : c2.getAppointments()){
                if(c1App.equals(c2App)){ // TODO Möglicherweise ein eigenes conflict für zwei Appointments
                    return true;
                }
            }
        }
        return false;
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
