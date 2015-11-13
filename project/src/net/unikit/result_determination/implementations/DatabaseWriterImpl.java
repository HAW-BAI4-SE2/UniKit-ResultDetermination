package net.unikit.result_determination.implementations;

import net.unikit.database.common.interfaces.DatabaseConfiguration;
import net.unikit.result_determination.interfaces.AllocationPlan;
import net.unikit.result_determination.interfaces.AllocationPlanWriter;

/**
 * Created by Andreas on 13.11.2015.
 */
final class DatabaseWriterImpl implements AllocationPlanWriter {
    private DatabaseWriterImpl(DatabaseConfiguration resultDatabaseConfiguration) {
        throw new UnsupportedOperationException();
    }

    public static AllocationPlanWriter create(DatabaseConfiguration resultDatabaseConfiguration) {
        return new DatabaseWriterImpl(resultDatabaseConfiguration);
    }

    @Override
    public void write(AllocationPlan allocationPlan) {
        throw new UnsupportedOperationException();
    }
}
