package net.unikit.result_determination.models.interfaces;

import net.unikit.database.external.interfaces.CourseGroup;
import net.unikit.database.external.interfaces.Student;

import java.util.List;
import java.util.Map;

/**
 * Created by abq307 on 16.11.2015.
 */
public interface AllocationPlan {

    public Map<CourseGroup, List<Student>> getAllocations();
}
