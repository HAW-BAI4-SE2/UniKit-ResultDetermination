package net.unikit.result_determination.models.implementations;

import net.unikit.result_determination.models.interfaces.AllocationPlan;
import net.unikit.result_determination.models.interfaces.Course;

import java.io.File;
import java.util.List;

/**
 * Created by Jones on 15.11.2015.
 */
public class AllocationPlanImpl implements AllocationPlan {

    List<Course> courses;

    public AllocationPlanImpl(List<Course> courses){
        this.courses = courses;
    }

    @Override
    public void exportAsCSV(File file) {
    // TODO Jana :D
    }
}
