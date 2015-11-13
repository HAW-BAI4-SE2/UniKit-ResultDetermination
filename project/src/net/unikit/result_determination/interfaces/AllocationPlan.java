package net.unikit.result_determination.interfaces;

import net.unikit.database.external.interfaces.CourseGroup;
import net.unikit.database.external.interfaces.Student;

import java.util.List;
import java.util.Map;

/**
 * Created by Andreas on 13.11.2015.
 */
public interface AllocationPlan {
    Map<CourseGroup, List<Student>> getAllocations();
}
