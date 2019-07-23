package osf.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import osf.chat.ChatFormatter;

public class CommandList extends OSFCommand implements CommandExecutor {

    public CommandList() {
        super("list",true);
    }

    public void run(CommandSender sender, String[] args) {
        respondPlain(ChatColor.AQUA + "" + ChatColor.UNDERLINE + "Online Staff:");
        respondPlain("");
        boolean foundAnyStaffMembers = false;
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.hasPermission("osf.staff")) {
                foundAnyStaffMembers = true;
                respondPlain(ChatColor.GRAY + "- " + ChatFormatter.formatPlayerNameWithPrefix(online));
            }
        }
        if (!foundAnyStaffMembers)
            respondPlain(ChatColor.RED + "" + ChatColor.BOLD + "None");

    }

}
