package osf.commands;

import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import osf.OSF;
import osf.utils.MathUtil;

public class CommandBountyHunter extends OSFCommand implements CommandExecutor {

    public CommandBountyHunter() {
        super("bountyHunter", true, "osf.bountyhunter");
    }


    public void run(CommandSender sender, String[] args) {
        respond(ChatColor.RED + "Need to configure");
        ItemStack itemInHand = getPlayer().getInventory().getItemInMainHand();

        if(itemInHand == null || itemInHand.getType() != Material.SKULL_ITEM) {
            respond(ChatColor.RED + "Come back to me with a player head to claim!");
            return;
        }

        NBTItem nbtItem = new NBTItem(itemInHand);

        if(!(nbtItem.hasNBTData() && nbtItem.getBoolean("bountyhunter") && nbtItem.hasKey("value"))) {
            respond(ChatColor.RED + "Come back to me with a player head to claim!");
            return;
        }

        double value = nbtItem.getDouble("value");

        getPlayer().getInventory().setItemInMainHand(null);
        respond(ChatColor.YELLOW + "You have redeemed " + itemInHand.getItemMeta().getDisplayName() + ChatColor.YELLOW + " for " + ChatColor.GREEN + "$" + MathUtil.formatToCurrency(value) );
        OSF.getInstance().getEconomy().depositPlayer(getPlayer(), value);
    }
}
