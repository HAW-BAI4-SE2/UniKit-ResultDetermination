package net.unikit.result_determination.models.implementations.dummys;


import net.unikit.database.interfaces.entities.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by abq307 on 17.11.2015.
 */
public class DummyDataGenerator {

    List<Course> courses;
    List<Student> students;
    List<CourseRegistration> courseRegistrations;
    List<Team> teams;
    Map<String,CourseGroupAppointment> dummyAppointmentMap;

    /**
     * Initializes the Object
     * @param numberStudents The number of students that shall be generated
     */
    public DummyDataGenerator(int numberStudents, int maxGroupSize){
        courses = new ArrayList<>();
        courseRegistrations = new ArrayList<>();
        teams = new ArrayList<>();
        students = new ArrayList<>();
        dummyAppointmentMap = new HashMap<>();

        /*
         * Reihenfolge muss sein:
         * 1. Termine erstellen,
         * 2. Studenten erstellen,
         * 3. Veranstaltungen erstellen,
         * 4. Studenten für die veranstaltung registrieren
         * 5. (Optional) Teams erstellen
         */
        generateAppointments();
        generateStudents(numberStudents);
        generateCourses(maxGroupSize);
        registrateAllStudentsAsSingleRegistration();
    }

    public List<Course> getDummyCourses(){
        return courses;
    }

    public List<Student> getStudents(){
        return students;
    }

    public List<Team> getTeams(){
        return teams;
    }

    /*
     * generiert die Termine...
     * aktuell nur Termine für Montags von 8:15Uhr bis 11:30Uhr
     */
    private void generateAppointments(){
        DummyDate startDate = null;
        DummyDate endDate = null;
        CourseGroupAppointment appointment = null;
        int erstekalenderWoche = 43;
        int letzteKalenderWoche = 56;

        // Generiert Appointments und speichert sie in der Map dummyAppointmentMap. zugriff über "KWxx_Wochentag_Früh" oder "KWxx_Wochentag_Spät"
        for(int i=erstekalenderWoche; i<=letzteKalenderWoche;i++){
            startDate = new DummyDate(i+"","Montag","08:15");
            endDate = new DummyDate(i+"","Montag","11:30");
            appointment = new DummyAppointmentImpl(startDate,endDate);
            dummyAppointmentMap.put("KW" + i + "_Montag_Früh", appointment);
        }
    }


    /*
     * Generiert eine Anzahl von Studenten
     */
    private void generateStudents(int numberStudents){
        for(int i=0; i<numberStudents;i++){
            Student s = new DummyStudentImpl(new DummyStudentNumberImpl(i+1+""));
            students.add(s);
        }
    }

    /*
     * Generiert Testdaten für die beiden veranstaltungen LB und RMP
     * Es werden die Termine generiert und die Praktikumsgruppen erstellt.
     */
    private void generateCourses(int maxGroupSize){
        // LB -> immer Montag_Früh
        DummyCourseImpl lb = new DummyCourseImpl("Logik und Berechenbarkeit",2,1,2); // DummyCourseImpl(String name, Integer semester, int min, int max)
        List<CourseGroup> lbGroups = new ArrayList<>();

        // LBP/01 -> 43,46,49,54
        List<CourseGroupAppointment> lb01Appointments = new ArrayList<>();
        lb01Appointments.add(dummyAppointmentMap.get("KW43_Montag_Früh"));
        lb01Appointments.add(dummyAppointmentMap.get("KW46_Montag_Früh"));
        lb01Appointments.add(dummyAppointmentMap.get("KW49_Montag_Früh"));
        lb01Appointments.add(dummyAppointmentMap.get("KW54_Montag_Früh"));

        // LBP/02 -> 44,47,50,55
        List<CourseGroupAppointment> lb02Appointments = new ArrayList<>();
        lb02Appointments.add(dummyAppointmentMap.get("KW44_Montag_Früh"));
        lb02Appointments.add(dummyAppointmentMap.get("KW47_Montag_Früh"));
        lb02Appointments.add(dummyAppointmentMap.get("KW50_Montag_Früh"));
        lb02Appointments.add(dummyAppointmentMap.get("KW55_Montag_Früh"));

        // LBP/03 -> 45,48,51,56
        List<CourseGroupAppointment> lb03Appointments = new ArrayList<>();
        lb03Appointments.add(dummyAppointmentMap.get("KW45_Montag_Früh"));
        lb03Appointments.add(dummyAppointmentMap.get("KW48_Montag_Früh"));
        lb03Appointments.add(dummyAppointmentMap.get("KW51_Montag_Früh"));
        lb03Appointments.add(dummyAppointmentMap.get("KW56_Montag_Früh"));

        CourseGroup lb01 = new DummyCourseGroupImpl(lb,lb01Appointments,1,maxGroupSize);
        CourseGroup lb02 = new DummyCourseGroupImpl(lb,lb02Appointments,2,maxGroupSize);
        CourseGroup lb03 = new DummyCourseGroupImpl(lb,lb03Appointments,3,maxGroupSize);

        lbGroups.add(lb01);
        lbGroups.add(lb02);
        lbGroups.add(lb03);

//      ****************************************************************************************************************

        // RMP
        DummyCourseImpl rmp = new DummyCourseImpl("Rechner und Maschinennahe Programmierung",2,1,2);
        List<CourseGroup> rmpGroups = new ArrayList<>();
        // RMPP/01 -> 45,48,51,56
        List<CourseGroupAppointment> rmpp1Appointments = new ArrayList<>();
        rmpp1Appointments.add(dummyAppointmentMap.get("KW45_Montag_Früh"));
        rmpp1Appointments.add(dummyAppointmentMap.get("KW48_Montag_Früh"));
        rmpp1Appointments.add(dummyAppointmentMap.get("KW51_Montag_Früh"));
        rmpp1Appointments.add(dummyAppointmentMap.get("KW56_Montag_Früh"));

        // RMPP/02 -> 43,46,49,54
        List<CourseGroupAppointment> rmpp2Appointments = new ArrayList<>();
        rmpp2Appointments.add(dummyAppointmentMap.get("KW43_Montag_Früh"));
        rmpp2Appointments.add(dummyAppointmentMap.get("KW46_Montag_Früh"));
        rmpp2Appointments.add(dummyAppointmentMap.get("KW49_Montag_Früh"));
        rmpp2Appointments.add(dummyAppointmentMap.get("KW54_Montag_Früh"));
        // RMPP/03 -> 44,47,50,55
        List<CourseGroupAppointment> rmpp3Appointments = new ArrayList<>();
        rmpp3Appointments.add(dummyAppointmentMap.get("KW44_Montag_Früh"));
        rmpp3Appointments.add(dummyAppointmentMap.get("KW47_Montag_Früh"));
        rmpp3Appointments.add(dummyAppointmentMap.get("KW50_Montag_Früh"));
        rmpp3Appointments.add(dummyAppointmentMap.get("KW55_Montag_Früh"));

        CourseGroup rmpp1 = new DummyCourseGroupImpl(rmp,rmpp1Appointments,1,maxGroupSize);
        CourseGroup rmpp2 = new DummyCourseGroupImpl(rmp,rmpp2Appointments,2,maxGroupSize);
        CourseGroup rmpp3 = new DummyCourseGroupImpl(rmp,rmpp3Appointments,3,maxGroupSize);

        rmpGroups.add(rmpp1);
        rmpGroups.add(rmpp2);
        rmpGroups.add(rmpp3);

        lb.setCourseGroups(lbGroups);
        rmp.setCourseGroups(rmpGroups);

        courses.add(lb);
        courses.add(rmp);
    }

    private void registrateAllStudentsAsSingleRegistration(){

        for(Course c : courses){
            List<CourseRegistration> singleRegistrations = c.getSingleRegistrations();
            for (Student s : students){
                singleRegistrations.add(new DummyCourseRegistrationImpl(s,c));
            }
            ((DummyCourseImpl)c).setCourseRegistrations(singleRegistrations);
        }
    }
}
