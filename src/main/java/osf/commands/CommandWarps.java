package osf.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandWarps extends OSFCommand implements CommandExecutor {


    public CommandWarps() {
        super("warps", true);
    }

    public void run(CommandSender sender, String[] args) {
        respond(ChatColor.RED + "" + ChatColor.UNDERLINE + "Need to configure warps:");
    }
}
