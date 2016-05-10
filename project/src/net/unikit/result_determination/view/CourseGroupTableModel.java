package net.unikit.result_determination.view;

import net.unikit.database.interfaces.entities.TeamRegistration;
import net.unikit.result_determination.models.implementations.ExtendedCourseGroup;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Created by Jones on 04.01.2016.
 */
public class CourseGroupTableModel extends AbstractTableModel {

    final String[] tableHeaders;
    private List<ExtendedCourseGroup> courseGroups;

    public CourseGroupTableModel(List<ExtendedCourseGroup> courseGroups){
        tableHeaders = new String[]{ "Gruppe" , "Studenten" , "Max"};
        this.courseGroups = courseGroups;
    }

    @Override
    public int getRowCount() {
        return courseGroups.size();
    }

    @Override
    public int getColumnCount() {
        return tableHeaders.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        ExtendedCourseGroup courseGroup = courseGroups.get(row);
        switch(col){
            case 0 : return courseGroup.getGroupNumber();
            case 1 : return courseGroup.getTeamRegistrations().size();
            case 2 : return courseGroup.getMaxGroupSize();
        }
        return null;
    }

    @Override
    public String getColumnName(int col) {
        return tableHeaders[col];
    }

    public ExtendedCourseGroup getCourseGroup(int row) {
        return courseGroups.get(row);
    }
}
