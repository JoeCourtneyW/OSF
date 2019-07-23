package osf;


import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class Configuration {

    private OSF plugin;
    private FileConfiguration config;
    private File configFile;

    /*
        Configuration file values
     */
    private boolean databaseEnabled;
    private String databaseHost;
    private int databasePort;
    private String databaseUsername;
    private String databasePassword;
    private String databaseName;

    private String prefix;

    private String itemShowKeyword;

    public Configuration(OSF plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        saveDefault();
    }

    public void loadConfigurationIntoMemory() {
        this.databaseEnabled = config.getBoolean("database.enabled");
        this.databaseHost = config.getString("database.host");
        this.databasePort = config.getInt("database.port");
        this.databaseUsername = config.getString("database.username");
        this.databasePassword = config.getString("database.password");
        this.databaseName = config.getString("database.databaseName");

        this.prefix = ChatColor.translateAlternateColorCodes('&', config.getString("chat.prefix"));
        this.itemShowKeyword = config.getString("chat.itemshowkeyword");
    }

    public boolean isDatabaseEnabled() {
        return databaseEnabled;
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public int getDatabasePort() {
        return databasePort;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getItemShowKeyword() {
        return itemShowKeyword;
    }

    public void save() {
        plugin.saveConfig();
    }

    public void saveDefault() {
        plugin.saveDefaultConfig();
    }

    public void reload() {
        plugin.reloadConfig();
    }
}
