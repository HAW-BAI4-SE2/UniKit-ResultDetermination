package net.unikit.result_determination.view;

import net.unikit.result_determination.models.implementations.ExtendedCourse;
import net.unikit.result_determination.utils.AlgorithmUtils;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Created by Jones on 03.01.2016.
 */
public class AlgorithmResultTableModel extends AbstractTableModel {

    final String[] tableHeaders;
    List<ExtendedCourse> courses;

    public AlgorithmResultTableModel(List<ExtendedCourse> courses){
        this.courses = courses;
        tableHeaders = new String[]{ "Veranstaltung", "Gruppen", "Studenten", "Belegungen","Teamerhaltung" };
    }

    @Override
    public String getColumnName(int columnIndex) {
        return tableHeaders[columnIndex];
    }

    @Override
    public int getRowCount() {
        return courses.size();
    }

    @Override
    public int getColumnCount() {
        return tableHeaders.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        ExtendedCourse course = courses.get(row);
        switch(col){
            case 0 : return course;
            case 1 : return course.getCourseGroups().size();
            case 2 : return course.getTeamRegistrations().size(); // TODO Sobald Datenbank läuft: getAllCourseRegistrations verwenden
            case 3 : return AlgorithmUtils.round(course.getAssignmentQuantity(),2)+"%";
            case 4 : return AlgorithmUtils.round(course.getTeamPreservation(),2)+"%";
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
