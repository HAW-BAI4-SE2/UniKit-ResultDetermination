package net.unikit.result_determination.models.implementations;

import net.unikit.database.interfaces.entities.*;
import net.unikit.database.interfaces.entities.Student;
import net.unikit.result_determination.models.exceptions.NotEnoughCourseGroupsException;
import net.unikit.result_determination.models.implementations.dummys.DummyCourseGroupImpl;

import java.util.List;
import java.util.Map;

/**
 * Created by Jones on 20.11.2015.
 */
public final class AlgorithmUtils {

    /**
     * Checks, if there are enough available slots to assign each Student to a courseGroup
     * @param c the Course for which shall be checked if there are enough groups and slots to assign each student
     */
    public static void checkAvailableSlots(Course c) throws NotEnoughCourseGroupsException {
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
    public static CourseGroup findPossibileCourseGroupFor(CourseRegistration singleRegistration, Course course, Map<Student,List<CourseGroup>> studentsCourseGroups) {
        for(CourseGroup courseGroup : course.getCourseGroups()){
            if((!((DummyCourseGroupImpl)courseGroup).isFull()) && !conflict(singleRegistration,courseGroup, studentsCourseGroups)){
                return courseGroup;
            }
        }
        System.out.println("Für " + singleRegistration.getStudent() + " und Kurs " + course.getName() + " konnte keine freie Gruppe mehr gefunden werden.");
        return null;
    }

    /*
 * Prüft, ob eine Einzelanmeldung mit einer Gruppe im Konflikt steht.
 * Ein konflikt trifft auf, falls der Student in der Einzelanmeldung bereits für eine Praktikumsgruppe registriert ist,
 * die an einem gleichen Termin stattfindet, wie die zu überprüfende Gruppe.
 */
    private static boolean conflict(CourseRegistration singleRegistration, CourseGroup courseGroup,Map<Student,List<CourseGroup>> studentsCourseGroups) {
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
    public static boolean conflict(CourseGroup c1, CourseGroup c2){
        for(Appointment c1App : c1.getAppointments()){
            for(Appointment c2App : c2.getAppointments()){
                if(c1App.equals(c2App)){ // TODO Möglicherweise ein eigenes conflict für zwei Appointments
                    return true;
                }
            }
        }
        return false;
    }
}
