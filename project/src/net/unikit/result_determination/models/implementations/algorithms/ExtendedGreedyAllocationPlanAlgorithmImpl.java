package net.unikit.result_determination.models.implementations.algorithms;

import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.CourseGroup;
import net.unikit.database.interfaces.entities.CourseRegistration;
import net.unikit.database.interfaces.entities.Team;
import net.unikit.result_determination.models.exceptions.CourseGroupDoesntExistException;
import net.unikit.result_determination.models.exceptions.NotEnoughCourseGroupsException;
import net.unikit.result_determination.models.implementations.AllocationPlanImpl;
import net.unikit.result_determination.models.interfaces.AlgorithmSettings;
import net.unikit.result_determination.models.interfaces.AllocationPlan;

import java.util.*;

/**
 * Created by abq307 on 26.11.2015.
 */
public class ExtendedGreedyAllocationPlanAlgorithmImpl extends AbstractAllocationPlanAlgorithm {

    private AlgorithmSettings settings;
    private List<CourseRegistration> notMatchable;
    private Map<Course, Map<CourseRegistration,Integer>> dangerValues;
    private Map<Course, List<Course>> possibileCourseConflicts; // TODO auch Map<Course, Map<Course,Integer>> courseConflicts w�re denkbar, um die genaue Anzahl der Konflikte zu kennen


    /**
     * Initializes the object.
     * @param settings The AlgorithmSettings
     */
    public ExtendedGreedyAllocationPlanAlgorithmImpl(AlgorithmSettings settings){
        this.settings = settings;
        possibileCourseConflicts = new HashMap<>();
    }

    /*
        TODO Man k�nnte die Laufzeit hier wahrscheinlich noch ein wenig verk�rzen
     */
    private void calculateConflictsBetweenCourses(List<Course> courses) {
        for(Course course : courses){
            List<Course> possibileConflicts = new ArrayList<>();

            for(Course otherCourse : courses){
                if(!course.equals(otherCourse) && conflict(course,otherCourse)){
                    possibileConflicts.add(otherCourse);
                }
            }
            possibileCourseConflicts.put(course,possibileConflicts);
        }
    }

    @Override
    /**
     * TODO the algorithm ignores TeamRegistrations at this moment
     * TODO Zurzeit ist der Algorithmus auch noch nicht Random -> es wird noch nichts geshufflet (k�nnte man ganz am Anfang einmal machen)
     *
     */
    public AllocationPlan calculateAllocationPlan(List<Course> courses) throws NotEnoughCourseGroupsException, CourseGroupDoesntExistException {
        AllocationPlan allocPlan = new AllocationPlanImpl(courses);
        notMatchable = new ArrayList<>();
        // Check, if there are enough available slots to assign each Student to a courseGroup
        checkAvailableSlots(courses);

        calculateConflictsBetweenCourses(courses);

        /*
         * The Algorithm:
         *
         * 1. For every Course c
         *    2. For every single Registration s
         *       Group g = findGroup(s,c); //
         *       if (g has no conflicts with other groups of s and g isn't full yet)
         *          g.addRegistration(s);
         *       else if (g has no conflicts with other groups of s and g is full)
         *          try to find a student in all alternative conflictfree groups who is able to switch the group with s
         *
         *
         */
        for(Course course : courses){
            dangerValues = new HashMap<>();

            calculateSingleRegistrations(courses, course, allocPlan);


        }
        return allocPlan;
    }

    private void calculateSingleRegistrations(List<Course> courses, Course course, AllocationPlan allocPlan) throws CourseGroupDoesntExistException {
        List<CourseRegistration> singleRegistrations = course.getSingleRegistrations();

        // TODO M�glicherweise je nach Danger-Wert des Studenten sortieren
        Collections.shuffle(singleRegistrations); // Keinen Studenten bevorzugen

        for(CourseRegistration singleRegistration :singleRegistrations){
//                calculateDANGERValue(singleRegistration, course); // TODO wird noch nicht genutzt

            CourseGroup courseGroup = findCourseGroupFor(singleRegistration, course, allocPlan);
            boolean courseGroupChanged = false;

//                System.out.println(singleRegistration + " wird versucht in Gruppe: " + courseGroup + " einzuf�gen");

            if(courseGroup != null){
                // Wenn eine konfliktfreie Gruppe gefunden wurde, k�nnen wir diese dem Studenten zuweisen // TODO die conflict �berpr�fung wird so doppelt ausgef�hrt.. vielleicht besser �ber einen Boolean in einer Map realisieren
                if(!conflict(singleRegistration,courseGroup)){
                    registerStudent(singleRegistration, courseGroup, allocPlan);
                    courseGroupChanged=true;
                }
                else{
                    // Liefert eine leere Liste oder alle Gruppen in denen der Termin g�ltig w�re (k�nnen allerdings schon voll sein)
                    List<CourseGroup> possibileCourseGroups = findAlternativeCourseGroups(singleRegistration, course); // TODO k�nnte man sich auch vorher bereits speichern w�hrend findCourseGroupFor

                    for(CourseGroup possibileGroup : possibileCourseGroups){
                        CourseRegistration changeableStudent = findChangeableStudent(possibileGroup, courseGroup, allocPlan);
                        if(changeableStudent != null){
                            removeStudent(changeableStudent,possibileGroup,allocPlan);
                            registerStudent(changeableStudent, courseGroup, allocPlan);
                            registerStudent(singleRegistration,possibileGroup,allocPlan);
                            courseGroupChanged=true;
                            break;
                        }
                    }
                }
            }

            if(!courseGroupChanged){
                notMatchable.add(singleRegistration); // TODO m�sste eigentlich HashMap sein Course->Registration
            }
        }
    }

    private CourseRegistration findChangeableStudent(CourseGroup from, CourseGroup to, AllocationPlan allocPlan) throws CourseGroupDoesntExistException {
        CourseRegistration changeAbleStudent = null;
        List<CourseRegistration> courseRegistrations = allocPlan.getCourseRegistrations(from);
        for(CourseRegistration courseRegistration : courseRegistrations) {
            if (!conflict(courseRegistration, to)) {
                changeAbleStudent = courseRegistration;
            }
        }
        return changeAbleStudent;
    }



    /*
        Berechnet den DANGER Wert eines Studenten im Zusammenspiel einer Veranstaltung.
        Dabei wird geschaut, ob der Student f�r andere Veranstaltungen angemeldet ist, die potentiell mit dieser Veranstaltung im Konflikt stehen.
        TODO Auch hier werden derzeit Teams noch ignoriert! Muss nachgef�gt werden.
     */
    private void calculateDANGERValue(CourseRegistration singleRegistration, Course course) {
        List<CourseRegistration> courseRegistrations = singleRegistration.getStudent().getCourseRegistrations();
        Map<CourseRegistration,Integer> studentsConflicts = new HashMap<>();
        int numberOfConflicts=0;
        for(CourseRegistration courseRegistration : courseRegistrations){
            if(possibileCourseConflicts.get(course).contains(courseRegistration.getCourse())){
                numberOfConflicts++;
            }
        }
        studentsConflicts.put(singleRegistration, numberOfConflicts);
        dangerValues.put(course,studentsConflicts);
    }

    /**
     * Tries to find a CourseGroup for one CourseRegistration
     * This means: a group that isn't full and doesn't lead to a conflict with other registered courseGroups for the Student
     * If no such group can be found, an alternative group which isn't full will be returned
     * @param singleRegistration the Registration for which an available CourseGroup shall be found
     * @param course the Course for which the CourseGroup shall be found
     * @param allocPlan the allocation plan, that has the knowledge about the assigned students
     */
    private CourseGroup findCourseGroupFor(CourseRegistration singleRegistration, Course course, AllocationPlan allocPlan) throws CourseGroupDoesntExistException {
        CourseGroup group = null;
        for(CourseGroup courseGroup : course.getCourseGroups()){
            boolean notFull = !allocPlan.isCourseGroupFull(courseGroup);
            boolean noConflict = !conflict(singleRegistration, courseGroup);
            if(notFull){
                group = courseGroup;
            }
            if(notFull && noConflict){
                break; // aufh�ren, sobald eine konfliktfreie Gruppe gefunden wurde.
            }
        }
        return group;
    }

    /**
     * Tries to find every potential CourseGroup.
     * A potential CourseGroup is a CourseGroup that doesn't conflict with other CourseGroups of the Student
     * Warning! The CourseGroups might be full.
     * @param singleRegistration
     * @param course
     * @return
     */
    private List<CourseGroup> findAlternativeCourseGroups(CourseRegistration singleRegistration, Course course){
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

    @Override
    public List<Team> getNotMatchableTeams() {
        return null;
    }

    /*
     * Calculates the Danger-Value of alle registrations
     * TODO Not Finished!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    private void calculateDangerValues(List<CourseRegistration> singleRegistrations) {
        /*
         * F�r alle Registrierungen die im aktuellen Kurs gemacht wurden
         */
        for(CourseRegistration courseRegistration : singleRegistrations){
            int numberOfPotentialConflictingCourses = 0;
            for(CourseRegistration creg1 : courseRegistration.getStudent().getCourseRegistrations()){ // TODO Was passiert mit CourseRegistrations, die abgeschlossen sind? An dieser Stelle interessiert mich nur, f�r welche Kurse sich der Student aktuell angemeldet hat
                for(CourseRegistration creg2 : courseRegistration.getStudent().getCourseRegistrations()){
                    if(conflict(creg1.getCourse(),creg2.getCourse())){
                        numberOfPotentialConflictingCourses++;
                    }
                }
            }
        }
    }

}
