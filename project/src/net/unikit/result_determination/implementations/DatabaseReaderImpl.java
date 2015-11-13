package net.unikit.result_determination.implementations;

import net.unikit.database.common.interfaces.DatabaseConfiguration;
import net.unikit.result_determination.interfaces.Registrations;
import net.unikit.result_determination.interfaces.RegistrationsReader;

/**
 * Created by Andreas on 13.11.2015.
 */
final class DatabaseReaderImpl implements RegistrationsReader {
    private DatabaseReaderImpl(DatabaseConfiguration internalDatabaseConfiguration, DatabaseConfiguration externalDatabaseConfiguration) {
        throw new UnsupportedOperationException();
    }

    public static RegistrationsReader create(DatabaseConfiguration internalDatabaseConfiguration, DatabaseConfiguration externalDatabaseConfiguration) {
        return new DatabaseReaderImpl(internalDatabaseConfiguration, externalDatabaseConfiguration);
    }

    @Override
    public Registrations read() {
        Registrations result = Factory.createRegistrations();
        throw new UnsupportedOperationException();
        //return result;
    }
}
