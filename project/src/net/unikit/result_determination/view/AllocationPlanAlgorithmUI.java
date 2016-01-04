package net.unikit.result_determination.view;

import net.unikit.result_determination.models.implementations.ExtendedCourse;
import net.unikit.result_determination.models.implementations.NewAllocationPlan;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.List;


public class AllocationPlanAlgorithmUI {

    private JPanel overviewPanel;
    private JPanel contentPane;
    private JPanel diagramPane;
    private JPanel registrationsDiagramPanel; // oben links
    private JPanel algorithmInformationPanel; // unten links
    private JPanel successfulAssignmentsDiagramPanel; // oben rechts
    private JPanel teamPreservationDiagramPanel; // unten rechts
    private JSplitPane diagramSplitPane;
    private JSplitPane diagramPaneLeft;
    private JSplitPane diagramPaneRight;
    private JTable resultTable;
    private JFrame frame;
    private JMenuItem createAllocationPlanMenuItem;
    private JMenuItem exportAllocationPlanMenuItem;
    private JMenuItem autoFixAllocationPlanMenuItem;

    final String teamPreservation = "Teamerhaltung";
    final String preservations = "Erhaltene Teams";
    final String destroyedTeams = "Zerstörte Teams";
    final String registeredTeams = "Angemeldete Teams";

    final String assignments = "Registrierungen";
    final String unsuccessfulAssignment = "Nicht erfolgreich";
    final String successfulAssignment = "Erfolgreich";
    final String course = "Veranstaltung";


    private JLabel numberOfCoursesLabel;
    private JLabel algorithmRuntimeLabel;
    private JLabel numberOfStudentsLabel;
    private JLabel numberOfRegistratedTeamsLabel;
    private JLabel numberOfTeamPreservationsLabel;
    private JLabel numberOfRegistrationsLabel;
    private JLabel numberOfNotMatchableStudentsLabel;
    private String algorithmRuntime;
    private String numberOfCourses;
    private String numberOfStudents;
    private String numberOfRegistratedTeams;
    private String numberOfTeamPreservations;
    private String numberOfRegistrations;
    private String numberOfNotMatchableStudents;

    /**
     * Create the frame.
     */
    public AllocationPlanAlgorithmUI() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setBounds(100, 100, 800, 640);
        frame.setResizable(false);

        initMenuBar();

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        frame.setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        initResultTable(tabbedPane);
        initDiagramPane(tabbedPane);
        initAlgorithmInformations();

        frame.setLocationRelativeTo(null);
    }

    public JTable getResultTable() {
        return resultTable;
    }

    public JFrame getFrame() {
        return frame;
    }

    public JMenuItem getCreateAllocationPlanMenuItem() {
        return createAllocationPlanMenuItem;
    }

    public JMenuItem getAutoFixAllocationPlanMenuItem() {
        return autoFixAllocationPlanMenuItem;
    }

    public JMenuItem getExportAllocationPlanMenuItem() {
        return exportAllocationPlanMenuItem;
    }

    /**
     * Shows the UI.
     */
    public void showUI() {
        frame.setVisible(true);
    }

    public JPanel getTeamPreservationDiagramPanel() {
        return teamPreservationDiagramPanel;
    }

    public JPanel getSuccessfulAssignmentsDiagramPanel() {
        return successfulAssignmentsDiagramPanel;
    }

    public JPanel getAlgorithmInformationPanel() {
        return algorithmInformationPanel;
    }

    public JPanel getRegistrationsDiagramPanel() {
        return registrationsDiagramPanel;
    }

    public void updateUI(NewAllocationPlan allocationPlan){
        List<ExtendedCourse> courses = allocationPlan.getCourses();
        int numberOfStudents = allocationPlan.getNumberOfStudents();
        int numberOfTeams = allocationPlan.getNumberOfTeams();
        int numberOfRegistrations = allocationPlan.getNumberOfRegistrations();
        long algorithmRuntime = allocationPlan.getAlgorithmRuntime();
        int numberOfTeamPreservations = allocationPlan.getNumberOfTeamPreservation();
        int numberOfNotMatchable = allocationPlan.getNumberOfNotMatchableRegistrations();
        int matchable = numberOfRegistrations - numberOfNotMatchable;

        updateTeamPreservationDiagram(numberOfTeamPreservations, numberOfTeams - numberOfTeamPreservations);
        updateAssignmentDiagram(matchable, numberOfNotMatchable);
        updateResultData(courses.size(), numberOfStudents, numberOfTeams, numberOfRegistrations, numberOfTeamPreservations, numberOfNotMatchable, algorithmRuntime);
        updateResultTable(courses);
    }

    /**
     * Updates the TeamPreservationDiagram
     *
     * @param numberOfTeamPreservations
     * @param numberOfDestroyedTeams
     */
    private void updateTeamPreservationDiagram(int numberOfTeamPreservations, int numberOfDestroyedTeams) {
        /* Team-preservation Diagram */

        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue(preservations, numberOfTeamPreservations);
        dataset.setValue(destroyedTeams, numberOfDestroyedTeams);

        JFreeChart pieChart = ChartFactory.createPieChart(teamPreservation, dataset, true, true, false);
        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setSectionPaint(preservations, Color.green);
        plot.setSectionPaint(destroyedTeams, Color.red);
        ChartPanel chartPanel = new ChartPanel(pieChart);

        if (teamPreservationDiagramPanel.getComponent(0) != null) {
            teamPreservationDiagramPanel.remove(0);
        }
        teamPreservationDiagramPanel.add(chartPanel);
    }

    /**
     * Updates the AssignmentDiagram
     *
     * @param numberOfSuccessfulAssignments
     * @param numberOfNotMatchables
     */
    private void updateAssignmentDiagram(int numberOfSuccessfulAssignments, int numberOfNotMatchables) {
                /* Assignement Diagram */
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue(successfulAssignment, numberOfSuccessfulAssignments);
        dataset.setValue(unsuccessfulAssignment, numberOfNotMatchables);

        JFreeChart pieChart = ChartFactory.createPieChart(assignments, dataset, true, true, false);
        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setSectionPaint(successfulAssignment, Color.green);
        plot.setSectionPaint(unsuccessfulAssignment, Color.red);
        ChartPanel chartPanel = new ChartPanel(pieChart);
        if (successfulAssignmentsDiagramPanel.getComponent(0) != null) {
            successfulAssignmentsDiagramPanel.remove(0);
        }
        successfulAssignmentsDiagramPanel.add(chartPanel);
    }

    private void updateResultData(int size, int numberOfStudents, int numberOfTeams, int numberOfRegistrations, int numberOfTeamPreservations, int notMatchable, long algorithmRuntime) {
        this.numberOfCoursesLabel.setText(numberOfCourses + size);
        this.algorithmRuntimeLabel.setText(this.algorithmRuntime + algorithmRuntime + "ms");
        this.numberOfStudentsLabel.setText(this.numberOfStudents + numberOfStudents);
        this.numberOfRegistratedTeamsLabel.setText(this.numberOfRegistratedTeams + numberOfTeams);
        this.numberOfTeamPreservationsLabel.setText(this.numberOfTeamPreservations + numberOfTeamPreservations);
        this.numberOfRegistrationsLabel.setText(this.numberOfRegistrations + numberOfRegistrations);
        this.numberOfNotMatchableStudentsLabel.setText(this.numberOfNotMatchableStudents + notMatchable);
    }

    /*
         * Initializes the Menu
         */
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu allocationPlanMenu = new JMenu("Belegungsplan");
        menuBar.add(allocationPlanMenu);

        createAllocationPlanMenuItem = new JMenuItem("Erstellen");
        allocationPlanMenu.add(createAllocationPlanMenuItem);

        autoFixAllocationPlanMenuItem = new JMenuItem("Automatische Konfliktbehebung starten");
        allocationPlanMenu.add(autoFixAllocationPlanMenuItem);

        exportAllocationPlanMenuItem = new JMenuItem("Exportieren");
        allocationPlanMenu.add(exportAllocationPlanMenuItem);
    }

    private void updateResultTable(List<ExtendedCourse> courses) {
        resultTable.setModel(new AlgorithmResultTableModel(courses));
    }

    /*
     * Initializes the resultTable tab
     */
    private void initResultTable(JTabbedPane tabbedPane) {
        overviewPanel = new JPanel();
        overviewPanel.setLayout(new BorderLayout(0, 0));
        overviewPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        resultTable = new JTable();
        resultTable.setDragEnabled(false);
        overviewPanel.add(resultTable.getTableHeader(), BorderLayout.PAGE_START);
        overviewPanel.add(resultTable, BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(overviewPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        resultTable.setFillsViewportHeight(true);
        tabbedPane.addTab("Veranstaltungen", null, scrollPane, null);
    }

    /*
     * Initializes the diagram tab
     */
    private void initDiagramPane(JTabbedPane tabbedPane) {
        diagramPane = new JPanel();
        tabbedPane.addTab("Zusammenfassung", null, diagramPane, null);
        diagramPane.setLayout(new BorderLayout(0, 0));

        diagramSplitPane = new JSplitPane();
        diagramSplitPane.setDividerSize(1);
        diagramPane.add(diagramSplitPane, BorderLayout.CENTER);

        // Left Split Pane Side
//        diagramPaneLeft = new JSplitPane();
//        diagramPaneLeft.setDividerSize(1);
//        diagramPaneLeft.setOrientation(JSplitPane.VERTICAL_SPLIT);
//        diagramSplitPane.setLeftComponent(diagramPaneLeft);
//
//        registrationsDiagramPanel = new JPanel();
//        diagramPaneLeft.setLeftComponent(registrationsDiagramPanel);
//        registrationsDiagramPanel.setLayout(new BorderLayout(0, 0));
//
//        algorithmInformationPanel = new JPanel();
//        diagramPaneLeft.setRightComponent(algorithmInformationPanel);
//        algorithmInformationPanel.setLayout(new GridLayout(8, 1));
//        diagramPaneLeft.setDividerLocation(frame.getHeight() / 2);

        JPanel panelLeft = new JPanel(new BorderLayout(0, 0));
        diagramSplitPane.setLeftComponent(panelLeft);
        algorithmInformationPanel = new JPanel();
        panelLeft.add(algorithmInformationPanel, BorderLayout.CENTER);
        algorithmInformationPanel.setLayout(new GridLayout(8, 1));


        // Right Split Pane Side
        diagramPaneRight = new JSplitPane();
        diagramPaneRight.setDividerSize(1);
        diagramPaneRight.setOrientation(JSplitPane.VERTICAL_SPLIT);
        diagramSplitPane.setRightComponent(diagramPaneRight);


        DefaultPieDataset dataset = new DefaultPieDataset();
        JFreeChart pieChart = ChartFactory.createPieChart(teamPreservation, dataset, true, true, false);
        ChartPanel chartPanel = new ChartPanel(pieChart);

        teamPreservationDiagramPanel = new JPanel();
        diagramPaneRight.setRightComponent(teamPreservationDiagramPanel);
        teamPreservationDiagramPanel.setLayout(new BorderLayout(0, 0));
        teamPreservationDiagramPanel.add(chartPanel);

        dataset = new DefaultPieDataset();
        pieChart = ChartFactory.createPieChart(assignments, dataset, true, true, false);
        chartPanel = new ChartPanel(pieChart);

        successfulAssignmentsDiagramPanel = new JPanel();
        diagramPaneRight.setLeftComponent(successfulAssignmentsDiagramPanel);
        successfulAssignmentsDiagramPanel.setLayout(new BorderLayout(0, 0));
        successfulAssignmentsDiagramPanel.add(chartPanel);
        diagramPaneRight.setDividerLocation((frame.getHeight() / 2) - 50);
        diagramSplitPane.setDividerLocation((frame.getWidth() / 2));
    }

    private void initAlgorithmInformations() {

        algorithmRuntime = "Algorithmus-Laufzeit:  ";
        numberOfCourses = "Veranstaltungen:  ";
        numberOfStudents = "Studenten:  ";
        numberOfRegistratedTeams = "Angemeldete Teams:  ";
        numberOfTeamPreservations = "Erhaltene Teams:  ";
        numberOfRegistrations = "Registrierungen Gesamt:  ";
        numberOfNotMatchableStudents = "Nicht Verarbeitete Registrierungen:  ";

        algorithmRuntimeLabel = new JLabel(algorithmRuntime);
        numberOfCoursesLabel = new JLabel(numberOfCourses);
        numberOfStudentsLabel = new JLabel(numberOfStudents);
        numberOfRegistratedTeamsLabel = new JLabel(numberOfRegistratedTeams);
        numberOfTeamPreservationsLabel = new JLabel(numberOfTeamPreservations);
        numberOfRegistrationsLabel = new JLabel(numberOfRegistrations);
        numberOfNotMatchableStudentsLabel = new JLabel(numberOfNotMatchableStudents);

        algorithmInformationPanel.add(new JLabel("************************ Algorithmus Ergebnisse ************************"));
        algorithmInformationPanel.add(numberOfCoursesLabel);
        algorithmInformationPanel.add(algorithmRuntimeLabel);
        algorithmInformationPanel.add(numberOfStudentsLabel);
        algorithmInformationPanel.add(numberOfRegistratedTeamsLabel);
        algorithmInformationPanel.add(numberOfTeamPreservationsLabel);
        algorithmInformationPanel.add(numberOfRegistrationsLabel);
        algorithmInformationPanel.add(numberOfNotMatchableStudentsLabel);

    }
}
