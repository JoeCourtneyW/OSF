package osf.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandRename extends OSFCommand implements CommandExecutor {

    public CommandRename() {
        super("rename", true, "osf.rename", "/rename [new name]");
    }

    public void run(CommandSender sender, String[] args) {
        Player p = getPlayer();
        if(args.length > 0) {
            String name = ChatColor.translateAlternateColorCodes('&', getStringFromArgs(args, 0));
            if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() != Material.AIR) {
                ItemStack item = p.getInventory().getItemInMainHand();
                ItemMeta itemmeta = item.getItemMeta();
                itemmeta.setDisplayName(name);
                item.setItemMeta(itemmeta);
                respond(ChatColor.AQUA + "Item renamed to " + name);
            } else {
                respond(ChatColor.RED + "Your hand is empty");
            }
        }
    }
}
