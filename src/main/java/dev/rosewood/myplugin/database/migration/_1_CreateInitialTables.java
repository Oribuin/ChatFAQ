package dev.rosewood.myplugin.database.migration;

import dev.rosewood.rosegarden.database.DataMigration;
import dev.rosewood.rosegarden.database.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class _1_CreateInitialTables extends DataMigration {

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS `myplugin` (
                `uuid` VARCHAR(36) NOT NULL,
                PRIMARY KEY (`uuid`)
            )
            """;

    public _1_CreateInitialTables() {
        super(1);
    }

    @Override
    public void migrate(DatabaseConnector connector, Connection connection, String tablePrefix) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_TABLE)) {
            statement.executeUpdate();
        }
    }

}
