package osf.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandNews extends OSFCommand implements CommandExecutor {



    public CommandNews() {
        super("news", true);
    }

    public void run(CommandSender sender, String[] args) {
        respond(ChatColor.RED + "" + ChatColor.UNDERLINE + "Need to configure news");
    }
}
