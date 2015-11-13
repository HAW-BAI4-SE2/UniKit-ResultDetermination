package net.unikit.result_determination.implementations;

import net.unikit.result_determination.interfaces.AllocationPlan;
import net.unikit.result_determination.interfaces.AllocationPlanBuilder;
import net.unikit.result_determination.interfaces.Registrations;

/**
 * Created by Andreas on 13.11.2015.
 */
final class AllocationPlanBuilderImpl implements AllocationPlanBuilder {
    private AllocationPlanBuilderImpl() {
        throw new UnsupportedOperationException();
    }

    public static AllocationPlanBuilder create() {
        return new AllocationPlanBuilderImpl();
    }

    @Override
    public AllocationPlan build(Registrations registrations) {
        AllocationPlan result = Factory.createAllocationPlan();
        throw new UnsupportedOperationException();
        //return result;
    }
}
