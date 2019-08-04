package osf.blacklist;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import osf.OSF;

public class CraftingBlacklist implements Listener {
    private static final Material[] BLACKLISTED_ITEMS = {Material.TNT, Material.HOPPER};

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        Material craftedMaterial = event.getRecipe().getResult().getType();
        for(Material blacklistedMaterial : BLACKLISTED_ITEMS) {
            if(craftedMaterial == blacklistedMaterial) {
                event.setCancelled(true);
                event.getWhoClicked().sendMessage(OSF.PREFIX + ChatColor.RED + "This item can not be crafted!");
            }
        }
    }

}
