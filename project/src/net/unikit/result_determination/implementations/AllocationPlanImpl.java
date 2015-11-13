package net.unikit.result_determination.implementations;

import net.unikit.database.external.interfaces.CourseGroup;
import net.unikit.database.external.interfaces.Student;
import net.unikit.result_determination.interfaces.AllocationPlan;

import java.util.List;
import java.util.Map;

/**
 * Created by Andreas on 13.11.2015.
 */
final class AllocationPlanImpl implements AllocationPlan {
    private AllocationPlanImpl() {
        throw new UnsupportedOperationException();
    }

    public static AllocationPlan create() {
        return new AllocationPlanImpl();
    }

    @Override
    public Map<CourseGroup, List<Student>> getAllocations() {
        throw new UnsupportedOperationException();
    }
}
