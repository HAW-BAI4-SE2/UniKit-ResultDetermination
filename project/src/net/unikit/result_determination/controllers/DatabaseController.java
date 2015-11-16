package net.unikit.result_determination.controllers;

import net.unikit.database.common.implementations.DatabaseConfigurationUtils;
import net.unikit.database.common.interfaces.DatabaseConfiguration;
import net.unikit.database.external.implementations.ImportDatabaseManagerFactory;
import net.unikit.database.external.interfaces.*;
import net.unikit.database.unikit_.implementations.UniKitDatabaseManagerFactory;
import net.unikit.database.unikit_.interfaces.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class DatabaseController {
    private AppointmentManager appointmentManager;
    private CourseGroupManager courseGroupManager;
    private CourseManager courseManager;
    private FieldOfStudyManager fieldOfStudyManager;
    private StudentManager studentManager;
    private CourseRegistrationManager courseRegistrationManager;
    private TeamApplicationManager teamApplicationManager;
    private TeamInvitationManager teamInvitationManager;
    private TeamManager teamManager;
    private TeamRegistrationManager teamRegistrationManager;

    public DatabaseController() throws IOException {
        init();
    }

    public void init() throws IOException {
//        Logger.info("Initializing application...");

        // Load internal database
//        Logger.info("Loading internal database...");
        //Play.application().resourceAsStream("database_internal.properties");
        Properties prop = new Properties();
        InputStream in = getClass().getResourceAsStream("/database_external.properties");
        DatabaseConfiguration databaseConfigurationExternal = null;
        prop.load(in);

        databaseConfigurationExternal = DatabaseConfigurationUtils.createDatabaseConfiguration(prop); //DatabaseConfiguration.createFromProperties();
        ImportDatabaseManager externalDatabaseManager = ImportDatabaseManagerFactory.createDatabaseManager(databaseConfigurationExternal);


        prop = new Properties();
        in = getClass().getResourceAsStream("/database_internal.properties");
        DatabaseConfiguration databaseConfigurationInternal = null;
        prop.load(in);

        databaseConfigurationInternal = DatabaseConfigurationUtils.createDatabaseConfiguration(prop); //DatabaseConfiguration.createFromProperties();
        UniKitDatabaseManager internalDatabaseManager = UniKitDatabaseManagerFactory.createDatabaseManager(databaseConfigurationInternal);

        // Store database managers in global values
//        Logger.info("Initializing database managers...");

        appointmentManager = externalDatabaseManager.getAppointmentManager();
        courseGroupManager = externalDatabaseManager.getCourseGroupManager();
        courseManager = externalDatabaseManager.getCourseManager();
        fieldOfStudyManager = externalDatabaseManager.getFieldOfStudyManager();
        studentManager = externalDatabaseManager.getStudentManager();
        courseRegistrationManager = internalDatabaseManager.getCourseRegistrationManager();
        teamApplicationManager = internalDatabaseManager.getTeamApplicationManager();
        teamInvitationManager = internalDatabaseManager.getTeamInvitationManager();
        teamManager = internalDatabaseManager.getTeamManager();
        teamRegistrationManager = internalDatabaseManager.getTeamRegistrationManager();

//        Logger.info("Application initialized!");
    }

    public AppointmentManager getAppointmentManager() {
        return appointmentManager;
    }

    public CourseGroupManager getCourseGroupManager() {
        return courseGroupManager;
    }

    public CourseManager getCourseManager() {
        return courseManager;
    }

    public FieldOfStudyManager getFieldOfStudyManager() {
        return fieldOfStudyManager;
    }

    public StudentManager getStudentManager() {
        return studentManager;
    }

    public CourseRegistrationManager getCourseRegistrationManager() {
        return courseRegistrationManager;
    }

    public TeamApplicationManager getTeamApplicationManager() {
        return teamApplicationManager;
    }

    public TeamInvitationManager getTeamInvitationManager() {
        return teamInvitationManager;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public TeamRegistrationManager getTeamRegistrationManager() {
        return teamRegistrationManager;
    }
}