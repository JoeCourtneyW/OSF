package osf;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sainttx.holograms.api.HologramManager;
import com.sainttx.holograms.HologramPlugin;
import de.dustplanet.util.SilkUtil;
import me.lucko.luckperms.api.LuckPermsApi;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.writers.FileWriter;
import osf.blacklist.CraftingBlacklist;
import osf.chat.ChatFormatter;
import osf.chat.ItemShow;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import osf.commands.*;
import osf.crates.CommandCrateKey;
import osf.crates.CommandCrates;
import osf.crates.CrateManager;
import osf.database.DatabaseManager;
import osf.database.tables.PlayerDataTable;
import osf.gui.InventoryManager;
import osf.player.PlayerModel;
import osf.player.PlayerModelListener;
import osf.playerheads.PlayerHeadDropListener;
import osf.potions.InfinitePotionEffectListener;
import osf.shop.CommandShop;
import osf.shop.ShopGUI;
import osf.utils.ConcurrentUtil;
import osf.utils.SpigotConsoleWriter;
import osf.vouchers.VoucherListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.bukkit.Bukkit.getPluginManager;

public class OSF extends JavaPlugin {

    public static String PREFIX = "[OSF]";

    private static OSF instance;

    public static HologramManager hologramManager;
    public static InventoryManager inventoryManager;
    public static CrateManager crateManager;
    public static SilkUtil silkUtil;
    public static JsonNode shopJson;

    private static Configuration configuration;
    private static ConcurrentUtil concurrentUtil;
    private static DatabaseManager databaseManager;

    private static LuckPermsApi luckPermsApi;
    private static Economy econ;
    private static Permission perms;
    private static Chat chat;

    private static PlayerDataTable playerDataTable;

    public void onEnable() {
        instance = this;
        setupTinyLog();

        Logger.info("Enabling Core osf.plugin");

        configuration = new Configuration(this);
        configuration.loadConfigurationIntoMemory();
        Logger.info("Loaded configuration into memory");

        PREFIX = configuration.getPrefix();

        concurrentUtil = new ConcurrentUtil(this);
        Logger.info("Registered ConcurrentUtil to " + getClass().getName());

        Logger.info("Initializing MySQL Database and tables");
        databaseManager = new DatabaseManager(getConfiguration());

        playerDataTable = new PlayerDataTable(databaseManager.getDatabase());
        PlayerModel.registerPlayerTable(playerDataTable);
        Logger.info("Initialized PlayerData Table");


        Logger.info("Successfully initialized MySQL Database and tables");
        registerCommands();
        Logger.info("Registered commands");

        registerEventListeners();
        Logger.info("Registered event listeners");

        setupEconomy();
        setupChat();
        setupPermissions();
        if (econ != null && chat != null && perms != null)
            Logger.info("Hooked into Vault (Chat, Economy, and Permissions)");
        else {
            Logger.error("Failed to hook into Vault, is it present?");
            return;
        }
        setupLuckPerms();
        Logger.info("Hooked into LuckPerms");


        setupSilkUtil();
        Logger.info("Hooked into SilkSpawners");

        setupHologramManager();
        Logger.info("Hooked into Holograms");

        setupInventoryManager();
        Logger.info("Loading custom inventory gui manager");

        setupShops();
        Logger.info("Registering Shop GUIs");

        setupCrates();
        Logger.info("Registering Crates and Crate GUIs");

        Logger.info("Enabled!");
    }

    public void onDisable() {
        databaseManager.cleanup();
        Logger.info("Flushed MySQL Database connection");

        Logger.info("Disabling Core osf.plugin");
        instance = null;

    }

    public static OSF getInstance() {
        return instance;
    }


    public static Configuration getConfiguration() {
        return configuration;
    }

    public static ConcurrentUtil getConcurrentUtil() {
        return concurrentUtil;
    }

    private void registerCommands() {
        new CommandRename().register();
        new CommandLore().register();
        new CommandList().register();
        new CommandShop().register();
        new CommandSendMessage().register();
        new CommandNews().register();
        new CommandVote().register();
        new CommandWarps().register();
        new CommandBountyHunter().register();
        new CommandEnchanter().register();
        new CommandCrates().register();
        new CommandCrateKey().register();
    }

    private void registerEventListeners() {
        getPluginManager().registerEvents(new ItemShow(), this);
        getPluginManager().registerEvents(new ChatFormatter(), this);
        getPluginManager().registerEvents(new PlayerHeadDropListener(), this);
        getPluginManager().registerEvents(new PlayerModelListener(), this);
        getPluginManager().registerEvents(new InfinitePotionEffectListener(), this);
        getPluginManager().registerEvents(new CraftingBlacklist(), this);
        getPluginManager().registerEvents(new VoucherListener(), this);
    }

    private void setupTinyLog() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Configurator.defaultConfig()
                .writer(new SpigotConsoleWriter(), Level.DEBUG, "{message}")
                .addWriter(new FileWriter(OSF.getInstance().getDataFolder() + File.separator + "logs" + File.separator + dateFormat.format(date) + ".txt"),
                        Level.INFO, "[{date:HH:mm:ss}] {class_name}.{method}() [{level}]: {message}")
                .activate();
    }

    private void cleanupOldLogs() {
        for(File logFile : new File(getDataFolder(), "logs").listFiles()){
            if(System.currentTimeMillis() - logFile.lastModified() > 1000*60*60*48) { //48 hours old
                logFile.delete();
            }
        }
    }

    private void setupShops() {
        File shopFile = new File(getDataFolder(), "shop.json");
        try {
            shopFile.createNewFile();
            shopJson = new ObjectMapper().readTree(shopFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new ShopGUI(inventoryManager);
    }

    private void setupCrates() {
        File cratesFile = new File(getDataFolder(), "crates.json");
        JsonNode cratesJson;
        try {
            cratesFile.createNewFile();
            cratesJson = new ObjectMapper().readTree(cratesFile);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.error("Failed to read crates.json from data folder. Crates will not be activated");
            return;
        }
        crateManager = new CrateManager(cratesJson, inventoryManager);
    }

    private void setupSilkUtil() {
        silkUtil = SilkUtil.hookIntoSilkSpanwers();
    }

    private void setupInventoryManager() {
        inventoryManager = new InventoryManager(this);
        inventoryManager.init();
    }

    private void setupHologramManager() {
        hologramManager = JavaPlugin.getPlugin(HologramPlugin.class).getHologramManager();
    }

    private void setupLuckPerms() {
        RegisteredServiceProvider<LuckPermsApi> provider = Bukkit.getServicesManager().getRegistration(LuckPermsApi.class);
        if (provider != null) {
            luckPermsApi = provider.getProvider();

        }
    }

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        econ = rsp.getProvider();
    }

    private void setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
    }

    private void setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
    }


    public JsonNode getShopJsonNode() {
        return shopJson;
    }

    public Economy getEconomy() {
        return econ;
    }

    public Permission getPermissions() {
        return perms;
    }

    public Chat getChat() {
        return chat;
    }

    public LuckPermsApi getLuckPermsApi() {
        return luckPermsApi;
    }


}
