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
    Map<String,Appointment> dummyAppointmentMap;

    /**
     * Initializes the Object
     * @param numberStudents The number of students that shall be generated
     */
    public DummyDataGenerator(int numberStudents){
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
         * 4. Studenten f�r die veranstaltung registrieren
         * 5. (Optional) Teams erstellen
         */
        generateAppointments();
        generateStudents(numberStudents);
        generateCourses();
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
     * aktuell nur Termine f�r Montags von 8:15Uhr bis 11:30Uhr
     */
    private void generateAppointments(){
        DummyDate startDate = null;
        DummyDate endDate = null;
        Appointment appointment = null;
        int erstekalenderWoche = 43;
        int letzteKalenderWoche = 56;

        // Generiert Appointments und speichert sie in der Map dummyAppointmentMap. zugriff �ber "KWxx_Wochentag_Fr�h" oder "KWxx_Wochentag_Sp�t"
        for(int i=erstekalenderWoche; i<=letzteKalenderWoche;i++){
            startDate = new DummyDate(i+"","Montag","08:15");
            endDate = new DummyDate(i+"","Montag","11:30");
            appointment = new DummyAppointmentImpl(startDate,endDate);
            dummyAppointmentMap.put("KW"+i+"_Montag_Fr�h", appointment);
        }
    }


    /*
     * Generiert eine Anzahl von Studenten
     */
    private void generateStudents(int numberStudents){
        for(int i=0; i<numberStudents;i++){
            Student s = new DummyStudentImpl(new DummyStudentNumberImpl((i*2000)+""));
            students.add(s);
        }
    }

    /*
     * Generiert Testdaten f�r die beiden veranstaltungen LB und RMP
     * Es werden die Termine generiert und die Praktikumsgruppen erstellt.
     */
    private void generateCourses(){
        int maxGroupSize = 16;

        // LB -> immer Montag_Fr�h
        Course lb = new DummyCourseImpl("Logik und Berechenbarkeit",2,1,2);
        List<CourseGroup> lbGroups = new ArrayList<>();

        // LBP/01 -> 43,46,49,54
        List<Appointment> lb01Appointments = new ArrayList<>();
        lb01Appointments.add(dummyAppointmentMap.get("KW43_Montag_Fr�h"));
        lb01Appointments.add(dummyAppointmentMap.get("KW46_Montag_Fr�h"));
        lb01Appointments.add(dummyAppointmentMap.get("KW49_Montag_Fr�h"));
        lb01Appointments.add(dummyAppointmentMap.get("KW54_Montag_Fr�h"));

        // LBP/02 -> 44,47,50,55
        List<Appointment> lb02Appointments = new ArrayList<>();
        lb02Appointments.add(dummyAppointmentMap.get("KW44_Montag_Fr�h"));
        lb02Appointments.add(dummyAppointmentMap.get("KW47_Montag_Fr�h"));
        lb02Appointments.add(dummyAppointmentMap.get("KW50_Montag_Fr�h"));
        lb02Appointments.add(dummyAppointmentMap.get("KW55_Montag_Fr�h"));

        // LBP/03 -> 45,48,51,56
        List<Appointment> lb03Appointments = new ArrayList<>();
        lb03Appointments.add(dummyAppointmentMap.get("KW45_Montag_Fr�h"));
        lb03Appointments.add(dummyAppointmentMap.get("KW48_Montag_Fr�h"));
        lb03Appointments.add(dummyAppointmentMap.get("KW51_Montag_Fr�h"));
        lb03Appointments.add(dummyAppointmentMap.get("KW56_Montag_Fr�h"));

        CourseGroup lb01 = new DummyCourseGroupImpl(lb,lb01Appointments,1,maxGroupSize);
        CourseGroup lb02 = new DummyCourseGroupImpl(lb,lb02Appointments,2,maxGroupSize);
        CourseGroup lb03 = new DummyCourseGroupImpl(lb,lb03Appointments,3,maxGroupSize);

        lbGroups.add(lb01);
        lbGroups.add(lb02);
        lbGroups.add(lb03);

//      ****************************************************************************************************************

        // RMP
        Course rmp = new DummyCourseImpl("Rechner und Maschinennahe Programmierung",2,1,2);
        List<CourseGroup> rmpGroups = new ArrayList<>();
        // RMPP/01 -> 45,48,51,56
        List<Appointment> rmpp1Appointments = new ArrayList<>();
        rmpp1Appointments.add(dummyAppointmentMap.get("KW45_Montag_Fr�h"));
        rmpp1Appointments.add(dummyAppointmentMap.get("KW48_Montag_Fr�h"));
        rmpp1Appointments.add(dummyAppointmentMap.get("KW51_Montag_Fr�h"));
        rmpp1Appointments.add(dummyAppointmentMap.get("KW56_Montag_Fr�h"));

        // RMPP/02 -> 43,46,49,54
        List<Appointment> rmpp2Appointments = new ArrayList<>();
        rmpp2Appointments.add(dummyAppointmentMap.get("KW43_Montag_Fr�h"));
        rmpp2Appointments.add(dummyAppointmentMap.get("KW46_Montag_Fr�h"));
        rmpp2Appointments.add(dummyAppointmentMap.get("KW49_Montag_Fr�h"));
        rmpp2Appointments.add(dummyAppointmentMap.get("KW54_Montag_Fr�h"));
        // RMPP/03 -> 44,47,50,55
        List<Appointment> rmpp3Appointments = new ArrayList<>();
        rmpp3Appointments.add(dummyAppointmentMap.get("KW44_Montag_Fr�h"));
        rmpp3Appointments.add(dummyAppointmentMap.get("KW47_Montag_Fr�h"));
        rmpp3Appointments.add(dummyAppointmentMap.get("KW50_Montag_Fr�h"));
        rmpp3Appointments.add(dummyAppointmentMap.get("KW55_Montag_Fr�h"));

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
            c.setCourseRegistrations(singleRegistrations);
        }
    }
}
