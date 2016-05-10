package net.unikit.result_determination.controllers;

import net.unikit.database.implementations.DatabaseConfigurationUtils;
import net.unikit.database.implementations.DatabaseManagerFactory;
import net.unikit.database.interfaces.DatabaseConfiguration;
import net.unikit.database.interfaces.DatabaseManager;
import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.TeamRegistration;
import net.unikit.result_determination.models.exceptions.CourseGroupDoesntExistException;
import net.unikit.result_determination.models.exceptions.CourseGroupFullException;
import net.unikit.result_determination.models.exceptions.NoTeamRegistrationsFoundException;
import net.unikit.result_determination.models.exceptions.NotEnoughCourseGroupsException;
import net.unikit.result_determination.models.implementations.*;
import net.unikit.result_determination.models.implementations.algorithms.GreedyAllocationPlanAlgorithm;
import net.unikit.result_determination.models.implementations.dummys.DummyDataGenerator;
import net.unikit.result_determination.utils.AlgorithmUtils;
import net.unikit.result_determination.view.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by Jones on 03.01.2016.
 */
public class NewResultDeterminationController {

    private DatabaseManager dbmanager;
    private DummyDataGenerator dummyDataGenerator;
    private AllocationPlanAlgorithmUI ui;
    private GreedyAllocationPlanAlgorithm allocPlanAlgorithm;
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
        allocPlanAlgorithm = new GreedyAllocationPlanAlgorithm();
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
                    if (allocationPlan != null) {
                        int option = JOptionPane.showConfirmDialog(ui.getFrame(), "Es ist breits ein Belegungsplan vorhanden. Möchten Sie den alten überschreiben?");
                        if (option == 0) {
                            createAllocationPlan();
                            ui.updateUI(allocationPlan);
                        }
                    } else {
                        createAllocationPlan();
                        ui.updateUI(allocationPlan);
                    }

                    if (allocationPlan.getNumberOfNotMatchableRegistrations() != 0) {
                        int option = JOptionPane.showConfirmDialog(ui.getFrame(), "Das Ergebnis ist nicht perfekt. " +
                                "\nEs konnte nicht für alle Registrierungen eine passende Gruppe gefunden werden." +
                                "\nMöchten Sie, dass automatisch nach einer Belegung gesucht wird?" +
                                "\n(Warnung! Dies kann dazu führen, dass die Gruppengrößen verändert werden!)");
                        if (option == 0) {
                            allocPlanAlgorithm.fixNotMatchables(courses, allocationPlan);
                            ui.updateUI(allocationPlan);
                        }
                    }
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

        ui.getAutoFixAllocationPlanMenuItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    if (allocationPlan != null) {
                        if (allocationPlan.getNumberOfNotMatchableRegistrations() != 0) {
                            int option = JOptionPane.showConfirmDialog(ui.getFrame(), "Das Ergebnis ist nicht perfekt. " +
                                    "\nEs konnte nicht für alle Registrierungen eine passende Gruppe gefunden werden." +
                                    "\nMöchten Sie, dass automatisch nach einer Belegung gesucht wird?" +
                                    "\n(Warnung! Dies kann dazu führen, dass die Gruppengrößen verändert werden!)");
                            if (option == 0) {
                                allocPlanAlgorithm.fixNotMatchables(courses, allocationPlan);
                                ui.updateUI(allocationPlan);
                            }
                        }
                    }
                } catch (CourseGroupDoesntExistException e1) {
                    e1.printStackTrace();
                } catch (CourseGroupFullException e1) {
                    e1.printStackTrace();
                }
            }
        });

        ui.getResultTable().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable table = ui.getResultTable();
                int column = table.getColumnModel().getColumnIndexAtX(e.getX());
                int row = e.getY() / table.getRowHeight();
                if (!(table.getModel() instanceof DefaultTableModel)) {
                    AlgorithmResultTableModel tableModel = (AlgorithmResultTableModel) table.getModel();
                    if (row <= tableModel.getRowCount() && column <= tableModel.getColumnCount()) {
                        ExtendedCourse course = (ExtendedCourse) tableModel.getValueAt(row, 0);
                        startCourseFocusUI(course);
                    }
                }
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

    private void startCourseFocusUI(ExtendedCourse course) {
        CourseFocusUI courseFocus = new CourseFocusUI(course);

        Map<ExtendedCourseGroup,Integer> oldMaxGroupSizes = new HashMap<>();
        for(ExtendedCourseGroup group : course.getCourseGroups()){
            oldMaxGroupSizes.put(group,group.getMaxGroupSize());
        }

        AssignedStudentsTableModel assignedTableModel = new AssignedStudentsTableModel(course.getAssignedTeamRegistrations(), allocPlanAlgorithm.getStudentsCourseGroups());
        JTable assigned = courseFocus.getAssignedStudentsTable();
        assigned.setModel(assignedTableModel);
        assigned.setRowSorter(new TableRowSorter<>(assignedTableModel));

        UnassignedStudentsTableModel unassignedTableModel = new UnassignedStudentsTableModel(course.getNotMatchableTeamRegistrations());
        courseFocus.getUnassignedStudentsTable().setModel(unassignedTableModel);
        courseFocus.getUnassignedStudentsTable().setRowSorter(new TableRowSorter<>(unassignedTableModel));

        courseFocus.getAutoSolutionButton().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    System.out.println("GetAutoSolutionButton pressed");
                    allocPlanAlgorithm.fixNotMatchables(course, allocationPlan);
                    ui.updateUI(allocationPlan);
                    courseFocus.updateUI(course, allocPlanAlgorithm.getStudentsCourseGroups());
                } catch (CourseGroupFullException e1) {
                    e1.printStackTrace();
                } catch (CourseGroupDoesntExistException e1) {
                    e1.printStackTrace();
                }
            }
        });

        courseFocus.getManuallySolutionButton().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTable unassignedStudents = courseFocus.getUnassignedStudentsTable();
                int row = unassignedStudents.getSelectedRow();
//                    JComboBox groups = courseFocus.getCourseGroupsComboBox();
                JTable groupTable = courseFocus.getCourseGroupsTable();
                int groupRow = groupTable.getSelectedRow();

                if (row >= 0 && groupRow >= 0) {
                    TeamRegistration teamRegistration = unassignedTableModel.getTeamRegistration(row);

                    CourseGroupTableModel courseGroupTableModel = (CourseGroupTableModel) groupTable.getModel();
                    ExtendedCourseGroup courseGroup = courseGroupTableModel.getCourseGroup(groupRow);

                    int option = 2;
                    if (allocPlanAlgorithm.isPossibileCourseGroup(teamRegistration, courseGroup)) {
                        String inf = "Sind Sie sicher, dass Sie Student: " + teamRegistration.getStudent().getStudentNumber().getValue()
                                + " in die Gruppe: " + courseGroup.getGroupNumber() + " einfügen möchten?";
                        option = JOptionPane.showConfirmDialog(null, inf);
                    } else {
                        List<ExtendedCourseGroup> possibileGroups = allocPlanAlgorithm.findPossibileCourseGroupsFor(teamRegistration, course);
                        String inf = "Das Einfügen von Student: " + teamRegistration.getStudent().getStudentNumber().getValue()
                                + " in die Gruppe: " + courseGroup.getGroupNumber() + " würde zu einem Konflikt führen." +
                                "\nFolgende Gruppen wären möglich: " + possibileGroups +
                                "\nMöchten Sie dennoch fotfahren?";
                        option = JOptionPane.showConfirmDialog(null, inf);
                    }

                    if(option == 0){
                        // extend Group size
                        courseGroup.setMaxGroupSize(courseGroup.getMaxGroupSize() + 1);
                        course.removeNotMatchableTeamRegistration(teamRegistration);
                        allocPlanAlgorithm.registerTeamRegistration(teamRegistration, courseGroup);
                        allocationPlan.setNumberOfNotMatchableRegistrations(allocationPlan.getNumberOfNotMatchableRegistrations() - 1);

                        ui.updateUI(allocationPlan);
                        courseFocus.updateUI(course, allocPlanAlgorithm.getStudentsCourseGroups());
                    }
                }
            }
        });

        courseFocus.getRetryButton().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // reset MaxGroupSize
//                    for(ExtendedCourseGroup group : course.getCourseGroups()){
//                        group.setMaxGroupSize(group.getCourseGroup().getMaxGroupSize());
//                    }
                    allocPlanAlgorithm.recalculateAllocations(course, allocationPlan);

                    ui.updateUI(allocationPlan);
//                    courseFocus.getFrame().dispose();
//                    startCourseFocusUI(course);
                    courseFocus.updateUI(course,allocPlanAlgorithm.getStudentsCourseGroups());
                } catch (CourseGroupDoesntExistException e1) {
                    e1.printStackTrace();
                }
            }
        });

        courseFocus.getDeleteAllocationButton().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // reset MaxGroupSize
                for(ExtendedCourseGroup group : course.getCourseGroups()){
                    group.setMaxGroupSize(group.getCourseGroup().getMaxGroupSize());
                }
                allocPlanAlgorithm.resetAllocation(course, allocationPlan);
                ui.updateUI(allocationPlan);
//                courseFocus.getFrame().dispose();
//                startCourseFocusUI(course);
                courseFocus.updateUI(course, allocPlanAlgorithm.getStudentsCourseGroups());
            }
        });

        courseFocus.showUI();
    }

    /**
     * Creates the AllocationPlan
     *
     * @return the AllocationPlan
     * @ensure Every student will be assigned to a courseGroup. It could be possible that some teams have to be splitted to ensure that.
     */
    public void createAllocationPlan() throws NotEnoughCourseGroupsException, CourseGroupDoesntExistException, NoTeamRegistrationsFoundException, CourseGroupFullException {

        /*  All courses for which the allocations shall be created  */
        //List<Course> courses = dbmanager.getCourseManager().getAllEntities(); --> wird später verwendet!!! Erstmal nur mit Dummys arbeiten!
        List<Course> c = dummyDataGenerator.getDummyCourses();

        courses = AlgorithmUtils.wrapCourses(c);
        /* The Algorithm that does the work */

        allocPlanAlgorithm = new GreedyAllocationPlanAlgorithm();
        NewAllocationPlan allocPlan = allocPlanAlgorithm.calculateAllocationPlan(courses);

        int numberOfTeams = dummyDataGenerator.getNumberOfTeams();
        int numberOfRegistrations = dummyDataGenerator.getNumberOfRegistrations();

        allocPlan.setNumberOfTeams(numberOfTeams);
        allocPlan.setNumberOfRegistrations(numberOfRegistrations);

        System.out.println("*** Print AllocationPlan ***");
        System.out.println(allocPlan);

        allocationPlan = allocPlan;

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
