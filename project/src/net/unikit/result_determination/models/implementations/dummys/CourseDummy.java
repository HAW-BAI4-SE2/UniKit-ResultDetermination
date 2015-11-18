package net.unikit.result_determination.models.implementations.dummys;

import net.unikit.database.interfaces.entities.*;
import net.unikit.database.interfaces.ids.CourseId;

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

    @Override
    public CourseId getId() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String s) {

    }

    @Override
    public String getAbbreviation() {
        return null;
    }

    @Override
    public void setAbbreviation(String s) {

    }

    @Override
    public Integer getSemester() {
        return null;
    }

    @Override
    public void setSemester(Integer integer) {

    }

    @Override
    public int getMinTeamSize() {
        return 0;
    }

    @Override
    public void setMinTeamSize(int i) {

    }

    @Override
    public int getMaxTeamSize() {
        return 0;
    }

    @Override
    public void setMaxTeamSize(int i) {

    }

    @Override
    public List<CourseGroup> getCourseGroups() {
        return null;
    }

    @Override
    public void setCourseGroups(List<CourseGroup> list) {

    }

    @Override
    public List<FieldOfStudy> getFieldOfStudies() {
        return null;
    }

    @Override
    public void setFieldOfStudies(List<FieldOfStudy> list) {

    }

    @Override
    public List<CourseRegistration> getSingleRegistrations() {
        return null;
    }

    @Override
    public void setCourseRegistrations(List<CourseRegistration> list) {

    }

    @Override
    public List<Team> getTeams() {
        return null;
    }

    @Override
    public void setTeams(List<Team> list) {

    }
}
