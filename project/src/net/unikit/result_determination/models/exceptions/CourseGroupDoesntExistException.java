package net.unikit.result_determination.models.exceptions;

/**
 * Created by abq307 on 25.11.2015.
 */
public class CourseGroupDoesntExistException extends Exception {

    public CourseGroupDoesntExistException(){
        super("This CourseGroup doesn't exist.");
    }
}
