package osf.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import osf.OSF;

import java.util.regex.Pattern;

public class CommandSendMessage extends OSFCommand implements CommandExecutor {

    public CommandSendMessage() {
        super("sendMessage", false, "osf.sendmessage", "/sendMessage [Player] [Message]");
    }


    public void run(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sendUsage();
        }

        Player recipient = Bukkit.getPlayer(args[0]);
        if (recipient == null) {
            respond(ChatColor.RED + "Player is not online");
            return;
        }

        String message = "";
        if (args.length > 1) {
            message = getStringFromArgs(args, 1).replaceAll(Pattern.quote("[prefix]"), OSF.PREFIX);
            message = PlaceholderAPI.setPlaceholders(recipient, message);
        }
        recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

    }
}
