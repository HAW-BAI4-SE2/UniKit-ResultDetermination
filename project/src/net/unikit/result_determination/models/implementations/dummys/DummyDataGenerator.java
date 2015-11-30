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
//        buildTeamAndRegister();
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

        // ********************************** Allgemeine Termine *******************************************************
        // KW -> 43, 46, 49, 54
        List<CourseGroupAppointment> zeiteinheitEins_Montagmorgens = new ArrayList<>();
        zeiteinheitEins_Montagmorgens.add(dummyAppointmentMap.get("KW43_Montag_Früh"));
        zeiteinheitEins_Montagmorgens.add(dummyAppointmentMap.get("KW46_Montag_Früh"));
        zeiteinheitEins_Montagmorgens.add(dummyAppointmentMap.get("KW49_Montag_Früh"));
        zeiteinheitEins_Montagmorgens.add(dummyAppointmentMap.get("KW54_Montag_Früh"));

        // KW -> 44,47,50,55
        List<CourseGroupAppointment> zeiteinheitZwei_Montagmorgens = new ArrayList<>();
        zeiteinheitZwei_Montagmorgens.add(dummyAppointmentMap.get("KW44_Montag_Früh"));
        zeiteinheitZwei_Montagmorgens.add(dummyAppointmentMap.get("KW47_Montag_Früh"));
        zeiteinheitZwei_Montagmorgens.add(dummyAppointmentMap.get("KW50_Montag_Früh"));
        zeiteinheitZwei_Montagmorgens.add(dummyAppointmentMap.get("KW55_Montag_Früh"));

        // KW -> 45,48,51,56
        List<CourseGroupAppointment> zeiteinheitDrei_Montagmorgens = new ArrayList<>();
        zeiteinheitDrei_Montagmorgens.add(dummyAppointmentMap.get("KW45_Montag_Früh"));
        zeiteinheitDrei_Montagmorgens.add(dummyAppointmentMap.get("KW48_Montag_Früh"));
        zeiteinheitDrei_Montagmorgens.add(dummyAppointmentMap.get("KW51_Montag_Früh"));
        zeiteinheitDrei_Montagmorgens.add(dummyAppointmentMap.get("KW56_Montag_Früh"));

        // *************************************************************************************************************
        // LB -> immer Montag_Früh
        DummyCourseImpl lb = new DummyCourseImpl("Logik und Berechenbarkeit",2,1,2); // DummyCourseImpl(String name, Integer semester, int min, int max)
        List<CourseGroup> lbGroups = new ArrayList<>();
        // LBP/01 -> 43,46,49,54
        CourseGroup lb01 = new DummyCourseGroupImpl(lb,zeiteinheitEins_Montagmorgens,1,maxGroupSize);
        // LBP/02 -> 44,47,50,55
        CourseGroup lb02 = new DummyCourseGroupImpl(lb,zeiteinheitZwei_Montagmorgens,2,maxGroupSize);
        // LBP/03 -> 45,48,51,56
        CourseGroup lb03 = new DummyCourseGroupImpl(lb,zeiteinheitDrei_Montagmorgens,3,maxGroupSize);

        lbGroups.add(lb01);
        lbGroups.add(lb02);
        lbGroups.add(lb03);

//      ****************************************************************************************************************

        // RMP
        DummyCourseImpl rmp = new DummyCourseImpl("Rechner und Maschinennahe Programmierung",2,1,2);
        List<CourseGroup> rmpGroups = new ArrayList<>();
        // RMPP/01 -> 45,48,51,56
        CourseGroup rmpp1 = new DummyCourseGroupImpl(rmp,zeiteinheitDrei_Montagmorgens,1,maxGroupSize);
        // RMPP/02 -> 43,46,49,54
        CourseGroup rmpp2 = new DummyCourseGroupImpl(rmp,zeiteinheitEins_Montagmorgens,2,maxGroupSize);
        // RMPP/03 -> 44,47,50,55
        CourseGroup rmpp3 = new DummyCourseGroupImpl(rmp,zeiteinheitZwei_Montagmorgens,3,maxGroupSize);

        rmpGroups.add(rmpp1);
        rmpGroups.add(rmpp2);
        rmpGroups.add(rmpp3);


        // *************************************************************************************************************

        // DB
        DummyCourseImpl db = new DummyCourseImpl("Datenbanken",2,1,2);
        List<CourseGroup> dbGroups = new ArrayList<>();
        // DBP/01 -> 45,48,51,56
        CourseGroup dbp1 = new DummyCourseGroupImpl(db,zeiteinheitDrei_Montagmorgens,1,maxGroupSize);
        // DBP/02 -> 43,46,49,54
        CourseGroup dbp2 = new DummyCourseGroupImpl(db,zeiteinheitEins_Montagmorgens,2,maxGroupSize);
        // DBP/03 -> 44,47,50,55
        CourseGroup dbp3 = new DummyCourseGroupImpl(db,zeiteinheitZwei_Montagmorgens,3,maxGroupSize);

        dbGroups.add(dbp1);
        dbGroups.add(dbp2);
        dbGroups.add(dbp3);


        // *************************************************************************************************************

        // SE
        DummyCourseImpl se = new DummyCourseImpl("Software Entwicklung",2,1,2);
        List<CourseGroup> seGroups = new ArrayList<>();

        // SEP/01 -> 45,48,51,56
        CourseGroup se1 = new DummyCourseGroupImpl(se,zeiteinheitDrei_Montagmorgens,1,maxGroupSize);
        // SEP/02 -> 43,46,49,54
        CourseGroup se2 = new DummyCourseGroupImpl(se,zeiteinheitEins_Montagmorgens,2,maxGroupSize);
        // SEP/03 -> 44,47,50,55
        CourseGroup se3 = new DummyCourseGroupImpl(se,zeiteinheitZwei_Montagmorgens,3,maxGroupSize);

        seGroups.add(se1);
        seGroups.add(se2);
        seGroups.add(se3);

        // *************************************************************************************************************

        lb.setCourseGroups(lbGroups);
        rmp.setCourseGroups(rmpGroups);
        db.setCourseGroups(dbGroups);
        se.setCourseGroups(seGroups);

        courses.add(lb);
        courses.add(rmp);
        courses.add(db);
//        courses.add(se);
    }

    private void registrateAllStudentsAsSingleRegistration(){

        for(Course c : courses){
            List<CourseRegistration> singleRegistrations = c.getSingleRegistrations();
            for (Student s : students){
                CourseRegistration singleRegistration = new DummyCourseRegistrationImpl(s,c);
                singleRegistrations.add(singleRegistration);
                ((DummyStudentImpl)s).addCourseRegistration(singleRegistration);
            }
            ((DummyCourseImpl)c).setCourseRegistrations(singleRegistrations);
        }
    }

    /*
     *  Wandelt alle CourseRegistrations in TeamRegistrations um und simuliert die Teambildung.
     *  Es gibt nur noch Teams.
     */
    public void buildTeamAndRegister(){

        for(Course c : courses){
            System.out.println("\n**** Create Teams for " + c.getName() + ". ****");
            List<Team> teams = new ArrayList<>();

            for(int i=0; i< c.getSingleRegistrations().size()/c.getMaxTeamSize();i++){
                Team t = new DummyTeamImpl(c);
                teams.add(t);
            }

            for(CourseRegistration courseRegistration : c.getSingleRegistrations()){
                Student s = courseRegistration.getStudent();
                Team t = findTeam(teams, c.getMaxTeamSize());
                TeamRegistration teamReg = new DummyTeamRegistration(t,s);
                ((DummyTeamImpl)t).addTeamRegistration(teamReg);
            }
            ((DummyCourseImpl)c).setTeams(teams);

            // ****************************************** AUSGABE ******************************************************
            for(Team t:teams){
                System.out.println(t);
            }
            ((DummyCourseImpl) c).setCourseRegistrations(new ArrayList<>());
        }

    }

    private Team findTeam(List<Team> teams, int maxTeamSize) {
        Team t = null;
        for(Team team : teams){
            if(team.getTeamRegistrations().size() < maxTeamSize ){
                t = team;
                break;
            }
        }
        return t;
    }
}
