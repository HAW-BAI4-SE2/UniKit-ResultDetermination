package net.unikit.result_determination.models.implementations;

import net.unikit.result_determination.models.interfaces.AllocationPlan;
import net.unikit.database.interfaces.entities.Course;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Jones on 15.11.2015.
 */
public class AllocationPlanImpl implements AllocationPlan {

    private List<Course> courses;

    private final String CRLF = "\r\n";
    private String delimiter = ";";
    private String delimiterStudents = ",";

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public AllocationPlanImpl(List<Course> courses){
        this.courses = courses;
    }

    @Override
    public void exportAsCSV( File file) {
        try {
            FileWriter writer = new FileWriter(file);
            //add date
            writer.append(LocalDateTime.now().toString());
            writer.append(delimiter);
            writer.append(CRLF);
            //Go through List of all Courses
            for (Course course : courses) {
                //Go through List of all Groups of one Course
                for (int courseGroup = 0; courseGroup < course.getCourseGroups().size(); courseGroup++) {
                    writer.append(
                            //add course name
                            course.getName()
                    );
                    //add delimiter
                    writer.append(
                            delimiter
                    );
                    //add groupNumber to the List
                    writer.append(
                           String.valueOf(course.getCourseGroups().get(courseGroup).getGroupNumber())
                    );
                    //add delimiter
                    writer.append(
                            delimiter
                    );
                    //Go through all Students of a courseGroup
                    for (int student = 0; student < course.getCourseGroups().get(courseGroup).getMaxGroupSize(); student++) {
                        writer.append(
                                // add all students of the Group
                                 // TO DO List<Student> getStudentsOfTheGroup
                                course.getCourseGroups().get(courseGroup).get(student).getStudentNumber().toString()
                        );
                        //add delimiter and eol
                        if (student < course.getCourseGroups().get(courseGroup).getMaxGroupSize() - 1) {
                            writer.append(delimiterStudents);
                        } else {
                            writer.append(CRLF);
                        }
                    }
                }
            }
            //makes buffer empty
            writer.flush();
            //close the stream
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
