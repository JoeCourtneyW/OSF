package osf.chat;


import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import osf.OSF;

public class ChatFormatter implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        if (!event.isCancelled()) {
            event.setCancelled(true);
            for (Player recipient : event.getRecipients()) {
                recipient.sendMessage(formatChatMessage(event.getPlayer(), recipient, event.getMessage()));
            }
            logChatMessageToConsole(event.getPlayer(), event.getMessage());
        }
    }


    public static String formatChatMessage(Player sender, Player recipient, String message) {
        String prefix = OSF.getInstance().getChat().getPlayerPrefix(sender);
        String suffix = OSF.getInstance().getChat().getPlayerSuffix(sender);

        String faction = getFormattedFactionsPrefix(sender, recipient);

        prefix = PlaceholderAPI.setPlaceholders(sender, faction + " " + prefix);
        return ChatColor.translateAlternateColorCodes('&', prefix + sender.getDisplayName() + "&7: " + suffix) + message;
    }

    public static void logChatMessageToConsole(Player sender, String message) {
        String prefix = OSF.getInstance().getChat().getPlayerPrefix(sender);
        String suffix = OSF.getInstance().getChat().getPlayerSuffix(sender);

        String faction = getFormattedFactionsPrefix(sender, sender); //Console doesn't count as proper recipient for relation calculation

        prefix = PlaceholderAPI.setPlaceholders(sender, faction + " " + prefix);
        Bukkit.getConsoleSender().sendMessage(
                ChatColor.translateAlternateColorCodes('&', prefix + sender.getDisplayName() + "&7: " + suffix) + message);
    }

    public static String getFormattedFactionsPrefix(Player sender, Player recipient) {
        String relationColor;
        if (sender.getUniqueId().equals(recipient.getUniqueId()))
            relationColor = "&a";
        else
            relationColor = PlaceholderAPI.setRelationalPlaceholders(sender, recipient, "%rel_factionsuuid_relation_color%");
        return PlaceholderAPI.setPlaceholders(sender, "&7[" + relationColor + "%factionsuuid_player_role%%factionsuuid_faction_name%&7]");
    }

    public static String formatPlayerNameWithPrefix(Player player) {

        String prefix = OSF.getInstance().getChat().getPlayerPrefix(player);
        return ChatColor.translateAlternateColorCodes('&', prefix + player.getDisplayName());
    }

}
