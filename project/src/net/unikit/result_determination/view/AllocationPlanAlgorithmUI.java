package net.unikit.result_determination.view;

import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.CourseGroup;
import net.unikit.database.interfaces.entities.Team;
import net.unikit.result_determination.models.exceptions.CourseGroupDoesntExistException;
import net.unikit.result_determination.models.exceptions.NoTeamRegistrationsFoundException;
import net.unikit.result_determination.models.interfaces.AllocationPlan;
import net.unikit.result_determination.models.interfaces.AllocationPlanAlgorithm;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import java.awt.GridBagLayout;
import java.util.List;


public class AllocationPlanAlgorithmUI
{

    private JPanel contentPane;
    private JPanel diagramPane;
    private JTable resultTable;
    private JFrame frame;
    private JMenuItem createAllocationPlanMenuItem;
    private JMenuItem exportButton;


    /**
     * Create the frame.
     */
    public AllocationPlanAlgorithmUI()
    {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 450, 300);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu allocationPlanButton = new JMenu("Belegungsplan");
        menuBar.add(allocationPlanButton);

        createAllocationPlanMenuItem = new JMenuItem("Erstellen");
        allocationPlanButton.add(createAllocationPlanMenuItem);

        exportButton = new JMenuItem("Exportieren");
        allocationPlanButton.add(exportButton);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        frame.setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel overviewPane = new JPanel();
        tabbedPane.addTab("Überblick", null, overviewPane, null);
        overviewPane.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        overviewPane.add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        resultTable = new JTable();
        panel.add(resultTable, BorderLayout.CENTER);

        diagramPane = new JPanel();
        tabbedPane.addTab("Diagramme", null, diagramPane, null);
        GridBagLayout gbl_diagramPane = new GridBagLayout();
        gbl_diagramPane.columnWidths = new int[]{0};
        gbl_diagramPane.rowHeights = new int[]{0};
        gbl_diagramPane.columnWeights = new double[]{Double.MIN_VALUE};
        gbl_diagramPane.rowWeights = new double[]{Double.MIN_VALUE};
        diagramPane.setLayout(gbl_diagramPane);

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

    public JMenuItem getExportButton() {
        return exportButton;
    }

    public void showUI() {
        frame.setVisible(true);
    }

    public void addDefaultBarChart() {

        final String fiat = "FIAT";
        final String audi = "AUDI";
        final String ford = "FORD";
        final String speed = "Speed";
        final String millage = "Millage";
        final String userrating = "User Rating";
        final String safety = "safety";

        final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );

        dataset.addValue( 1.0 , fiat , speed );
        dataset.addValue( 3.0 , fiat , userrating );
        dataset.addValue( 5.0 , fiat , millage );
        dataset.addValue( 5.0 , fiat , safety );

        dataset.addValue( 5.0 , audi , speed );
        dataset.addValue( 6.0 , audi , userrating );
        dataset.addValue( 10.0 , audi , millage );
        dataset.addValue( 4.0 , audi , safety );

        dataset.addValue( 4.0 , ford , speed );
        dataset.addValue( 2.0 , ford , userrating );
        dataset.addValue( 3.0 , ford , millage );
        dataset.addValue( 6.0 , ford , safety );

        JFreeChart barChart = ChartFactory.createBarChart(
                "CAR USAGE STATIStICS",
                "Category", "Score",
                dataset, PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        diagramPane.add(chartPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    public void updateAllocationPlanDiagram(List<Course> courses, AllocationPlan allocPlan, AllocationPlanAlgorithm allocPlanAlgorithm) throws CourseGroupDoesntExistException, NoTeamRegistrationsFoundException {
        int notMatchable=0;
        int numberOfRegistrations=0;
        int numberOfTeamRegistrations = 0;
        for(Course course : courses){
            System.out.println("\nKurs: "+course.getName());
            System.out.println("NotMatchable: "+allocPlanAlgorithm.getNotMatchableTeams().get(course));

            for(Team t: course.getTeams()){
                numberOfRegistrations+=t.getTeamRegistrations().size();
            }

            numberOfTeamRegistrations += course.getTeams().size();
            notMatchable += allocPlanAlgorithm.getNotMatchableTeams().get(course).size();
            for(CourseGroup courseGroup : course.getCourseGroups()){
                System.out.println(courseGroup + " Teilnehmer: " + (allocPlan.getCourseRegistrations(courseGroup).size() + allocPlan.getTeamRegistrations(courseGroup).size()));
            }
        }
        System.out.println(
                "\nVeranstaltungen: " + courses.size() +
                "\nAnmeldungen: " + numberOfRegistrations +
                "\nErhaltenen Teams: " + allocPlanAlgorithm.getNumberOfTeamPreservations() + " von " + numberOfTeamRegistrations +
                "\nNotMatchable global: " + notMatchable + " von " + numberOfRegistrations);
        System.out.println("****************************** END ALGORITHM ******************************");


        final String teamPreservation = "Teamerhaltung";
        final String preservations = "Erhaltene Teams";
        final String destroyedTeams = "Zerstörte Teams";
        final String registeredTeams = "Angemeldete Teams";

        final String anzahlNotMatchable = "Unverarbeitet";
        final String registrations = "Anmeldungen";

        if(numberOfTeamRegistrations==0){
            throw new NoTeamRegistrationsFoundException();
        }

        double pres = Math.abs((double)allocPlanAlgorithm.getNumberOfTeamPreservations()/(double)numberOfTeamRegistrations)*100;
        double del = ((double)(numberOfTeamRegistrations-allocPlanAlgorithm.getNumberOfTeamPreservations())/numberOfTeamRegistrations)*100;

        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue(preservations,pres);
        dataset.setValue(destroyedTeams,del);

        JFreeChart barChart = ChartFactory.createPieChart("Ergebnisse",dataset);

        ChartPanel chartPanel = new ChartPanel(barChart);
        diagramPane.add(chartPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }
}
