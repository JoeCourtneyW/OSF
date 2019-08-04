package osf.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.pmw.tinylog.Logger;
import osf.OSF;
import osf.player.PlayerModel;

import java.util.StringJoiner;

public abstract class OSFCommand implements CommandExecutor {

    private static final String noPermissionMessage = ChatColor.RED + "You don't have permission to run this command";
    private static final String playerOnlyMessage = ChatColor.RED + "You must be a player to execute this command!";

    public String commandName;
    public boolean playerOnly;
    public String permission;
    public String usage;

    private CommandSender sender;
    private org.bukkit.command.Command command;
    private String commandLabel;
    private String[] args;

    public OSFCommand(String commandName) {
        this(commandName, false);
    }

    public OSFCommand(String commandName, boolean playerOnly) {
        this(commandName, playerOnly, "");
    }

    public OSFCommand(String commandName, boolean playerOnly, String permission) {
        this(commandName, playerOnly, permission, "");
    }

    public OSFCommand(String commandName, boolean playerOnly, String permission, String usage) {
        this.commandName = commandName;
        this.playerOnly = playerOnly;
        this.permission = permission;
        this.usage = usage;

    }

    public void register() {
        if (OSF.getInstance().getCommand(commandName) != null)
            OSF.getInstance().getCommand(commandName).setExecutor(this);
        else
            Logger.warn("Command (" + commandName + ") needs to be registered in the osf.plugin.yml");
    }

    public abstract void run(CommandSender sender, String[] args);

    public final boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String commandLabel, String[] args) {
        this.sender = sender;
        this.command = command;
        this.commandLabel = commandLabel;
        this.args = args;

        if (playerOnly) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(playerOnlyMessage);
                return true;
            }
        }

        if (!permission.isEmpty()) {
            if (!sender.hasPermission(permission)) {
                sender.sendMessage(noPermissionMessage);
                return true;
            }
        }

        run(sender, args);
        return true;
    }

    protected void respond(String message) {
        respondPlain(OSF.PREFIX + message);
    }

    protected void respondPlain(String message) {
        sender.sendMessage(message);
    }

    protected void sendUsage() {
        respond(ChatColor.RED + "Usage: " + usage);
    }

    protected Player getPlayer() {
        if (sender instanceof Player)
            return (Player) sender;
        else
            return null;
    }

    protected PlayerModel getPlayerModel() {
        return PlayerModel.getPlayerModel(getPlayer());
    }

    protected Player getTargetPlayerFromArg(int arg) {
        return Bukkit.getPlayer(args[arg]);
    }

    protected String getStringFromArgs(String[] args, int startIndex) {
        StringJoiner string = new StringJoiner(" ");
        for (int i = startIndex; i < args.length; i++) {
            string.add(args[i]);
        }
        return string.toString();
    }
}
