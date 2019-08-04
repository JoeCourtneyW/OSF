package osf.database;

import org.pmw.tinylog.Logger;
import osf.Configuration;

import java.sql.SQLException;

public class DatabaseManager {

    private static DatabaseManager INSTANCE;

    private Database database;


    public DatabaseManager(Configuration config) {
        if (config.isDatabaseEnabled()) {
            database = new Database(config.getDatabaseHost(),
                    config.getDatabasePort(),
                    config.getDatabaseUsername(),
                    config.getDatabasePassword(),
                    config.getDatabaseName());
            database.connect();
        } else {
            Logger.warn("Database disabled in config, startup will fail.");
        }
    }

    public Database getDatabase() {
        return database;
    }

    public void cleanup() {
        try {
            database.flush();
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.error("Failed to close connection to MySQL Database");
        }
    }

    public static DatabaseManager getInstance() {
        return INSTANCE;
    }
}
