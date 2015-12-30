package net.unikit.result_determination.models.interfaces;

import net.unikit.database.interfaces.entities.*;
import net.unikit.result_determination.models.exceptions.CourseGroupDoesntExistException;
import net.unikit.result_determination.models.exceptions.CourseGroupFullException;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by abq307 on 16.11.2015.
 */
public interface AllocationPlan {


    /**
     * Exports the AllocationPlan as a CSV-File
     *
     * @param file the file where the data shall be stored
     */
    void exportAsCSV(File file);

    /**
     *
     * @param courseGroup
     * @return
     * @throws CourseGroupDoesntExistException
     */
    boolean isCourseGroupFull(CourseGroup courseGroup) throws CourseGroupDoesntExistException;

    /**
     * Checks if two Courses have potential conflicts between CourseGroups and their Appointments
     *
     * @param course1 the first course
     * @param course2 the second course
     * @return True if a conflict appeared
     */
    boolean conflict(Course course1, Course course2);

    /**
     * Checks if there is a conflict between one of the team members and the courseGroup
     *
     * @param team the team that shall be checked
     * @param courseGroup the courseGroup that shall be checked
     * @return true if there is allready a CourseGroup assigned to one of the team members that has a conflict with
     *         the current courseGroup
     */
    boolean conflict(Team team, CourseGroup courseGroup);

    /**
     * Checks if there is a conflict between one of the assigned coursegroups of the singleRegistration and
     * another courseGroup.
     *
     * @param singleRegistration the registration that shall be checked
     * @param courseGroup the courseGroup that shall be checked
     * @return true if there is allready a CourseGroup assigned to the singleRegistration that has a conflict with
     *         the current courseGroup
     */
    boolean conflict(CourseRegistration singleRegistration, CourseGroup courseGroup);

    /**
     * Checks if there is a conflict between two CourseGroups. A conflict exists if there is at least one similar
     * appointment that each CourseGroup has.
     *
     * @param course1 the first CourseGroup
     * @param course2 the second CourseGroup
     * @returns true, if there is a conflict between those two CourseGroups
     */
    boolean conflict(CourseGroup course1, CourseGroup course2);

    /**
     * Removes a CourseRegistration from a courseGroup
     *
     * @param courseRegistration
     * @param courseGroup
     */
    void removeStudent(CourseRegistration courseRegistration, CourseGroup courseGroup);

    /**
     * Assignes the CourseRegistration to the CourseGroup
     *
     * @param courseRegistration
     * @param courseGroup
     */
    void registerStudent(CourseRegistration courseRegistration, CourseGroup courseGroup);

    /**
     * Assignes every TeamRegistration of the Team to the CourseGroup
     *
     * @param team
     * @param courseGroup
     */
    void registerTeam(Team team, CourseGroup courseGroup);

    Map<Student,List<CourseGroup>> getStudentsCourseGroups();

    List<CourseRegistration> getCourseRegistrations(CourseGroup courseGroup) throws CourseGroupDoesntExistException;

    List<TeamRegistration> getTeamRegistrations(CourseGroup courseGroup) throws CourseGroupDoesntExistException;
}
