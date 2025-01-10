package dev.rosewood.myplugin.manager;

import dev.rosewood.myplugin.database.migration._1_CreateInitialTables;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.database.DataMigration;
import dev.rosewood.rosegarden.manager.AbstractDataManager;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class DataManager extends AbstractDataManager {

    private static final String SELECT_USERS = "SELECT `uuid` FROM `myplugin`";
    private static final String INSERT_USER = "INSERT INTO `myplugin` (`uuid`) VALUES (?)";

    public DataManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    /**
     * Save a list of users to the database asynchronously
     *
     * @param users The list of users to save
     *
     * @return A completable future once the users have been saved
     */
    public CompletableFuture<Void> saveUsers(List<UUID> users) {
        return CompletableFuture.runAsync(() -> this.databaseConnector.connect(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(INSERT_USER)) {
                for (UUID user : users) {
                    statement.setString(1, user.toString());
                    statement.addBatch();
                }

                statement.executeBatch();
            }
        }));
    }

    /**
     * Retrieve all users from the database asynchronously
     *
     * @return The list of users
     */
    public CompletableFuture<List<UUID>> getUsers() {
        return CompletableFuture.supplyAsync(() -> {
            List<UUID> users = new ArrayList<>();
            this.databaseConnector.connect(connection -> {
                try (PreparedStatement statement = connection.prepareStatement(SELECT_USERS)) {
                    ResultSet result = statement.executeQuery();
                    while (result.next()) {
                        users.add(UUID.fromString(result.getString("uuid")));
                    }
                }
            });

            return users;
        });
    }


    @Override
    public @NotNull List<Supplier<? extends DataMigration>> getDataMigrations() {
        return List.of(_1_CreateInitialTables::new);
    }

}
