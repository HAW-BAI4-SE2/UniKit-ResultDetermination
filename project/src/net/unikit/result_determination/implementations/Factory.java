package net.unikit.result_determination.implementations;

import net.unikit.database.common.interfaces.DatabaseConfiguration;
import net.unikit.result_determination.interfaces.*;

/**
 * Created by Andreas on 13.11.2015.
 */
public final class Factory {
    public static RegistrationsReader createDatabaseReader(DatabaseConfiguration internalDatabaseConfiguration, DatabaseConfiguration externalDatabaseConfiguration) {
        return DatabaseReaderImpl.create(internalDatabaseConfiguration, externalDatabaseConfiguration);
    }

    public static Registrations createRegistrations() {
        return RegistrationsImpl.create();
    }

    public static AllocationPlanBuilder createAllocationPlanBuilder() {
        return AllocationPlanBuilderImpl.create();
    }

    public static AllocationPlan createAllocationPlan() {
        return AllocationPlanImpl.create();
    }

    public static AllocationPlanWriter createDatabaseWriter(DatabaseConfiguration resultDatabaseConfiguration) {
        return DatabaseWriterImpl.create(resultDatabaseConfiguration);
    }
}
