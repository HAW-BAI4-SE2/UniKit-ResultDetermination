package net.unikit.result_determination.models.exceptions;

/**
 * Created by abq307 on 19.11.2015.
 */
public class NotEnoughCourseGroupsException extends Exception {

    public NotEnoughCourseGroupsException(){
        super("There aren't enough CourseGroups to register all students.");
    }
}
