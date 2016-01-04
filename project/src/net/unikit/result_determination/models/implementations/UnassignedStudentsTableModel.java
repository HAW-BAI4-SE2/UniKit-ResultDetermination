package net.unikit.result_determination.models.implementations;

import net.unikit.database.interfaces.entities.Student;
import net.unikit.database.interfaces.entities.TeamRegistration;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Map;

/**
 * Created by Jones on 04.01.2016.
 */
public class UnassignedStudentsTableModel extends AbstractTableModel {

    final String[] tableHeaders;
    private List<TeamRegistration> unassignedTeamRegistrations;

    public UnassignedStudentsTableModel(List<TeamRegistration> unassignedTeamRegistrations){
        tableHeaders = new String[]{ "Matrikelnummer" , "Studiengang" , "Semester" };
        this.unassignedTeamRegistrations = unassignedTeamRegistrations;
    }

    @Override
    public int getRowCount() {
        return unassignedTeamRegistrations.size();
    }

    @Override
    public int getColumnCount() {
        return tableHeaders.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        TeamRegistration teamRegistration = unassignedTeamRegistrations.get(row);
        switch(col){
            case 0 : return teamRegistration.getStudent().getStudentNumber().getValue();
            case 1 : return teamRegistration.getStudent().getFieldOfStudy().getName();
            case 2 : return teamRegistration.getStudent().getSemester();
        }
        return null;
    }

    @Override
    public String getColumnName(int col) {
        return tableHeaders[col];
    }
}
