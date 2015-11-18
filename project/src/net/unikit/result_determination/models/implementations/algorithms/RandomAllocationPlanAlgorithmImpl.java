package net.unikit.result_determination.models.implementations.algorithms;

import net.unikit.database.interfaces.entities.*;
import net.unikit.result_determination.models.implementations.AllocationPlanImpl;
import net.unikit.result_determination.models.implementations.dummys.DummyCourseGroupImpl;
import net.unikit.result_determination.models.interfaces.AlgorithmSettings;
import net.unikit.result_determination.models.interfaces.AllocationPlan;
import net.unikit.result_determination.models.interfaces.AllocationPlanAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jones on 15.11.2015.
 */
public class RandomAllocationPlanAlgorithmImpl implements AllocationPlanAlgorithm {

    AlgorithmSettings settings;
    Map<Student,List<CourseGroup>> studentListMap;
    List<CourseRegistration> notMatchable;

    public RandomAllocationPlanAlgorithmImpl(AlgorithmSettings settings){
        this.settings = settings;
        notMatchable = new ArrayList<>();
    }

    @Override
    /**
     * TODO the algorithm ignores TeamRegistrations at this moment
     */
    public AllocationPlan calculateAllocationPlan(List<Course> courses) {
        AllocationPlan allocPlan = null;
        /*
         * Iterates over every Course
         */
        for(Course c : courses){

            /*
             * At this Moment the algorithm just focuses on singleRegistrations and ignores the teams.
             */
            for(CourseRegistration singleRegistration : c.getSingleRegistrations()){

                CourseGroup possibleCourseGroup = findPossibileCourseGroupFor(singleRegistration,c);

                // es wurde eine mögliche Gruppe gefunden
                if(possibleCourseGroup != null){
                    ((DummyCourseGroupImpl)possibleCourseGroup).addStudent(singleRegistration.getStudent());
                }
                else{
//                    tryToChange
                    notMatchable.add(singleRegistration);
                }
            }
        }

        allocPlan = new AllocationPlanImpl(courses);
        return allocPlan;
    }

    private CourseGroup findPossibileCourseGroupFor(CourseRegistration singleRegistration, Course c) {
        for(CourseGroup courseGroup : c.getCourseGroups()){
            if(!conflict(singleRegistration,courseGroup)){
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
        List<CourseGroup> studentCourseGroups = studentListMap.get(singleRegistration.getStudent());

        // hier wird für alle Praktikumsgruppen, in denen ein Student Mitglied ist überprüft,
        // ob es mit der zu überprüfenden Gruppe zu einem Konflikt käme
        for(CourseGroup group : studentCourseGroups){
            if(conflict(group,courseGroup)){
                return false;
            }
        }
        return true; // kein konflikt ist aufgetreten
    }

    /*
     * Prüft, ob es einen Konflikt zwischen zwei Praktikumsgruppen gibt.
     * Ein konflikt tritt auf, wenn beide Praktikumstermine einen Überschneidungstermin haben
     */
    private boolean conflict(CourseGroup c1, CourseGroup c2){
        for(Appointment c1App : c1.getAppointments()){
            for(Appointment c2App : c2.getAppointments()){
                if(c1App.equals(c2App)){
                    return false;
                }
            }
        }
        return true;
    }

    public List<CourseRegistration> getNotMatchable(){
        return notMatchable;
    }
}
