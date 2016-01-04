package net.unikit.result_determination.view;

import net.unikit.database.interfaces.entities.Student;
import net.unikit.database.interfaces.entities.TeamRegistration;
import net.unikit.result_determination.models.implementations.AssignedStudentsTableModel;
import net.unikit.result_determination.models.implementations.ExtendedCourse;
import net.unikit.result_determination.models.implementations.ExtendedCourseGroup;
import net.unikit.result_determination.models.implementations.UnassignedStudentsTableModel;
import net.unikit.result_determination.utils.AlgorithmUtils;

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class CourseFocusUI
{

    private final String teamPreservation = "Teamerhaltung:  ";
    private final String assignments = "Belegungen:  ";
    private JFrame frame;
    private JPanel contentPane;
    private final JLabel teamPreservationLabel;
    private final JLabel assignmentsLabel;
    private JLabel courseNameLabel;
    private final JComboBox courseGroupsComboBox;

    private final JButton manuallySolutionButton;

    private JButton autoSolutionButton;
    private JTable assignedStudentsTable;
    private JTable unassignedStudentsTable;
    public void showUI() {
        frame.setVisible(true);
    }

    /**
     * Create the frame.
     */
    public CourseFocusUI(ExtendedCourse course)
    {
        frame = new JFrame(course.getName());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setBounds(100, 100, 650, 400);
        frame.setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        frame.setContentPane(contentPane);

        JSplitPane splitPane = new JSplitPane();
        contentPane.add(splitPane, BorderLayout.CENTER);

        JPanel optionsPanel = new JPanel();
        splitPane.setLeftComponent(optionsPanel);
        optionsPanel.setLayout(new BorderLayout(0, 0));

        JPanel courseNamePanel = new JPanel();
        optionsPanel.add(courseNamePanel, BorderLayout.NORTH);
        courseNamePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 20));


        courseNameLabel = new JLabel("Veranstaltung: "+course.getAbbreviation());
        courseNameLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        courseNamePanel.add(courseNameLabel);

        JPanel buttonPanel = new JPanel();
        optionsPanel.add(buttonPanel, BorderLayout.CENTER);
        buttonPanel.setLayout(new GridLayout(4, 1, 0, 0));

        teamPreservationLabel = new JLabel(teamPreservation + AlgorithmUtils.round(course.getTeamPreservation(),2)+"%");
        teamPreservationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        buttonPanel.add(teamPreservationLabel);

        assignmentsLabel = new JLabel(assignments+ AlgorithmUtils.round(course.getAssignmentQuantity(), 2)+"%");
        assignmentsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        buttonPanel.add(assignmentsLabel);

        manuallySolutionButton = new JButton("Manuell lösen");
        buttonPanel.add(manuallySolutionButton);

        autoSolutionButton = new JButton("Finde Lösung");
        buttonPanel.add(autoSolutionButton);

        JPanel courseGroupsPanel = new JPanel();
        optionsPanel.add(courseGroupsPanel, BorderLayout.SOUTH);
        courseGroupsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 15));

        courseGroupsComboBox = new JComboBox();

        for(ExtendedCourseGroup courseGroup : course.getCourseGroups()){
            courseGroupsComboBox.addItem(courseGroup);
        }

        courseGroupsPanel.add(courseGroupsComboBox);

        JPanel tablePanel = new JPanel();
        splitPane.setRightComponent(tablePanel);
        tablePanel.setLayout(new BorderLayout(0, 0));

        JSplitPane studentsSplitPane = new JSplitPane();
        studentsSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        tablePanel.add(studentsSplitPane, BorderLayout.CENTER);
        studentsSplitPane.setDividerLocation(200);
        splitPane.setDividerLocation(150);

        // ***** Tables *****

        // assigned Students
        JPanel assignedStudentsUnderlyingPanel = new JPanel();
        assignedStudentsUnderlyingPanel.setLayout(new BorderLayout(0, 0));
        assignedStudentsUnderlyingPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        assignedStudentsTable = new JTable();
        assignedStudentsTable.setDragEnabled(false);

        assignedStudentsUnderlyingPanel.add(assignedStudentsTable.getTableHeader(), BorderLayout.PAGE_START);
        assignedStudentsUnderlyingPanel.add(assignedStudentsTable, BorderLayout.CENTER);

        JScrollPane assignedStudentsPane = new JScrollPane(assignedStudentsUnderlyingPanel,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        studentsSplitPane.setLeftComponent(assignedStudentsPane);
        assignedStudentsTable.setFillsViewportHeight(true);

        // unassigned Students
        JPanel unassignedStudentsUnderlyingPanel = new JPanel();
        unassignedStudentsUnderlyingPanel.setLayout(new BorderLayout(0,0));
        unassignedStudentsUnderlyingPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        unassignedStudentsTable = new JTable();
        unassignedStudentsTable.setDragEnabled(false);
        unassignedStudentsTable.setFillsViewportHeight(true);

        unassignedStudentsUnderlyingPanel.add(unassignedStudentsTable.getTableHeader(), BorderLayout.PAGE_START);
        unassignedStudentsUnderlyingPanel.add(unassignedStudentsTable,BorderLayout.CENTER);
        JScrollPane notAssignedStudentPane = new JScrollPane(unassignedStudentsUnderlyingPanel,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        studentsSplitPane.setRightComponent(notAssignedStudentPane);
    }

    public JButton getManuallySolutionButton() {
        return manuallySolutionButton;
    }

    public JButton getAutoSolutionButton() {
        return autoSolutionButton;
    }

    public JTable getAssignedStudentsTable() {
        return assignedStudentsTable;
    }

    public JTable getUnassignedStudentsTable() {
        return unassignedStudentsTable;
    }

    public void updateUI(ExtendedCourse course, Map<Student, List<ExtendedCourseGroup>> studentsCourseGroups) {

        AssignedStudentsTableModel assignedTableModel = new AssignedStudentsTableModel(course.getAssignedTeamRegistrations(), studentsCourseGroups);
        assignedStudentsTable.setModel(assignedTableModel);
        UnassignedStudentsTableModel unassignedTableModel = new UnassignedStudentsTableModel(course.getNotMatchableTeamRegistrations());
        unassignedStudentsTable.setModel(unassignedTableModel);

        teamPreservationLabel.setText(teamPreservation + AlgorithmUtils.round(course.getTeamPreservation(),2)+"%");
        assignmentsLabel.setText(assignments+ AlgorithmUtils.round(course.getAssignmentQuantity(), 2)+"%");
    }
}
