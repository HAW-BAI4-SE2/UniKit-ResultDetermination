package net.unikit.result_determination.models.implementations;

import net.unikit.database.external.interfaces.CourseGroup;
import net.unikit.database.external.interfaces.Student;
import net.unikit.result_determination.models.interfaces.AllocationPlan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jones on 15.11.2015.
 */
public class AllocationPlanImpl implements AllocationPlan {

    private Map<CourseGroup,List<Student>> allocations;

    public AllocationPlanImpl(){
        allocations = new HashMap<>();
    }

    public void addAllocation(CourseGroup courseGroup,List<Student> students){
        // TODO checken, ob die CourseGroup bereits in der Map war?
        allocations.put(courseGroup,students);
    }

    @Override
    public Map<CourseGroup, List<Student>> getAllocations() {
        return allocations;
    }
}
