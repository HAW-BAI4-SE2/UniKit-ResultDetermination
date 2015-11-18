package net.unikit.result_determination.models.implementations.dummys;

import net.unikit.database.interfaces.entities.*;
import net.unikit.database.interfaces.ids.CourseId;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by abq307 on 17.11.2015.
 * Note: just a Dummy implementation for courses.
 *       this is just for testing purposes until the real data will be available.
 *       This Implementation may be deleted later
 */
public class DummyCourseImpl implements Course{

    private CourseId id;
    private String name;
    private String abbreviation;
    private Integer semester;
    private int minTeamSize;
    private int maxTeamSize;

    private List<FieldOfStudy> fieldOfStudies;
    private List<CourseGroup> courseGroups;
    private List<CourseRegistration> singleRegistrations;
    private List<Team> teamsRegistrations;

    public DummyCourseImpl(CourseId id, String name, String abbreviation, Integer semester, int minTeamSize, int maxTeamSize, List<FieldOfStudy> fieldOfStudies,
                           List<CourseGroup> courseGroups, List<CourseRegistration> singleRegistrations, List<Team> teamsRegistrations){
        this.id = id;
        this.name = name;
        this.abbreviation = abbreviation;
        this.semester = semester;
        this.minTeamSize = minTeamSize;
        this.maxTeamSize = maxTeamSize;
        this.fieldOfStudies = fieldOfStudies;
        this.courseGroups = courseGroups;
        this.singleRegistrations = singleRegistrations;
        this.teamsRegistrations = teamsRegistrations;
    }

    /**
     * Initializes Dummy
     * @param name The name of this course
     * @param semester the semester
     *
     * note: minTeamSize = 1;
     *       maxTeamSize = 2;
     *       abbreviation = name;
     */
    public DummyCourseImpl(String name, Integer semester, int min, int max){
        this.id = null;
        this.name = name;
        this.abbreviation = name;
        this.minTeamSize = min;
        this.maxTeamSize = max;
        this.fieldOfStudies = new ArrayList<>();
        this.courseGroups = new ArrayList<>();
        this.singleRegistrations = new ArrayList<>();
        this.teamsRegistrations = new ArrayList<>();
    }

    public void addCourseGroup(CourseGroup courseGroup){
        this.courseGroups.add(courseGroup);
    }


    @Override
    public CourseId getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAbbreviation() {
        return abbreviation;
    }

    @Override
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Override
    public Integer getSemester() {
        return semester;
    }

    @Override
    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    @Override
    public int getMinTeamSize() {
        return minTeamSize;
    }

    @Override
    public void setMinTeamSize(int min) {
        this.minTeamSize = min;
    }

    @Override
    public int getMaxTeamSize() {
        return maxTeamSize;
    }

    @Override
    public void setMaxTeamSize(int max) {
        this.maxTeamSize = max;
    }

    @Override
    public List<CourseGroup> getCourseGroups() {
        return courseGroups;
    }

    @Override
    public void setCourseGroups(List<CourseGroup> courseGroups) {
        this.courseGroups = courseGroups;
    }

    @Override
    public List<FieldOfStudy> getFieldOfStudies() {
        return fieldOfStudies;
    }

    @Override
    public void setFieldOfStudies(List<FieldOfStudy> fieldOfStudies) {
        this.fieldOfStudies = fieldOfStudies;
    }

    @Override
    public List<CourseRegistration> getSingleRegistrations() {
        return singleRegistrations;
    }

    @Override
    public void setCourseRegistrations(List<CourseRegistration> singleRegistrations) {
        this.singleRegistrations = singleRegistrations;
    }

    @Override
    public List<Team> getTeams() {
        return teamsRegistrations;
    }

    @Override
    public void setTeams(List<Team> teamsRegistrations) {
        this.teamsRegistrations = teamsRegistrations;
    }
}
