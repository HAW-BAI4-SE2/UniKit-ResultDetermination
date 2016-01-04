package net.unikit.result_determination.controllers;

import net.unikit.database.implementations.DatabaseConfigurationUtils;
import net.unikit.database.implementations.DatabaseManagerFactory;
import net.unikit.database.interfaces.DatabaseConfiguration;
import net.unikit.database.interfaces.DatabaseManager;
import net.unikit.database.interfaces.entities.Course;
import net.unikit.result_determination.models.exceptions.CourseGroupDoesntExistException;
import net.unikit.result_determination.models.exceptions.CourseGroupFullException;
import net.unikit.result_determination.models.exceptions.NoTeamRegistrationsFoundException;
import net.unikit.result_determination.models.exceptions.NotEnoughCourseGroupsException;
import net.unikit.result_determination.models.implementations.ExtendedCourse;
import net.unikit.result_determination.models.implementations.NewAllocationPlan;
import net.unikit.result_determination.models.implementations.algorithms.GreedyAllocationPlanAlgorithm;
import net.unikit.result_determination.models.implementations.dummys.DummyDataGenerator;
import net.unikit.result_determination.utils.AlgorithmUtils;
import net.unikit.result_determination.view.AllocationPlanAlgorithmUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Jones on 03.01.2016.
 */
public class NewResultDeterminationController {

    private DatabaseManager dbmanager;
    private DummyDataGenerator dummyDataGenerator;
    private AllocationPlanAlgorithmUI ui;
    private List<ExtendedCourse> courses;
    private NewAllocationPlan allocationPlan;

    /**
     * Initializes the Object
     *
     * @throws IOException
     */
    public NewResultDeterminationController() throws IOException {
//        initDatabaseManager(); // TODO zurzeit kam hier noch eine UnsupportedOperationException -> muss Andi noch implementieren
        int numberOfStudents = 48;
        dummyDataGenerator = new DummyDataGenerator(numberOfStudents, numberOfStudents / 3);
        dummyDataGenerator.buildTeamAndRegister();
        courses = new ArrayList<>();
        allocationPlan = null;
        ui = new AllocationPlanAlgorithmUI();
        ui.showUI();
        addUIListener();
    }

    private void addUIListener() {
        ui.getCreateAllocationPlanMenuItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    NewAllocationPlan allocationPlan = createAllocationPlan();
                } catch (NotEnoughCourseGroupsException e1) {
                    e1.printStackTrace();
                } catch (CourseGroupDoesntExistException e1) {
                    e1.printStackTrace();
                } catch (NoTeamRegistrationsFoundException e1) {
                    e1.printStackTrace();
                } catch (CourseGroupFullException e1) {
                    e1.printStackTrace();
                }
            }
        });

        ui.getExportAllocationPlanMenuItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO versuchen das Ergebnis zu verbessern
            }
        });

        ui.getResultTable().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable table = ui.getResultTable();
                int column = table.getColumnModel().getColumnIndexAtX(e.getX());
                int row = e.getY() / table.getRowHeight();
                System.out.println("Spalte: " + column + " und Reihe: " + row);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    /**
     * Creates the AllocationPlan
     *
     * @return the AllocationPlan
     * @ensure Every student will be assigned to a courseGroup. It could be possible that some teams have to be splitted to ensure that.
     */
    public NewAllocationPlan createAllocationPlan() throws NotEnoughCourseGroupsException, CourseGroupDoesntExistException, NoTeamRegistrationsFoundException, CourseGroupFullException {

        /*  All courses for which the allocations shall be created  */
        //List<Course> courses = dbmanager.getCourseManager().getAllEntities(); --> wird später verwendet!!! Erstmal nur mit Dummys arbeiten!
        List<Course> c = dummyDataGenerator.getDummyCourses();

        courses = AlgorithmUtils.wrapCourses(c);
        /* The Algorithm that does the work */
        GreedyAllocationPlanAlgorithm allocPlanAlgorithm = new GreedyAllocationPlanAlgorithm();

        NewAllocationPlan allocPlan = allocPlanAlgorithm.calculateAllocationPlan(courses);

        int numberOfStudents = dummyDataGenerator.getStudents().size();
        int numberOfTeams = dummyDataGenerator.getNumberOfTeams();
        int numberOfRegistrations = dummyDataGenerator.getNumberOfRegistrations();

        allocPlan.setNumberOfStudents(numberOfStudents);
        allocPlan.setNumberOfTeams(numberOfTeams);
        allocPlan.setNumberOfRegistrations(numberOfRegistrations);

        System.out.println("*** Print AllocationPlan ***");
        System.out.println(allocPlan);

        ui.updateUI(allocPlan);

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
