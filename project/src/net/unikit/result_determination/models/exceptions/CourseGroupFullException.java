package net.unikit.result_determination.models.exceptions;

/**
 * Created by abq307 on 19.11.2015.
 */
public class CourseGroupFullException extends Exception{

    public CourseGroupFullException(){
        super("The course you want to add something is already full.");
    }
}
