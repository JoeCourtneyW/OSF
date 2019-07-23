package osf.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CommandLore extends OSFCommand implements CommandExecutor {

    public CommandLore() {
        super("lore", true, "osf.lore", "/lore [add, remove, clear] <Text>");
    }

    public void run(CommandSender sender, String[] args) {
        Player p = getPlayer();

        if (args.length < 1) {
            sendUsage();
            return;
        }

        if (p.getInventory().getItemInMainHand() == null || p.getInventory().getItemInMainHand().getType() == Material.AIR) {
            respond(ChatColor.RED + "Your hand is empty");
            return;
        }

        if (args[0].equalsIgnoreCase("add") && args.length > 1) {
            String line = ChatColor.translateAlternateColorCodes('&', getStringFromArgs(args, 1));
            ItemStack item = p.getInventory().getItemInMainHand();
            ItemMeta itemMeta = item.getItemMeta();
            List<String> lore = itemMeta.getLore();
            if (lore == null)
                lore = new ArrayList<>();
            lore.add(line);
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
            respond(ChatColor.AQUA + "Added item lore line: " + line);
        } else if (args[0].equalsIgnoreCase("remove")) {
            ItemStack item = p.getInventory().getItemInMainHand();
            ItemMeta itemMeta = item.getItemMeta();
            List<String> lore = itemMeta.getLore();
            if (lore == null || lore.isEmpty()) {
                respond(ChatColor.RED + "There is no lore on the item");
                return;
            }
            lore.remove(itemMeta.getLore().size() - 1);
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
            respond(ChatColor.AQUA + "Item lore line removed");
        } else if (args[0].equalsIgnoreCase("clear")) {
            ItemStack item = p.getInventory().getItemInMainHand();
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setLore(null);
            item.setItemMeta(itemMeta);
            respond(ChatColor.AQUA + "Item lore cleared");
        } else {
            sendUsage();
        }

    }
}