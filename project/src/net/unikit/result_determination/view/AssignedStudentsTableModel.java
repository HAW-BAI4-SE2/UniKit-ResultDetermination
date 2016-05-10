package net.unikit.result_determination.view;

import net.unikit.database.interfaces.entities.Student;
import net.unikit.database.interfaces.entities.TeamRegistration;
import net.unikit.result_determination.models.implementations.ExtendedCourseGroup;
import net.unikit.result_determination.utils.AlgorithmUtils;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Map;

/**
 * Created by Jones on 04.01.2016.
 */
public class AssignedStudentsTableModel extends AbstractTableModel {

    final String[] tableHeaders;
    private List<TeamRegistration> teamRegistrations;
    private  Map<Student, List<ExtendedCourseGroup>> studentsCourseGroups;

    public AssignedStudentsTableModel(List<TeamRegistration> teamRegistrations, Map<Student, List<ExtendedCourseGroup>> studentsCourseGroups){
        tableHeaders = new String[]{ "MatrNr.", "Gruppe", "Studiengang" , "Semester"};
        this.teamRegistrations = teamRegistrations;
        this.studentsCourseGroups = studentsCourseGroups;
    }
    @Override
    public int getRowCount() {
        return teamRegistrations.size();
    }

    @Override
    public int getColumnCount() {
        return tableHeaders.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        if(teamRegistrations.size() > 0){
            TeamRegistration teamRegistration = teamRegistrations.get(row);
            switch(col){
                case 0 : return teamRegistration.getStudent().getStudentNumber().getValue();
                case 1 : {
                    List<ExtendedCourseGroup> groups = studentsCourseGroups.get(teamRegistration.getStudent());
                    for(ExtendedCourseGroup group : groups){
                        if(group.getCourse().equals(teamRegistration.getTeam().getCourse())){
                            return group.getGroupNumber();
                        }
                    }
                    return "";
                }
                case 2 : return teamRegistration.getStudent().getFieldOfStudy().getName();
                case 3 : return teamRegistration.getStudent().getSemester();
            }
        }
        return null;
    }

    @Override
    public String getColumnName(int col) {
        return tableHeaders[col];
    }


}
