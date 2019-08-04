package osf.vouchers;

import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import osf.OSF;

public class VoucherListener implements Listener {
    @EventHandler
    public void onInteractWithVoucher(PlayerInteractEvent event) {
        if(event.getItem() != null && event.getMaterial() == Material.PAPER || event.getMaterial() == Material.NAME_TAG) {
            NBTItem nbtItem = new NBTItem(event.getItem());
            if(nbtItem.hasKey("rank")) {
                String rank = nbtItem.getString("rank");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + event.getPlayer().getName() + " parent add " + rank);
                event.getPlayer().sendMessage(OSF.PREFIX + ChatColor.GRAY + "You redeemed the " + rank + " rank.");
            } else if(nbtItem.hasKey("tag")) {
                String tag = nbtItem.getString("tag");
                //Add tag to player
                event.getPlayer().sendMessage(OSF.PREFIX + ChatColor.GRAY + "You redeemed the " + tag + ChatColor.GRAY + " tag. It has been added to your collection.");
            }
        }
    }
}
