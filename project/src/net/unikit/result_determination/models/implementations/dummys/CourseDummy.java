package net.unikit.result_determination.models.implementations.dummys;

import net.unikit.database.external.interfaces.CourseGroup;
import net.unikit.database.external.interfaces.FieldOfStudy;
import net.unikit.result_determination.models.interfaces.Course;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by abq307 on 17.11.2015.
 */
public class CourseDummy implements Course{

    private final Integer id;
    private final String name;
    private final String abbreviation;
    private List<FieldOfStudy> fieldOfStudies;
    private Integer semester;
    private final int minTeamSize;
    private final int maxTeamSize;

    private List<CourseGroup> groups;

    public CourseDummy(Integer id, String name, String abbreviation, List<FieldOfStudy> fieldOfStudies, Integer semester, int minTeamSize, int maxTeamSize){
        this.id = id;
        this.name = name;
        this.abbreviation = abbreviation;
        this.fieldOfStudies = fieldOfStudies;
        this.semester = semester;
        this.minTeamSize = minTeamSize;
        this.maxTeamSize = maxTeamSize;

        this.groups = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public List<CourseGroup> getCourseGroups() {
        return groups;
    }

    public List<FieldOfStudy> getFieldOfStudies() {
        return fieldOfStudies;
    }

    public Integer getSemester() {
        return semester;
    }

    public int getMinTeamSize() {
        return minTeamSize;
    }

    public int getMaxTeamSize() {
        return maxTeamSize;
    }
}
