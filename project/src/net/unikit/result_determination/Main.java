package net.unikit.result_determination;

import net.unikit.database.common.interfaces.DatabaseConfiguration;
import net.unikit.result_determination.implementations.Factory;
import net.unikit.result_determination.interfaces.*;

/**
 * Created by Andreas Berks on 10.11.2015.
 */
public class Main {
    public static void main(String[] args) {
        // Read registrations from database
        DatabaseConfiguration internalDatabaseConfiguration = null; // TODO: Create
        DatabaseConfiguration externalDatabaseConfiguration = null; // TODO: Create
        RegistrationsReader registrationsReader = Factory.createDatabaseReader(internalDatabaseConfiguration, externalDatabaseConfiguration);
        Registrations registrations = registrationsReader.read();

        // Build allocation plan
        AllocationPlanBuilder allocationPlanBuilder = Factory.createAllocationPlanBuilder();
        AllocationPlan allocationPlan = allocationPlanBuilder.build(registrations);

        // Write allocation plan to database
        DatabaseConfiguration resultDatabaseConfiguration = null; // TODO: Create
        AllocationPlanWriter allocationPlanWriter = Factory.createDatabaseWriter(resultDatabaseConfiguration);
        allocationPlanWriter.write(allocationPlan);
    }
}
