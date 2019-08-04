package discord.plugin;

import discord.bot.BotDaemon;
import discord.plugin.commands.CommandLink;
import org.bukkit.plugin.java.JavaPlugin;
import osf.utils.ConcurrentUtil;

public class OSFDiscord extends JavaPlugin {

    private static final String BOT_TOKEN = "NjAwNTQwNzE4NTQ4OTEwMDkw.XUGq8A.qNOiutR3YP9-feI4g7clzxpHKDY";

    private static OSFDiscord instance;
    private static ConcurrentUtil concurrentUtil;
    private static BotDaemon botDaemon;


    public void onEnable() {
        instance = this;

        registerCommands();

        concurrentUtil = new ConcurrentUtil(this);
        startDiscordDaemon();

    }

    public void onDisable() {
        instance = null;

        stopDiscordDaemon();
    }

    public static OSFDiscord getInstance() {
        return instance;
    }

    private void registerCommands() {
        new CommandLink().register();
    }

    private void registerEventListeners() {
    }

    private void startDiscordDaemon() {

        botDaemon = new BotDaemon(BOT_TOKEN);
        concurrentUtil.async(() -> botDaemon.login());
    }

    private void stopDiscordDaemon() {
        botDaemon.logout();
    }

}

