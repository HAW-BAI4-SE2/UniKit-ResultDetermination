package net.unikit.result_determination.models.implementations;

import net.unikit.database.external.interfaces.CourseGroup;
import net.unikit.database.external.interfaces.Student;
import net.unikit.result_determination.models.interfaces.AllocationPlan;

import java.util.List;
import java.util.Map;

/**
 * Created by Jones on 15.11.2015.
 */
public class AllocationPlanImpl implements AllocationPlan {

    @Override
    public Map<CourseGroup, List<Student>> getAllocations() {
        throw new UnsupportedOperationException();
    }
}
