package osf.playerheads;

import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import osf.OSF;
import osf.utils.ItemBuilder;
import osf.utils.MathUtil;


public class PlayerHeadDropListener implements Listener {

    @EventHandler
    public void playerDeathListener(PlayerDeathEvent event) {
        Player deadPlayer = event.getEntity();
        Player killer = event.getEntity().getKiller();

        if (killer == null)
            return;
        if (MathUtil.rand(0, 100) < 5) {
            double value = MathUtil.roundTwoPoints(OSF.getInstance().getEconomy().getBalance(deadPlayer) / 20);

            OSF.getInstance().getEconomy().withdrawPlayer(deadPlayer, value);
            deadPlayer.sendMessage(OSF.PREFIX + ChatColor.RED + "You lost 5% (" + ChatColor.GREEN + "$" + MathUtil.formatToCurrency(value) + ")" + ChatColor.RED + " of your balance and your head was dropped");

            ItemBuilder itemBuilder = new ItemBuilder(Material.SKULL_ITEM).data((short) 3)
                    .displayName(ChatColor.AQUA + "" + ChatColor.BOLD + "" + deadPlayer.getName() + "'s Head")
                    .lore(ChatColor.WHITE + "Value: " + ChatColor.GREEN + "" + ChatColor.BOLD + "" + MathUtil.formatToCurrency(value), ChatColor.GRAY + "Turn this in to the", ChatColor.GOLD + "" + ChatColor.BOLD + "BOUNTY HUNTER" + ChatColor.GRAY + "to", ChatColor.GRAY + "receive a reward");
            ItemStack headDrop = itemBuilder.grab();
            SkullMeta skullMeta = (SkullMeta) headDrop.getItemMeta();
            skullMeta.setOwningPlayer(deadPlayer);
            headDrop.setItemMeta(skullMeta);

            NBTItem nbtHead = new NBTItem(headDrop);
            nbtHead.setBoolean("bountyhunter", true);
            nbtHead.setDouble("value", value);

            event.getDrops().add(nbtHead.getItem());
        }
    }

    @EventHandler
    public void blockPlaceListener(BlockPlaceEvent event) {
        if(event.getBlockPlaced().getType() == Material.SKULL_ITEM) {
            SkullMeta skullMeta = (SkullMeta) event.getItemInHand().getItemMeta();
            if(skullMeta.hasOwner())
                event.setCancelled(true);
        }
    }
}
