package net.unikit.result_determination.models.implementations.algorithms;

import net.unikit.database.interfaces.entities.*;
import net.unikit.result_determination.models.exceptions.CourseGroupDoesntExistException;
import net.unikit.result_determination.models.exceptions.CourseGroupFullException;
import net.unikit.result_determination.models.exceptions.NotEnoughCourseGroupsException;
import net.unikit.result_determination.models.interfaces.AllocationPlan;
import net.unikit.result_determination.models.interfaces.AllocationPlanAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by abq307 on 26.11.2015.
 */
abstract class AbstractAllocationPlanAlgorithm implements AllocationPlanAlgorithm {

    protected Map<Student,List<CourseGroup>> studentsCourseGroups;
    protected Map<CourseRegistration,CourseGroup> conflicts;

    public AbstractAllocationPlanAlgorithm(){
        this.studentsCourseGroups = new HashMap<>();
        this.conflicts = new HashMap<>();
    }

    /**
     * Checks, if there are enough available slots to assign each Student to a courseGroup
     * @param courses the courses that shall be checked
     */
    public void checkAvailableSlots(List<Course> courses) throws NotEnoughCourseGroupsException {
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

    /**
     * Checks if two Courses have potential conflicts between CourseGroups and their Appointments
     * @param c1 the first course
     * @param c2 the second course
     * @return True if a conflict appeared
     */
    public boolean conflict(Course c1, Course c2){
        for(CourseGroup c1Group : c1.getCourseGroups()){
            for(CourseGroup c2Group : c2.getCourseGroups()){
                if(conflict(c1Group, c2Group)){
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * Prüft für alle Teammitglieder, ob es zu einem Konflikt mit dieser Gruppe käme.
     */
    public boolean conflict(Team team, CourseGroup courseGroup){
        List<TeamRegistration> teamRegistrations = team.getTeamRegistrations();
        for(TeamRegistration teamReg : teamRegistrations){
            List<CourseGroup> studentCourseGroups = studentsCourseGroups.get(teamReg.getStudent());

            if(studentCourseGroups != null){
                // hier wird für alle Praktikumsgruppen, in denen ein Student Mitglied ist überprüft,
                // ob es mit der zu überprüfenden Gruppe zu einem Konflikt käme
                for(CourseGroup group : studentCourseGroups){
                    if(conflict(group,courseGroup)){
                        return true;
                    }
                }
            }
        }
        return false; // kein konflikt ist aufgetreten
    }

    /*
    * Prüft, ob eine Einzelanmeldung mit einer Gruppe im Konflikt steht.
    * Ein konflikt trifft auf, falls der Student in der Einzelanmeldung bereits für eine Praktikumsgruppe registriert ist,
    * die an einem gleichen Termin stattfindet, wie die zu überprüfende Gruppe.
    */
    public boolean conflict(CourseRegistration singleRegistration, CourseGroup courseGroup) {
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

    protected void removeStudent(CourseRegistration changeableStudent, CourseGroup possibileGroup, AllocationPlan allocPlan){
        try {
            allocPlan.removeCourseGroupRegistration(changeableStudent, possibileGroup);
        } catch (CourseGroupDoesntExistException e) {
            e.printStackTrace();
        }

        if(this.studentsCourseGroups.get(changeableStudent.getStudent()) != null){
            List<CourseGroup> studentsCourseGroups = this.studentsCourseGroups.get(changeableStudent.getStudent());
            studentsCourseGroups.remove(possibileGroup);
            this.studentsCourseGroups.put(changeableStudent.getStudent(), studentsCourseGroups); // update
        }
    }

    protected void registerStudent(CourseRegistration singleRegistration, CourseGroup courseGroup, AllocationPlan allocPlan) {
        try {
            allocPlan.addCourseRegistration(courseGroup, singleRegistration);
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

        studentsCourseGroups.add(courseGroup);
//        System.out.println(singleRegistration.getStudent()+" Gruppen:"+studentsCourseGroups);
        this.studentsCourseGroups.put(singleRegistration.getStudent(), studentsCourseGroups); // update
    }

    protected void registerTeam(Team team, CourseGroup courseGroup, AllocationPlan allocPlan){
        for(TeamRegistration teamReg : team.getTeamRegistrations() ){
            try {
                allocPlan.addTeamRegistration(courseGroup, teamReg);
            } catch (CourseGroupFullException e) {
                e.printStackTrace();
            } catch (CourseGroupDoesntExistException e) {
                e.printStackTrace();
            }
            List<CourseGroup> studentsCourseGroups;

            // Wenn der Student noch keiner einzigen Gruppe zugewiesen wurde (dann hat er auch noch keinen Map Eintrag)
            if(this.studentsCourseGroups.get(teamReg.getStudent()) == null){
                studentsCourseGroups = new ArrayList<>();
            }
            else{
                studentsCourseGroups = this.studentsCourseGroups.get(teamReg.getStudent());
            }

            studentsCourseGroups.add(courseGroup);
//            System.out.println(teamReg.getStudent()+" Gruppen:"+studentsCourseGroups);
            this.studentsCourseGroups.put(teamReg.getStudent(), studentsCourseGroups); // update
        }
    }

}
