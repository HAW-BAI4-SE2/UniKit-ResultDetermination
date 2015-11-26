package net.unikit.result_determination.models.implementations.algorithms;

import net.unikit.database.interfaces.entities.*;
import net.unikit.result_determination.models.exceptions.NotEnoughCourseGroupsException;
import net.unikit.result_determination.models.interfaces.AllocationPlanAlgorithm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by abq307 on 26.11.2015.
 */
abstract class AbstractAllocationPlanAlgorithm implements AllocationPlanAlgorithm {

    protected Map<Student,List<CourseGroup>> studentsCourseGroups;

    public AbstractAllocationPlanAlgorithm(){
        this.studentsCourseGroups = new HashMap<>();
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

    /*
    * Pr�ft, ob eine Einzelanmeldung mit einer Gruppe im Konflikt steht.
    * Ein konflikt trifft auf, falls der Student in der Einzelanmeldung bereits f�r eine Praktikumsgruppe registriert ist,
    * die an einem gleichen Termin stattfindet, wie die zu �berpr�fende Gruppe.
    */
    public boolean conflict(CourseRegistration singleRegistration, CourseGroup courseGroup) {
        // alle praktikumsgruppen die einem Studenten derzeit zugewiesen wurden
        List<CourseGroup> studentCourseGroups = studentsCourseGroups.get(singleRegistration.getStudent());

        if(studentCourseGroups != null){
            // hier wird f�r alle Praktikumsgruppen, in denen ein Student Mitglied ist �berpr�ft,
            // ob es mit der zu �berpr�fenden Gruppe zu einem Konflikt k�me
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
                if(c1App.equals(c2App)){ // TODO M�glicherweise ein eigenes conflict f�r zwei Appointments
                    return true;
                }
            }
        }
        return false;
    }


}
