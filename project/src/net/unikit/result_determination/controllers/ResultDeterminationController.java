package net.unikit.result_determination.controllers;

import net.unikit.database.implementations.DatabaseConfigurationUtils;
import net.unikit.database.implementations.DatabaseManagerFactory;
import net.unikit.database.interfaces.DatabaseConfiguration;
import net.unikit.database.interfaces.DatabaseManager;
import net.unikit.database.interfaces.entities.Course;
import net.unikit.result_determination.models.exceptions.NotEnoughCourseGroupsException;
import net.unikit.result_determination.models.implementations.algorithms.RandomAllocationPlanAlgorithmImpl;
import net.unikit.result_determination.models.implementations.dummys.DummyDataGenerator;
import net.unikit.result_determination.models.interfaces.AlgorithmSettings;
import net.unikit.result_determination.models.interfaces.AllocationPlan;
import net.unikit.result_determination.models.interfaces.AllocationPlanAlgorithm;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Created by Jones on 15.11.2015.
 *
 * Note:
 *
 * This is the main Controller.
 * Every interaction will be noticed here and will be delegated to the individual models or
 * to other controllers, that handle for example the database.
 *
 * To create an AllocationPlan just use the controllers method.
 */
public class ResultDeterminationController{
    private DatabaseManager dbmanager;
    private DummyDataGenerator dummyDataGenerator;

    /**
     * Initializes the Object
     * @throws IOException
     */
    public ResultDeterminationController() throws IOException {
//        initDatabaseManager(); // TODO zurzeit kam hier noch eine UnsupportedOperationException -> muss Andi noch implementieren
        dummyDataGenerator = new DummyDataGenerator(48); // 3*16 Studenten. -> Es gibt pro Kurs 3 Gruppen a 16 Studenten
    }

    /**
     * Creates the AllocationPlan
     * @param algorithmSettings the settings which affect the algorithms behavier
     * @return the AllocationPlan
     * @ensure Every student will be assigned to a courseGroup. It could be possible that some teams have to be splitted to ensure that.
     */
    public AllocationPlan createAllocationPlan(AlgorithmSettings algorithmSettings) throws NotEnoughCourseGroupsException {

        /*  All courses for which the allocations shall be created  */
        //List<Course> courses = dbmanager.getCourseManager().getAllEntities(); --> wird später verwendet!!! Erstmal nur mit Dummys arbeiten!
        List<Course> courses = dummyDataGenerator.getDummyCourses();

        /* The Algorithm that does the work */
        AllocationPlanAlgorithm allocPlanAlgorithm = new RandomAllocationPlanAlgorithmImpl(algorithmSettings);

        // *************************** Idee 1 *************************************
        AllocationPlan allocPlan = allocPlanAlgorithm.calculateAllocationPlan(courses);

        return allocPlan;
    }

    /*
     * Initializes the DatabaseManager for gaining acess to the Database
     */
    private void initDatabaseManager() throws IOException {
        Properties prop = new Properties();
        DatabaseConfiguration dbConfigExtern = null;
        InputStream inputStream = getClass().getResourceAsStream("/database_external.properties");
        prop.load(inputStream);

        dbConfigExtern = DatabaseConfigurationUtils.createDatabaseConfiguration(prop);

        prop = new Properties();
        DatabaseConfiguration dbConfigIntern = null;
        inputStream = getClass().getResourceAsStream("/database_internal.properties");
        prop.load(inputStream);

        dbConfigIntern = DatabaseConfigurationUtils.createDatabaseConfiguration(prop);

        DatabaseManagerFactory dbManagerFactory = new DatabaseManagerFactory();
        dbmanager = dbManagerFactory.createDatabaseManager(dbConfigIntern, dbConfigExtern);
    }
}
